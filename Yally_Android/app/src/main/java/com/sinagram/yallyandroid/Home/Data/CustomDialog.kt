package com.sinagram.yallyandroid.Home.Data

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Button
import com.sinagram.yallyandroid.R

class CustomDialog(private val context: Context) {
    fun showDialog(deletePost: () -> Unit) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_delete_post)
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