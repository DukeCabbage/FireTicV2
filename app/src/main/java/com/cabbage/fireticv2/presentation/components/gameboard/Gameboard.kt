package com.cabbage.fireticv2.presentation.components.gameboard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import butterknife.BindViews
import butterknife.ButterKnife
import com.cabbage.fireticv2.R
import timber.log.Timber

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
            Timber.v("Sector on click: $sectorIndex")

            if (sector.isActive) return@OnClickListener

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

    fun setSectorData(sectorIndex: Int, gridIndex: Int? = null, data: List<Int>) {
        if (gridIndex != null) {
            if (currentActive != gridIndex) {
                sectorViews[currentActive!!].isActive = false
            }

            sectorViews[gridIndex].isActive = true
            currentActive = gridIndex
        }

        sectorViews[sectorIndex].setData(data, changeIndex = gridIndex)
    }
}