package e.tracker.task.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import e.tracker.task.database.Task
import e.tracker.task.utils.DateFormatter
import e.tracker.task.viewmodel.SortOrder
import e.tracker.task.viewmodel.TaskUiState
import e.tracker.task.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onAddTask: () -> Unit,
    onEditTask: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showFilters by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        viewModel.updateSearchQuery(searchQuery)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTask) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Tasks") },
                actions = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(Icons.Default.Sort, contentDescription = "Filter and Sort")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search tasks...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Filter and Sort Options
            if (showFilters) {
                FilterSortSection(viewModel = viewModel)
            }

            // Task List Content
            when (uiState) {
                is TaskUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is TaskUiState.Empty -> {
                    EmptyState(onAddTask = onAddTask)
                }

                is TaskUiState.Error -> {
                    ErrorState(
                        message = (uiState as TaskUiState.Error).message,
                        onRetry = { /* Reload logic */ }
                    )
                }

                is TaskUiState.Success -> {
                    val tasks = (uiState as TaskUiState.Success).tasks
                    TaskList(
                        tasks = tasks,
                        onEditTask = onEditTask,
                        onDeleteTask = { taskId -> viewModel.deleteTask(taskId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterSortSection(viewModel: TaskViewModel) {
    var expandedSort by remember { mutableStateOf(false) }
    var expandedStatus by remember { mutableStateOf(false) }
    var expandedPriority by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Status Filter
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Status:", modifier = Modifier.weight(1f))
            Box {
                Button(onClick = { expandedStatus = true }) {
                    Text("Select Status")
                }
                DropdownMenu(
                    expanded = expandedStatus,
                    onDismissRequest = { expandedStatus = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All") },
                        onClick = {
                            viewModel.updateFilterStatus(null)
                            expandedStatus = false
                        }
                    )
                    Task.Status.entries.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status.name.replace('_', ' ')) },
                            onClick = {
                                viewModel.updateFilterStatus(status)
                                expandedStatus = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Priority Filter
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Priority:", modifier = Modifier.weight(1f))
            Box {
                Button(onClick = { expandedPriority = true }) {
                    Text("Select Priority")
                }
                DropdownMenu(
                    expanded = expandedPriority,
                    onDismissRequest = { expandedPriority = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All") },
                        onClick = {
                            viewModel.updateFilterPriority(null)
                            expandedPriority = false
                        }
                    )
                    Task.Priority.values().forEach { priority ->
                        DropdownMenuItem(
                            text = { Text(priority.name) },
                            onClick = {
                                viewModel.updateFilterPriority(priority)
                                expandedPriority = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sort Options
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Sort by:", modifier = Modifier.weight(1f))
            Box {
                Button(onClick = { expandedSort = true }) {
                    Text("Sort")
                }
                DropdownMenu(
                    expanded = expandedSort,
                    onDismissRequest = { expandedSort = false }
                ) {
                    SortOrder.entries.forEach { sortOrder ->
                        DropdownMenuItem(
                            text = { Text(sortOrder.name.replace('_', ' ')) },
                            onClick = {
                                viewModel.updateSortOrder(sortOrder)
                                expandedSort = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
    }
}

@Composable
private fun TaskList(
    tasks: List<Task>,
    onEditTask: (Long) -> Unit,
    onDeleteTask: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks, key = { it.id }) { task ->
            TaskItem(
                task = task,
                onEdit = { onEditTask(task.id) },
                onDelete = { onDeleteTask(task.id) }
            )
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            taskTitle = task.title,
            onConfirm = {
                onDelete()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Card(
        onClick = onEdit,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                // Priority and Delete Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Priority Chip
                    Text(
                        text = task.priority.name,
                        color = when (task.priority) {
                            Task.Priority.HIGH -> MaterialTheme.colorScheme.error
                            Task.Priority.MEDIUM -> MaterialTheme.colorScheme.primary
                            Task.Priority.LOW -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        style = MaterialTheme.typography.labelSmall
                    )

                    // Delete Button
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Task",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            if (task.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = when (task.status) {
                                    Task.Status.TO_DO -> MaterialTheme.colorScheme.onSurfaceVariant
                                    Task.Status.IN_PROGRESS -> MaterialTheme.colorScheme.primary
                                    Task.Status.DONE -> MaterialTheme.colorScheme.tertiary
                                },
                                shape = CircleShape
                            )
                    )

                    Text(
                        text = task.status.name.replace('_', ' '),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                task.dueDate?.let { dueDate ->
                    val dateText = if (task.isOverdue) {
                        "Overdue: ${DateFormatter.formatDate(dueDate, "MMM dd, yyyy")}"
                    } else {
                        "Due: ${DateFormatter.formatDateWithRelative(dueDate)}"
                    }
                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (task.isOverdue) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    taskTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete Task",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete \"$taskTitle\"? This action cannot be undone.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun EmptyState(onAddTask: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No tasks yet",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Create your first task to get started",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAddTask) {
            Text("Create Task")
        }
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Try Again")
        }
    }
}