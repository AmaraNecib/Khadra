package com.example.khadra.data.di

import android.content.Context
import com.example.khadra.SupabaseClientProvider
import com.example.khadra.data.remote.SupabaseTreeDataSource
import com.example.khadra.data.repository.AuthRepository
import com.example.khadra.data.repository.TreeRepository
import com.example.khadra.data.repository.TreeRepositoryImpl
import com.example.khadra.data.repository.TreeTypeRepository
import com.example.khadra.data.repository.TreeTypeRepositoryImpl
import com.example.khadra.data.source.TreeDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideSupabaseClientProvider(): SupabaseClientProvider {
        return SupabaseClientProvider()
    }

    @Provides
    @Singleton
    fun provideSupabaseClient(provider: SupabaseClientProvider): SupabaseClient {
        return provider.client
    }

    @Provides
    @Singleton
    fun provideTreeDataSource(
        @ApplicationContext context: Context,
        client: SupabaseClient
    ): TreeDataSource {
        return SupabaseTreeDataSource(client, context)
    }

    @Provides
    @Singleton
    fun provideTreeRepository(
        treeDataSource: TreeDataSource
    ): TreeRepository {
        return TreeRepositoryImpl(treeDataSource)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        client: SupabaseClient
    ): AuthRepository {
        return AuthRepository(client)
    }

    @Provides
    @Singleton
    fun provideTreeTypeRepository(): TreeTypeRepository {
        return TreeTypeRepositoryImpl()
    }
}