package ru.ama.diary.domain.usecase

import ru.ama.diary.domain.repository.DiaryRepository
import javax.inject.Inject

class LoadJobsFromAssetsUseCase @Inject constructor(
    private val repository: DiaryRepository
) {

    suspend operator fun invoke() = repository.loadJobsFromAssets()
}
