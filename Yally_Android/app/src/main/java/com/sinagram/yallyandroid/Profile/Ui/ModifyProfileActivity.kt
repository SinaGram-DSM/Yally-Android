package com.sinagram.yallyandroid.Profile.Ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.sinagram.yallyandroid.Profile.ViewModel.ProfileViewModel
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.activity_modify_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class ModifyProfileActivity : AppCompatActivity() {
    private val OPEN_GALLERY = 1
    private var getImageFile: File? = null
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_profile)

        setInit()

        modify_changeProfile_imagebutton.setOnClickListener {
            getImage()
        }
    }

    private fun setInit() {
        getImageFile = File(Environment.getExternalStorageDirectory(), "yally.png")
        setSupportActionBar(toolbar)

        modify_changeProfile_imagebutton.bringToFront()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.modify_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.toolbar_save_button -> {
                sendData()
                return true
            }
            R.id.toolbar_back_button -> {
                val intent = Intent(this,ProfileFragment::class.java)
                startActivity(intent)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, OPEN_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Activity.RESULT_OK) {
            if (data != null) {
                var imageUri = data.data

                modify_changeProfile_imageView.setImageURI(imageUri)

                getImageFile!!.delete()
                getImageFile!!.createNewFile()

                val out = FileOutputStream(getImageFile)
                contentResolver.openInputStream(imageUri!!)?.copyTo(out)
                out.close()
            }
        }
    }

    private fun sendData() {
        var nickname = modify_changeNickname_editText.text.toString()

        if (nickname != null && getImageFile != null) {
            var nicknamePart = MultipartBody.Part.createFormData(
                    "nickname",
                    nickname
            )

            var imagePart = MultipartBody.Part.createFormData(
                    "img",
                    getImageFile!!.name,
                    getImageFile!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            )

            viewModel.modifyProfile(nicknamePart,imagePart)
        }
    }

}