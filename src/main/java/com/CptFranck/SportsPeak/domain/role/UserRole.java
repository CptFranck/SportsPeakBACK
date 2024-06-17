package com.CptFranck.SportsPeak.domain.role;

public enum UserRole {
    ADMIN("Admin"),
    STAFF("Staff"),
    MODERATOR("Moderator"),
    USER("User");

    public final String label;

    UserRole(String label) {
        this.label = label;
    }

    public static UserRole valueOfLabel(String label) {
        for (UserRole e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
