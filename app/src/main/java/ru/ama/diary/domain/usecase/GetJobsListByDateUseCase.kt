package ru.ama.diary.domain.usecase

import ru.ama.diary.domain.repository.DiaryRepository
import javax.inject.Inject

class GetJobsListByDateUseCase @Inject constructor(
    private val repository: DiaryRepository
) {
    operator fun invoke(mDate: String) = repository.getJobsList(mDate)
}
