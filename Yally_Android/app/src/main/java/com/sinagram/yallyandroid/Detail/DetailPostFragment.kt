package com.sinagram.yallyandroid.Detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.Data.PostAdaptConnector
import com.sinagram.yallyandroid.Home.View.MainTimeLineAdapter
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.fragment_detail_post.view.*

class DetailPostFragment : Fragment() {
    val detailPostData: Post by lazy { requireArguments().getParcelable("postData")!! }
    val detailPostViewModel: DetailPostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.detail_post_recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = MainTimeLineAdapter(mutableListOf(detailPostData), PostAdaptConnector())
        }

        detailPostViewModel.successLiveData.observe(viewLifecycleOwner, {
            view.detail_post_comment_recyclerView.run {
                layoutManager = LinearLayoutManager(activity)
            }
        })
    }
}