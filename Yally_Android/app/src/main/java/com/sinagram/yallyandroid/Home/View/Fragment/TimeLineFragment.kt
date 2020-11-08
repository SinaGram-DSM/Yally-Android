package com.sinagram.yallyandroid.Home.View.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bekawestberg.loopinglayout.library.LoopingLayoutManager
import com.sinagram.yallyandroid.Detail.View.DetailPostActivity
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.Data.PostAdaptConnector
import com.sinagram.yallyandroid.Home.View.MainTimeLineAdapter
import com.sinagram.yallyandroid.Home.ViewModel.TimeLineViewModel
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.YallyMediaPlayer
import kotlinx.android.synthetic.main.fragment_time_line.*

class TimeLineFragment : Fragment() {
    private val timeLineViewModel: TimeLineViewModel by viewModels()
    private lateinit var mainTimeLineAdapter: MainTimeLineAdapter
    private var pageId = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_line, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        timeLineViewModel.getTimeLineItem(pageId)

        val postAdaptConnector = PostAdaptConnector().apply {
            setAttributeFromTimeLine(timeLineViewModel, viewLifecycleOwner)
            moveToComment = { post: Post ->
                val intent = Intent(context, DetailPostActivity::class.java)
                intent.putExtra("postData", post.id)
                intent.putExtra("isMine", post.isMine)
                startActivity(intent)
            }
        }

        mainTimeLineAdapter = MainTimeLineAdapter(mutableListOf(), postAdaptConnector)
        setRecyclerView()

        timeLineViewModel.notPageLiveData.observe(viewLifecycleOwner, {
            if (mainTimeLineAdapter.postsList.isNotEmpty()) {
                timeLine_recyclerView.run {
                    clearOnScrollListeners()
                    layoutManager = LoopingLayoutManager(context, LoopingLayoutManager.VERTICAL, false)
                }
            }
        })

        timeLineViewModel.successDeleteLiveData.observe(viewLifecycleOwner, {
            mainTimeLineAdapter.removeAt(it)
        })

        timeLineViewModel.successLiveData.observe(viewLifecycleOwner, {
            mainTimeLineAdapter.postsList.addAll(it)
            mainTimeLineAdapter.notifyDataSetChanged()
            pageId++
        })
    }

    private fun setRecyclerView() {
        timeLine_recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = mainTimeLineAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val manager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                    val totalItemCount = manager.itemCount
                    val lastVisible = manager.findLastCompletelyVisibleItemPosition()

                    if (lastVisible >= totalItemCount - 1) {
                        timeLineViewModel.getTimeLineItem(pageId)
                    }
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        YallyMediaPlayer.stopMediaPlayer()
    }
}