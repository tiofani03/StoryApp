package com.tiooooo.core.ui.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tiooooo.core.utils.extensions.isEmailValid


class MyCustomEditText : TextInputLayout {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private var editText: TextInputEditText? = null
    private fun init() {
        setWillNotDraw(false)
        editText = TextInputEditText(context)
        editText?.let {
            createEditBox(it)
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let {
                        error = if (s.isEmailValid()) null
                        else "Email tidak valid"
                    } ?: kotlin.run {
                        error = "Field is required"
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }


            })
        }

    }

    private fun createEditBox(editText: TextInputEditText) {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        with(editText) {
            setPadding(32, 10, 10, 10)
            editText.layoutParams = layoutParams
            editText.minimumHeight = 150
        }

        addView(editText)
    }
}