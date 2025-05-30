package com.nahyun.mz.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.nahyun.mz.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object BindingAdapter {
    private const val TIME_PATTERN = "HH:mm"
    private const val DATE_PATTERN = "MM/dd"
    private const val FULL_DATE_PATTERN = "MM/dd HH:mm"

    @JvmStatic
    @BindingAdapter("fluidDate")
    fun getFluidDate(textView: TextView, date: LocalDateTime) { // "2024-08-28T14:11:52" 형태의 LocalDateTime
        // 현재 시간과의 차이를 계산
        val now = LocalDateTime.now()
        val minuteDifference = ChronoUnit.MINUTES.between(date, now)
        val hoursDifference = ChronoUnit.HOURS.between(date, now)
        val daysDifference = ChronoUnit.DAYS.between(date.toLocalDate(), now.toLocalDate())

        textView.text = when {
            hoursDifference < 1 -> "${minuteDifference}분 전"
            daysDifference < 1 -> date.format(DateTimeFormatter.ofPattern(TIME_PATTERN)) // 시간으로 표시
            else -> date.format(DateTimeFormatter.ofPattern(DATE_PATTERN)) // 이후는 날짜로 표시
        }
    }

    @JvmStatic
    @BindingAdapter("formattedDate")
    fun getFormattedDate(textView: TextView, date: LocalDateTime) {
        textView.text = date.format(DateTimeFormatter.ofPattern(FULL_DATE_PATTERN))
    }

    @JvmStatic
    @BindingAdapter("profileImage")
    fun loadProfileImage(view: ImageView, imageUrl: String?) {
        val defaultImage = R.drawable.ic_profile_default
        Glide.with(view)
            .load(imageUrl)
            .placeholder(defaultImage)
            .error(defaultImage)
            .fallback(defaultImage)
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, imageUrl: String?) {
        val defaultImage = R.color.tint
        Glide.with(view)
            .load(imageUrl)
            .placeholder(defaultImage)
            .error(defaultImage)
            .fallback(defaultImage)
            .into(view)
    }
}