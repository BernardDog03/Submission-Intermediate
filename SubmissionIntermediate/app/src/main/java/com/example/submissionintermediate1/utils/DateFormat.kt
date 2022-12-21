package com.example.submissionintermediate1.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormat {

    fun setLocalTime(timestamp: String): String?{

        val d: Date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).parse(timestamp) as Date
        val cal = Calendar.getInstance()
        cal.time = d
        return SimpleDateFormat("dd MMM yyyy. HH:mm:ss").format(cal.time)
    }
}