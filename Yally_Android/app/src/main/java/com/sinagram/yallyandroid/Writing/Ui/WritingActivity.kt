package com.sinagram.yallyandroid.Writing.Ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.sinagram.yallyandroid.Home.View.Fragment.TimeLineFragment
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Writing.ViewModel.WritingViewModel
import kotlinx.android.synthetic.main.activity_writing.*
import java.io.File
import java.io.FileOutputStream

private const val REQUEST_RECORD_AUDIO = 200
private const val REQUEST_OPEN_GALLERY = 300

class WritingActivity : AppCompatActivity() {
    private var count = 1
    private var soundIs = false
    private lateinit var imageFile: File
    private lateinit var soundFile: File
    private lateinit var getSoundFile: File
    private val viewModel: WritingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        setInit()

        writing_recording_button.setOnClickListener {
            count++
            recording()
        }

        writing_voiceCover_button.setOnClickListener {
            getImage()
        }

        writing_voiceFile_button.setOnClickListener {
            getVoice()
        }

    }

    private fun setInit() {
        getSoundFile = File(Environment.getExternalStorageDirectory(), "yally.mp3")
        soundFile = File(Environment.getExternalStorageDirectory(), "yally.mp3")
        imageFile = File(Environment.getExternalStorageDirectory(), "yally.png")

        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.writing_toolbar, menu)
        return true
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
            it.replace(R.id.home_fragment, TimeLineFragment())
            it.addToBackStack(null)
            it.commit()
        }
    }

    private fun postWriting() {
        val content = writing_writing_edit.text.toString()

        if(soundIs) viewModel.writing(soundFile, imageFile, content)
        else viewModel.writing(getSoundFile, imageFile, content)

        changeView()
    }

    private fun getVoice() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ("audio/*")
        intent.type = MediaStore.Audio.Media.CONTENT_TYPE
        startActivityForResult(intent, REQUEST_RECORD_AUDIO)
    }

    private fun getImage() {
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, REQUEST_OPEN_GALLERY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_RECORD_AUDIO -> {
                    if (data != null) {
                        var audioUri = data.data
                        getSoundFile = File(audioUri!!.path)
                        writing_voice_imageView.setImageResource(R.drawable.icon_sound)

                        soundIs = false
                    }
                }
                REQUEST_OPEN_GALLERY -> {
                    if (data != null) {
                        var imageUri = data.data
                        writing_voiceCover_imageView.setImageURI(imageUri)

                        imageFile.delete()
                        imageFile.createNewFile()

                        val out = FileOutputStream(imageFile)
                        contentResolver.openInputStream(imageUri!!)?.copyTo(out)
                        out.close()
                    }
                }
            }
        }
    }

    private fun recording() {
        soundIs = true
        when (count % 2) {
            0 -> {
                writing_recording_imageView.visibility = View.VISIBLE
                writing_recording_textView.visibility = View.VISIBLE
                writing_text_textView.visibility = View.VISIBLE

                viewModel.startRecording(soundFile.absolutePath)
            }
            1 -> {
                writing_recording_imageView.visibility = View.GONE
                writing_recording_textView.visibility = View.GONE
                writing_text_textView.visibility = View.GONE

                viewModel.stopRecording()
            }
        }
    }
}
