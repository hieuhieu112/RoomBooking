package com.app.backend.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    //
    INVALID_REPORT("ERROR.INVALID_REPORT", "Bạn đã tạo báo cáo hôm nay", HttpStatus.BAD_REQUEST),


    USER_PASSWORD_WEAK("ERROR.USER.PASSWORD_WEAK", "Mật khẩu phải >= 6 ký tự", HttpStatus.BAD_REQUEST),

    USER_CONFIRM_PASSWORD_NOT_MATCH("ERROR.USER.CONFIRM_PASSWORD_NOT_MATCH", "Xác nhận mật khẩu không khớp", HttpStatus.BAD_REQUEST),

    USER_RETYPE_PASSWORD_INVALID("ERROR.USER.USER_RETYPE_PASSWORD_INVALID", "Mật khẩu cũ không đúng", HttpStatus.BAD_REQUEST),

    USER_PASSWORD_DUPLICATE("ERROR.USER.PASSWORD_DUPLICATE", "Mật khẩu mới phải khác mật khẩu cũ", HttpStatus.BAD_REQUEST),
    //file
    ERROR_VALID_FILE("ERROR.INVALID_FILE", "Ảnh không hợp lệ", HttpStatus.BAD_REQUEST),

    ERROR_VALID_DATE("ERROR.INVALID_DATE", "Có lỗi ngày tháng", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND("ERROR.ENTITY.NOT_FOUND", "Dữ liệu không tồn tại trên hệ thống.", HttpStatus.NOT_FOUND),
    // ===== ROLE
    ROLE_NOT_FOUND("ERROR.ROLE.NOT_FOUND", "Role not found", HttpStatus.NOT_FOUND),
    // ===== CONTRACTS
    CONTRACT_INVALID_COMMISSION("ERROR.CONTRACT.INVALID_COMMISSION", "Commission must <= 1", HttpStatus.BAD_REQUEST),
    CONTRACT_DEPOSIT_INVALID("ERROR.CONTRACT.INVALID_DEPOSIT", "Loại cọc không phù hợp", HttpStatus.BAD_REQUEST),
    CONTRACT_INVALID_DISCOUNT("ERROR.CONTRACT.INVALID_DISCOUNT", "Discount cannot exceed room price", HttpStatus.BAD_REQUEST),


    // ===== AUTH =====
    AUTH_UNAUTHORIZED("ERROR.AUTH.UNAUTHORIZED", "Unauthorized", HttpStatus.UNAUTHORIZED),
    AUTH_FORBIDDEN("ERROR.AUTH.FORBIDDEN", "Truy cập bị từ chối", HttpStatus.FORBIDDEN),
    AUTH_WRONG("ERROR.AUTH.WRONG", "Thông tin đăng nhập không chính xác", HttpStatus.FORBIDDEN),
    AUTH_TOKEN_EXPIRED("ERROR.AUTH.TOKEN_EXPIRED", "Het phien lam viec", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_INVALID("ERROR.AUTH.TOKEN_INVALID", "Token invalid", HttpStatus.UNAUTHORIZED),
    AUTH_LOGIN_FAILED("ERROR.AUTH.LOGIN_FAILED", "Login failed", HttpStatus.UNAUTHORIZED),
    AUTH_ACCOUNT_INACTIVE("ERROR.AUTH.ACCOUNT_INACTIVE", "Tài khoan chua kich hoat", HttpStatus.FORBIDDEN),
    AUTH_ACCOUNT_LOCKED("ERROR.AUTH.ACCOUNT_LOCKED", "Account is locked", HttpStatus.FORBIDDEN),
    AUTH_EMAIL_NOTMATCH("ERROR.AUTH.EMAIL_NOTMATCH", "Email khong khop voi username", HttpStatus.FORBIDDEN),
    AUTH_OTP_NOTMATCH("ERROR.AUTH.OTP_NOTMATCH", "OTP khong chinh xac", HttpStatus.FORBIDDEN),
    AUTH_RESET_TOKEN_NOTMATCH("ERROR.AUTH.RESET_TOKEN_NOTMATCH", "RESET_TOKEN khong chinh xac", HttpStatus.FORBIDDEN),
    AUTH_ACCOUNT_DISABLED("ERROR.AUTH.ACCOUNT_DISABLED", "Account is disabled", HttpStatus.FORBIDDEN),

    // ===== USER =====
    USER_NOT_FOUND("ERROR.USER.NOT_FOUND", "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("ERROR.USER.ALREADY_EXISTS", "User already exists", HttpStatus.CONFLICT),
    USER_SORTID_EXISTS("ERROR.USER.SORTID_EXISTS", "User sort id already exists", HttpStatus.CONFLICT),
    USER_EMAIL_EXISTS("ERROR.USER.EMAIL_EXISTS", "Email already exists", HttpStatus.CONFLICT),
    USER_USERNAME_EXISTS("ERROR.USER.USERNAME_EXISTS", "Username already exists", HttpStatus.CONFLICT),
    USER_INVALID_STATUS("ERROR.USER.INVALID_STATUS", "Invalid user status", HttpStatus.BAD_REQUEST),
    USER_PASSWORD_INVALID("ERROR.USER.PASSWORD_INVALID", "Invalid password", HttpStatus.BAD_REQUEST),
    USER_OLD_PASSWORD_WRONG("ERROR.USER.OLD_PASSWORD_WRONG", "Old password is wrong", HttpStatus.BAD_REQUEST),
    // ===== Customer =====
    CUSTOMER_NOT_FOUND("ERROR.CUSTOMER.NOT_FOUND", "Customer not found", HttpStatus.NOT_FOUND),

    // ===== SHIFT =====
    SHIFT_NOT_FOUND("ERROR.SHIFT.NOT_FOUND", "SHIFT not found", HttpStatus.NOT_FOUND),
    SHIFT_INVALID_DATE("ERROR.SHIFT.INVALID_DATE", "SHIFT invalid date", HttpStatus.NOT_FOUND),

    // ===== LOG =====
    LOG_NOT_FOUND("ERROR.LOG_NOT_FOUND", "Không tìm thấy log", HttpStatus.NOT_FOUND),

    // ===== MANUFACTOR_DEVICE =====
    MANUFACTOR_DEVICE_NOT_FOUND("ERROR.MANUFACTOR_DEVICE_NOT_FOUND", "Không tìm thấy nhà sản xuất thiết bị", HttpStatus.NOT_FOUND),

    // ===== HOUSE =====
    HOUSE_NOT_FOUND("ERROR.HOUSE_NOT_FOUND", "Không tìm thấy nhà", HttpStatus.NOT_FOUND),

    // ===== MANAGER_GROUP =====
    MANAGER_GROUP_NOT_FOUND("ERROR.MANAGER_GROUP_NOT_FOUND", "Không tìm thấy nhóm quản lý", HttpStatus.NOT_FOUND),

    // ===== DEVICE_TYPE =====
    DEVICE_TYPE_NOT_FOUND("ERROR.DEVICE_TYPE_NOT_FOUND", "Không tìm thấy loại thiết bị", HttpStatus.NOT_FOUND),
    // ===== DEVICE_CATEGORY =====
    DEVICE_CATEGORY_NOT_FOUND("ERROR.DEVICE_CATEGORY_NOT_FOUND", "Không tìm thấy thể loại thiết bị", HttpStatus.NOT_FOUND),
    // ===== DEVICE=====
    DEVICE_NOT_FOUND("ERROR.DEVICE_NOT_FOUND", "Không tìm thấy thiết bị", HttpStatus.NOT_FOUND),
    // ===== DEVICE_MODEL =====
    DEVICE_MODEL_NOT_FOUND("ERROR.DEVICE_MODEL_NOT_FOUND", "Không tìm thấy model thiết bị", HttpStatus.NOT_FOUND),
    // ===== DEVICE_INCIENT =====
    DEVICE_INCIENT_NOT_FOUND("ERROR.DEVICE_INCIENT_NOT_FOUND", "Không tìm thấy ICIENT thiết bị", HttpStatus.NOT_FOUND),

    // ===== ROOM_TYPE =====
    ROOM_TYPE_NOT_FOUND("ERROR.ROOM_TYPE_NOT_FOUND", "Không tìm thấy loại phòng", HttpStatus.NOT_FOUND),
    // ===== ROOM =====
    ROOM_NOT_FOUND("ERROR.ROOM_NOT_FOUND", "Không tìm thấy phòng", HttpStatus.NOT_FOUND),

    // ===== Booking =====
    BOOKING_NOT_FOUND("ERROR.BOOKING_NOT_FOUND", "Không tìm thấy booking", HttpStatus.NOT_FOUND),


    SHIFT_TEMPLATE_NOT_FOUND("ERROR.SHIFTTEMPLATE.NOT_FOUND", "SHIFT TEMPLATE not found", HttpStatus.NOT_FOUND),

    // ===== JOB =====
    JOB_NOT_FOUND("ERROR.JOB.NOT_FOUND", "Job not found", HttpStatus.NOT_FOUND),
    JOB_ALREADY_CLOSED("ERROR.JOB.ALREADY_CLOSED", "Job already closed", HttpStatus.BAD_REQUEST),
    JOB_EXPIRED("ERROR.JOB.EXPIRED", "Job expired", HttpStatus.BAD_REQUEST),
    JOB_INVALID_STATUS("ERROR.JOB.INVALID_STATUS", "Invalid job status", HttpStatus.BAD_REQUEST),
    JOB_INVALID_SALARY("ERROR.JOB.INVALID_SALARY", "Invalid salary", HttpStatus.BAD_REQUEST),
    JOB_CREATE_FAILED("ERROR.JOB.CREATE_FAILED", "Create job failed", HttpStatus.INTERNAL_SERVER_ERROR),
    JOB_UPDATE_FAILED("ERROR.JOB.UPDATE_FAILED", "Update job failed", HttpStatus.INTERNAL_SERVER_ERROR),
    JOB_DELETE_FAILED("ERROR.JOB.DELETE_FAILED", "Delete job failed", HttpStatus.INTERNAL_SERVER_ERROR),
    JOB_COMPANY_ID_REQUIRED("ERROR.JOB.COMPANY_ID_REQUIRED", "Company is required", HttpStatus.INTERNAL_SERVER_ERROR),
    // ===== VALIDATION =====
    VALIDATION("ERROR.VALIDATION", "Validation failed", HttpStatus.BAD_REQUEST),
    REQUEST_INVALID("ERROR.REQUEST.INVALID", "Invalid request", HttpStatus.BAD_REQUEST),
    REQUEST_MISSING_PARAM("ERROR.REQUEST.MISSING_PARAM", "Missing parameter", HttpStatus.BAD_REQUEST),
    REQUEST_BAD_FORMAT("ERROR.REQUEST.BAD_FORMAT", "Bad request format", HttpStatus.BAD_REQUEST),
    REQUEST_NOT_SUPPORTED("ERROR.REQUEST.NOT_SUPPORTED", "Request not supported", HttpStatus.METHOD_NOT_ALLOWED),

    // ===== SYSTEM =====
    SYSTEM("ERROR.SYSTEM", "Lỗi hệ thống", HttpStatus.INTERNAL_SERVER_ERROR),
    SYSTEM_INTERNAL("ERROR.SYSTEM.INTERNAL", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    SYSTEM_TIMEOUT("ERROR.SYSTEM.TIMEOUT", "System timeout", HttpStatus.GATEWAY_TIMEOUT),
    SYSTEM_UNKNOWN("ERROR.SYSTEM.UNKNOWN", "Unknown system error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String defaultMessage, HttpStatus httpStatus) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
