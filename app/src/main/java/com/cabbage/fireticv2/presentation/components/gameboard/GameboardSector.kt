package com.cabbage.fireticv2.presentation.components.gameboard

import android.content.Context
import android.content.res.TypedArray
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.AccelerateDecelerateInterpolator
import butterknife.BindViews
import butterknife.ButterKnife
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.presentation.utils.ElevationSetter
import kotlinx.android.synthetic.main.gameboard_sector.view.*
import java.lang.ref.WeakReference

class GameboardSector(context: Context, attributeSet: AttributeSet?)
    : CardView(context, attributeSet) {

    companion object {
        private const val ZOOM_FACTOR = 3.0f
    }

    private val colorWhiteSmoke = resources.getColor(R.color.colorGrey100)
    private val colorDivider = resources.getColor(R.color.divider)
    private val colorPlayer1 = resources.getColor(R.color.player1)
    private val colorPlayer2 = resources.getColor(R.color.player2)
    private val defaultElevation = resources.getDimension(R.dimen.sector_default_elevation)

    private val horizontalOrder: Order.HorizontalOrder
    private val verticalOrder: Order.VerticalOrder
    val sectorIndex get() = Order.orderToIndex(verticalOrder, horizontalOrder)

    @Suppress("ProtectedInFinal")
    @BindViews(
            R.id.btn_0,
            R.id.btn_1,
            R.id.btn_2,
            R.id.btn_3,
            R.id.btn_4,
            R.id.btn_5,
            R.id.btn_6,
            R.id.btn_7,
            R.id.btn_8
    )
    protected lateinit var gridViews: List<@JvmSuppressWildcards View>

    //region View
    init {
        val typedArray: TypedArray = context.theme.obtainStyledAttributes(attributeSet,
                R.styleable.GameboardSector,
                0, 0)

        try {
            val ordinalH = typedArray.getInt(R.styleable.GameboardSector_horizontalOrder, Order.HorizontalOrder.CENTER.ordinal)
            horizontalOrder = Order.HorizontalOrder.values()[ordinalH]

            val ordinalV = typedArray.getInt(R.styleable.GameboardSector_verticalOrder, Order.VerticalOrder.CENTER.ordinal)
            verticalOrder = Order.VerticalOrder.values()[ordinalV]

            View.inflate(context, R.layout.gameboard_sector, this)
        } finally {
            typedArray.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ButterKnife.bind(this, this)
        gridViews.forEach { it.setOnClickListener(gridOnClickListener) }
    }

    /**
     * If this sector is not active (zoomed in), the whole sector will be the target of touch events,
     * otherwise, individual grids will handle the events
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val intercept = !isActive  // Whole sector as the target of touch events
        return intercept || super.onInterceptTouchEvent(ev)
    }

    //endregion

    private val gridOnClickListener = OnClickListener { view ->
        val gridIndex = Integer.valueOf(view.tag.toString())
//        Timber.v("Child on click: $gridIndex")
        callback?.onUserClick(sectorIndex, gridIndex, data)
    }

    //region Properties
    var callback: Callback? = null

    var isActive: Boolean = false
        set(value) {
            toggleActiveMode(value)
            field = value
        }

    var winner = Player.Open.token
        set(value) {
            setBorderColorBy(value)
            field = value
        }

    private val data = MutableList(9, init = { _ -> Player.Open.token })

    /**
     * Set ownership of grid for all grids, or at specified index
     *
     * @throws IllegalArgumentException, size of the list is not 9
     */
    fun setData(newData: List<Int>, changeIndex: Int? = null) {
        if (newData.size != 9) throw IllegalArgumentException("Expects list of size of 9!")

        if (changeIndex == null) {
            // Update every grid if applicable
            for ((index, value) in newData.withIndex()) {

                val oldValue = data[index]
                if (oldValue != value) {
                    setGridColorBy(value, index)
                    data[index] = value
                }
            }
        } else {
            val value = newData[changeIndex]
            val oldValue = data[changeIndex]
            if (oldValue != value) {
                setGridColorBy(value, changeIndex)
                data[changeIndex] = value
            }
        }

        winner = Konst.checkWin(data)
    }

    //endregion

    //region visual update

    private fun setGridColorBy(playerToken: Int, gridIndex: Int) {
        when (playerToken) {
            Player.One.token -> gridViews[gridIndex].setBackgroundColor(colorPlayer1)
            Player.Two.token -> gridViews[gridIndex].setBackgroundColor(colorPlayer2)
            else -> gridViews[gridIndex].setBackgroundColor(colorWhiteSmoke)
        }
    }

    private fun setBorderColorBy(playerToken: Int) {
        when (playerToken) {
            Player.One.token -> sectorTableLayout.setBackgroundColor(colorPlayer1)
            Player.Two.token -> sectorTableLayout.setBackgroundColor(colorPlayer2)
            else -> sectorTableLayout.setBackgroundColor(colorDivider)
        }
    }

    // Animate size and elevation
    private fun toggleActiveMode(isEnlarging: Boolean) {
        if (this.isActive == isEnlarging) return

        val currentScale = this.scaleX
        val animator = this.animate()
        val width = this.width
        val height = this.height
        when (horizontalOrder) {
            Order.HorizontalOrder.LEFT -> this.pivotX = (width * 0.1).toInt().toFloat()
            Order.HorizontalOrder.RIGHT -> this.pivotX = (width * 0.9).toInt().toFloat()
            else -> this.pivotX = (width / 2).toFloat()
        }

        when (verticalOrder) {
            Order.VerticalOrder.TOP -> this.pivotY = (height * 0.1).toInt().toFloat()
            Order.VerticalOrder.BOTTOM -> this.pivotY = (height * 0.9).toInt().toFloat()
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

    //endregion

    interface Callback {
        fun onUserClick(sectorIndex: Int, gridIndex: Int, currentData: List<Int>)
    }
}