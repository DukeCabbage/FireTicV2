package com.cabbage.fireticv2.data.game

data class ModelRoom(val roomId: String = "",
                     val hostPlayerId: String = "",
                     val guestPlayerId: String? = null,
                     val gameId: String? = null,
                     val privateRoom: Boolean = false,
                     val inviteCode: String? = null,
                     val creationTime: Long = 0L,
                     val progress: String = Empty) {

    init {
        if (privateRoom && inviteCode == null) {
            throw IllegalStateException("Private room requires invite code")
        } else if (!privateRoom && inviteCode != null) {
            throw IllegalStateException("Non-private room shouldn't have invite code")
        }
    }

    companion object {
        const val Empty = "empty"
        const val Started = "started"
        const val Abandoned = "abandoned"
        const val Finished = "finished"
    }
}