package ru.ama.diary.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ama.diary.domain.entity.DiaryDomModelWithHour

@Dao
interface JobListDao {
    @Query(
        "SELECT * FROM todo_table ORDER BY id asc "
    )
    fun getAllJobsList(): LiveData<List<DiaryDBModel>>

    @Query("SELECT count(id) as count FROM todo_table ORDER BY id asc")
    fun getCountJobs(): Int

    @Query(
        "WITH RECURSIVE hours AS (select 0 as hh UNION ALL SELECT hh + 1 " +
                "FROM hours WHERE hh < 23) " +
                "select hours.hh||':00' as time_start," +
                "CASE WHEN (hours.hh+1) =24 THEN 0||':00' ELSE (hours.hh+1)||':00' END  as time_end ," +
                "CASE WHEN yt.name IS NULL THEN '' ELSE yt.name END AS name ," +
                "CASE WHEN yt.id IS NULL THEN -1 ELSE yt.id END AS id ," +
                "CASE WHEN yt.idFromJson IS NULL THEN -1 ELSE yt.idFromJson END AS idFromJson ," +
                "CASE WHEN yt.date_start IS NULL THEN 0 ELSE yt.date_start END AS date_start ," +
                "CASE WHEN yt.date_finish IS NULL THEN 0 ELSE yt.date_finish END AS date_finish ," +
                "CASE WHEN yt.description IS NULL THEN '' ELSE yt.description END AS description " +
                "from hours " +
                "LEFT JOIN todo_table AS yt " +
                "ON strftime('%Y-%m-%d', date(:mDate),  'localtime')||' '" +
                "||hours.hh = strftime('%Y-%m-%d', yt.date_start / 1000, 'unixepoch', 'localtime')||" +
                "' '||CASE WHEN (strftime('%H', date_start / 1000, 'unixepoch', 'localtime'))='00'" +
                " THEN '0' ELSE ltrim(strftime('%H', date_start / 1000, 'unixepoch', 'localtime'), '0') END  " +
                "GROUP BY hours.hh  " +
                "ORDER BY hours.hh ASC"
    )
    fun getJobsList(mDate: String): List<DiaryDBModelWithHour>


    @Query("SELECT * FROM todo_table  ORDER BY id asc ")
    fun getJobsList(): List<DiaryDBModel>


    @Query(
        "WITH RECURSIVE dates(d) AS (VALUES(DATE(:mDate, 'start of month')) " +
                "UNION ALL SELECT date(d, '+1 day') FROM dates WHERE d < DATE(:mDate, " +
                "'start of month', '+1 month', '-1 day')) " +
                "SELECT CASE WHEN yt.date_start IS NULL THEN -1 " +
                "ELSE yt.idFromJson END AS id," +
                " (CAST(strftime('%s',d)|| substr(strftime('%f',d),4) as int)) as mDate, " +
                " count(strftime('%Y-%m-%d', yt.date_start / 1000, 'unixepoch', 'localtime') ) as mCount " +
                "FROM dates LEFT JOIN todo_table AS yt ON dates.d = " +
                "strftime('%Y-%m-%d', yt.date_start / 1000, 'unixepoch', 'localtime') " +
                "GROUP BY dates.d ORDER BY dates.d ASC"
    )
    fun getCalendarByMonth(mDate: String): List<CalendarDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: DiaryDBModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobsList(jobsList: List<DiaryDBModel>): List<Long>
}

