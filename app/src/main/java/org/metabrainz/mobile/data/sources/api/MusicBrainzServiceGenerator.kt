package org.metabrainz.mobile.data.sources.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.metabrainz.mobile.App
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MusicBrainzServiceGenerator {

    private const val API_BASE_URL = "https://musicbrainz.org/ws/2/"
    const val AUTH_URL = "https://musicbrainz.org/oauth2/authorize"
    const val ACOUST_ID_BASE_URL = "https://api.acoustid.org/v2/lookup"
    const val CLIENT_ID = "BZn9PT8PXtzoUvR1ZIaXFw"
    const val CLIENT_SECRET = "WN6o5cjehjPAP4dib0zOmQ"
    const val OAUTH_REDIRECT_URI = "org.metabrainz.mobile://oauth"
    const val ACOUST_ID_KEY = "5mgEECwRkp"

    private const val cacheSize = (5 * 1024 * 1024).toLong()
    private val  myCache = Cache(App.getContext().cacheDir, cacheSize)

    private var authenticator: OAuthAuthenticator? = null
    private val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    private var headerInterceptor: HeaderInterceptor? = null

    private val httpClientBuilder = OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(App.getContext())!!)
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                else
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                chain.proceed(request)
            }

    private val builder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    private var retrofit = builder.build()

    private fun hasNetwork(context: Context): Boolean? {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    @JvmStatic
    fun <S> createService(service: Class<S>, requiresAuthenticator: Boolean): S {
        headerInterceptor = HeaderInterceptor()
        addInterceptors(headerInterceptor)

        // Authenticator should not be added for requests to refresh token and gaining access token
        if (requiresAuthenticator) addAuthenticator()
        addInterceptors(loggingInterceptor)
        return retrofit.create(service)
    }

    @JvmStatic
    fun <S> createTestService(service: Class<S>, testUrl: HttpUrl): S {
        val testRetrofit = builder.baseUrl(testUrl).build()
        return testRetrofit.create(service)
    }

    private fun addAuthenticator() {
        authenticator = OAuthAuthenticator()
        httpClientBuilder.authenticator(authenticator!!)
        builder.client(httpClientBuilder.build())
        retrofit = builder.build()
    }

    private fun addInterceptors(interceptor: Interceptor?) {
        if (!httpClientBuilder.interceptors().contains(interceptor)) {
            httpClientBuilder.addInterceptor(interceptor!!)
            builder.client(httpClientBuilder.build())
            retrofit = builder.build()
        }
    }

}