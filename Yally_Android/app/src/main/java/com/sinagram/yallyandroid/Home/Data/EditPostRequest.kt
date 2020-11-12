package com.sinagram.yallyandroid.Home.Data

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class EditPostRequest(
    var content: String = "",
    var sound: File? = null,
    var img: File? = null,
    var soundPart: MultipartBody.Part? = null,
    var imgPart: MultipartBody.Part? = null,
    var requestHashMap: HashMap<String, RequestBody> = HashMap()
) {
    fun addCondtent() {
        requestHashMap["content"] = content.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }

    fun addSound() {
        soundPart = MultipartBody.Part.createFormData(
            "sound",
            sound!!.name,
            sound!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
    }

    fun addImage() {
        imgPart = MultipartBody.Part.createFormData(
            "img",
            img!!.name,
            img!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
    }

    fun addHashTags() {
        if (content != "") {

            val res = mutableListOf<Char>()
            var isCharacter = false

            for (i in content.indices) {
                if (content[i] == '#') isCharacter = true
                if (content[i] == ' ') isCharacter = false
                if (isCharacter) res.add(content[i])
            }

            val result = mutableListOf<String>()
            var count = -1
            if ('#' in res) {
                for (i in res) {
                    if (i == '#') {
                        count++
                        result.add("")
                    } else {
                        result[count] += i.toString()
                    }
                }
            }

            for (i in result.indices) {
                requestHashMap["hashtags[$i]"] =
                    result[i].toRequestBody("multipart/form-data".toMediaTypeOrNull())
            }
        }
    }
}