package e.tracker.task

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import e.tracker.task.database.DB_FILE_NAME
import e.tracker.task.database.TaskDatabase
import platform.Foundation.NSHomeDirectory

fun getDatabaseBuilder(): RoomDatabase.Builder<TaskDatabase> {
    val dbFilePath = NSHomeDirectory() + "/$DB_FILE_NAME"
    return Room.databaseBuilder<TaskDatabase>(
        name = dbFilePath,
    ).setDriver(BundledSQLiteDriver())
}