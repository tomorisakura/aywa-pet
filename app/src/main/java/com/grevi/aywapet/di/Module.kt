package com.grevi.aywapet.di

import android.content.Context
import androidx.work.WorkManager
import com.grevi.aywapet.datasource.services.ApiHelper
import com.grevi.aywapet.datasource.services.ApiHelperImpl
import com.grevi.aywapet.datasource.services.ApiService
import com.grevi.aywapet.db.DatabaseHelperImpl
import com.grevi.aywapet.repository.RemoteRepos
import com.grevi.aywapet.repository.Repository
import com.grevi.aywapet.repository.mapper.EntityMapperImpl
import com.grevi.aywapet.utils.Constant
import com.grevi.aywapet.utils.SharedUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideOkHttpClient() : OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiHelper(apiHelperImpl: ApiHelperImpl) : ApiHelper {
        return apiHelperImpl
    }

    @Provides
    @Singleton
    fun provideRepository(remoteRepos: RemoteRepos) : Repository {
        return remoteRepos
    }

    @Provides
    @Singleton
    fun provideSharedUtils(@ApplicationContext context: Context) : SharedUtils {
        return SharedUtils(context)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context) : WorkManager = WorkManager.getInstance(context)
}