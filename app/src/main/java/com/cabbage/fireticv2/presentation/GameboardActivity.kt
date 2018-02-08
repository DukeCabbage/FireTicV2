package com.cabbage.fireticv2.presentation

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import android.widget.Toast
import butterknife.ButterKnife
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.presentation.components.gameboard.GameboardSector
import com.cabbage.fireticv2.presentation.components.gameboard.Player
import com.cabbage.fireticv2.presentation.utils.ViewUtil
import kotlinx.android.synthetic.main.activity_gameboard.*
import kotlinx.android.synthetic.main.include_appbar_collapsing.*
import kotlinx.android.synthetic.main.include_player_status.*
import timber.log.Timber

class GameboardActivity : AppCompatActivity(),
                          GameboardSector.Callback {

    private var currentPlayer = Player.One
    private var windowWidth: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameboard)
        ButterKnife.bind(this)
        setUpActionBar()

        gameboard.setCallback(this)
        fabTest.setOnClickListener { togglePlayerStatusBar(currentPlayer.toggle()) }
    }

    override fun onStart() {
        super.onStart()
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        windowWidth = size.x

        val lp = barContainer.layoutParams as FrameLayout.LayoutParams
        lp.setMargins(-size.x / 2, lp.topMargin, -size.x / 2, lp.bottomMargin)
    }

    override fun onUserClick(sectorIndex: Int, gridIndex: Int, currentData: List<Int>) {
        Toast.makeText(this, "$sectorIndex $gridIndex", Toast.LENGTH_SHORT).show()

        if (currentData[gridIndex] != Player.Open.token) {
            Timber.w("Already occupied")
            return
        }

        currentData.toMutableList().let { data ->
            data[gridIndex] = currentPlayer.token
            gameboard.setSectorData(sectorIndex, gridIndex, data)
        }

        togglePlayerStatusBar(currentPlayer.toggle())
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Compensate for translucent status bar
        ViewUtil.getStatusBarHeight(this).let { statusBarHeight ->
            toolbar.setPadding(
                    toolbar.paddingLeft,
                    toolbar.paddingTop + statusBarHeight,
                    toolbar.paddingRight,
                    toolbar.paddingBottom)

            toolbar.layoutParams.height += statusBarHeight
        }
    }

    private fun togglePlayerStatusBar(nextPlayer: Player, isWinner: Boolean = false) {
        Timber.d("toggleUserIndicator %d", nextPlayer.token)
        windowWidth?.let { width ->
            val animator = barContainer.animate()
            animator.duration = 667L
            val shiftAmount = if (isWinner)
                0.6f * width
            else 0.3f * width

            when (nextPlayer) {
                Player.One -> animator.translationX(shiftAmount)
                Player.Two -> animator.translationX(-shiftAmount)
                else -> {
                    animator.translationX(0f)
                    animator.duration = 0L
                }
            }
        }

        currentPlayer = nextPlayer
    }
}