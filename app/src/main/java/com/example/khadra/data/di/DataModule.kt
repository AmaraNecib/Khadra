package com.example.khadra.data.di

import com.example.khadra.data.repository.TreeRepository
import com.example.khadra.data.repository.TreeRepositoryImpl
import com.example.khadra.data.repository.TreeTypeRepository
import com.example.khadra.data.repository.TreeTypeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {


    @Provides
    @Singleton
    fun provideTreeRepository(): TreeRepository {
        return TreeRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideTreeTypeRepository(): TreeTypeRepository {
        return TreeTypeRepositoryImpl()
    }


}