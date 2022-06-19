package com.tiooooo.core.ui.custom

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.tiooooo.core.R
import timber.log.Timber

object LoadingDialog {
    private var dialog: Dialog? = null
    fun Activity.displayLoadingDialog() {
        dialog = Dialog(this)
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.layout_loading)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(false)
        }
        try {
            if (dialog != null) dialog?.show()
            else dialog?.dismiss()
        } catch (e: Exception) {
        }
    }

    fun hideLoadingDialog() {
        try {
            if (dialog != null) dialog?.dismiss()
            else dialog?.dismiss()
        } catch (e: Exception) {
            dialog?.dismiss()
            Timber.e(e)
        }
    }
}