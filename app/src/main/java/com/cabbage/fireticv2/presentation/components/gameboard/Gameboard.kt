package com.cabbage.fireticv2.presentation.components.gameboard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import butterknife.BindViews
import butterknife.ButterKnife
import com.cabbage.fireticv2.R

class Gameboard(context: Context, attributeSet: AttributeSet?)
    : FrameLayout(context, attributeSet) {

    private var currentActive: Int? = null

    @Suppress("ProtectedInFinal")
    @BindViews(
            R.id.sector_0,
            R.id.sector_1,
            R.id.sector_2,
            R.id.sector_3,
            R.id.sector_4,
            R.id.sector_5,
            R.id.sector_6,
            R.id.sector_7,
            R.id.sector_8
    )
    protected lateinit var sectorViews: List<GameboardSector>

    init {
        View.inflate(context, R.layout.gameboard, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ButterKnife.bind(this, this)

        for (sector in sectorViews) sector.setOnClickListener(sectorOnClickListener)
    }

    private val sectorOnClickListener = OnClickListener { view ->
        (view as GameboardSector).let { sector ->
            val sectorIndex = sector.sectorIndex

            if (currentActive != null) {
                sectorViews[currentActive!!].isActive = false
            }

            currentActive = sectorIndex
            sector.isActive = true
        }
    }

    fun setCallback(callback: GameboardSector.Callback) {
        for (sector in sectorViews) sector.callback = callback
    }
}