package com.enterprise.cleanqueen.util;

/**
 * Common API response documentation patterns to maintain consistency
 * Contains standardized response descriptions and status codes
 */
public class ApiResponseDocumentation {
    
    // Response descriptions
    public static final String UNAUTHORIZED_DESC = "❌ Unauthorized - Invalid or missing authentication token";
    public static final String FORBIDDEN_DESC = "❌ Access denied - Insufficient privileges to access this resource";
    public static final String ADMIN_ONLY_DESC = "❌ Access denied - Admin role required";
    public static final String CUSTOMER_ONLY_DESC = "❌ Access denied - Customer role required";
    public static final String SUPERVISOR_ONLY_DESC = "❌ Access denied - Supervisor role required";
    
    // Private constructor to prevent instantiation
    private ApiResponseDocumentation() {
        throw new UnsupportedOperationException("Utility class");
    }
}
