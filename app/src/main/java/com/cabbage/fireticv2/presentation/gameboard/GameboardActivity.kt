package com.cabbage.fireticv2.presentation.gameboard

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import android.widget.Toast
import butterknife.ButterKnife
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.presentation.utils.ViewUtil
import kotlinx.android.synthetic.main.activity_gameboard.*
import kotlinx.android.synthetic.main.include_appbar_basic.*
import kotlinx.android.synthetic.main.include_player_status.*
import timber.log.Timber

class GameboardActivity : AppCompatActivity(),
                          Gameboard.Delegate {

    private var currentPlayer = Player1Token    // The player with whom the next move will be made
    private var windowWidth: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameboard)
        ButterKnife.bind(this)
        setUpActionBar()

        gameboard.delegate = this
        // Dev
        fabTest.setOnClickListener {
            currentPlayer = -currentPlayer
            this.togglePlayerStatusBar()
        }
    }

    override fun onStart() {
        super.onStart()
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        windowWidth = size.x

//        val lp = barContainer.layoutParams as FrameLayout.LayoutParams
//        lp.setMargins(-size.x / 2, lp.topMargin, -size.x / 2, lp.bottomMargin)
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Compensate for translucent status bar
        ViewUtil.getStatusBarHeight(this)?.let { statusBarHeight ->
            toolbar.setPadding(
                    toolbar.paddingLeft,
                    toolbar.paddingTop + statusBarHeight,
                    toolbar.paddingRight,
                    toolbar.paddingBottom)

            toolbar.layoutParams.height += statusBarHeight
        }
    }

    // Shift the player bar left and right according to who's turn it is to play
    private fun togglePlayerStatusBar(isWinner: Boolean = false) {
        Timber.d("toggleUserIndicator %d", currentPlayer)
        windowWidth?.let { width ->
//            val animator = barContainer.animate()
//            animator.duration = 667L
//            val shiftAmount = if (isWinner)
//                0.6f * width
//            else 0.3f * width
//
//            when (currentPlayer) {
//                Player1Token -> animator.translationX(shiftAmount)
//                Player2Token -> animator.translationX(-shiftAmount)
//                else -> {
//                    animator.translationX(0f)
//                    animator.duration = 0L
//                }
//            }
        }
    }

    override fun makeMove(gameboard: Gameboard, move: Pair<Int, Int>)
            : Triple<Int, Int, Long>? {
        // TODO: check if it's player's turn
        val previousPlayer = currentPlayer
        currentPlayer = -currentPlayer
        this.togglePlayerStatusBar()

        // TODO: transmit this move to some kind of repository

        return Triple(move.first, move.second, previousPlayer)
    }

    override fun isGameOver(gameboard: Gameboard): Boolean {
        val winner = checkWin(gameboard.sectorOwners)
        if (winner == OpenGrid) {
            return false
        } else {
            currentPlayer = winner
            togglePlayerStatusBar(isWinner = true)
            Toast.makeText(this, "Game over!", Toast.LENGTH_LONG).show()
            return true
        }
    }
}