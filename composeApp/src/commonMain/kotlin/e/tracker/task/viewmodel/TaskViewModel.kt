package e.tracker.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import e.tracker.task.database.Task
import e.tracker.task.repository.TaskRepository
import e.tracker.task.validation.TaskValidation
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    
    // Search and filter state
    private val _searchQuery = MutableStateFlow("")
    private val _selectedStatus = MutableStateFlow<Task.Status?>(null)
    private val _selectedPriority = MutableStateFlow<Task.Priority?>(null)
    private val _sortOrder = MutableStateFlow(SortOrder.CREATED_DATE_DESC)
    
    // UI State
    private val _uiState = MutableStateFlow<TaskUiState>(TaskUiState.Loading)
    val uiState: StateFlow<TaskUiState> = _uiState
    
    // Form state
    private val _formState = MutableStateFlow(TaskFormState())
    val formState: StateFlow<TaskFormState> = _formState
    
    // Actions
    private val _actions = MutableSharedFlow<TaskAction>()
    val actions: SharedFlow<TaskAction> = _actions
    
    private val tasksFlow = combine(
        repository.getAllTasks(),
        _searchQuery,
        _selectedStatus,
        _selectedPriority,
        _sortOrder
    ) { tasks, query, status, priority, sortOrder ->
        var filteredTasks = tasks
        
        // Apply search
        if (query.isNotBlank()) {
            filteredTasks = filteredTasks.filter { 
                it.title.contains(query, ignoreCase = true) 
            }
        }
        
        // Apply filters
        status?.let { filteredTasks = filteredTasks.filter { it.status == status } }
        priority?.let { filteredTasks = filteredTasks.filter { it.priority == priority } }
        
        // Apply sorting
        filteredTasks = when (sortOrder) {
            SortOrder.PRIORITY_ASC -> filteredTasks.sortedBy { it.priority }
            SortOrder.PRIORITY_DESC -> filteredTasks.sortedByDescending { it.priority }
            SortOrder.DUE_DATE_ASC -> filteredTasks.sortedBy { it.dueDate ?: Long.MAX_VALUE }
            SortOrder.DUE_DATE_DESC -> filteredTasks.sortedByDescending { it.dueDate ?: 0 }
            SortOrder.STATUS -> filteredTasks.sortedBy { it.status }
            SortOrder.CREATED_DATE_ASC -> filteredTasks.sortedBy { it.createdTimestamp }
            SortOrder.CREATED_DATE_DESC -> filteredTasks.sortedByDescending { it.createdTimestamp }
        }
        
        filteredTasks
    }
    
    init {
        loadTasks()
    }
    
    fun loadTasks() {
        viewModelScope.launch {
            tasksFlow
                .onStart { _uiState.value = TaskUiState.Loading }
                .catch { error ->
                    _uiState.value = TaskUiState.Error("Failed to load tasks: ${error.message}")
                }
                .collect { tasks ->
                    _uiState.value = if (tasks.isEmpty()) {
                        TaskUiState.Empty
                    } else {
                        TaskUiState.Success(tasks)
                    }
                }
        }
    }
    
    // Search and filter methods
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun updateFilterStatus(status: Task.Status?) {
        _selectedStatus.value = status
    }
    
    fun updateFilterPriority(priority: Task.Priority?) {
        _selectedPriority.value = priority
    }
    
    fun updateSortOrder(sortOrder: SortOrder) {
        _sortOrder.value = sortOrder
    }
    
    // Form methods
    fun updateFormTitle(title: String) {
        val validation = TaskValidation.validateTitle(title)
        _formState.update { it.copy(
            title = title,
            titleError = validation.errorMessage
        ) }
    }
    
    fun updateFormDescription(description: String) {
        val validation = TaskValidation.validateDescription(description)
        _formState.update { it.copy(
            description = description,
            descriptionError = validation.errorMessage
        ) }
    }
    
    fun updateFormPriority(priority: Task.Priority) {
        _formState.update { it.copy(priority = priority) }
    }
    
    fun updateFormStatus(status: Task.Status) {
        _formState.update { it.copy(status = status) }
    }
    
    fun updateFormDueDate(dueDate: Long?) {
        val validation = TaskValidation.validateDueDate(dueDate)
        _formState.update { it.copy(
            dueDate = dueDate,
            dueDateError = validation.errorMessage
        ) }
    }
    
    fun loadTaskForEditing(taskId: Long) {
        viewModelScope.launch {
            try {
                val task = repository.getTaskById(taskId)
                task?.let {
                    _formState.value = TaskFormState(
                        id = task.id,
                        title = task.title,
                        description = task.description,
                        priority = task.priority,
                        status = task.status,
                        dueDate = task.dueDate
                    )
                }
            } catch (e: Exception) {
                _actions.emit(TaskAction.ShowError("Failed to load task: ${e.message}"))
            }
        }
    }
    
    fun resetForm() {
        _formState.value = TaskFormState()
    }
    
    // CRUD Operations
    fun saveTask() {
        viewModelScope.launch {
            val form = _formState.value
            val validation = TaskValidation.validateTask(form.title, form.description, form.dueDate)
            
            if (!validation.isValid) {
                _actions.emit(TaskAction.ShowError(validation.errorMessage ?: "Invalid task data"))
                return@launch
            }
            
            try {
                val task = Task(
                    id = form.id,
                    title = form.title,
                    description = form.description,
                    priority = form.priority,
                    status = form.status,
                    dueDate = form.dueDate
                )
                
                if (form.id > 0) {
                    repository.updateTask(task)
                    _actions.emit(TaskAction.TaskUpdated)
                } else {
                    repository.insertTask(task)
                    _actions.emit(TaskAction.TaskCreated)
                }
                
                resetForm()
            } catch (e: Exception) {
                _actions.emit(TaskAction.ShowError("Failed to save task: ${e.message}"))
            }
        }
    }
    
    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteTaskById(taskId)
                _actions.emit(TaskAction.TaskDeleted)
            } catch (e: Exception) {
                _actions.emit(TaskAction.ShowError("Failed to delete task: ${e.message}"))
            }
        }
    }
}

// UI State
sealed class TaskUiState {
    data object Loading : TaskUiState()
    data object Empty : TaskUiState()
    data class Error(val message: String) : TaskUiState()
    data class Success(val tasks: List<Task>) : TaskUiState()
}

// Form State
data class TaskFormState(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val priority: Task.Priority = Task.Priority.MEDIUM,
    val status: Task.Status = Task.Status.TO_DO,
    val dueDate: Long? = null,
    val titleError: String? = null,
    val descriptionError: String? = null,
    val dueDateError: String? = null
) {
    val isValid: Boolean
        get() = titleError == null && descriptionError == null && dueDateError == null &&
                title.isNotBlank()
}

// Actions
sealed class TaskAction {
    data object TaskCreated : TaskAction()
    data object TaskUpdated : TaskAction()
    data object TaskDeleted : TaskAction()
    data class ShowError(val message: String) : TaskAction()
}

// Sort Order
enum class SortOrder {
    PRIORITY_ASC, PRIORITY_DESC, DUE_DATE_ASC, DUE_DATE_DESC, STATUS, CREATED_DATE_ASC, CREATED_DATE_DESC
}