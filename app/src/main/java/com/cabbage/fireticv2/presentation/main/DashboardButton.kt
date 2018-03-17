package com.cabbage.fireticv2.presentation.main

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import butterknife.ButterKnife
import com.cabbage.fireticv2.R
import kotlinx.android.synthetic.main.dashboard_button.view.*

class DashboardButton(context: Context, attributeSet: AttributeSet)
    : CardView(context, attributeSet) {

    private val defaultContentTint get() = resources.getColor(R.color.colorTextPrimary)

    private var image: Drawable
    private var label: String
    private var contentTint: Int = 0

    init {
        val typedArray: TypedArray = context.theme
                .obtainStyledAttributes(attributeSet,
                        R.styleable.DashboardButton,
                        0, 0)

        try {
            image = typedArray.getDrawable(R.styleable.DashboardButton_image)
            label = typedArray.getString(R.styleable.DashboardButton_label)
            contentTint = typedArray.getColor(R.styleable.DashboardButton_contentTint, defaultContentTint)
            View.inflate(context, R.layout.dashboard_button, this)
        } finally {
            typedArray.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ButterKnife.bind(this, this)

        DrawableCompat.setTint(image, contentTint)
        ivImage.setImageDrawable(image)

        tvLabel.text = label
        tvLabel.setTextColor(contentTint)

        this.isClickable = true
        this.isFocusable = true
    }
}