package com.sinagram.yallyandroid.Writing.Ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Writing.Data.WritingRequest
import com.sinagram.yallyandroid.Writing.ViewModel.WritingViewModel
import kotlinx.android.synthetic.main.activity_writing.*
import java.io.File

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
private const val REQUEST_OPEN_GALLERY_PERMISSION = 300

class WritingActivity : AppCompatActivity() {
    private var count = 1
    private val OPEN_GALLERY = 1
    private val OPEN_AUDIO = 2
    private var getImageFile: File? = null
    private var getAudioFile: File? = null
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private lateinit var file: File
    private val viewModel: WritingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        setInit()

        writing_recording_button.setOnClickListener {
            count++
            recording(count)
        }

        writing_voiceCover_button.setOnClickListener {
            getImage()
        }

        writing_voiceFile_button.setOnClickListener {
            getVoice()
        }

    }

    private fun setInit() {
        file = File(Environment.getExternalStorageDirectory(), "yally.mp3")
        setSupportActionBar(writing_toolbar)
        writing_toolbar.title = " "
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_back_item -> {
                changeView()
                return true
            }
            R.id.toolbar_upload_item -> {
                postWriting()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun changeView() {
        val transaction = supportFragmentManager.beginTransaction().let {
            /*it.replace(R.id.fragment_time_line, newFragment)
            it.addToBackStack(null)
            it.commit()*/
        }
    }

    private fun postWriting() {
        var writing = writing_writing_edit.toString()
        var hashtags: MutableList<String> = viewModel.hashtags(writing)


        viewModel.writing()
        changeView()
    }

    private fun getVoice() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ("audio/*")
        intent.type = MediaStore.Audio.Media.CONTENT_TYPE
        startActivityForResult(intent, OPEN_AUDIO)
    }

    private fun getImage() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, OPEN_GALLERY)
        } else {
            ActivityCompat.requestPermissions(
                this@WritingActivity,
                permissions,
                REQUEST_OPEN_GALLERY_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Activity.RESULT_OK) {
            when (requestCode) {
                OPEN_AUDIO -> {
                    if (data?.extras?.get("data") != null) {
                        var audioUri = data.data
                        getAudioFile = File(audioUri!!.path)
                        writing_voice_imageView.setImageResource(R.drawable.icon_sound)
                    }
                }
                OPEN_GALLERY -> {
                    if (data?.extras?.get("data") != null) {
                        var imageUri = data.data
                        getImageFile = File(imageUri!!.path)
                        writing_voiceCover_imageView.setImageURI(imageUri)
                    }
                }
            }
        }
    }


    private fun recording(count: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            when (count % 2) {
                0 -> {
                    writing_recording_imageView.visibility = View.VISIBLE
                    writing_recording_textView.visibility = View.VISIBLE
                    writing_text_textView.visibility = View.VISIBLE

                    viewModel.startRecording(file.absolutePath)
                }
                1 -> {
                    viewModel.stopRecording()

                    writing_recording_imageView.visibility = View.GONE
                    writing_recording_textView.visibility = View.GONE
                    writing_text_textView.visibility = View.GONE
                }
                else -> {
                    ActivityCompat.requestPermissions(
                        this@WritingActivity,
                        permissions,
                        REQUEST_RECORD_AUDIO_PERMISSION
                    )
                }
            }
        }
    }
}