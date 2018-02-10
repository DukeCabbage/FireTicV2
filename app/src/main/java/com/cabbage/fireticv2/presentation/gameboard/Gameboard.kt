package com.cabbage.fireticv2.presentation.gameboard

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import butterknife.BindViews
import butterknife.ButterKnife
import com.cabbage.fireticv2.R
import timber.log.Timber

class Gameboard(context: Context, attributeSet: AttributeSet?)
    : FrameLayout(context, attributeSet),
      GameboardSector.Callback {

    //region Res
    private val colorDivider = ContextCompat.getColor(context, R.color.divider)
    private val colorSecondary = ContextCompat.getColor(context, R.color.colorSecondary)
    private val colorPlayer1Dark = ContextCompat.getColor(context, R.color.player1Dark)
    private val colorPlayer2Dark = ContextCompat.getColor(context, R.color.player2Dark)
    //endregion

    private var lockGameboard = false
    private var enlargedSector: Int? = null
    private var lastMove: Triple<Int, Int, Long>? = null

    var gridOwners = List(GridCount * SectorCount, { _ -> OpenGrid })
        private set
    var sectorOwners = List(SectorCount, { _ -> OpenGrid })
        private set

    @BindViews(
            R.id.sector_0, R.id.sector_1, R.id.sector_2,
            R.id.sector_3, R.id.sector_4, R.id.sector_5,
            R.id.sector_6, R.id.sector_7, R.id.sector_8)
    protected lateinit var sectorViews: List<GameboardSector>

    var delegate: Delegate? = null

    init {
        View.inflate(context, R.layout.gameboard, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ButterKnife.bind(this, this)

        sectorViews.forEach { it.callback = this }
    }

    // Validate the move before propagating this event to the activity
    override fun onGridClicked(sectorIndex: Int, gridIndex: Int) {
        // Check if this sector is unlocked
        val unlockedSectorIndex = lastMove?.second
        if (unlockedSectorIndex != null && unlockedSectorIndex != sectorIndex) {
            Toast.makeText(context, "This sector is not unlocked", Toast.LENGTH_SHORT).show()
            Timber.w("Sector locked")
            return
        }

        // Check if this grid is taken
        val index = sectorIndex * GridCount + gridIndex
        if (gridOwners[index] != OpenGrid) {
            Toast.makeText(context, "This grid is already taken", Toast.LENGTH_SHORT).show()
            Timber.w("Grid already taken")
            return
        }

        this.makeMove(sectorIndex, gridIndex)
    }

    // Inform the delegate that player wish to make a move, and get whose turn it is from the delegate
    private fun makeMove(sectorIndex: Int, gridIndex: Int) {
        delegate?.makeMove(this, Pair(sectorIndex, gridIndex))?.let { newMove ->

            // Update the ownership of the grid being played on
            val indexPlayedOn = sectorIndex * GridCount + gridIndex
            gridOwners = gridOwners.mapIndexed { index, original ->
                if (indexPlayedOn == index) newMove.third else original
            }
            sectorViews[sectorIndex].setGridForPlayer(newMove.third, gridIndex)

            // Update the sector ownership, and sets background color and elevation
            checkSectorWinner(sectorIndex)
            // Enlarge the next sector
            onSectorClicked(gridIndex)

            lastMove = newMove

            delegate?.isGameOver(this)?.let { gameOver ->
                if (gameOver) {
                    toggleSectorHighlight(enlargedSector, false)
                    lockGameboard = true
                }
            }
        }
    }

    private fun checkSectorWinner(sectorIndex: Int) {
        val localData = gridOwners.subList(sectorIndex * GridCount, sectorIndex * GridCount + GridCount)
        val localWinner = checkWin(localData)
        sectorOwners = sectorOwners.mapIndexed { index, original ->
            if (sectorIndex == index) localWinner else original
        }
    }

    // Scale up the sector clicked, and return the previously enlarged sector to default state
    // If the sector clicked is already enlarged, do nothing
    override fun onSectorClicked(sectorIndex: Int) {
        if (lockGameboard) return

        Timber.v("Sector on click: $sectorIndex")
        if (enlargedSector != sectorIndex) {
            toggleSectorHighlight(enlargedSector, false)
            toggleSectorHighlight(sectorIndex, true)
        }
    }

    private fun toggleSectorHighlight(sectorIndex: Int?, highlight: Boolean) {
        if (sectorIndex == null) return

        val view = sectorViews[sectorIndex]
        view.enlarged = highlight
        if (highlight) {
            view.setCardBackgroundColor(colorSecondary)
        } else {
            when (sectorOwners[sectorIndex]) {
                Player1Token -> view.setCardBackgroundColor(colorPlayer1Dark)
                Player2Token -> view.setCardBackgroundColor(colorPlayer2Dark)
                else -> view.setCardBackgroundColor(colorDivider)
            }
        }

        enlargedSector = if (highlight) sectorIndex else null
    }

    interface Delegate {
        fun makeMove(gameboard: Gameboard, move: Pair<Int, Int>)
                : Triple<Int, Int, Long>?

        fun isGameOver(gameboard: Gameboard): Boolean
    }
}