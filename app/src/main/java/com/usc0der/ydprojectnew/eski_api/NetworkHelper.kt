package com.usc0der.ydprojectnew.connection

import android.annotation.SuppressLint
import android.util.Log
import com.usc0der.ydprojectnew.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class NetworkHelper {
    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
        })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }
    private fun provideTrustedManager(): Array<TrustManager> {
        return arrayOf(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }
        })
    }
    private fun provideSslContext(): SSLContext? {
        val trustAllCerts = provideTrustedManager()
        var sslContext: SSLContext? = null

        try {
            sslContext = SSLContext.getInstance("SSL")
            sslContext!!.init(null, trustAllCerts, java.security.SecureRandom())

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        return sslContext
    }

    private fun provideSslSocketFactory(): SSLSocketFactory? {
        return provideSslContext()?.socketFactory

    }
    private fun provideOkHttpClient(): OkHttpClient {


        val httpLoggingInterceptor: HttpLoggingInterceptor = provideHttpLoggingInterceptor()

        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }
    private fun provideRetrofit(url: String): Retrofit {
        val okHttpClient: OkHttpClient = provideOkHttpClient()
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private fun getRetrofitRestApi(): RetrofitRestApi {
        val retrofit: Retrofit = provideRetrofit(Constants.BASIC_URL)

        return retrofit.create(RetrofitRestApi::class.java)
    }

    ///    ALL REQUESTS    ///

//    fun login(request: LoginForm): MutableLiveData<Response<RestResponse<LoginResponse>>> {
//        return getRetrofitRestApi()
//            .login(request)
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread())
//    }
//
//    fun register(request: UserForm): MutableLiveData<Response<Void>> {
//        return getRetrofitRestApi()
//            .register(request)
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread())
//    }
//
//    fun languages(token: String): MutableLiveData<Response<LanguagesListResponse>> {
//        return getRetrofitRestApi()
//            .languages(token)
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread())
//    }
//
//    fun audios(token: String, id: Int): MutableLiveData<Response<LinkedList<AudiosListItemResponse>>> {
//        return getRetrofitRestApi()
//            .audios(token, id)
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread())
//    }
//
//    fun videos(token: String, id: Int): MutableLiveData<Response<LinkedList<VideosListItemResponse>>> {
//        return getRetrofitRestApi()
//            .videos(token, id)
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread())
//    }
//
//
//    fun item(token: String, id: Int): MutableLiveData<Response<ItemResponse>> {
//        return getRetrofitRestApi()
//            .item(token, id)
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread())
//    }
//
//    fun change(token: String, request: ChangeUser): MutableLiveData<Response<Void>> {
//        return getRetrofitRestApi()
//            .change(token, request)
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread())
//    }
//
//    fun account(token: String): MutableLiveData<Response<AccountResponse>> {
//        return getRetrofitRestApi()
//            .getAccount(token)
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread())
//    }

}