package com.cabbage.fireticv2.presentation.gameboard

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import butterknife.BindViews
import butterknife.ButterKnife
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.presentation.utils.ElevationSetter
import java.lang.ref.WeakReference


class GameboardSector(context: Context, attributeSet: AttributeSet?)
    : CardView(context, attributeSet) {

    companion object {
        private const val ZOOM_FACTOR = 2.2f
    }

    //region Res
    private val colorWhiteSmoke = ContextCompat.getColor(context, R.color.colorGrey100)
    private val colorPlayer1 = ContextCompat.getColor(context, R.color.player1)
    private val colorPlayer2 = ContextCompat.getColor(context, R.color.player2)

    private val defaultElevation = resources.getDimension(R.dimen.sector_default_elevation)
    //endregion

    //region Properties
    private val horizontalOrder: Order.HorizontalOrder
    private val verticalOrder: Order.VerticalOrder
    private val sectorIndex get() = Order.orderToIndex(verticalOrder, horizontalOrder)

    @BindViews(
            R.id.btn_0, R.id.btn_1, R.id.btn_2,
            R.id.btn_3, R.id.btn_4, R.id.btn_5,
            R.id.btn_6, R.id.btn_7, R.id.btn_8)
    protected lateinit var gridViews: List<@JvmSuppressWildcards View>

    var callback: Callback? = null

    var enlarged: Boolean = false
        set(value) {
            toggleActiveMode(value)
            field = value
        }

    //endregion

    //region View
    init {
        val typedArray = context.theme
                .obtainStyledAttributes(attributeSet, R.styleable.GameboardSector, 0, 0)

        try {
            val ordinalH = typedArray.getInt(R.styleable.GameboardSector_horizontalOrder, Order.HorizontalOrder.CENTER.ordinal)
            horizontalOrder = Order.HorizontalOrder.values()[ordinalH]

            val ordinalV = typedArray.getInt(R.styleable.GameboardSector_verticalOrder, Order.VerticalOrder.CENTER.ordinal)
            verticalOrder = Order.VerticalOrder.values()[ordinalV]
        } finally {
            typedArray.recycle()
        }

        View.inflate(context, R.layout.gameboard_sector, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ButterKnife.bind(this, this)

        this.gridViews.forEach {
            it.setOnClickListener { grid ->
                val gridIndex = Integer.valueOf(grid.tag.toString())
                callback?.onGridClicked(sectorIndex, gridIndex)
            }
        }

        this.setOnClickListener { callback?.onSectorClicked(sectorIndex) }
    }

    /**
     * If this sector is not active (zoomed in), the whole sector will be the target of touch events,
     * otherwise, individual grids will handle the events
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val intercept = !enlarged  // Whole sector as the target of touch events
        return intercept || super.onInterceptTouchEvent(ev)
    }

    //endregion

    fun setGridForPlayer(@Player token: Long, gridIndex: Int) {
        if (gridIndex in 0..GridCount) {
            when (token) {
                Player1Token -> gridViews[gridIndex].setBackgroundColor(colorPlayer1)
                Player2Token -> gridViews[gridIndex].setBackgroundColor(colorPlayer2)
                OpenGrid -> gridViews[gridIndex].setBackgroundColor(colorWhiteSmoke)
                else -> throw IllegalArgumentException()
            }
        } else {
            throw IndexOutOfBoundsException()
        }

    }

    fun highlightGrid(gridIndex: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // Animate size and elevation
    private fun toggleActiveMode(isEnlarging: Boolean) {
        if (this.enlarged == isEnlarging) return

        val currentScale = this.scaleX
        val animator = this.animate()
        val width = this.width
        val height = this.height
        when (horizontalOrder) {
            Order.HorizontalOrder.LEFT -> this.pivotX = (width * 0.2).toInt().toFloat()
            Order.HorizontalOrder.RIGHT -> this.pivotX = (width * 0.8).toInt().toFloat()
            else -> this.pivotX = (width / 2).toFloat()
        }

        when (verticalOrder) {
            Order.VerticalOrder.TOP -> this.pivotY = (height * 0.2).toInt().toFloat()
            Order.VerticalOrder.BOTTOM -> this.pivotY = (height * 0.8).toInt().toFloat()
            else -> this.pivotY = (height / 2).toFloat()
        }

        if (currentScale == 1f && isEnlarging) {
            animator.scaleX(ZOOM_FACTOR).scaleY(ZOOM_FACTOR)
        } else if (!isEnlarging) {
            animator.scaleX(1f).scaleY(1f)
        } else {
            return
        }

        animator.setDuration(333L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(ElevationSetter(WeakReference(this), defaultElevation))
                .start()
    }

    interface Callback {
        fun onGridClicked(sectorIndex: Int, gridIndex: Int)

        fun onSectorClicked(sectorIndex: Int)
    }
}