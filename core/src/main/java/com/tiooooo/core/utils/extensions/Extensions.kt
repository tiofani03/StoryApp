package com.tiooooo.core.utils.extensions

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.tiooooo.core.R
import com.tiooooo.core.utils.constant.InfoEnum
import timber.log.Timber


fun Context.getColorId(colorId: Int): Int {
    return if (Build.VERSION.SDK_INT < 23) {
        ContextCompat.getColor(this, colorId)
    } else {
        resources.getColor(colorId, null)
    }
}

fun Activity.snackBar(
    message: String?,
    type: InfoEnum,
    duration: Int = Snackbar.LENGTH_SHORT,
    topPosition: Boolean = false
): Snackbar? {
    if (message == null) return null
    val view = window.decorView.rootView.findViewById<View>(android.R.id.content)
    val sb = Snackbar.make(view, message, duration)

    if (topPosition) {
        val layoutParams = ActionBar.LayoutParams(sb.view.layoutParams)
        layoutParams.gravity = Gravity.TOP
        sb.view.setPadding(0, 10, 0, 0)
        sb.view.layoutParams = layoutParams
        sb.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    } else sb.animationMode = Snackbar.ANIMATION_MODE_SLIDE


    sb.setBackgroundTint(
        getColorId(
            when (type) {
                InfoEnum.INFO -> R.color.info_color
                InfoEnum.SUCCESS -> R.color.success_color
                InfoEnum.WARNING -> R.color.warning_color
                InfoEnum.DANGER -> R.color.danger_color
            }
        )
    )
    sb.show()
    Timber.i("[SNACKBAR] $message")
    return sb
}

fun Fragment.snackBar(
    message: String?,
    type: InfoEnum,
    duration: Int = Snackbar.LENGTH_SHORT,
    topPosition: Boolean = false,
): Snackbar? {
    return activity?.snackBar(message, type, duration, topPosition)
}

object TextListener {
    const val BEFORE = "before"
    const val CHANGE = "change"
    const val AFTER = "after"
}

fun getTextWatcher(afterTextChanged: (type: String, String) -> Unit) = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        afterTextChanged(TextListener.BEFORE, s.toString())
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        afterTextChanged(TextListener.CHANGE, s.toString())
    }

    override fun afterTextChanged(editable: Editable?) {
        afterTextChanged(TextListener.AFTER, editable.toString())
    }
}