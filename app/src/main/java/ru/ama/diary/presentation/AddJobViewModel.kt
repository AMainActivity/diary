package ru.ama.diary.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.ama.diary.R
import ru.ama.diary.domain.entity.DiaryDomModel
import ru.ama.diary.domain.usecase.AddJobUseCase
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random

class AddJobViewModel @Inject constructor(
    private val addJobUseCase: AddJobUseCase,
    private val application: Application

) : ViewModel() {


    private val _isSuccessSave = MutableLiveData<String>()
    val isSuccessSave: LiveData<String>
        get() = _isSuccessSave

    private val _errorDate = MutableLiveData<Boolean>()
    val errorDate: LiveData<Boolean>
        get() = _errorDate


    private val _errorName = MutableLiveData<Boolean>()
    val errorName: LiveData<Boolean>
        get() = _errorName

    private val _errorDescription = MutableLiveData<Boolean>()
    val errorDescription: LiveData<Boolean>
        get() = _errorDescription


    fun tryJobSave(
        mDate: String,
        mTime: String,
        name: String,
        description: String
    ) {
        if (mDate.isNotEmpty() && mTime.isNotEmpty() && name.isNotEmpty() && description.isNotEmpty()) {
            addJob(
                application.getString(
                    R.string.frgmnt_addjob_date, mDate, mTime
                ), name, description
            )
        } else {
            validateInputData(mDate, JobAttributeNames.JOB_DATE)
            validateInputData(name, JobAttributeNames.JOB_NAME)
            validateInputData(description, JobAttributeNames.JOB_DESCRIPTION)
        }

    }


    private fun convertStringToDateInMilis(mdate: String) =
        SimpleDateFormat("dd.MM.yyyy HH:mm").parse(mdate).time

    fun validateInputData(name: String, idData: JobAttributeNames) {
        when (idData) {
            JobAttributeNames.JOB_DATE -> {
                _errorDate.value = name.isEmpty()
            }

            JobAttributeNames.JOB_NAME -> {
                _errorName.value = name.isEmpty()
            }

            JobAttributeNames.JOB_DESCRIPTION -> {
                _errorDescription.value = name.isEmpty()
            }
        }
    }

    fun resetError(idData: JobAttributeNames) {
        when (idData) {
            JobAttributeNames.JOB_DATE -> _errorDate.value = false
            JobAttributeNames.JOB_NAME -> _errorName.value = false
            JobAttributeNames.JOB_DESCRIPTION -> _errorDescription.value = false
        }
    }

    fun parseTime(time: String): String {
        var temp = time.split(DELIMITER)[INT_ZERO]
        if (temp.length == 1) temp = STRING_ZERO + temp
        return temp
    }

    fun getDate(milliSeconds: Long): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val cal = Calendar.getInstance()
        cal.timeInMillis = milliSeconds
        return formatter.format(cal.time)
    }

    private fun addJob(
        mDate: String,
        name: String,
        description: String
    ) {
        val d1 = viewModelScope.async(Dispatchers.IO) {
            addJobUseCase(
                DiaryDomModel(
                    Random.nextInt(INT_ZERO, INT_THOUSANDs),
                    convertStringToDateInMilis(mDate),
                    convertStringToDateInMilis(mDate + MILIS_IN_HOUR),
                    name,
                    description
                )
            )
        }
        viewModelScope.launch {
            val f = d1.await()
            _isSuccessSave.value =
                if (f < INT_ZERO) application.getString(R.string.frgmnt_addjob_error_save) else application.getString(
                    R.string.frgmnt_addjob_success_save
                )
        }
    }

    companion object {
        private const val MILIS_IN_HOUR = 3600000
        private const val INT_ZERO = 0
        private const val DELIMITER = ":"
        private const val STRING_ZERO = "0"
        private const val INT_THOUSANDs = 10000
    }

}
