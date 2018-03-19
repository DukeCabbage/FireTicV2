package com.cabbage.fireticv2.presentation.gameboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cabbage.fireticv2.R
import kotlinx.android.synthetic.main.fragment_gameboard.*

class GameboardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gameboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.post {
            val parent = gameboard.parent as View
            Math.min(parent.width, parent.height).let {
                val lp = gameboard.layoutParams
                lp.width = it
                lp.height = it
                gameboard.layoutParams = lp
            }
        }
    }
}