package com.enterprise.cleanqueen.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.cleanqueen.dto.review.CreateReviewRequest;
import com.enterprise.cleanqueen.dto.review.CreateReviewResponse;
import com.enterprise.cleanqueen.entity.Project;
import com.enterprise.cleanqueen.entity.Review;
import com.enterprise.cleanqueen.entity.Task;
import com.enterprise.cleanqueen.entity.User;
import com.enterprise.cleanqueen.repository.ProjectRepository;
import com.enterprise.cleanqueen.repository.ReviewRepository;
import com.enterprise.cleanqueen.repository.TaskRepository;
import com.enterprise.cleanqueen.repository.UserRepository;
import com.enterprise.cleanqueen.service.ReviewService;
import com.enterprise.cleanqueen.util.CodeGenerator;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CodeGenerator codeGenerator;

    @Override
    public CreateReviewResponse createReview(CreateReviewRequest request, String supervisorEmail) {
        // Find supervisor
        User supervisor = userRepository.findByEmail(supervisorEmail)
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));

        // Find task
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Find project
        Project project = projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Validate supervisor is assigned to this project
        if (!supervisor.getId().equals(project.getSupervisorId())) {
            throw new RuntimeException("You are not assigned to this project");
        }

        // Check if task is a leaf task (has no children)
        long childrenCount = taskRepository.countChildrenTasks(task.getId());
        if (childrenCount > 0) {
            throw new RuntimeException("Reviews can only be created for leaf tasks (tasks with no subtasks)");
        }

        // Check if task already has a review
        if (reviewRepository.existsByTaskId(task.getId())) {
            throw new RuntimeException("This task has already been reviewed");
        }

        // Create review
        Review review = new Review();
        review.setId(codeGenerator.generateReviewId());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setTaskId(task.getId());
        review.setSupervisorId(supervisor.getId());

        reviewRepository.save(review);

        // Set task rating
        task.setAverageRating(request.getRating().floatValue());
        taskRepository.save(task);

        // Propagate ratings up the hierarchy
        boolean propagated = propagateRatingsUp(task);

        logger.info("Review created for task {} with rating propagation: {}", task.getId(), propagated);

        return new CreateReviewResponse(
                true,
                "Review created successfully. Rating propagated through task hierarchy.",
                review.getId(),
                task.getId(),
                task.getName(),
                request.getRating(),
                propagated
        );
    }

    private boolean propagateRatingsUp(Task currentTask) {
        boolean propagationOccurred = false;

        // If this is not a root task, check parent
        if (currentTask.getParentId() != null) {
            Task parentTask = taskRepository.findById(currentTask.getParentId())
                    .orElse(null);

            if (parentTask != null) {
                // Get all sibling tasks (including current task)
                List<Task> siblingTasks = taskRepository.findByParentId(parentTask.getId());

                // Check if all siblings have ratings
                boolean allSiblingsRated = siblingTasks.stream()
                        .allMatch(task -> task.getAverageRating() != null);

                if (allSiblingsRated && !siblingTasks.isEmpty()) {
                    // Calculate average rating for parent
                    double averageRating = siblingTasks.stream()
                            .mapToDouble(Task::getAverageRating)
                            .average()
                            .orElse(0.0);

                    // Update parent task rating
                    parentTask.setAverageRating((float) averageRating);
                    taskRepository.save(parentTask);

                    propagationOccurred = true;

                    // Recursively propagate up
                    propagationOccurred |= propagateRatingsUp(parentTask);
                }
            }
        } else {
            // This is a root task, check if we can update project rating
            propagationOccurred |= updateProjectRatingIfReady(currentTask.getProjectId());
        }

        return propagationOccurred;
    }

    private boolean updateProjectRatingIfReady(String projectId) {
        // Get all root tasks for this project
        List<Task> rootTasks = taskRepository.findRootTasksByProjectId(projectId);

        // Check if all root tasks have ratings
        boolean allRootTasksRated = rootTasks.stream()
                .allMatch(task -> task.getAverageRating() != null);

        if (allRootTasksRated && !rootTasks.isEmpty()) {
            // Calculate project average rating
            double projectAverageRating = rootTasks.stream()
                    .mapToDouble(Task::getAverageRating)
                    .average()
                    .orElse(0.0);

            // Update project rating
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new RuntimeException("Project not found"));

            project.setAverageRating((float) projectAverageRating);
            projectRepository.save(project);

            logger.info("Project {} rating updated to: {}", projectId, projectAverageRating);
            return true;
        }

        return false;
    }
}
