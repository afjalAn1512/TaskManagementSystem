package e.tracker.task.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import e.tracker.task.database.Task
import e.tracker.task.utils.DateFormatter
import e.tracker.task.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    viewModel: TaskViewModel,
    onBack: () -> Unit
) {
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    
    // Date Picker State
    var showDatePicker by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(if (formState.id > 0) "Edit Task" else "Add Task") 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = { viewModel.saveTask() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    enabled = formState.isValid && formState.title.isNotBlank()
                ) {
                    Text(if (formState.id > 0) "Update Task" else "Create Task")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Title Field
            OutlinedTextField(
                value = formState.title,
                onValueChange = { viewModel.updateFormTitle(it) },
                label = { Text("Title *") },
                modifier = Modifier.fillMaxWidth(),
                isError = formState.titleError != null,
                supportingText = {
                    formState.titleError?.let { error ->
                        Text(text = error)
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Description Field
            OutlinedTextField(
                value = formState.description,
                onValueChange = { viewModel.updateFormDescription(it) },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                isError = formState.descriptionError != null,
                supportingText = {
                    formState.descriptionError?.let { error ->
                        Text(text = error)
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Priority Selection
            Text("Priority", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Task.Priority.entries.forEach { priority ->
                    FilterChip(
                        selected = formState.priority == priority,
                        onClick = { viewModel.updateFormPriority(priority) },
                        label = { Text(priority.name) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Status Selection
            Text("Status", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Task.Status.entries.forEach { status ->
                    FilterChip(
                        selected = formState.status == status,
                        onClick = { viewModel.updateFormStatus(status) },
                        label = { Text(status.name.replace('_', ' ')) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Due Date Selection
            Text("Due Date (Optional)", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { showDatePicker = true }
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formState.dueDate?.let { dueDate ->
                            DateFormatter.formatDate(dueDate, "MMM dd, yyyy")
                        } ?: "Select Date"
                    )
                }
                
                if (formState.dueDate != null) {
                    TextButton(
                        onClick = { viewModel.updateFormDueDate(null) }
                    ) {
                        Text("Clear")
                    }
                }
            }
            
            formState.dueDateError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            // Date Picker Dialog
            if (showDatePicker) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { timestamp ->
                                    viewModel.updateFormDueDate(timestamp)
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}