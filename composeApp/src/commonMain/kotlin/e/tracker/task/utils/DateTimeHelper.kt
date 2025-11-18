package e.tracker.task.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object DateTimeHelper {

    fun currentTimeMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    
    fun createDate(year: Int, month: Int, day: Int): Long {
        return try {
            val localDate = LocalDate(year, month, day)
            localDate.atStartOfDayIn(TimeZone.currentSystemDefault())
                .toEpochMilliseconds()
        } catch (e: Exception) {
            currentTimeMillis()
        }
    }
    
    fun getDateComponents(millis: Long): DateComponents {
        val localDateTime = Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        return DateComponents(
            year = localDateTime.year,
            month = localDateTime.month.number,
            day = localDateTime.dayOfMonth
        )
    }
    
    data class DateComponents(val year: Int, val month: Int, val day: Int)
}

// Extension functions for easier usage
fun Long.toFormattedDate(): String = DateFormatter.formatDate(this)
fun Long.toRelativeDate(): String = DateFormatter.formatDateWithRelative(this)
fun Long.toDateTimeString(): String = DateFormatter.formatDateTime(this)