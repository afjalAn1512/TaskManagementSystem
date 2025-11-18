package e.tracker.task.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(entities = [Task::class], version = 1)
@ConstructedBy(TaskDatabaseConstructor::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object TaskDatabaseConstructor : RoomDatabaseConstructor<TaskDatabase> {
    override fun initialize(): TaskDatabase
}

const val DB_FILE_NAME = "tasks.db"