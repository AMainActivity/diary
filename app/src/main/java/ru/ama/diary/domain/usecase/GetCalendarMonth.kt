package ru.ama.diary.domain.usecase

import ru.ama.diary.domain.repository.DiaryRepository
import javax.inject.Inject

class GetCalendarMonth @Inject constructor(
    private val repository: DiaryRepository
) {

    operator fun invoke(mDate: String) = repository.getCalendarMonth(mDate)
}
