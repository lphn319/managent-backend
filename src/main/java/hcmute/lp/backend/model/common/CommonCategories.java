package hcmute.lp.backend.model.common;

public class CommonCategories {
    public static final String GENDER = "GENDER";

    // Các giá trị cho mỗi category
    public static class GenderType {
        public static final String MALE = "MALE";
        public static final String FEMALE = "FEMALE";
        public static final String OTHER = "OTHER";
    }

    public static class ImportStatus {
        public static final String PROCESSING = "PROCESSING";
        public static final String COMPLETED = "COMPLETED";
        public static final String CANCELLED = "CANCELLED";
    }

    public static class RoleType {
        public static final String ADMIN = "ADMIN";
        public static final String EMPLOYEE = "EMPLOYEE";
        public static final String CUSTOMER = "CUSTOMER";
        public static final String MANAGER = "MANAGER";
    }

    public static class UserStatus {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
    }
}

