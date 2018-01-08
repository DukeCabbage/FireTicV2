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
import kotlinx.android.synthetic.main.gameboard_sector.view.*
import timber.log.Timber
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

    @Suppress("ProtectedInFinal")
    @BindViews(R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8)
    protected lateinit var gridList: List<@JvmSuppressWildcards View>

    var isActive: Boolean = false
        set(value) {
            this.toggleActiveMode(value)
            field = value
        }


    private var mCallback: Callback? = null
    private val horizontalOrder: Order.HorizontalOrder
    private val verticalOrder: Order.VerticalOrder

    private val sectorIndex get() = Order.orderToIndex(verticalOrder, horizontalOrder)

    private val gridOnClickListener = OnClickListener { view ->
        val viewTag = view.tag.toString()
        Timber.v("Child on click: %s", viewTag)

        val gridIndex = Integer.valueOf(viewTag)
        val moveMadeBy = mCallback?.onUserClick(this.sectorIndex, gridIndex)
        this.isActive = false
    }

    init {
        val typedArray: TypedArray = context.theme
                .obtainStyledAttributes(attributeSet,
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
        this.gridList.forEach { grid -> grid.setOnClickListener(gridOnClickListener) }
    }

    /**
     * If this sector is not active (zoomed in), the whole sector will be the target of touch events,
     * otherwise, individual grids will handle the events
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return !this.isActive || super.onInterceptTouchEvent(ev)
    }

    fun setCallback(callback: Callback) {
        this.mCallback = callback
    }

    fun moveMade(gridIndex: Int, byPlayer: Int): Boolean {
        when (byPlayer) {
            Konst.Player1Token -> this.gridList[gridIndex].setBackgroundColor(colorPlayer1)
            Konst.Player2Token -> this.gridList[gridIndex].setBackgroundColor(colorPlayer2)
            Konst.OpenGrid -> this.gridList[gridIndex].setBackgroundColor(colorWhiteSmoke)
            else -> return false
        }

        return true
    }

    fun setLocalWinner(player: Int) {
        when (player) {
            Konst.Player1Token -> this.sectorTableLayout.setBackgroundColor(colorPlayer1)
            Konst.Player2Token -> this.sectorTableLayout.setBackgroundColor(colorPlayer2)
            else -> this.sectorTableLayout.setBackgroundColor(colorDivider)
        }
    }

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

    interface Callback {
        fun onUserClick(sectorIndex: Int, gridIndex: Int): Int
    }
}