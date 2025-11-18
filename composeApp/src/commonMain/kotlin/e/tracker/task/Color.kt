package e.tracker.task

import androidx.compose.ui.graphics.Color

object AppColors {
    val Primary = Color(0xFF6750A4)       // Deep purple, modern & vibrant
    val Secondary = Color(0xFF625B71)      // Muted violet, great for secondary elements
    val Tertiary = Color(0xFF7D5260)       // Elegant dark rose, good for accents

    val Background = Color(0xFFFDF8FD)     // Light pastel background for readability
    val Surface = Color(0xFFFFFBFE)        // Almost white, but with warmth
    val Error = Color(0xFFB3261E)          // Strong red for error states

    val OnPrimary = Color(0xFFFFFFFF)      // White for contrast on primary
    val OnSecondary = Color(0xFFFFFFFF)    // White text/icons on secondary
    val OnTertiary = Color(0xFFFFFFFF)     // White text/icons on tertiary
    val OnBackground = Color(0xFF1C1B1F)   // Dark gray for readable text
    val OnSurface = Color(0xFF1C1B1F)      // Same as OnBackground
    val OnError = Color(0xFFFFFFFF)        // White text/icons on error states

    // 🌙 **Dark Mode Variant**
    val DarkPrimary = Color(0xFFD0BCFF)     // Lighter purple for dark themes
    val DarkSecondary = Color(0xFFCCC2DC)   // Softer violet-gray
    val DarkTertiary = Color(0xFFEFB8C8)    // Muted pink accent

    val DarkBackground = Color(0xFF1C1B1F)  // Dark charcoal background
    val DarkSurface = Color(0xFF1C1B1F)     // Same as background for seamless UI
    val DarkError = Color(0xFFF2B8B5)       // Softer error red

    val DarkOnPrimary = Color(0xFF381E72)   // Deep purple for contrast
    val DarkOnSecondary = Color(0xFF332D41) // Dark violet for contrast
    val DarkOnTertiary = Color(0xFF492532)  // Dark pinkish tone
    val DarkOnBackground = Color(0xFFE6E1E5) // Light text for readability
    val DarkOnSurface = Color(0xFFE6E1E5)    // Same as OnBackground
    val DarkOnError = Color(0xFF601410)     // Darker red for contrast
}