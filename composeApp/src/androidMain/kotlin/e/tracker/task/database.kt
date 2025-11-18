package e.tracker.task

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import e.tracker.task.database.DB_FILE_NAME
import e.tracker.task.database.TaskDatabase


fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<TaskDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(DB_FILE_NAME)
    return Room.databaseBuilder<TaskDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}