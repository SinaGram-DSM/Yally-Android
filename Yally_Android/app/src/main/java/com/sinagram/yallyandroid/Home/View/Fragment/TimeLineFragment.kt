package com.sinagram.yallyandroid.Home.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sinagram.yallyandroid.Home.Data.Listening
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.Data.StateOnPostMenu
import com.sinagram.yallyandroid.Home.Data.User
import com.sinagram.yallyandroid.Home.View.MainTimeLineAdapter
import com.sinagram.yallyandroid.Home.ViewModel.TimeLineViewModel
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.fragment_time_line.*

class TimeLineFragment : Fragment() {
    private val timeLineViewModel: TimeLineViewModel by viewModels()
    private var timeLineList: MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_line, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        timeLineViewModel.getTimeLineItem(1)
//
//        timeLineViewModel.successLiveData.observe(viewLifecycleOwner, {
//            timeLine_recyclerView.apply {
//                setHasFixedSize(true)
//                layoutManager = LinearLayoutManager(activity)
//                adapter = MainTimeLineAdapter(it, clickYally)
//            }
//        })

        val post = Post(
            id = "42736b0ace29e3a9b5cfa3a0240d4c0e",
            content = "아니 당근 빳따 아이유가 진리인데…ㅠ 왜 다들 몰라줄까요… 이거 다들 진짜 인정하시는 부분이죠?! 저만그런거 아니죠?!?! #아이유 #짱 #ㅎㅋ #으앜",
            sound = "1602582644569.JPG",
            img = "1602582644582.JPG",
            createdAt = "2020-10-13 18:50:44",
            user = User(
                email = "admin123@gmail.com",
                nickname = "admin", img = "user.jpg"
            ),
            comment = 0,
            yally = 0,
            isYally = true,
            isMine = true
        )

        timeLineList.add(post)

        val clickYally = { data: Post, observer: Observer<Boolean> ->
            timeLineViewModel.clickYally(data).observe(viewLifecycleOwner, observer)
        }

        val getListeningOnPost = { observer: Observer<List<Listening>> ->
            timeLineViewModel.getListeningList().observe(viewLifecycleOwner, observer)
        }

        val listeningOnPost =
            { state: StateOnPostMenu, email: String, observer: Observer<StateOnPostMenu> ->
                timeLineViewModel.sendListeningToUser(state, email)
                    .observe(viewLifecycleOwner, observer)
            }

        val deletePost = { id: String, index: Int -> timeLineViewModel.deletePost(id, index) }

        val adapter = MainTimeLineAdapter(
            timeLineList,
            clickYally,
            getListeningOnPost,
            listeningOnPost,
            deletePost
        )

        timeLineViewModel.successDeleteLiveData.observe(viewLifecycleOwner, {
            adapter.removeAt(it)
        })

        timeLine_recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            this.adapter = adapter
        }
    }
}