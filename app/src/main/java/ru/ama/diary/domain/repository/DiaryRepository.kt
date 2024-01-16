package ru.ama.diary.domain.repository

import androidx.lifecycle.LiveData
import ru.ama.diary.domain.entity.CalendarDomModel
import ru.ama.diary.domain.entity.DiaryDomModel
import ru.ama.diary.domain.entity.DiaryDomModelWithHour

interface DiaryRepository {
    suspend fun loadJobsFromAssets(): List<Int>
    suspend fun getCountOfJobs(): Int
    fun getCalendarMonth(mDate: String): List<CalendarDomModel>
    fun getJobsList(mDate: String): List<DiaryDomModelWithHour>
    suspend fun addJob(domModel: DiaryDomModel): Long
    fun getAllData(): LiveData<List<DiaryDomModel>>
}
