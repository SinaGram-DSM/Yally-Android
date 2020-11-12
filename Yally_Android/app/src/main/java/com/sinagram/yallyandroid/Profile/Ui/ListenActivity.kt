package com.sinagram.yallyandroid.Profile.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import com.sinagram.yallyandroid.Profile.ViewModel.ProfileViewModel
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.activity_modify_profile.*

class ListenActivity : AppCompatActivity() {
    val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listen)
        setInit()
    }

    private fun setInit(){
        setSupportActionBar(toolbar)
        title=""

        var s = intent.getStringExtra("select")

        when(s){
            "listener" -> viewModel.getListener()
            "listening" -> viewModel.getListening()
            else -> Toast.makeText(this,"올바르지 않은 요청입니다",Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.listen_toolbar, menu)
        return true
    }
}