package ru.ama.diary.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.ama.diary.data.database.AppDatabase
import ru.ama.diary.data.database.JobListDao
import ru.ama.diary.data.repository.DiaryRepositoryImpl
import ru.ama.diary.domain.repository.DiaryRepository

@Module
interface DataModule {
    @ApplicationScope
    @Binds
    fun bindDiaryRepository(impl: DiaryRepositoryImpl): DiaryRepository
    companion object {
        @Provides
        @ApplicationScope
        fun provideJobDao(
            application: Application
        ): JobListDao {
            return AppDatabase.getInstance(application).jobListDao()
        }

    }
}
