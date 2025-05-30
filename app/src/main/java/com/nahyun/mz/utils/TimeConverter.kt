package com.nahyun.mz.utils

import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object TimeConverter {
    fun parseTimeStampToLocalDateTime(timestamp: Timestamp): LocalDateTime {
        // 1. Timestamp → Instant
        val instant = Instant.ofEpochSecond(timestamp.seconds, timestamp.nanoseconds.toLong())
        // 2. Instant → LocalDateTime (예: 시스템 기본 시간대)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

        println(localDateTime)
        return localDateTime
    }
}