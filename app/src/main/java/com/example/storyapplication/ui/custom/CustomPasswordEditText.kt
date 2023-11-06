package com.example.storyapplication.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class CustomPasswordEditText:AppCompatEditText {

    constructor(context: Context):super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if(text.toString().length<8){
            setError("The password cannot be less than 8 characters",null)
        }else{
            error=null
        }
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }


}