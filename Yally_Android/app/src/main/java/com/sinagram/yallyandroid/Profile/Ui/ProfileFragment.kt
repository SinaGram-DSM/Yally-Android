package com.sinagram.yallyandroid.Profile.Ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sinagram.yallyandroid.Profile.Ui.Adapter.ProfileTimeLineAdapter
import com.sinagram.yallyandroid.Profile.ViewModel.ProfileViewModel
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.SharedPreferencesManager
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()
    lateinit var recyclerView: RecyclerView
    lateinit var profileTimeLineAdapter : ProfileTimeLineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        recyclerView = view.findViewById(R.id.profile_timeLine_recycler)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return view
    }

    private fun setInit(){
        setProfile()
        setTimeLinePost()

        profileTimeLineAdapter = ProfileTimeLineAdapter(mutableListOf())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setInit()

        profile_listening_button.setOnClickListener {
            changeView("listener")
        }

        profile_listener_button.setOnClickListener {
            changeView("listening")
        }

    }

    private fun changeView(select: String) {
        val intent = Intent(activity, ListenActivity::class.java)
        intent.putExtra("select", select)
        startActivity(intent)
    }

    private fun setProfile(){
        viewModel.setProfile()

        viewModel.setLiveData.observe(viewLifecycleOwner, {
            proflie_nickname_textView.text = it.nickname
            profile_listeningCount_textView.text = it.listening.toString()
            profile_listenerCount_textView.text = it.listener.toString()
            Glide.with(this).load(it.image).into(profile_profile_ImageView)

            if(it.isMine){
                profile_mail_textView.visibility = View.VISIBLE
                profile_mail_textView.text = SharedPreferencesManager.getInstance().email
            }
        })
    }

    private fun setTimeLinePost(){
        viewModel.setMyTimeLine()

        viewModel.timeLineData.observe(viewLifecycleOwner, {
            profileTimeLineAdapter = ProfileTimeLineAdapter(it)
            recyclerView.adapter = profileTimeLineAdapter
        })
    }
}