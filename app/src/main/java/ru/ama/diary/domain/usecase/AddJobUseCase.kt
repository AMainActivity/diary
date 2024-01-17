package ru.ama.diary.domain.usecase

import ru.ama.diary.domain.entity.DiaryDomModel
import ru.ama.diary.domain.repository.DiaryRepository
import javax.inject.Inject

class AddJobUseCase @Inject constructor(
    private val repository: DiaryRepository
) {
    suspend operator fun invoke(domModel: DiaryDomModel) = repository.addJob(domModel)
}
