package com.example.submissionintermediate1.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.submissionintermediate1.R

class MyPasswordEditText : AppCompatEditText{


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
        hint = resources.getString(R.string.password)
        gravity = Gravity.CENTER
        transformationMethod = PasswordTransformationMethod.getInstance()
    }

    private fun init() {
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty() && p0.length < 6 )
                    error = context.getString(R.string.password_error)
                }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }
}