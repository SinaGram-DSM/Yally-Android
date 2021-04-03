package com.sinagram.yallyandroid.Home.View.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sinagram.yallyandroid.Home.Data.Friend
import com.sinagram.yallyandroid.Home.View.FindUserAdapter
import com.sinagram.yallyandroid.Home.ViewModel.SearchViewModel
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {
    private val searchViewModel: SearchViewModel by viewModels()
    private var findUserAdapter: FindUserAdapter<Friend>? = null
    private var isExpand = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findUserAdapter = FindUserAdapter(mutableListOf())
        { email: String, isListening: Boolean, observer: Observer<Boolean> ->
            if (isListening) {
                searchViewModel.cancelListening(email)
                    .observe(viewLifecycleOwner, observer)
            } else {
                searchViewModel.doListening(email).observe(viewLifecycleOwner, observer)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.search_result_recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = findUserAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setClickMoreTextView()
        searchViewModel.getRecomendedList()
        searchViewModel.recomendListLivedata.observe(viewLifecycleOwner, {
            findUserAdapter?.userList?.addAll(it)
            findUserAdapter?.notifyDataSetChanged()
        })
    }

    private fun setClickMoreTextView() {
        search_expand_textView.setOnClickListener {
            isExpand = !isExpand

            if (isExpand) {
                search_expand_textView.text = getString(R.string.briefly)
                search_result_recyclerView.run {
                    layoutManager = GridLayoutManager(context, 3)
                }
            } else {
                search_expand_textView.text = getString(R.string.more)
                search_result_recyclerView.run {
                    layoutManager = LinearLayoutManager(context).apply {
                        orientation = LinearLayoutManager.HORIZONTAL
                    }
                }
            }
        }
    }
}