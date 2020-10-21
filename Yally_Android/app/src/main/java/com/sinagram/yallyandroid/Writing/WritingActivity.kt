package com.sinagram.yallyandroid.Writing

import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.activity_writing.*
import java.io.File


class WritingActivity : AppCompatActivity() {
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var file: File? = null
    private var count: Int = 1
    private var fileName: String? = null
    private val PICK_NAME: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        writing_recording_button.setOnClickListener {

            writing_recording_textView.visibility = View.VISIBLE
            writing_recording_button.visibility = View.VISIBLE
            writing_text_textView.visibility = View.VISIBLE

            count++
            recording(count)
        }

        writing_voiceCover_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, PICK_NAME)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_NAME && requestCode == RESULT_OK){
            data?.data?.let {
                val selectImage = it
            }
        }
    }


    private fun recording(count: Int) {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.let {
            it.setAudioSource(MediaRecorder.AudioSource.MIC)
            it.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            it.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            it.setOutputFile(fileName)

            if (count / 2 == 0) {
                it.prepare()
                it.start()
            } else if (it != null) {
                file = File(fileName)
                it.stop()
                it.release()
                println("success")

//                var audioUri = contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,)!!
            }
        }
    }


}
