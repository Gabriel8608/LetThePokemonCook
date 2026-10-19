package com.example.ltpc.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TimeHelper {
    var hourOffset: Int = 0

    fun getCurrentAdjustedHour(): Int {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        var adjusted = currentHour + hourOffset
        if (adjusted >= 24) adjusted -= 24
        if (adjusted < 0) adjusted += 24
        return adjusted
    }

    fun isNightTime(): Boolean {
        val hour = getCurrentAdjustedHour()
        return hour !in 6..17
    }

    fun toggleOffset() {
        hourOffset = if (hourOffset == 0) 12 else 0
    }

    fun getFormattedCurrentTime(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, hourOffset)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(calendar.time)
    }
}
