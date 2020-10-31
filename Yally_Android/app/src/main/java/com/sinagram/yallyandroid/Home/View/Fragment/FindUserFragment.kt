package com.sinagram.yallyandroid.Home.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.sinagram.yallyandroid.Home.Data.User
import com.sinagram.yallyandroid.Home.View.FindUserAdapter
import com.sinagram.yallyandroid.Home.ViewModel.SearchViewModel
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.fragment_search.view.*

class FindUserFragment : Fragment() {
    private val searchViewModel: SearchViewModel by viewModels()
    private var findUserAdapter: FindUserAdapter<User>? = null
    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        query = arguments?.getString("findQuery")
        findUserAdapter = FindUserAdapter(mutableListOf())
        { email: String, isListening: Boolean, observer: Observer<Boolean> ->
            if (isListening) {
                searchViewModel.cancelListening(email).observe(viewLifecycleOwner, observer)
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

        view.search_result_textView.text = getString(R.string.search_result)
        view.search_expand_textView.visibility = View.GONE
        view.search_result_recyclerView.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 3)
            adapter = findUserAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchViewModel.getUserListBySearchName(query)
        searchViewModel.findUserLiveData.observe(viewLifecycleOwner, {
            findUserAdapter?.userList?.addAll(it)
            findUserAdapter?.notifyDataSetChanged()
        })
    }
}