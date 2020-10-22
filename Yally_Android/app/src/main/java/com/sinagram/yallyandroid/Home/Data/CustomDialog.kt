package com.sinagram.yallyandroid.Home.Data

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Button
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.dialog_delete_post.*

class CustomDialog(private val context: Context) {
    fun showDialog(isPost: Boolean, deletePost: () -> Unit) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_delete_post)

        if (!isPost) {
            dialog.run {
                dialog_title_textView.text = "댓글 삭제"
                dialog_description_textView.text = "댓글을 삭제하면 다시 되돌릴 수 없습니다."
            }
        }

        dialog.show()

        val okButton = dialog.findViewById<View>(R.id.dialog_ok_button) as Button
        val cancelButton = dialog.findViewById<View>(R.id.dialog_cancel_button) as Button

        okButton.setOnClickListener {
            deletePost()
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}