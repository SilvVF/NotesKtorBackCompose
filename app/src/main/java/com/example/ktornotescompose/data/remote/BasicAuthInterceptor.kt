package com.example.ktornotescompose.data.remote

import com.example.ktornotescompose.util.Constants.IGNORE_AUTH_URLS
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor: Interceptor {
    //set at runtime when the user logs in
    val email: String? = null
    val password: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.encodedPath in IGNORE_AUTH_URLS) return chain.proceed(request)

        val authRequest = request.newBuilder()
            .header(
                "Authorization", Credentials.basic(
                    username = email ?: "",
                    password = password ?: ""
                )
            )
            .build()
        return chain.proceed(authRequest)
    }
}