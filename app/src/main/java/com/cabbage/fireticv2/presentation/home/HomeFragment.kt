package com.cabbage.fireticv2.presentation.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.data.Outcome
import com.cabbage.fireticv2.presentation.gameboard.*
import com.cabbage.fireticv2.presentation.utils.toast
import com.cabbage.fireticv2.presentation.utils.windowWidth

import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.include_player_status.*
import timber.log.Timber

class HomeFragment : Fragment(),
                     Gameboard.Delegate {

    private var currentPlayer: Long = Player1Token

    private val mViewModel by lazy {
        ViewModelProviders.of(activity!!).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adjustGameboardSize()
        adjustPlayerStatusBar()

        // Dev
        fabTest?.setOnClickListener {
            //            currentPlayer = -currentPlayer
//            togglePlayerStatusBar()


//            val wrong = "b1379553-2008-4cbb-aa9a-ce14701438e5"
//            val correct = "rOqLbgPOQwYbHqDzbdG1"
//
//            mViewModel.getGame(correct).observe(this, Observer {
//                //            mViewModel.createNewGame().observe(this, Observer {
//                when (it) {
//                    is Outcome.Progress -> Timber.w("loading: ${it.loading}")
//                    is Outcome.Success -> Timber.w(it.data.toString())
//                    is Outcome.Failure -> Timber.w(it.e.message)
//                }
//            })
        }

        fabTest2?.setOnClickListener {
//            mViewModel.sthCrazy(0, 3, -1)
        }

        gameboard.delegate = this
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        mViewModel.getGame().observe(this, Observer { model ->
//            Timber.v(model.toString())
//        })
    }

    // Adjust the size of gameboard and its children sectors, keeping gameboard in a square shape,
    // and its side length equal to the smaller dimension of its parent view
    private fun adjustGameboardSize() {
        view?.post {
            val parent = gameboard.parent as View
            Math.min(parent.width, parent.height).let { smallerDimen ->
                val lp = gameboard.layoutParams
                lp.width = smallerDimen
                lp.height = smallerDimen
                gameboard.layoutParams = lp
            }
        }
    }

    // Extend the player status bar outside the window, so it can be shifted left and right
    private fun adjustPlayerStatusBar() {
        (barContainer?.layoutParams as? FrameLayout.LayoutParams)?.let { lp ->
            lp.setMargins(
                    -activity.windowWidth / 2,
                    lp.topMargin,
                    -activity.windowWidth / 2,
                    lp.bottomMargin
            )
        }
    }

    // Shift the player bar left and right according to who's turn it is to play
    // TODO: Need landscape version
    private fun togglePlayerStatusBar(isWinner: Boolean = false) {
        Timber.d("toggleUserIndicator %d", currentPlayer)

        if (barContainer == null) return

        val animator = barContainer!!.animate()
        animator.duration = 667L
        val shiftAmount = if (isWinner)
            0.6f * activity.windowWidth
        else 0.3f * activity.windowWidth

        when (currentPlayer) {
            Player1Token -> animator.translationX(shiftAmount)
            Player2Token -> animator.translationX(-shiftAmount)
            else -> {
                animator.translationX(0f)
                animator.duration = 0L
            }
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
            activity?.toast("Game over!")
            return true
        }
    }
}