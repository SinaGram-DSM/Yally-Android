package com.sinagram.yallyandroid.Home.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sinagram.yallyandroid.Home.ViewModel.TimeLineViewModel
import com.sinagram.yallyandroid.R

class TimeLineFragment : Fragment() {
    private val timeLineViewModel: TimeLineViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_line, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        timeLineViewModel.getTimeLineItem(1)

        timeLineViewModel.successLiveData.observe(viewLifecycleOwner, {
            Log.e("TimeLineFragment", it.toString())
        })
    }
}