package hsmida.exam.fdj.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor(
    private val apiKey: String,
    private val apiVersion: String
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.url.newBuilder()
        request.url.pathSegments.forEachIndexed { index, value ->
            when (value) {
                "{api_key}" -> builder.setPathSegment(index, apiKey)
                "{api_version}" -> builder.setPathSegment(index, apiVersion)
            }
        }

        val requestBuilder = request.newBuilder().url(builder.build())

        return chain.proceed(requestBuilder.build())
    }
}