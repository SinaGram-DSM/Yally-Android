package com.sinagram.yallyandroid.Detail.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import com.bumptech.glide.Glide
import com.sinagram.yallyandroid.Detail.ViewModel.DetailPostViewModel
import com.sinagram.yallyandroid.Home.Data.EditPostRequest
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Network.YallyConnector
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.TextWatcherImpl
import kotlinx.android.synthetic.main.activity_edit_post.*
import java.io.File

class EditPostActivity : AppCompatActivity() {
    private val postData: Post by lazy {
        intent.getParcelableExtra("editPost")!!
    }
    private val editPostRequest: EditPostRequest = EditPostRequest()
    private val detailPostViewModel: DetailPostViewModel by viewModels()
    private val REQUEST_GALLERY = 200
    private val REQUEST_AUDIO = 300
    private var isClickRecorder: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)

        setViews()
        edit_post_complete_textView.setOnClickListener {
            val content = edit_post_content_editText.text.toString()
            if (content.isNotEmpty()) {
                detailPostViewModel.toEditPost(postData.id!!, editPostRequest)
                finish()
            }
        }
    }

    private fun setViews() {
        Glide.with(this).load(YallyConnector.s3 + postData.user.img).circleCrop().into(
            edit_post_user_imageView
        )
        edit_post_content_editText.setText(postData.content)
        edit_post_content_editText.addTextChangedListener(object : TextWatcherImpl() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                editPostRequest.content = s.toString()
            }
        })

        edit_voiceFile_button.setOnClickListener {
            openAudio()
        }

        edit_voiceCover_button.setOnClickListener {
            openGallery()
        }

        edit_recording_button.setOnClickListener {
            if (!isClickRecorder) {
                editPostRequest.sound = File(Environment.getExternalStorageDirectory(), "yally.mp3")
                detailPostViewModel.startRecord(editPostRequest.sound!!.absolutePath, 50000)
                edit_post_complete_textView.isEnabled = false
            } else {
                detailPostViewModel.stopRecord()
                edit_post_complete_textView.isEnabled = true
            }
        }

        detailPostViewModel.recorderLiveData.observe(this, {
            val message = if (it) "녹음이 시작되었습니다." else "녹음이 종료되었습니다."
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            isClickRecorder = !isClickRecorder
        })
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY)
    }

    private fun openAudio() {
        val intent = Intent()
        intent.type = "audio/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_AUDIO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            when(requestCode) {
                REQUEST_GALLERY -> {
                    try {
                        editPostRequest.img = data.data!!.toFile()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                REQUEST_AUDIO -> {
                    try {
                        val uri: Uri = data.data!!
                        editPostRequest.sound = uri.toFile()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun backPress(view: View) {
        onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        detailPostViewModel.stopRecord()
    }
}