package ru.ama.diary.presentation

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

class DetailJobViewModel @Inject constructor(
) : ViewModel() {

    fun getDate(milliSeconds: Long): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val cal = Calendar.getInstance()
        cal.timeInMillis = milliSeconds
        return formatter.format(cal.time)
    }

}
