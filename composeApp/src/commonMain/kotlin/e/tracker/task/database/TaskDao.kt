package e.tracker.task.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    
    @Query("SELECT * FROM tasks ORDER BY created_timestamp DESC")
    fun getAllTasks(): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%'")
    fun searchTasks(query: String): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE status = :status")
    fun getTasksByStatus(status: Task.Status): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE priority = :priority")
    fun getTasksByPriority(priority: Task.Priority): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE due_date < :currentTime AND status != 'DONE'")
    fun getOverdueTasks(currentTime: Long): Flow<List<Task>>
    
    // Sorting queries
    @Query("SELECT * FROM tasks ORDER BY due_date ASC")
    fun getTasksSortedByDueDate(): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks ORDER BY CASE priority WHEN 'HIGH' THEN 1 WHEN 'MEDIUM' THEN 2 WHEN 'LOW' THEN 3 END")
    fun getTasksSortedByPriority(): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks ORDER BY created_timestamp DESC")
    fun getTasksSortedByCreated(): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): Task?
    
    @Insert
    suspend fun insertTask(task: Task): Long
    
    @Update
    suspend fun updateTask(task: Task)
    
    @Delete
    suspend fun deleteTask(task: Task)
    
    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long)
}