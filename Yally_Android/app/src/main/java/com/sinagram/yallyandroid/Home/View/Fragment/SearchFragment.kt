package com.sinagram.yallyandroid.Home.View.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sinagram.yallyandroid.Home.ViewModel.SearchViewModel
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchViewModel.getRecomendedList()
        searchViewModel.recomendListLivedata.observe(viewLifecycleOwner, {
            search_result_recyclerView.run {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)

            }
        })
    }
}