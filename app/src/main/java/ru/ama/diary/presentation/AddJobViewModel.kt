package ru.ama.diary.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.ama.diary.domain.entity.DiaryDomModel
import ru.ama.diary.domain.usecase.AddJobUseCase
import java.text.SimpleDateFormat
import javax.inject.Inject
import kotlin.random.Random

class AddJobViewModel @Inject constructor(
    private val addJobUseCase: AddJobUseCase
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
            addJob("$mDate $mTime:00", name, description)


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
                if (name.isNotEmpty()) {
                    _errorDate.value = false
                } else
                    _errorDate.value = true
            }

            JobAttributeNames.JOB_NAME -> {
                _errorName.value = name.isEmpty()
            }

            JobAttributeNames.JOB_DESCRIPTION -> {
                if (name.isNotEmpty()) {
                    _errorDescription.value = false
                } else
                    _errorDescription.value = true
            }
        }
    }

    /* fun resetAllError() {
         _errorDate.value = false
         _errorName.value = false
         _errorDescription.value = false
     }*/

    fun resetError(idData: JobAttributeNames) {
        when (idData) {
            JobAttributeNames.JOB_DATE -> _errorDate.value = false
            JobAttributeNames.JOB_NAME -> _errorName.value = false
            JobAttributeNames.JOB_DESCRIPTION -> _errorDescription.value = false
        }

    }

    private fun addJob(
        mDate: String,
        name: String,
        description: String
    ) {
        Log.e("addJob", mDate + " ${convertStringToDateInMilis(mDate)}")

        val d1 = viewModelScope.async(Dispatchers.IO) {
            addJobUseCase(
                DiaryDomModel(
                    Random.nextInt(0, 10000),
                    convertStringToDateInMilis(mDate),
                    convertStringToDateInMilis(mDate + MILIS_IN_HOUR),
                    name,
                    description
                )
            )
            Log.e(
                "addJob1", DiaryDomModel(
                    Random.nextInt(),
                    convertStringToDateInMilis(mDate),
                    convertStringToDateInMilis(mDate + MILIS_IN_HOUR),
                    name,
                    description
                ).toString()
            )

        }
        viewModelScope.launch {
            val f = d1.await()
            _isSuccessSave.value = if (f < 0) "ошибка записи" else "успешно сохранено"
        }
    }

    companion object {
        private const val MILIS_IN_HOUR = 3600000
    }

}
