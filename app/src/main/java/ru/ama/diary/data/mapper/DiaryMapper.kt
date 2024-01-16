package ru.ama.diary.data.mapper

import ru.ama.diary.data.database.DiaryDBModel
import ru.ama.diary.data.database.DiaryDBModelWithHour
import ru.ama.diary.data.json.DiaryDto
import ru.ama.diary.domain.entity.DiaryDomModel
import ru.ama.diary.domain.entity.DiaryDomModelWithHour
import javax.inject.Inject

class DiaryMapper @Inject constructor() {


    fun mapDataDomToDbModel(domModel: DiaryDomModel) = DiaryDBModel(
        idFromJson = domModel.idFromJson,
        dateStart = domModel.dateStart,
        dateFinish = domModel.dateFinish,
        name = domModel.name,
        description = domModel.description
    )

    fun mapDataDtoToDbModel(dto: DiaryDto) = DiaryDBModel(
        idFromJson = dto.idFromJson,
        dateStart = dto.dateStart,
        dateFinish = dto.dateFinish,
        name = dto.name,
        description = dto.description
    )

    fun mapDataDbModelToEntity(dbModel: DiaryDBModel) = DiaryDomModel(
        idFromJson = dbModel.idFromJson,
        dateStart = dbModel.dateStart,
        dateFinish = dbModel.dateFinish,
        name = dbModel.name,
        description = dbModel.description
    )

    fun mapDataDbModelHourToEntity(dbModel: DiaryDBModelWithHour) = DiaryDomModelWithHour(
        id = dbModel.id,
        idFromJson = dbModel.idFromJson,
        dateStart = dbModel.dateStart,
        dateFinish = dbModel.dateFinish,
        name = dbModel.name,
        description = dbModel.description,
        timeStart = dbModel.timeStart,
        timeEnd = dbModel.timeEnd
    )
}
