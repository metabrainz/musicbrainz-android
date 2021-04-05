package org.metabrainz.mobile

import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object RetrofitUtils {
    fun <S> createTestService(service: Class<S>?, baseUrl: HttpUrl?): S {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl!!)
            .build()
        return retrofit.create(service)
    }
}