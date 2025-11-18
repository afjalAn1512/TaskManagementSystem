package e.tracker.task.validation

import e.tracker.task.utils.DateTimeHelper

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

object TaskValidation {

    fun validateTitle(title: String): ValidationResult {
        return when {
            title.isBlank() -> ValidationResult(false, "Title cannot be empty")
            title.length < 3 -> ValidationResult(false, "Title must be at least 3 characters")
            title.length > 100 -> ValidationResult(false, "Title too long")
            else -> ValidationResult(true)
        }
    }

    fun validateDescription(description: String): ValidationResult {
        return when {
            description.length > 500 -> ValidationResult(false, "Description too long")
            else -> ValidationResult(true)
        }
    }

    fun validateDueDate(dueDate: Long?): ValidationResult {
        return if (dueDate != null && dueDate < DateTimeHelper.currentTimeMillis()) {
            ValidationResult(false, "Due date cannot be in the past")
        } else {
            ValidationResult(true)
        }
    }

    fun validateTask(title: String, description: String, dueDate: Long?): ValidationResult {
        val titleValidation = validateTitle(title)
        if (!titleValidation.isValid) return titleValidation

        val descriptionValidation = validateDescription(description)
        if (!descriptionValidation.isValid) return descriptionValidation

        val dueDateValidation = validateDueDate(dueDate)
        if (!dueDateValidation.isValid) return dueDateValidation

        return ValidationResult(true)
    }
}