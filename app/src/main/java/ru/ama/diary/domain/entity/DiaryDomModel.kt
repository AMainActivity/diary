package ru.ama.diary.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiaryDomModel(
    val idFromJson: Int,
    val dateStart: Long,
    val dateFinish: Long,
    val name: String,
    val description: String
) : Parcelable
