package com.usc0der.ydprojectnew.api.di

import android.content.Context
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.usc0der.ydprojectnew.utils.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@[Module(includes = [ApiModule::class]) InstallIn(SingletonComponent::class)]
object NetworkModule {
    private const val BASE_URL = "http://yangidavr.com/api/"

    @[Singleton Provides]
    fun provideHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .addInterceptor(Interceptor { chain ->
                var request = chain.request()
                val url = request.url.toString()

                val builder = request.newBuilder()

                builder.addHeader("Content-Type", "application/json")
                request = builder.build()
                chain.proceed(request)
            })
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @[Singleton Provides]
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @[Singleton Provides]
    fun providerRetorfit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }
}