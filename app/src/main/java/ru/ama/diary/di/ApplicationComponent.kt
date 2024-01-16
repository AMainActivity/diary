package ru.ama.diary.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.ama.diary.presentation.*

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(fragment: MainFragment)
    fun inject(fragment: AddJobFragment)
    fun inject(fragment: DetailJobFragment)
    fun inject(activity: SplashActivity)

    fun inject(application: MyApplication)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}
