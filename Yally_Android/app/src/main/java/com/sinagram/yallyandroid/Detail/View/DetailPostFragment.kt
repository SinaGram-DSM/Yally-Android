package com.sinagram.yallyandroid.Detail.View

import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sinagram.yallyandroid.Detail.Data.CommentAdaptConnector
import com.sinagram.yallyandroid.Detail.Data.CommentRequest
import com.sinagram.yallyandroid.Detail.ViewModel.DetailPostViewModel
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.Data.PostAdaptConnector
import com.sinagram.yallyandroid.Home.View.MainTimeLineAdapter
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.YallyMediaPlayer
import kotlinx.android.synthetic.main.fragment_detail_post.*
import kotlinx.android.synthetic.main.fragment_detail_post.view.*
import java.io.File

class DetailPostFragment : Fragment() {
    private val detailPostDataId: String by lazy { requireArguments().getString("postDataId")!! }
    private val detailPostViewModel: DetailPostViewModel by viewModels()
    private var isClickRecorder: Boolean = false
    private var mLastClickTime: Long = 0
    private var commentRequest: CommentRequest = CommentRequest()
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var postAdapter: MainTimeLineAdapter
    var postData: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        commentRequest.file = File(Environment.getExternalStorageDirectory(), "yally.mp3")
        detailPostViewModel.getDetailPost(detailPostDataId)
        detailPostViewModel.getComments(detailPostDataId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.detail_post_soundInput_button.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }

            mLastClickTime = SystemClock.elapsedRealtime()

            try {
                if (!isClickRecorder) {
                    detailPostViewModel.startRecord(commentRequest.file!!.absolutePath, 10000)
                    view.detail_post_input_textView.isEnabled = false
                } else {
                    detailPostViewModel.stopRecord()
                    view.detail_post_input_textView.isEnabled = true
                }
            } catch (e: Exception) {
                Log.e("DetailPostFragment", e.message.toString())
            }
        }

        view.detail_post_input_textView.setOnClickListener {
            val content = view.detail_post_comment_editText.text.toString()
            if (content.isNotEmpty()) {
                commentRequest.content = content
                detailPostViewModel.sendComment(detailPostDataId, commentRequest)
                commentRequest.file = File(Environment.getExternalStorageDirectory(), "yally.mp3")
                view.detail_post_comment_editText.setText("")
            }
        }

        val commentAdaptConnector = CommentAdaptConnector()
        commentAdaptConnector.setAttributeFromComment(detailPostViewModel)
        commentAdapter = CommentAdapter(mutableListOf(), commentAdaptConnector)

        view.detail_post_comment_recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = commentAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val postAdaptConnector = PostAdaptConnector().apply {
            setAttributeFromComment(detailPostViewModel, viewLifecycleOwner)
        }

        detailPostViewModel.postLiveData.observe(viewLifecycleOwner, {
            postData = it
            postAdapter = if (postData != null) {
                postData!!.id = detailPostDataId
                MainTimeLineAdapter(mutableListOf(postData!!), postAdaptConnector)
            } else {
                MainTimeLineAdapter(mutableListOf(), postAdaptConnector)
            }

            detail_post_recyclerView.run {
                layoutManager = LinearLayoutManager(activity)
                adapter = postAdapter
            }
        })

        detailPostViewModel.successLiveData.observe(viewLifecycleOwner, {
            commentAdapter.commentList.clear()
            commentAdapter.commentList.addAll(it)
            commentAdapter.notifyDataSetChanged()
            detail_post_comment_recyclerView.adapter = commentAdapter
        })

        detailPostViewModel.deleteCommentLiveData.observe(viewLifecycleOwner, {
            YallyMediaPlayer.stopMediaPlayer()
            commentAdapter.removeAt(it)
            postAdapter.minusComment()
        })

        detailPostViewModel.recorderLiveData.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(activity, "녹음이 시작되었습니다.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(activity, "녹음이 종료되었습니다.", Toast.LENGTH_LONG).show()
            }

            isClickRecorder = !isClickRecorder
        })

        detailPostViewModel.successDeleteLiveData.observe(viewLifecycleOwner, {
            activity?.finish()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        detailPostViewModel.stopRecord()
    }
}