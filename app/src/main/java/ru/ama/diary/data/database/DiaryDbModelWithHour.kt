package ru.ama.diary.data.database

import androidx.room.ColumnInfo

data class DiaryDBModelWithHour(
    val id: Int,
    val idFromJson: Int,
    @ColumnInfo(name = "date_start")
    val dateStart: Long,
    @ColumnInfo(name = "date_finish")
    val dateFinish: Long,
    val name: String,
    val description: String,
    @ColumnInfo(name = "time_start")
    val timeStart: String,
    @ColumnInfo(name = "time_end")
    val timeEnd: String
)
