package com.sinagram.yallyandroid.Writing

import android.app.Activity
import android.content.Intent
import android.media.MediaRecorder
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.activity_writing.*
import java.io.File

class WritingActivity : AppCompatActivity() {
    private var mediaRecorder: MediaRecorder? = null
    private var count = 1
    private var fileName: String = " "
    private val OPEN_GALLERY = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        //버튼 눌렀을 때 보여주기
        writing_recording_button.setOnClickListener {
            writing_recording_imageView.visibility = View.VISIBLE
            writing_recording_button.visibility = View.VISIBLE
            writing_text_textView.visibility = View.VISIBLE

            count++
            recording(count)
        }

        //이미지 가져오기
        writing_voiceCover_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, OPEN_GALLERY)
        }

    }


    //이미지 세팅
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Activity.RESULT_OK) {

            if (requestCode == OPEN_GALLERY) {
                if (data?.extras?.get("data") != null) {
                    val uri = data?.data
                    writing_voiceCover_imageView.setImageURI(uri)
                }
            }

        }
    }

    // 녹음
    private fun recording(count: Int) {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.let {
            if(count/2==0){
                it.setAudioSource(MediaRecorder.AudioSource.MIC)
                it.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                it.setOutputFile(fileName)
                it.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)


                try{
                    it.prepare()
                }catch (e: Exception) {
                    Log.d(LOG_CAT,"prepare() failed")
                }
                it.start()
            } else {
                it.stop()
                it.release()

                mediaRecorder = null
            }




            if (count / 2 == 0) {
                it.prepare()
                it.start()
            } else if (it != null) {
                var file = File(fileName)
                it.stop()
                it.release()
                println("success")

//                var audioUri = contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, ContentValues?)
            }
        }
    }


}
