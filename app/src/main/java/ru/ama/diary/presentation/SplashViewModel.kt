package ru.ama.diary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.ama.diary.domain.usecase.GetCountOfJobsUseCase
import ru.ama.diary.domain.usecase.GetJobsListUseCase
import ru.ama.diary.domain.usecase.LoadJobsFromAssetsUseCase
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val getCountOfJobsUseCase: GetCountOfJobsUseCase,
    private val loadJobsFromAssetsUseCase: LoadJobsFromAssetsUseCase
) : ViewModel() {

    init {
        checkDb()
    }

    private fun checkDb() {
        val d1 = viewModelScope.async(Dispatchers.IO) {
            getCountOfJobsUseCase()
        }

        val d2 = viewModelScope.async(Dispatchers.IO) {
            val f = d1.await()
            if (f == 0)
                loadJobsFromAssetsUseCase()
        }
        viewModelScope.launch {
            val f = d2.await()
            _canStart.value = Unit
        }
    }

    private val _canStart = MutableLiveData<Unit>()
    val canStart: LiveData<Unit>
        get() = _canStart

    companion object {}
}
