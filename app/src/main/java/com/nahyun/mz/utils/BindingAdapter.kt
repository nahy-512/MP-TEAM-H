package com.nahyun.mz.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.nahyun.mz.R
import java.time.LocalDateTime

object BindingAdapter {
    val timeConverter = TimeConverter

    @JvmStatic
    @BindingAdapter("fluidDate")
    fun getFluidDate(textView: TextView, date: LocalDateTime) { // "2024-08-28T14:11:52" 형태의 LocalDateTime
        textView.text = timeConverter.getFluidDate(date)
    }

    @JvmStatic
    @BindingAdapter("formattedDate")
    fun getFormattedDate(textView: TextView, date: LocalDateTime) {
        textView.text = timeConverter.getFormattedDate(date)
    }

    @JvmStatic
    @BindingAdapter("originalText", "isAuthor")
    fun authorText(textView: TextView, originalText: String, isAuthor: Boolean) {
        textView.text = if (!isAuthor) originalText else "$originalText (글쓴이)"
    }

    @JvmStatic
    @BindingAdapter("imgRes")
    fun loadResImage(view: ImageView, resId: Int) {
        view.setImageResource(resId)
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