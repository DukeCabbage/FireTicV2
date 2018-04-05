package com.cabbage.fireticv2.presentation.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.data.Outcome
import com.cabbage.fireticv2.presentation.gameboard.Player1Token
import com.cabbage.fireticv2.presentation.gameboard.Player2Token
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.include_player_status.*
import timber.log.Timber

class HomeFragment : Fragment() {

    private var windowWidth: Int? = null
    private var currentPlayer: Long = Player1Token

    private val mViewModel: HomeViewModel
        get() = ViewModelProviders.of(activity!!).get(HomeViewModel::class.java)


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adjustGameboardSize()

        val size = Point()
        activity?.windowManager?.defaultDisplay?.getSize(size)
        windowWidth = size.x

        Timber.i("bar is null: ${barContainer == null}")

        (barContainer?.layoutParams as? FrameLayout.LayoutParams)?.let { lp ->
            lp.setMargins(-size.x / 2, lp.topMargin, -size.x / 2, lp.bottomMargin)
        }

//        barContainer?.postDelayed( { Timber.i("" + barContainer?.layoutParams?.width) },1000)

        fabTest?.setOnClickListener {
            //            currentPlayer = -currentPlayer
//            togglePlayerStatusBar()


            val wrong = "b1379553-2008-4cbb-aa9a-ce14701438e5"
            val correct = "rOqLbgPOQwYbHqDzbdG1"

            mViewModel.getGame(correct).observe(this, Observer {
//            mViewModel.createNewGame().observe(this, Observer {
                when (it) {
                    is Outcome.Progress -> Timber.w("loading: ${it.loading}")
                    is Outcome.Success -> Timber.w(it.data.toString())
                    is Outcome.Failure -> Timber.w(it.e.message)
                }
            })
        }

        fabTest2?.setOnClickListener {
            mViewModel.sthCrazy(0,3,-1)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.getGame().observe(this, Observer { model ->
            Timber.v(model.toString())
        })
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

    // Shift the player bar left and right according to who's turn it is to play
    private fun togglePlayerStatusBar(isWinner: Boolean = false) {
        Timber.d("toggleUserIndicator %d", currentPlayer)

        if (barContainer == null) return

        windowWidth?.let { width ->
            val animator = barContainer!!.animate()
            animator.duration = 667L
            val shiftAmount = if (isWinner)
                0.6f * width
            else 0.3f * width

            when (currentPlayer) {
                Player1Token -> animator.translationX(shiftAmount)
                Player2Token -> animator.translationX(-shiftAmount)
                else -> {
                    animator.translationX(0f)
                    animator.duration = 0L
                }
            }
        }
    }
}