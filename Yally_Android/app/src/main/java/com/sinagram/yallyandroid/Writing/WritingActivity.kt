package com.sinagram.yallyandroid.Writing

import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.activity_writing.*
import java.io.File
import java.lang.Exception

class WritingActivity : AppCompatActivity() {
    private var mediaRecorder: MediaRecorder? = null
    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        writing_recording_button.setOnClickListener {
            mediaRecorder = MediaRecorder()
            mediaRecorder?.let {
                it.setAudioSource(MediaRecorder.AudioSource.MIC)
                it.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                it.setOutputFile("/Yally/Voice");
                it.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                try{
                    it.prepare()
                    it.start()
                }catch (e: Exception){
                    println(e)
                }
                it.stop()
                it.reset()
                it.release()
                println("success")
            }
        }
    }
}