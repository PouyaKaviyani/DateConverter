package org.example.dateconverter.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


object IsoDateTime {

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

    /**
     * محاسبه فاصله بین دو تاریخ میلادی به صورت تقریبی و خوانا (سال، ماه، روز)
     * همیشه تاریخ کوچکتر را به عنوان شروع در نظر می‌گیرد
     */
    fun gregorianDateDifference(
        date1: LocalDate,
        date2: LocalDate
    ): String {
        val start = if (date1 <= date2) date1 else date2
        val end = if (date1 <= date2) date2 else date1

        var years = end.year - start.year
        var months = end.month.number - start.month.number
        var days = end.day - start.day

        // تنظیم روزها اگر منفی شد
        if (days < 0) {
            months -= 1
            // محاسبه تعداد روزهای ماه قبلی
            val previousMonth = if (start.month.number == 1) 12 else start.month.number - 1
            val previousYear = if (start.month.number == 1) start.year - 1 else start.year
            days += gregorianMonthLength(previousYear, previousMonth)
        }

        // تنظیم ماه‌ها اگر منفی شد
        if (months < 0) {
            years -= 1
            months += 12
        }

        return buildString {
            if (years > 0) append("$years سال ")
            if (months > 0) append("$months ماه ")
            if (days > 0 || (years == 0 && months == 0)) append("$days روز")

            if (isEmpty()) append("همان روز")
        }.trim()
    }

    /**
     * نسخه ساده‌تر: فاصله به تعداد روز دقیق
     */
    fun gregorianDaysBetween(date1: LocalDate, date2: LocalDate): Long {
        val start = if (date1 <= date2) date1 else date2
        val end = if (date1 <= date2) date2 else date1

        // تبدیل به تعداد روز از یک مبدا مشترک (مثلاً epoch day در kotlinx.datetime)
        return (end.toEpochDays() - start.toEpochDays())
    }

    fun safeJalaliDay(year: Int, month: Int, day: Int): Int {
        val maxDay = DateConverter(year, month, 1).getMonthLength()
        return day.coerceIn(1, maxDay)
    }

    fun safeGregorianDay(year: Int, month: Int, day: Int): Int {
        val maxDay = gregorianMonthLength(year, month)
        return day.coerceIn(1, maxDay)
    }

}
