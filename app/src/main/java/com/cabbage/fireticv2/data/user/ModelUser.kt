package com.cabbage.fireticv2.data.user

data class ModelUser(val userId: String = "",
                     val name: String = "Unknown",
                     val currentRoomId: String? = null,
                     val gameIds: List<String> = emptyList(),
                     val winCount: Int = 0)