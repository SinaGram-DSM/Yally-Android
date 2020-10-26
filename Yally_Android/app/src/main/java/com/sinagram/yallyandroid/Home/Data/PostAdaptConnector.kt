package com.sinagram.yallyandroid.Home.Data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.sinagram.yallyandroid.Home.ViewModel.TimeLineViewModel

data class PostAdaptConnector(
    var clickYally: (Post, Observer<Boolean>) -> Unit = { _, _ -> },
    var getListeningOnPost: (Observer<List<Listening>>) -> Unit = { _ -> },
    var listeningOnPost: (StateOnPostMenu, String, Observer<StateOnPostMenu>) -> Unit = { _, _, _ -> },
    var deletePost: (String, Int) -> Unit = { _, _ -> },
    var moveToComment: (String) -> Unit = { _ -> }
) {
    fun setAttributeFromTimeLine(
        timeLineViewModel: TimeLineViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        clickYally = { data: Post, observer: Observer<Boolean> ->
            timeLineViewModel.clickYally(data).observe(lifecycleOwner, observer)
        }

        getListeningOnPost = { observer: Observer<List<Listening>> ->
            timeLineViewModel.getListeningList().observe(lifecycleOwner, observer)
        }

        listeningOnPost =
            { state: StateOnPostMenu, email: String, observer: Observer<StateOnPostMenu> ->
                timeLineViewModel.sendListeningToUser(state, email)
                    .observe(lifecycleOwner, observer)
            }

        deletePost = { id: String, index: Int -> timeLineViewModel.deletePost(id, index) }
    }
}