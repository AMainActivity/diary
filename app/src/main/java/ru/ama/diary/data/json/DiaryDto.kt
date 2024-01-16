package ru.ama.diary.data.json

import com.google.gson.annotations.SerializedName

data class DiaryDto(
    val idFromJson: Int,
    @SerializedName("date_start")
    val dateStart: Long,
    @SerializedName("date_finish")
    val dateFinish: Long,
    val name: String,
    val description: String
)
