package ru.ama.diary.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class CalendarDomModel(
    val id: String,
    val mDate: Long,
    val mCount: Int
) : Parcelable
