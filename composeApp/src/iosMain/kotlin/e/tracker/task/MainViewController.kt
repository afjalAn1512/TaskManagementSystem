package e.tracker.task

import androidx.compose.ui.window.ComposeUIViewController
import e.tracker.task.di.initKoin
import e.tracker.task.entrypoint.TasksAppEntryPoint

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    TasksAppEntryPoint()
}