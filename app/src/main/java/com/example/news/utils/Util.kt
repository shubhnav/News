package com.example.news.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object Util {

    @SuppressLint("SimpleDateFormat")
    fun formatDates(oldDate: Date): String? {
        val simpleDateFormat = SimpleDateFormat("dd MMM  HH:mm aa", Locale.ENGLISH)
        return simpleDateFormat.format(oldDate)
    }
}