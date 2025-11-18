package e.tracker.task.database

import androidx.room.*
import e.tracker.task.utils.DateTimeHelper
import kotlinx.serialization.Serializable

@Entity(tableName = "tasks")
@Serializable
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "priority")
    val priority: Priority,

    @ColumnInfo(name = "status")
    val status: Status,

    @ColumnInfo(name = "due_date")
    val dueDate: Long?, // timestamp

    @ColumnInfo(name = "created_timestamp")
    val createdTimestamp: Long = DateTimeHelper.currentTimeMillis()
) {
    enum class Priority { LOW, MEDIUM, HIGH }
    enum class Status { TO_DO, IN_PROGRESS, DONE }

    val isOverdue: Boolean
        get() = dueDate?.let { it < DateTimeHelper.currentTimeMillis() } ?: false
}