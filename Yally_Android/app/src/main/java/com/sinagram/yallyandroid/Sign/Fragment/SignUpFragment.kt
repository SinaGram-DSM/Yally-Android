package com.sinagram.yallyandroid.Sign.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.signinup_layout.view.*

class SignUpFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signinup_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            signinup_title_textView.text = getString(R.string.welcome_first_visit)
            signinup_subtitle_textView.text = getString(R.string.start_sign_up)
            signinup_name_editText.visibility = View.VISIBLE
            signinup_do_sign_button.text = getString(R.string.sign_in)
            signinup_forgot_textView.visibility = View.GONE
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requireActivity().findViewById<TextView>(R.id.sign_title_textView).text =
            getString(R.string.sign_in)
    }
}