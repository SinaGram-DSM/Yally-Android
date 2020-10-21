package com.sinagram.yallyandroid.Home.Data

import android.graphics.Typeface
import android.text.Spannable
import android.text.Spanned
import android.text.style.StyleSpan

class PostTags(val content: String) {
    fun applyBoldToTags(span: Spannable) {
        var isCharacter = false
        var index = 0

        while (content.length >= index + 1) {
            if (content[index] == '#') isCharacter = !isCharacter
            if (content[index] == ' ') isCharacter = false
            if (isCharacter) {
                span.setSpan(
                    StyleSpan(Typeface.BOLD),
                    index,
                    index + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            index++
        }
    }
}