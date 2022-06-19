package com.tiooooo.core.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.tiooooo.core.R
import com.tiooooo.core.utils.extensions.isEmailValid


class MyCustomEditText : TextInputEditText {
    private var errorBackground: Drawable? = null
    private var defaultBackground: Drawable? = null
    private var isError: Boolean = false

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background =
            if (isError) errorBackground
            else defaultBackground
    }

    private fun init() {
        errorBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_white_rounded_4dp_stroke_red)
        defaultBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_white_rounded_4dp_stroke_grey)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
                validate(p0.toString())

            override fun afterTextChanged(p0: Editable?) =
                validate(p0.toString())

        })
    }

    private fun TextInputEditText.validate(input: String) {
        when (inputType) {
            EMAIL -> {
                isError = validateError(
                    !input.isEmailValid(),
                    context.getString(R.string.email_validation)
                )
            }
            PASSWORD -> {
                isError =
                    validateError(input.length < 6, context.getString(R.string.password_length))
            }

            PERSON_NAME -> {
                isError = validateError(input.isEmpty(), context.getString(R.string.error_name))
            }
        }
    }

    private fun TextInputEditText.validateError(state: Boolean, errorText: String): Boolean =
        if (state) {
            setError(errorText, null)
            true
        } else false


    companion object {
        const val EMAIL = 0x00000021
        const val PASSWORD = 0x00000081
        const val PERSON_NAME = 0x00000061
    }
}