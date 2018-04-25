package com.cabbage.fireticv2.presentation

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.data.FireTicRepository
import com.cabbage.fireticv2.data.Outcome
import com.cabbage.fireticv2.data.game.Game
import com.cabbage.fireticv2.data.game.Game2
import com.cabbage.fireticv2.data.game.Move
import com.cabbage.fireticv2.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.include_appbar_basic.*
import timber.log.Timber
import java.util.*

class TestActivity : BaseActivity() {

    private lateinit var repository: FireTicRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = activityComponent.repository()

        setContentView(R.layout.activity_test)
        setUpActionBar(toolbar)

        repository.userRepository.getSignedInUser()
                .observe(this, Observer {
                    Timber.w(it.toString())
                })

        fab.setOnClickListener {

            lalla("FemmB1JL2yNDHyKotUGN")
//            repository.createOnlineGame2()
//                    .observe(this, Observer {
//                        when (it) {
//                            is Outcome.Progress -> Timber.w("loading: ${it.loading}")
//                            is Outcome.Failure -> Timber.e(it.e)
//                            is Outcome.Success -> {
//                                Timber.w(it.data)
//                                lalla(it.data)
//                            }
//                        }
//                    })
        }

        fabAdd.setOnClickListener {
            val move = Move(position = Random().nextInt(81), player = Random().nextInt(3) - 1)
            repository.makeMove2(game!!, move)
                    .observe(this, Observer {
                        when (it) {
                            is Outcome.Progress -> Timber.w("loading: ${it.loading}")
                            is Outcome.Failure -> Timber.e(it.e)
                            is Outcome.Success -> {
                                Timber.w(it.data.toString())
                            }
                        }
                    })
        }
    }

    private var game: Game2? = null

    private fun lalla(gameId: String) {
        repository.startWatching2(gameId)
                .observe(this, Observer {
                    Timber.i(it.toString())
                    game = it
                })
    }
}