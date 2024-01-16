package ru.ama.diary.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.ama.diary.data.database.JobListDao
import ru.ama.diary.data.json.DiaryDto
import ru.ama.diary.data.mapper.CalendarMapper
import ru.ama.diary.data.mapper.DiaryMapper
import ru.ama.diary.domain.entity.CalendarDomModel
import ru.ama.diary.domain.entity.DiaryDomModel
import ru.ama.diary.domain.entity.DiaryDomModelWithHour
import ru.ama.diary.domain.repository.DiaryRepository
import java.io.IOException
import javax.inject.Inject


class DiaryRepositoryImpl @Inject constructor(
    private val mapper: DiaryMapper,
    private val calMapper: CalendarMapper,
    private val jobListDao: JobListDao,
    private val application: Application
) : DiaryRepository {


    private fun getJsonDataFromAsset(fileName: String): String? {
        val jsonString: String
        try {
            jsonString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    override suspend fun addJob(domModel: DiaryDomModel): Long {
        return jobListDao.insertJob(mapper.mapDataDomToDbModel(domModel))
    }

    override suspend fun loadJobsFromAssets(): List<Int> {
        val listOfItems: MutableList<Int> = mutableListOf<Int>()
        try {

            val jsonFileString = getJsonDataFromAsset("default_data.json")

            Log.i("data", jsonFileString!!)

            val gson = Gson()
            val listDTOType = object : TypeToken<List<DiaryDto>>() {}.type

            val jobListDTO: List<DiaryDto> = gson.fromJson(jsonFileString, listDTOType)
            val dbModelJobList = jobListDTO.map {
                mapper.mapDataDtoToDbModel(it)
            }

            val jobItemsCount = jobListDao.insertJobsList(dbModelJobList)
            listOfItems.add(jobItemsCount.size)

        } catch (e: Exception) {
        }
        return listOfItems


    }

    override suspend fun getCountOfJobs() = jobListDao.getCountJobs()

    override fun getCalendarMonth(mDate: String): List<CalendarDomModel> =
        (jobListDao.getCalendarByMonth(mDate)).map { calMapper.mapCalendarDbModelToEntity(it) }


    override fun getJobsList(mDate: String): List<DiaryDomModelWithHour> =
        (jobListDao.getJobsList(mDate)).map { mapper.mapDataDbModelHourToEntity(it) }


    override fun getAllData(): LiveData<List<DiaryDomModel>> {
        return Transformations.map(jobListDao.getAllJobsList()) {
            it.map {
                mapper.mapDataDbModelToEntity(it)
            }
        }
    }

}
