package com.sinagram.yallyandroid.Home.View.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sinagram.yallyandroid.Detail.View.DetailPostActivity
import com.sinagram.yallyandroid.Home.Data.CallPostsUp
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.Data.PostAdaptConnector
import com.sinagram.yallyandroid.Home.View.MainTimeLineAdapter
import com.sinagram.yallyandroid.Home.ViewModel.TimeLineViewModel
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.YallyMediaPlayer
import kotlinx.android.synthetic.main.fragment_time_line.*

class TimeLineFragment : Fragment() {
    private val timeLineViewModel: TimeLineViewModel by viewModels()
    private var timeLineList: MutableList<Post> = mutableListOf()
    private lateinit var mainTimeLineAdapter: MainTimeLineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_line, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        timeLineViewModel.getTimeLineItem(1)
        val postAdaptConnector = PostAdaptConnector().apply {
            setAttributeFromTimeLine(timeLineViewModel, viewLifecycleOwner)
            moveToComment = { id: String ->
                val intent = Intent(context, DetailPostActivity::class.java)
                intent.putExtra("postData", id)
                startActivity(intent)
            }
        }

        mainTimeLineAdapter = MainTimeLineAdapter(timeLineList, postAdaptConnector)

        timeLineViewModel.successLiveData.observe(viewLifecycleOwner, {
            timeLineList.addAll(it)
            mainTimeLineAdapter = MainTimeLineAdapter(timeLineList, postAdaptConnector)

            timeLine_recyclerView.run {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
                adapter = mainTimeLineAdapter

                val onScrollListener = CallPostsUp(it, timeLineViewModel)
                clearOnScrollListeners()
                addOnScrollListener(onScrollListener)
            }
        })

        timeLineViewModel.successDeleteLiveData.observe(viewLifecycleOwner, {
            mainTimeLineAdapter.removeAt(it)
        })
    }

    override fun onPause() {
        super.onPause()
        YallyMediaPlayer.stopMediaPlayer()
    }
}