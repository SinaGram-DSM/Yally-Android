package com.sinagram.yallyandroid.Detail.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sinagram.yallyandroid.Detail.Data.Comment
import com.sinagram.yallyandroid.Detail.Data.CommentAdaptConnector
import com.sinagram.yallyandroid.Detail.ViewModel.DetailPostViewModel
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.Data.PostAdaptConnector
import com.sinagram.yallyandroid.Home.View.MainTimeLineAdapter
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.fragment_detail_post.*
import kotlinx.android.synthetic.main.fragment_detail_post.view.*

class DetailPostFragment : Fragment() {
    private val detailPostData: Post by lazy { requireArguments().getParcelable("postData")!! }
    private val detailPostViewModel: DetailPostViewModel by viewModels()
    private var commentList: MutableList<Comment> = mutableListOf()

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

        detailPostViewModel.getComments(detailPostData.id)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val commentAdaptConnector = CommentAdaptConnector().apply {
            setAttributeFromComment(detailPostViewModel, viewLifecycleOwner)
        }
        var commentAdapter = CommentAdapter(commentList, commentAdaptConnector)

        detailPostViewModel.successLiveData.observe(viewLifecycleOwner, {
            commentList.addAll(it)
            commentAdapter = CommentAdapter(commentList, commentAdaptConnector)

            detail_post_comment_recyclerView.run {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
                adapter = commentAdapter
            }
        })

        detailPostViewModel.deleteCommentLiveData.observe(viewLifecycleOwner, {
            commentAdapter.removeAt(it)
        })
    }
}