package ru.ama.diary.presentation

import android.app.Application
import ru.ama.diary.di.DaggerApplicationComponent

class MyApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}
