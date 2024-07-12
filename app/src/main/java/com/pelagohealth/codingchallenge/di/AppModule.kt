package com.pelagohealth.codingchallenge.di

import com.pelagohealth.codingchallenge.BuildConfig
import com.pelagohealth.codingchallenge.BuildConfig.BASE_URL
import com.pelagohealth.codingchallenge.data.datasource.rest.FactsRestApi
import com.pelagohealth.codingchallenge.data.repository.FactRepositoryImpl
import com.pelagohealth.codingchallenge.domain.repository.FactRepository
import com.pelagohealth.codingchallenge.domain.use_cases.FactUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Request
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(
        headerInterceptor: Interceptor
    ) =
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(240, TimeUnit.SECONDS)
                .addInterceptor(headerInterceptor)
                .readTimeout(240, TimeUnit.SECONDS)
                .build()
        } else {
            OkHttpClient
                .Builder()
                .addInterceptor(headerInterceptor)
                .build()
        }

    @Singleton
    @Provides
    fun provideRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(
                    moshi
                )
            )
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()


    //Provide Moshi to convert JSON to Kotlin Objects
    @Provides
    @Singleton
    fun provideMoshiConverterFactory(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


    //Provide Header Interceptor
    @Provides
    @Singleton
    fun provideApplicationJsonInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val request: Request =
                chain.request().newBuilder().addHeader("Content-Type", "application/json").build()
            return@Interceptor chain.proceed(request)
        }

    }


    //Provide API Service
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): FactsRestApi =
        retrofit.create(FactsRestApi::class.java)

    @Provides
    @Singleton
    fun provideFactRepository(factsRestApi: FactsRestApi): FactRepository =
        FactRepositoryImpl(factsRestApi)

    @Provides
    @Singleton
    fun provideFactUseCase(factRepository: FactRepository): FactUseCase =
        FactUseCase(factRepository)

}