package com.sinagram.yallyandroid.Util

import java.text.SimpleDateFormat
import java.util.*

class PostDate(private val uploadedDate: String) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun setTimeFromUploadedTime(): String? {
        try {
            val dateCreated = dateFormat.parse(uploadedDate)!!
            val duration = getCurrentTime() - dateCreated.time
            val min = duration / 60000

            return when {
                min < 1 -> "방금전"
                min in 1..59 -> "${min}분"
                min in 60..1440 -> "${duration / 3600000}시간"
                else -> SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(dateCreated.time)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    private fun getCurrentTime(): Long {
        val now = System.currentTimeMillis()
        return Date(now).time
    }
}