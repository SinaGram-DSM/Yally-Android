package com.sinagram.yallyandroid.Home.View.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sinagram.yallyandroid.Home.ViewModel.SearchViewModel
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class FindUserFragment : Fragment() {
    private val searchViewModel: SearchViewModel by viewModels()
    private val query = requireArguments().getString("findQuery")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.search_result_textView.text = getString(R.string.search_result)
        view.search_expand_textView.visibility = View.GONE
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchViewModel.getUserListBySearchName(query)
        searchViewModel.findUserLiveData.observe(viewLifecycleOwner, {
            search_result_recyclerView.run {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(context, 3)
                adapter
            }
        })
    }
}