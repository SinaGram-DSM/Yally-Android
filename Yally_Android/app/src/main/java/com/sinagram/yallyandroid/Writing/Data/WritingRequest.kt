package com.sinagram.yallyandroid.Writing.Data

import java.io.File

data class WritingRequest (
    var sound: File,
    var content: String,
    var img: File,
    var hashtag:MutableList<String>
)