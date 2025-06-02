package com.nahyun.mz.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

object TimeConverter {
    private const val TIME_PATTERN = "HH:mm"
    private const val DATE_PATTERN = "MM/dd"
    private const val FULL_DATE_PATTERN = "MM/dd HH:mm"

    private val fullDateFormat = SimpleDateFormat(FULL_DATE_PATTERN, Locale.getDefault())

    fun getFluidDate(date: LocalDateTime): String { // "2024-08-28T14:11:52" 형태의 LocalDateTime
        // 현재 시간과의 차이를 계산
        val now = LocalDateTime.now()
        val minuteDifference = ChronoUnit.MINUTES.between(date, now)
        val hoursDifference = ChronoUnit.HOURS.between(date, now)
        val daysDifference = ChronoUnit.DAYS.between(date.toLocalDate(), now.toLocalDate())

        return when {
            hoursDifference < 1 -> "${minuteDifference}분 전"
            daysDifference < 1 -> date.format(DateTimeFormatter.ofPattern(TIME_PATTERN)) // 시간으로 표시
            else -> date.format(DateTimeFormatter.ofPattern(DATE_PATTERN)) // 이후는 날짜로 표시
        }
    }

    fun getFormattedDate(date: LocalDateTime): String {
        return date.format(DateTimeFormatter.ofPattern(FULL_DATE_PATTERN))
    }

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