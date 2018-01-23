package com.cabbage.fireticv2.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import butterknife.ButterKnife
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.ViewUtil
import com.cabbage.fireticv2.presentation.components.gameboard.GameboardSector
import com.cabbage.fireticv2.presentation.components.gameboard.Konst
import kotlinx.android.synthetic.main.activity_gameboard.*
import kotlinx.android.synthetic.main.include_appbar_collapsing.*

class GameboardActivity : AppCompatActivity(),
                          GameboardSector.Callback {

    private var currentPlayer = Konst.Player1Token

    override fun onUserClick(sectorIndex: Int, gridIndex: Int): Int {
        Toast.makeText(this, "$sectorIndex, $gridIndex", Toast.LENGTH_SHORT).show()
        testSector.moveMade(gridIndex, currentPlayer)
        testSector.setLocalWinner(currentPlayer)
        val returnValue = currentPlayer
        when (currentPlayer) {
            Konst.Player1Token -> currentPlayer = Konst.Player2Token
            Konst.Player2Token -> currentPlayer = Konst.Player1Token
        }

        return returnValue
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameboard)
        ButterKnife.bind(this)
        setUpActionBar()

        // Compensate for translucent status bar
        ViewUtil.getStatusBarHeight(this).let { statusBarHeight ->
            toolbar.setPadding(
                    toolbar.paddingLeft,
                    toolbar.paddingTop + statusBarHeight,
                    toolbar.paddingRight,
                    toolbar.paddingBottom)

            toolbar.layoutParams.height += statusBarHeight
        }

        testSector.setCallback(this)
        testSector.setOnClickListener { view ->
            (view as GameboardSector).let {
                view.isActive = true
            }
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}