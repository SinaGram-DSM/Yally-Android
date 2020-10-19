package com.sinagram.yallyandroid.Home.View

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.FontSpan
import kotlinx.android.synthetic.main.item_post_cardview.view.*
import java.text.SimpleDateFormat
import java.util.*

class MainTimeLineAdapter(var postsList: List<Post>, val font: Typeface?) :
    RecyclerView.Adapter<MainTimeLineAdapter.MainTimeLineViewHolder>() {

    override fun getItemCount(): Int = postsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainTimeLineViewHolder {
        return MainTimeLineViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_cardview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainTimeLineViewHolder, position: Int) {
        val postData = postsList[position]

        holder.apply {
            itemView.apply {
                post_user_name_textView.text = postData.user.nickname
                post_yally_count_textView.text =
                    Resources.getSystem().getString(R.string.yally_count, postData.yally)
                post_comments_count_textView.text =
                    Resources.getSystem().getString(R.string.comment_count, postData.comment)
            }

            setTimeFromUploadedTime(postData.createdAt)
            applyBoldToTags(postData.content)
            checkClickedYally(postData.isYally)
        }
    }

    inner class MainTimeLineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun setTimeFromUploadedTime(uploadedDate: String) {
            val now = System.currentTimeMillis()
            val dateNow = Date(now)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            try {
                val dateCreated = dateFormat.parse(uploadedDate)!!
                val duration = dateNow.time - dateCreated.time
                val min = duration / 60000

                val timeDifference = when {
                    min < 1 -> "방금전"
                    min in 1..59 -> "${min}분"
                    min in 60..1440 -> "${duration / 3600000}시간"
                    else -> SimpleDateFormat(
                        "MM월 dd일",
                        Locale.getDefault()
                    ).format(dateCreated.time)
                }

                itemView.post_uploaded_time_textView.text = timeDifference
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun checkClickedYally(isYally: Boolean) {
            if (isYally) {
                itemView.post_do_yally_imageView.setImageResource(R.drawable.ic_yally_note_colorful)
                itemView.post_do_yally_textView.setTextColor(Color.parseColor("#6E3CEF"))
            }
        }

        fun applyBoldToTags(content: String) {
            itemView.post_content_textView.text = content
            var isCharacter = false
            val span: Spannable = itemView.post_content_textView.text as Spannable
            var index = 0

            while (content.length >= index + 1) {
                if (content[index] == '#') isCharacter = !isCharacter
                if (content[index] == ' ') isCharacter = false
                if (isCharacter) {
                    span.setSpan(
                        FontSpan(font),
                        index,
                        index + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                index++
            }
        }
    }
}