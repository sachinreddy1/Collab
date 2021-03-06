package com.sachinreddy.feature.table.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.sachinreddy.feature.R

class OperationButton(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    init {
        View.inflate(context, R.layout.operation_button, this)
        val buttonCircle: ConstraintLayout = findViewById(R.id.button_circle)
        val buttonIcon: ImageView = findViewById(R.id.button_icon)
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.OperationButton)
        buttonCircle.backgroundTintList =
            attributes.getColorStateList(R.styleable.OperationButton_buttonCircleColor)
        buttonCircle.visibility =
            if (attributes.getBoolean(
                    R.styleable.OperationButton_buttonEnabled,
                    false
                )
            ) View.VISIBLE else View.INVISIBLE
        buttonIcon.setImageResource(
            attributes.getResourceId(
                R.styleable.OperationButton_buttonImage,
                R.drawable.ic_selection
            )
        )
        buttonIcon.imageTintList =
            attributes.getColorStateList(R.styleable.OperationButton_buttonImageTint)
        attributes.recycle()
    }
}