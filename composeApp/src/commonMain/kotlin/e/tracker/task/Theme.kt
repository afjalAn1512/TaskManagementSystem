package e.tracker.task

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import e.tracker.task.AppColors.Background
import e.tracker.task.AppColors.DarkBackground
import e.tracker.task.AppColors.DarkError
import e.tracker.task.AppColors.DarkOnBackground
import e.tracker.task.AppColors.DarkOnError
import e.tracker.task.AppColors.DarkOnPrimary
import e.tracker.task.AppColors.DarkOnSecondary
import e.tracker.task.AppColors.DarkOnSurface
import e.tracker.task.AppColors.DarkOnTertiary
import e.tracker.task.AppColors.DarkPrimary
import e.tracker.task.AppColors.DarkSecondary
import e.tracker.task.AppColors.DarkSurface
import e.tracker.task.AppColors.DarkTertiary
import e.tracker.task.AppColors.OnBackground
import e.tracker.task.AppColors.OnError
import e.tracker.task.AppColors.OnPrimary
import e.tracker.task.AppColors.OnSecondary
import e.tracker.task.AppColors.OnSurface
import e.tracker.task.AppColors.Error
import e.tracker.task.AppColors.OnTertiary
import e.tracker.task.AppColors.Primary
import e.tracker.task.AppColors.Secondary
import e.tracker.task.AppColors.Surface
import e.tracker.task.AppColors.Tertiary

@Composable
fun TasksAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette,
        content = content
    )
}

private val LightColorPalette = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
    background = Background,
    surface = Surface,
    error = Error,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary,
    onTertiary = OnTertiary,
    onBackground = OnBackground,
    onSurface = OnSurface,
    onError = OnError,
)

private val DarkColorPalette = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    error = DarkError,
    onPrimary = DarkOnPrimary,
    onSecondary = DarkOnSecondary,
    onTertiary = DarkOnTertiary,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
    onError = DarkOnError,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTopAppBarColorsTheme() = TopAppBarDefaults.topAppBarColors(
    containerColor = MaterialTheme.colorScheme.primary,
    titleContentColor = MaterialTheme.colorScheme.onPrimary,
    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
)