package e.tracker.task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import e.tracker.task.entrypoint.TasksAppEntryPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TasksAppEntryPoint()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    TasksAppEntryPoint()
}