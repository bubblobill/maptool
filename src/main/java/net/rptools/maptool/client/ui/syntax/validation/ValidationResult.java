package net.rptools.maptool.client.ui.syntax.validation;

/**
 * @param status
 * @param message
 */
public record ValidationResult(ValidationStatus status, String message) {
    public static ValidationResult valid(String message) {
        return new ValidationResult(ValidationStatus.VALID, message);
    }

    public static ValidationResult warning(String message) {
        return new ValidationResult(ValidationStatus.WARNING, message);
    }

    public static ValidationResult error(String message) {
        return new ValidationResult(ValidationStatus.ERROR, message);
    }
}
