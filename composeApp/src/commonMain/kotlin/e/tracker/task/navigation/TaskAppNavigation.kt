package e.tracker.task.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import e.tracker.task.screens.AddEditTaskScreen
import e.tracker.task.screens.TaskListScreen
import e.tracker.task.viewmodel.TaskAction
import e.tracker.task.viewmodel.TaskViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object TaskListRoute

@Serializable
object AddEditTaskRoute

@Serializable
data class AddEditTaskWithIdRoute(val taskId: Long)

@Composable
fun TaskAppNavigation() {
    val navController = rememberNavController()
    val viewModel: TaskViewModel = koinViewModel()

    // Handle navigation actions from ViewModel
    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is TaskAction.TaskCreated -> {
                    navController.popBackStack()
                }
                is TaskAction.TaskUpdated -> {
                    navController.popBackStack()
                }
                is TaskAction.ShowError -> {
                }
                else -> {}
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = TaskListRoute
    ) {
        composable<TaskListRoute> {
            TaskListScreen(
                viewModel = viewModel,
                onAddTask = {
                    navController.navigate(AddEditTaskRoute)
                },
                onEditTask = { taskId ->
                    navController.navigate(AddEditTaskWithIdRoute(taskId))
                }
            )
        }

        composable<AddEditTaskRoute> {
            AddEditTaskScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable<AddEditTaskWithIdRoute> { backStackEntry ->
            val taskId = backStackEntry.toRoute<AddEditTaskWithIdRoute>().taskId

            LaunchedEffect(taskId) {
                if (taskId > 0) {
                    viewModel.loadTaskForEditing(taskId)
                }
            }

            AddEditTaskScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}