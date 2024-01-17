package ru.ama.diary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ama.diary.domain.entity.CalendarDomModel
import ru.ama.diary.domain.entity.DiaryDomModel
import ru.ama.diary.domain.entity.DiaryDomModelWithHour
import ru.ama.diary.domain.usecase.GetCalendarMonth
import ru.ama.diary.domain.usecase.GetJobsListByDateUseCase
import ru.ama.diary.domain.usecase.GetJobsListUseCase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class MainFragmentViewModel @Inject constructor(
    private val getCalendarMonth: GetCalendarMonth,
    private val getJobsListByDateUseCase: GetJobsListByDateUseCase,
    private val getJobsListUseCase: GetJobsListUseCase
) : ViewModel() {

    val calendar = Calendar.getInstance()
    private var currentMonth = INT_ZERO
    var mDateL = LONG_ZERO

    var jobsListLD: LiveData<List<DiaryDomModel>>? = null

    private val _scrollPosition = MutableLiveData<Int>()
    val scrollPosition: LiveData<Int>
        get() = _scrollPosition

    private val _calendarDomModelLV = MutableLiveData<List<CalendarDomModel>>()
    val calendarDomModelLV: LiveData<List<CalendarDomModel>>
        get() = _calendarDomModelLV
    private val _jobListDomModelLV = MutableLiveData<List<DiaryDomModelWithHour>>()
    val jobListDomModelLV: LiveData<List<DiaryDomModelWithHour>>
        get() = _jobListDomModelLV

    init {
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]
        getDates()
        jobsListLD = getJobsListUseCase()
    }

    private fun initCal(mDAte: String) {
        viewModelScope.launch(Dispatchers.IO) {
            //Log.e("initCal", mDAte)
            val list = getCalendarMonth(mDAte)
            _calendarDomModelLV.postValue(list)
            _scrollPosition.postValue(INT_ZERO)
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            for ((index, element) in list.withIndex()) {
                if (formatter.format(element.mDate)
                        .compareTo(formatter.format(Calendar.getInstance().time)) == INT_ZERO
                ) {
                    _scrollPosition.postValue(index)
                    break
                }
            }
        }
    }

    fun getJobList(mDate: Long) {
        mDateL = mDate
        viewModelScope.launch(Dispatchers.IO) {
            //Log.e("mDate", getDate(mDate))
            _jobListDomModelLV.postValue(getJobsListByDateUseCase(getDate(mDate)))
        }
    }

    fun getDatesOfNextMonth() {
        currentMonth++
        if (currentMonth == INT_TWELVE) {
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + Calendar.YEAR)
            currentMonth = INT_ZERO
            //Log.e("currentMonth+", currentMonth.toString())
        }
        getDates()
    }

    fun getDatesOfPreviousMonth() {
        currentMonth--
        if (currentMonth == INT_MINUS_ONE) {
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - Calendar.YEAR)
            currentMonth = INT_ELEVEN
            //Log.e("currentMonth-", currentMonth.toString())
        }
        getDates()
    }


    private fun getDateFromCalendar(cal: Calendar): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(cal.time)
    }

    private fun getDate(milliSeconds: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance()
        cal.timeInMillis = milliSeconds
        return formatter.format(cal.time)
    }

    fun getDates() {
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, INT_ONE)
        initCal(getDateFromCalendar(calendar))
        //Log.e("currentMonth", currentMonth.toString())
    }

    fun getMonthName(date: Long): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        return SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
    }

    fun getMonthFullName(date: Long): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        return SimpleDateFormat("LLLL", Locale.getDefault()).format(calendar.time)
    }

    fun getDayNumber(date: Long): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        return SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
    }

    fun getYear(date: Long): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar.time)
    }


    companion object {
        const val LONG_ZERO = 0L
        const val INT_ZERO = 0
        const val INT_ELEVEN = 11
        const val INT_TWELVE = 12
        const val INT_ONE = 1
        const val INT_MINUS_ONE = -1

    }
}
