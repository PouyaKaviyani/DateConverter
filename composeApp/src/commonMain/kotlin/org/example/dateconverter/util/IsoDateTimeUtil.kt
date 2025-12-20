package org.example.dateconverter.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


object IsoDateTimeUtil {

    private val zone = TimeZone.currentSystemDefault()

    /** now -> ISO String */
    @OptIn(ExperimentalTime::class)
    fun nowIso(): String {
        val instant = Clock.System.now()
        val dt = instant.toLocalDateTime(zone)

        return buildString {
            append(dt.date)
            append('T')
            append(dt.hour.toString().padStart(2, '0'))
            append(':')
            append(dt.minute.toString().padStart(2, '0'))
            append(':')
            append(dt.second.toString().padStart(2, '0'))
        }
    }


    /** ISO String -> LocalDateTime */
    fun parseIso(iso: String): LocalDateTime {
        return LocalDateTime.parse(iso)
    }

    /** ISO String -> Gregorian LocalDate */
    fun isoToGregorianDate(iso: String): LocalDate {
        return parseIso(iso).date
    }

    /** ISO String -> Jalali DateConverter */
    fun isoToJalali(iso: String): DateConverter {
        val gDate = isoToGregorianDate(iso)
        return DateConverter(gDate)
    }

    /** Jalali DateConverter -> ISO String (Gregorian based) */
    fun jalaliToIso(
        jalali: DateConverter,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0
    ): String {
        val gDate = jalali.toGregorian()
        return LocalDateTime(
            year = gDate.year,
            month = gDate.month,
            day = gDate.day,
            hour = hour,
            minute = minute,
            second = second
        ).toString()
    }

    /** Gregorian LocalDate -> ISO String */
    fun gregorianToIso(
        date: LocalDate,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0
    ): String {
        return LocalDateTime(
            year = date.year,
            month = date.month,
            day = date.day,
            hour = hour,
            minute = minute,
            second = second
        ).toString()
    }
    fun gregorianMonthLength(year: Int, month: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (
                (year % 4 == 0 && year % 100 != 0) ||
                (year % 400 == 0)
            ) 29 else 28
            else -> 0
        }
    }

}

fun safeJalaliDay(year: Int, month: Int, day: Int): Int {
    val maxDay = DateConverter(year, month, 1).getMonthLength()
    return day.coerceIn(1, maxDay)
}

fun safeGregorianDay(year: Int, month: Int, day: Int): Int {
    val maxDay = IsoDateTimeUtil.gregorianMonthLength(year, month)
    return day.coerceIn(1, maxDay)
}
