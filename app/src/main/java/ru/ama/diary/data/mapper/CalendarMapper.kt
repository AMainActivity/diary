package ru.ama.diary.data.mapper

import ru.ama.diary.data.database.CalendarDBModel
import ru.ama.diary.domain.entity.CalendarDomModel
import javax.inject.Inject

class CalendarMapper @Inject constructor() {

    fun mapCalendarDbModelToEntity(dbModel: CalendarDBModel) = CalendarDomModel(
        id = dbModel.id,
        mDate = dbModel.mDate,
        mCount = dbModel.mCount
    )
}
