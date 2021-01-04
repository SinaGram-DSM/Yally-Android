package com.sinagram.yallyandroid.Profile.Ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bekawestberg.loopinglayout.library.LoopingLayoutManager
import com.bumptech.glide.Glide
import com.sinagram.yallyandroid.Detail.View.DetailPostActivity
import com.sinagram.yallyandroid.Home.View.MainTimeLineAdapter
import com.sinagram.yallyandroid.Home.ViewModel.TimeLineViewModel
import com.sinagram.yallyandroid.Network.YallyConnector
import com.sinagram.yallyandroid.Profile.Data.MyPostAdapterConnector
import com.sinagram.yallyandroid.Profile.Ui.Adapter.ProfileTimeLineAdapter
import com.sinagram.yallyandroid.Profile.ViewModel.ProfileViewModel
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.SharedPreferencesManager
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()
    lateinit var recyclerView: RecyclerView
    lateinit var profileAdapter: ProfileTimeLineAdapter
    private var pageId = 1

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        recyclerView = view.findViewById(R.id.profile_timeLine_recycler)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setProfile()

        button.setOnClickListener {
            val intent = Intent(context,ModifyProfileActivity::class.java)
            startActivity(intent)
        }

        profile_listening_button.setOnClickListener {
            changeView("listener")
        }

        profile_listener_button.setOnClickListener {
            changeView("listening")
        }

        viewModel.getTimeLineItem(pageId)

        val adaptConnector = MyPostAdapterConnector().apply {
            setAttributeFromTimeLine(TimeLineViewModel(), viewLifecycleOwner)
            moveToComment = { id: String ->
                val intent = Intent(context, DetailPostActivity::class.java)
                intent.putExtra("postData", id)
                startActivity(intent)
            }
        }

        profileAdapter = ProfileTimeLineAdapter(mutableListOf(), adaptConnector)
        setRecycleView()

        viewModel.notPageLiveData.observe(viewLifecycleOwner, {
            recyclerView.run {
                clearOnScrollListeners()
                layoutManager = LoopingLayoutManager(context, LoopingLayoutManager.VERTICAL, false)
            }
        })

        viewModel.successLiveData.observe(viewLifecycleOwner, {
            profileAdapter.postsList.addAll(it)
            profileAdapter.notifyDataSetChanged()
            pageId++
        })

    }

    private fun setRecycleView() {
        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = profileAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val manager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                    val totalItemCount = manager.itemCount
                    val lastVisible = manager.findLastCompletelyVisibleItemPosition()

                    if (lastVisible >= totalItemCount - 1) {
                        viewModel.getTimeLineItem(pageId)
                    }
                }
            })
        }
    }

    private fun changeView(select: String) {
        val intent = Intent(activity, ListenActivity::class.java)
        intent.putExtra("select", select)
        startActivity(intent)
    }

    private fun setProfile() {
        viewModel.setProfile()

        viewModel.setLiveData.observe(viewLifecycleOwner, {
            proflie_nickname_textView.text = it.nickname
            profile_listeningCount_textView.text = it.listener.toString()
            profile_listenerCount_textView.text = it.listening.toString()
            Glide.with(this).load(YallyConnector.s3+ it.image).into(profile_profile_ImageView)

            if (it.isMine) {
                profile_mail_textView.visibility = View.VISIBLE
                profile_mail_textView.text = SharedPreferencesManager.getInstance().email
            }
        })
    }

}