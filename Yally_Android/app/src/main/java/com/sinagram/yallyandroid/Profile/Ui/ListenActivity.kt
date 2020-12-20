package com.sinagram.yallyandroid.Profile.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sinagram.yallyandroid.Profile.Data.ListenUser
import com.sinagram.yallyandroid.Profile.Ui.Adapter.ListenAdapter
import com.sinagram.yallyandroid.Profile.ViewModel.ProfileViewModel
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.activity_listen.*

class ListenActivity : AppCompatActivity() {
    val viewModel: ProfileViewModel by viewModels()
    lateinit var recyclerView: RecyclerView
    lateinit var listenAdapter: ListenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listen)

        setRecyclerView()
        setListenList()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.listen_toolbar, menu)
        return true
    }

    private fun setRecyclerView() {
        recyclerView = findViewById<RecyclerView>(R.id.listen_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
    }

    private fun setListenList(){
        var select = intent.getStringExtra("select")

        when(select){
            "listener" -> {
                viewModel.getListener()

                viewModel.listenLiveData.observe(this, {

                    var listener = it.target.listener
                    var nickname = it.target.nickname
                    var listenerList = it.listeners

                    listen_title_textView.text = "$listener 명이 $nickname 님의 이야기를 듣고 있습니다."

                    if(listenerList != null){
                        listenAdapter = ListenAdapter(listenerList)
                        recyclerView.adapter = listenAdapter
                    }
                })
            }

            "listening" -> {
                viewModel.getListening()

                viewModel.listenLiveData.observe(this,{

                    var listening = it.target.listening
                    var nickname = it.target.nickname
                    var listeningList = it.listeners

                    listen_title_textView.text = "$nickname 님이 $listening 명의 이야기를 듣고 있습니다."

                    if(listeningList != null){
                        listenAdapter = ListenAdapter(listeningList)
                        recyclerView.adapter = listenAdapter
                    }

                })
            }
            else -> Toast.makeText(this,"올바르지 않은 요청입니다", Toast.LENGTH_LONG).show()
        }
    }

}