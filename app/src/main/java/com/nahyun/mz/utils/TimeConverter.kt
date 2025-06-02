package com.nahyun.mz.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

object TimeConverter {
    private const val FULL_DATE_PATTERN = "MM/dd HH:mm"

    private val fullDateFormat = SimpleDateFormat(FULL_DATE_PATTERN, Locale.getDefault())

    fun parseTimeStampToLocalDateTime(timestamp: Timestamp): LocalDateTime {
        // 1. Timestamp → Instant
        val instant = Instant.ofEpochSecond(timestamp.seconds, timestamp.nanoseconds.toLong())
        // 2. Instant → LocalDateTime (예: 시스템 기본 시간대)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

        println(localDateTime)
        return localDateTime
    }

    @JvmStatic
    fun parseLongToDateText(longDate: Long): String {
        return parseDateTimeToDateText(parseLongToDateTime(longDate))
    }

    private fun parseLongToDateTime(long: Long): Date {
        return Date(long)
    }

    private fun parseDateTimeToDateText(date: Date): String {
        return fullDateFormat.format(date)
    }
}