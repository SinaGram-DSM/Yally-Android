package com.sinagram.yallyandroid.Writing.Ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Writing.ViewModel.WritingViewModel
import kotlinx.android.synthetic.main.activity_writing.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
private const val REQUEST_OPEN_GALLERY_PERMISSION = 300

class WritingActivity : AppCompatActivity() {

    private var count = 1
    private val OPEN_GALLERY = 1
    private val OPEN_AUDIO = 2
    private lateinit var getImageFile: File
    private var getAudioFile: File? = null
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private lateinit var file: File
    private val viewModel: WritingViewModel by viewModels()
    private var sound: Boolean? = null

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
        getImageFile = File(Environment.getExternalStorageDirectory(), "yally.png")

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
            /*it.replace(R.id.fragment_time_line, newFragment)
            it.addToBackStack(null)
            it.commit()*/
        }
    }

    private fun postWriting() {
        var content = writing_writing_edit.text.toString()
        var hashtags: MutableList<String> = viewModel.hashtags(content)
        var requestHashMap: HashMap<String, RequestBody> = hashMapOf()
        var soundPart: MultipartBody.Part

        Log.e("WritingActivity", getImageFile.toString())
        Log.e("WritingActivity",file.toString())

        if (getImageFile != null && content != null) {
            when (sound) {
                true -> {
                    soundPart = MultipartBody.Part.createFormData(
                            "sound",
                            file.name,
                            getImageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    )
                }
                false -> {
                    soundPart = MultipartBody.Part.createFormData(
                            "sound",
                            file.name,
                            getImageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    )
                }
                else -> {
                    Toast.makeText(this, "오디오 파일이 필요합니다", Toast.LENGTH_LONG).show()
                    return
                }
            }

            var contentPart = MultipartBody.Part.createFormData(
                    "content",
                    content
            )

            var imagePart = MultipartBody.Part.createFormData(
                    "img",
                    getImageFile.name,
                    getImageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            )

            for (i in hashtags.indices) requestHashMap["hashtag[$i]"] =
                    hashtags[i].toRequestBody("multipart/form-data".toMediaTypeOrNull())

            viewModel.writing(requestHashMap,contentPart, imagePart, soundPart)
        }

        changeView()
    }

    private fun getVoice() {
        sound = false
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
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                OPEN_AUDIO -> {
                    if (data != null) {
                        var audioUri = data.data
                        getAudioFile = File(audioUri!!.path)
                        writing_voice_imageView.setImageResource(R.drawable.icon_sound)
                    }
                }
                OPEN_GALLERY -> {
                    if (data != null) {
                        var imageUri = data.data
                        writing_voiceCover_imageView.setImageURI(imageUri)

                        getImageFile.delete()
                        getImageFile.createNewFile()

                        val out = FileOutputStream(getImageFile)
                        contentResolver.openInputStream(imageUri!!)?.copyTo(out)
                        out.close()
                    }
                }
            }
        }
    }

    private fun recording(count: Int) {
        if (getAudioFile != null) {
            Toast.makeText(this, "오디오 파일이 이미 선택되었습니다.", Toast.LENGTH_LONG).show()
        } else if (ContextCompat.checkSelfPermission(
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

                    sound = true
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
