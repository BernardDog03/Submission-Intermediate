package com.example.submissionintermediate1.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.AutoText
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.submissionintermediate1.R
import java.io.InputStream

class MyEmailEditText : AppCompatEditText{

    constructor(context: Context): super(context){
        init()
    }


    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttributeSet: Int): super(context, attributeSet, defStyleAttributeSet){
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = resources.getString(R.string.email)
        gravity = Gravity.CENTER
    }


    private fun init(){
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(p0).matches())
                error = context.getString(R.string.email_error)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }
}