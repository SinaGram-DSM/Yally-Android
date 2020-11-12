package com.sinagram.yallyandroid.Profile.Ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sinagram.yallyandroid.Profile.ViewModel.ProfileViewModel
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
//    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profile_listener_button.setOnClickListener {
            changeView("listener")
        }

        profile_listening_button.setOnClickListener {
            changeView("listening")
        }

        return view
    }

    fun init(){
//        viewModel.setProfile()
    }

    fun changeView(select: String){
        val intent = Intent(activity, ListenActivity::class.java)
        intent.putExtra("select",select)
        startActivity(intent)
    }


}