package uz.pixyz.rentcar.retrofit

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "http://rentcars.pythonanywhere.com/"
    private const val BASE_URL_REGISTER = "http://rentcars.pythonanywhere.com/api/"

    fun getRetrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRegister(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_REGISTER)
            .client(client1())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun client(context: Context): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addNetworkInterceptor(NetworkConnectionInterceptor(context))
        okHttpClient.addInterceptor(httpLoggingInterceptor)
        return okHttpClient.build()

//        okHttpClient.connectTimeout(10, TimeUnit.MILLISECONDS)
//        okHttpClient.readTimeout(30, TimeUnit.MILLISECONDS)
//        okHttpClient.interceptors().add(object : Interceptor {
//            override fun intercept(chain: Interceptor.Chain): Response {
//                return chain.proceed(chain.request())
//            }
//        })

    }

    private fun client1(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addNetworkInterceptor(httpLoggingInterceptor)
        okHttpClient.addInterceptor(httpLoggingInterceptor)
        return okHttpClient.build()
    }

}

