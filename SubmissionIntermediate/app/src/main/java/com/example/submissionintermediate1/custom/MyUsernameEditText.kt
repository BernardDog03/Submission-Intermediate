package com.example.submissionintermediate1.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatEditText
import com.example.submissionintermediate1.R

class MyUsernameEditText: AppCompatEditText {

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
        hint = resources.getString(R.string.username)
        gravity = Gravity.CENTER
    }

    private fun init() {
        inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    error = context.getString(R.string.username_error)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}