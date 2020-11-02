package com.sinagram.yallyandroid.Writing

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Splash.SplashActivity
import kotlinx.android.synthetic.main.activity_writing.*

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
private const val REQUEST_OPEN_GALLERY_PERMISSION = 300

class WritingActivity : AppCompatActivity() {
    private var count = 1
    private val OPEN_GALLERY = 1
    private val OPEN_AUDIO = 2
    private var audioUri: Uri? = null
    private var imageUri: Uri? = null
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        setSupportActionBar(writing_toolbar)
        writing_toolbar.setTitle(" ")

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_back_item -> {
                changeView()
                return true
            }
            R.id.toolbar_upload_item -> {
                sendData()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }

        }
    }

    private fun changeView(){
        val intent = Intent(this@WritingActivity, HomeActivity::class.java)
    }

    private fun sendData() {
        var writing = writing_writing_edit.toString()
        changeView()
    }

    private fun getVoice() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ("audio/*")
        intent.type = MediaStore.Audio.Media.CONTENT_TYPE
        startActivityForResult(intent, OPEN_AUDIO)
    }

    private fun getImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, OPEN_GALLERY)
        } else {
            ActivityCompat.requestPermissions(this@WritingActivity, permissions, REQUEST_OPEN_GALLERY_PERMISSION)
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
                        audioUri = data?.data
                        writing_voice_imageView.setImageResource(R.drawable.icon_sound)
                    }
                }
                OPEN_GALLERY -> {
                    if (data?.extras?.get("data") != null) {
                        imageUri = data?.data
                        writing_voiceCover_imageView.setImageURI(imageUri)
                    }

                }
            }
        }
    }

    // 녹음
    private fun recording(count: Int) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

            writing_recording_imageView.visibility = View.VISIBLE
            writing_recording_textView.visibility = View.VISIBLE
            writing_text_textView.visibility = View.VISIBLE

            when (count % 2) {
                0 -> {

                }

                1 -> {
                    writing_recording_imageView.visibility = View.GONE
                    writing_recording_textView.visibility = View.GONE
                    writing_text_textView.visibility = View.GONE
                }
            }
        } else {
            ActivityCompat.requestPermissions(this@WritingActivity, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        }

    }
}
