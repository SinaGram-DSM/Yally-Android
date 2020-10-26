package com.sinagram.yallyandroid.Detail.Data

import com.sinagram.yallyandroid.Detail.ViewModel.DetailPostViewModel

data class CommentAdaptConnector(var clickDeleteComment: (String, Int) -> Unit = { _, _ -> }) {
    fun setAttributeFromComment(viewModel: DetailPostViewModel) {
        clickDeleteComment = { id: String, index: Int -> viewModel.deleteComment(id, index) }
    }
}