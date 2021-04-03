package com.sinagram.yallyandroid.Home.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Base.BasePostRepository
import com.sinagram.yallyandroid.Base.BasePostViewModel
import com.sinagram.yallyandroid.Home.Data.HomeRepository
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.Data.PostsResponse
import com.sinagram.yallyandroid.Network.Result
import kotlinx.coroutines.launch

class TimeLineViewModel : BasePostViewModel() {
    override var repository: BasePostRepository = HomeRepository()
    val notPageLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val successLiveData: MutableLiveData<List<Post>> = MutableLiveData()

    fun getTimeLineItem(page: Int) {
        viewModelScope.launch {
            val result = (repository as HomeRepository).getMainTimeLine(page)

            if (result is Result.Success) {
                timeLineSuccess(result)
            } else {
                Log.e("TimeLineViewModel", (result as Result.Error).exception)
            }
        }
    }

    private fun timeLineSuccess(result: Result.Success<PostsResponse>) {
        if (result.code == 200) {
            val data = result.data?.posts
            if (data?.isNotEmpty() == true) {
                successLiveData.postValue(result.data.posts)
            } else {
                notPageLiveData.postValue(true)
            }
        }
    }
}