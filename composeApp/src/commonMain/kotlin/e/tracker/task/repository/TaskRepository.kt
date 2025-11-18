package e.tracker.task.repository

import e.tracker.task.database.Task
import e.tracker.task.database.TaskDao
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    suspend fun getTaskById(id: Long): Task? = taskDao.getTaskById(id)
    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    suspend fun deleteTaskById(taskId: Long) = taskDao.deleteTaskById(taskId)
}