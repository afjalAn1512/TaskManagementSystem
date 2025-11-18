package e.tracker.task.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
@OptIn(ExperimentalTime::class)
object DateFormatter {
    

    fun formatDate(millis: Long?, pattern: String = "MMM dd, yyyy"): String {
        if (millis == null) return ""
        
        return try {
            val instant = Instant.fromEpochMilliseconds(millis)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            
            when (pattern) {
                "MMM dd, yyyy" -> {
                    val month = getMonthAbbreviation(localDateTime.month)
                    val day = localDateTime.dayOfMonth
                    val year = localDateTime.year
                    "$month $day, $year"
                }
                "yyyy-MM-dd" -> {
                    "${localDateTime.year}-${localDateTime.month.number.toString().padStart(2, '0')}-${localDateTime.dayOfMonth.toString().padStart(2, '0')}"
                }
                "dd/MM/yyyy" -> {
                    "${localDateTime.dayOfMonth.toString().padStart(2, '0')}/${localDateTime.month.number.toString().padStart(2, '0')}/${localDateTime.year}"
                }
                else -> {
                    // Default format
                    val month = getMonthAbbreviation(localDateTime.month)
                    val day = localDateTime.dayOfMonth
                    val year = localDateTime.year
                    "$month $day, $year"
                }
            }
        } catch (e: Exception) {
            "Invalid date"
        }
    }
    
    fun formatDateTime(millis: Long?): String {
        if (millis == null) return ""
        
        return try {
            val instant = Instant.fromEpochMilliseconds(millis)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            
            val month = getMonthAbbreviation(localDateTime.month)
            val day = localDateTime.dayOfMonth
            val year = localDateTime.year
            val hour = localDateTime.hour
            val minute = localDateTime.minute.toString().padStart(2, '0')
            
            "$month $day, $year at $hour:$minute"
        } catch (e: Exception) {
            "Invalid date"
        }
    }
    
    private fun getMonthAbbreviation(month: kotlinx.datetime.Month): String {
        return when (month) {
            kotlinx.datetime.Month.JANUARY -> "Jan"
            kotlinx.datetime.Month.FEBRUARY -> "Feb"
            kotlinx.datetime.Month.MARCH -> "Mar"
            kotlinx.datetime.Month.APRIL -> "Apr"
            kotlinx.datetime.Month.MAY -> "May"
            kotlinx.datetime.Month.JUNE -> "Jun"
            kotlinx.datetime.Month.JULY -> "Jul"
            kotlinx.datetime.Month.AUGUST -> "Aug"
            kotlinx.datetime.Month.SEPTEMBER -> "Sep"
            kotlinx.datetime.Month.OCTOBER -> "Oct"
            kotlinx.datetime.Month.NOVEMBER -> "Nov"
            kotlinx.datetime.Month.DECEMBER -> "Dec"
        }
    }
    
    fun isToday(millis: Long): Boolean {
        val today = Instant.fromEpochMilliseconds(DateTimeHelper.currentTimeMillis())
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        
        val targetDate = Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            
        return today == targetDate
    }
    
    fun isTomorrow(millis: Long): Boolean {
        val tomorrow = Instant.fromEpochMilliseconds(DateTimeHelper.currentTimeMillis() + 24 * 60 * 60 * 1000)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        
        val targetDate = Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            
        return tomorrow == targetDate
    }
    
    fun isYesterday(millis: Long): Boolean {
        val yesterday = Instant.fromEpochMilliseconds(DateTimeHelper.currentTimeMillis() - 24 * 60 * 60 * 1000)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        
        val targetDate = Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            
        return yesterday == targetDate
    }
    
    fun formatDateWithRelative(millis: Long?): String {
        if (millis == null) return ""
        
        return when {
            isToday(millis) -> "Today"
            isTomorrow(millis) -> "Tomorrow"
            isYesterday(millis) -> "Yesterday"
            else -> formatDate(millis, "MMM dd, yyyy")
        }
    }
}