package com.sinagram.yallyandroid.Home.Data

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sinagram.yallyandroid.Home.ViewModel.TimeLineViewModel

class CallPostsUp(val data: List<Post>, private val timeLineViewModel: TimeLineViewModel) :
    RecyclerView.OnScrollListener() {
    private var pageId = 1

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        LinearLayoutManager::class.java.cast(recyclerView.layoutManager)!!.run {
            if (findLastCompletelyVisibleItemPosition() >= itemCount - 1 && data.isNotEmpty()) {
                pageId++
                timeLineViewModel.getTimeLineItem(pageId)
            }
        }
    }
}