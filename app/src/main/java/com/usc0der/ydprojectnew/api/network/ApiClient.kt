package com.usc0der.ydprojectnew.api.network

import android.content.Context
import com.chuckerteam.chucker.BuildConfig
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import kotlinx.coroutines.newFixedThreadPoolContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "http://yangidavr.com/api/"

    private val client = buildClient()

//    val apiService: ApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ApiService::class.java)
//    }

    private val retrofit = buildRetrofit(OkHttpClient())
    private fun buildRetrofit(client: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(client)
            .build()
    }
    private fun buildClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
//        val chucker= ChuckerInterceptor.Builder(App.instance).build()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
            .callTimeout(40, TimeUnit.MINUTES)
            .addNetworkInterceptor(Interceptor { chain ->
                var request = chain.request()
                val builder = request.newBuilder()
                builder.addHeader("Content-Type", "application/json")
                request = builder.build()
                chain.proceed(request)
            })
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
//            builder.addInterceptor(chucker)
        }
        return builder.build()
    }


    @JvmStatic
    fun <T> createService(service: Class<T>?,context: Context): T {
        val client = OkHttpClient.Builder()
//            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .build()
        val newClient = client.newBuilder().addInterceptor(Interceptor { chain ->
            var request = chain.request()
            val builder = request.newBuilder()
            request = builder.build()
            chain.proceed(request)
        }).build()
        val newRetrofit = retrofit.newBuilder().client(newClient).build()
        return newRetrofit.create(service)
    }

    fun <T> createServiceWithToken(service: Class<T>?,context: Context): T {
        val client = OkHttpClient.Builder()
//            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .build()
        val pref = TokenManager.getInstance(context.getSharedPreferences("prefs", Context.MODE_PRIVATE))!!
        val newClient =
            client.newBuilder().addInterceptor(Interceptor { chain ->
                var request = chain.request()
                val builder = request.newBuilder()
                builder.addHeader("Authorization", "Bearer " + pref.token)
                request = builder.build()
                chain.proceed(request)
            }).build()

        val newRetrofit = retrofit.newBuilder().client(newClient).build()
        return newRetrofit.create(service)
    }
}

