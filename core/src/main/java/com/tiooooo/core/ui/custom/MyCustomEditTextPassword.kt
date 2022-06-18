package com.tiooooo.core.ui.custom

import android.graphics.Canvas

class MyCustomEditTextPassword : androidx.appcompat.widget.AppCompatEditText {
    constructor(context: android.content.Context) : super(context){
        init()
    }
    constructor(context: android.content.Context, attrs: android.util.AttributeSet) : super(
        context,
        attrs
    ){
        init()
    }

    constructor(
        context: android.content.Context,
        attrs: android.util.AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr){ init()}

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }


    private fun init(){
        this.transformationMethod = android.text.method.PasswordTransformationMethod.getInstance()
    }

}