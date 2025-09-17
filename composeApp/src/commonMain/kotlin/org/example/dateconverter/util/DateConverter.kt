package org.example.dateconverter.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

class DateConverter {
    var year: Int
        private set

    var month: Int
        private set

    var day: Int
        private set

    constructor() {
        val current = now()
        this.year = current.year
        this.month = current.month
        this.day = current.day
    }

    constructor(
        year: Int,
        month: Int,
        day: Int
    ) {
        this.year = year
        this.month = month
        this.day = day
    }

    constructor(
        date: LocalDate
    ) {
        val date = toJalali(date)
        this.year = date.year
        this.month = date.month
        this.day = date.day
    }

    /**
     * Converts a Gregorian date to a Jalali date.
     * @param date The Gregorian [LocalDate].
     * @return A [JalaliCalendar] of Jalali year, month, and day.
     */
    fun toJalali(year: Int, month: Int, day: Int): DateConverter {
        val g_d_m: IntArray = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        val gy2: Int = if (month > 2) (year + 1) else year
        var days: Int =
            355666 + (365 * year) + ((gy2 + 3) / 4) - ((gy2 + 99) / 100) + ((gy2 + 399) / 400) + day + g_d_m[month - 1]
        var jy: Int = -1595 + (33 * (days / 12053))
        days %= 12053
        jy += 4 * (days / 1461)
        days %= 1461
        if (days > 365) {
            jy += ((days - 1) / 365)
            days = (days - 1) % 365
        }
        var jm: Int
        var jd: Int
        if (days < 186) {
            jm = 1 + (days / 31)
            jd = 1 + (days % 31)
        } else {
            jm = 7 + ((days - 186) / 30)
            jd = 1 + ((days - 186) % 30)
        }
        return DateConverter(jy, jm, jd)
    }

    /**
     * Converts a Gregorian date to a Jalali date.
     * @param date The Gregorian [LocalDate].
     * @return A [JalaliCalendar] of Jalali year, month, and day.
     */
    fun toJalali(
        date: LocalDate
    ): DateConverter = toJalali(
        year = date.year,
        day = date.day,
        month = date.month.number
    )

    /**
     * Converts a Jalali date to a Gregorian date.
     * @param year Jalali year.
     * @param month Jalali month.
     * @param day Jalali day.
     * @return A Gregorian [LocalDate].
     */
    fun toGregorian(year: Int, month: Int, day: Int): LocalDate {
        val jy1: Int = year + 1595
        var days: Int =
            -355668 + (365 * jy1) + ((jy1 / 33) * 8) + (((jy1 % 33) + 3) / 4) + day + (if (month < 7) ((month - 1) * 31) else (((month - 7) * 30) + 186))
        var gy: Int = 400 * (days / 146097)
        days %= 146097
        if (days > 36524) {
            gy += 100 * (--days / 36524)
            days %= 36524
            if (days >= 365) days++
        }
        gy += 4 * (days / 1461)
        days %= 1461
        if (days > 365) {
            gy += ((days - 1) / 365)
            days = (days - 1) % 365
        }
        var gd: Int = days + 1
        val sal_a: IntArray = intArrayOf(
            0,
            31,
            if ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)) 29 else 28,
            31,
            30,
            31,
            30,
            31,
            31,
            30,
            31,
            30,
            31
        )
        var gm = 0
        while (gm < 13 && gd > sal_a[gm]) gd -= sal_a[gm++]
        return LocalDate(gy, gm, gd)
    }

    /**
     * Converts a Jalali date to a Gregorian date.
     * @return A Gregorian [LocalDate].
     */
    fun toGregorian(): LocalDate = toGregorian(
        year = this.year,
        month = this.month,
        day = this.day
    )

    /**
     * Gets the current Jalali date.
     * @return A [JalaliCalendar] of Jalali year, month, and day.
     */
    @OptIn(ExperimentalTime::class)
    private fun now(): DateConverter {
        val timeZone = TimeZone.currentSystemDefault()
        val localDateTime = kotlin.time.Clock.System.now().toLocalDateTime(timeZone)
        return toJalali(localDateTime.date)
    }

    /**
     * Determines if a given Jalali year is a leap year.
     * @param year Jalali year.
     * @return True if the year is a leap year, false otherwise.
     */
    fun isLeap(year: Int): Boolean = getLeapFactor(year) == 0

    /**
     * Determines if a given Jalali year is a leap year.
     * @return True if the year is a leap year, false otherwise.
     */
    fun isLeap(): Boolean = isLeap(this.year)

    /**
     * Gets the name of a Jalali month in Persian.
     * @param month Jalali month (1-based).
     * @return The Persian name of the month.
     */
    fun getMonthName(
        month: Int
    ): String = when (month) {
        1    -> "فروردین"
        2    -> "اردیبهشت"
        3    -> "خرداد"
        4    -> "تیر"
        5    -> "مرداد"
        6    -> "شهریور"
        7    -> "مهر"
        8    -> "آبان"
        9    -> "آذر"
        10   -> "دی"
        11   -> "بهمن"
        12   -> "اسفند"
        else -> "نامعلوم"
    }

    /**
     * Gets the name of a Jalali month in Persian.
     * @return The Persian name of the month.
     */
    fun getMonthName(): String = getMonthName(this.month)

    /**
     * Gets the length of a Jalali year (365 or 366 days).
     * @param year Jalali year.
     * @return The number of days in the year.
     */
    fun getYearLength(year: Int): Int {
        return if (isLeap(year)) 366 else 365
    }

    /**
     * Gets the length of a Jalali year (365 or 366 days).
     * @return The number of days in the year.
     */
    fun getYearLength(): Int = getYearLength(this.year)

    /**
     * Gets the length of a specific Jalali month.
     * @param year Jalali year.
     * @param month Jalali month (1-based).
     * @return The number of days in the month.
     */
    fun getMonthLength(
        year: Int,
        month: Int
    ): Int = when {
        month < 7   -> 31
        month < 12  -> 30
        month == 12 -> if (isLeap(year)) 30 else 29
        else        -> 0
    }

    /**
     * Gets the length of a specific Jalali month.
     * @param month Jalali month (1-based).
     * @return The number of days in the month.
     */
    fun getMonthLength(month: Int): Int = getMonthLength(this.year, month)

    /**
     * Gets the length of a specific Jalali month.
     * @return The number of days in the month.
     */
    fun getMonthLength(): Int = getMonthLength(this.year, this.month)

    fun getDayOfWeek(): Int = toGregorian().dayOfWeek.isoDayNumber % 7 + 1

    /**
     * Gets the Persian name of the day of the week for a given Gregorian date.
     * @param date A Gregorian [LocalDate].
     * @return The Persian name of the day of the week.
     */
    fun getDayOfWeekName(): String = when (getDayOfWeek()) {
        1    -> "یک‌شنبه"
        2    -> "دوشنبه"
        3    -> "سه‌شنبه"
        4    -> "چهارشنبه"
        5    -> "پنجشنبه"
        6    -> "جمعه"
        7    -> "شنبه"
        else -> "نامعلوم"
    }

    private fun getLeapFactor(jalaliYear: Int): Int {
        val breaks = intArrayOf(
            -61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210,
            1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178
        )

        var jp = breaks[0]

        for (j in 1..19) {
            val jm = breaks[j]
            val jump = jm - jp
            if (jalaliYear < jm) {
                var n = jalaliYear - jp

                if ((jump - n) < 6)
                    n = n - jump + (jump + 4) / 33 * 33

                var leap = ((n + 1) % 33 - 1) % 4

                if (leap == -1)
                    leap = 4

                return leap
            }

            jp = jm
        }

        return 0
    }

    override fun toString(): String {
        return "$year/$month/$day"
    }

}