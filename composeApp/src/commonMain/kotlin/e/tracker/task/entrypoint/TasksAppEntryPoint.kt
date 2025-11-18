package e.tracker.task.entrypoint

import androidx.compose.runtime.Composable
import e.tracker.task.TasksAppTheme
import e.tracker.task.navigation.TaskAppNavigation

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun TasksAppEntryPoint() {
    TasksAppTheme {
        TaskAppNavigation()
    }
}