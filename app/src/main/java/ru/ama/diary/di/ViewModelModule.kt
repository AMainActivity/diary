package ru.ama.diary.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ama.diary.presentation.AddJobViewModel
import ru.ama.diary.presentation.DetailJobViewModel
import ru.ama.diary.presentation.SplashViewModel
import ru.ama.diary.presentation.MainFragmentViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainFragmentViewModel::class)
    fun bindMainFragmentViewModel(viewModel: MainFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddJobViewModel::class)
    fun bindAddJobViewModel(viewModel: AddJobViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailJobViewModel::class)
    fun bindDetailJobViewModel(viewModel: DetailJobViewModel): ViewModel
}
