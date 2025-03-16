package com.example.khadra.domain.di

import com.example.khadra.data.repository.TreeRepository
import com.example.khadra.data.repository.TreeTypeRepository
import com.example.khadra.domain.usecase.AddTreeUseCase
import com.example.khadra.domain.usecase.GetTreeTypesUseCase
import com.example.khadra.domain.usecase.GetTreesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DomainModule {


    @Provides
    @Singleton
    fun provideGetTreesUseCase(treeRepository: TreeRepository): GetTreesUseCase {
        return GetTreesUseCase(treeRepository)
    }

    @Provides
    @Singleton
    fun provideGetTreeTypesUseCase(treeTypeRepository: TreeTypeRepository): GetTreeTypesUseCase {
        return GetTreeTypesUseCase(treeTypeRepository)
    }

    @Provides
    @Singleton
    fun provideAddTreeUseCase(treeRepository: TreeRepository): AddTreeUseCase {
        return AddTreeUseCase(treeRepository)
    }
}