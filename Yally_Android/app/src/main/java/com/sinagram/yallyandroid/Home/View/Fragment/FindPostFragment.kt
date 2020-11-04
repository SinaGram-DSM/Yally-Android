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
import com.sinagram.yallyandroid.Home.Data.PostAdaptConnector
import com.sinagram.yallyandroid.Home.View.MainTimeLineAdapter
import com.sinagram.yallyandroid.Home.ViewModel.SearchViewModel
import com.sinagram.yallyandroid.Home.ViewModel.TimeLineViewModel
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.YallyMediaPlayer
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class FindPostFragment : Fragment() {
    private val searchViewModel: SearchViewModel by viewModels()
    private val timeLineViewModel: TimeLineViewModel by viewModels()
    private var searchPostAdapter: MainTimeLineAdapter? = null
    private var query: String? = null
    private var pageId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        query = arguments?.getString("findQuery")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postAdaptConnector = PostAdaptConnector().apply {
            setAttributeFromTimeLine(timeLineViewModel, viewLifecycleOwner)
            moveToComment = { id: String ->
                val intent = Intent(context, DetailPostActivity::class.java)
                intent.putExtra("postData", id)
                startActivity(intent)
            }
        }
        searchPostAdapter = MainTimeLineAdapter(mutableListOf(), postAdaptConnector)

        view.search_result_textView.text = getString(R.string.search_result)
        view.search_expand_textView.visibility = View.GONE
        view.search_result_recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = searchPostAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchViewModel.getPostListBySearchTag(query, pageId)
        setRecyclerView()

        timeLineViewModel.successDeleteLiveData.observe(viewLifecycleOwner, {
            searchPostAdapter?.removeAt(it)
        })

        searchViewModel.notPageLiveData.observe(viewLifecycleOwner, {
            search_result_recyclerView.run {
                clearOnScrollListeners()
            }
        })

        searchViewModel.findPostLiveData.observe(viewLifecycleOwner, {
            searchPostAdapter?.postsList?.addAll(it)
            searchPostAdapter?.notifyDataSetChanged()
            pageId++
        })
    }

    private fun setRecyclerView() {
        search_result_recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = searchPostAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val manager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                    val totalItemCount = manager.itemCount
                    val lastVisible = manager.findLastCompletelyVisibleItemPosition()

                    if (lastVisible >= totalItemCount - 1) {
                        searchViewModel.getPostListBySearchTag(query, pageId)
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