package root.iv.ivandroidacademy.network.interceptor

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.internal.EMPTY_RESPONSE

class ConnectivityInterceptor(
    private val connectivityManager: ConnectivityManager
): Interceptor {

    constructor(ctx: Context): this(ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline()) return Response.Builder()
            .message("Not connect")
            .request(chain.request())
            .protocol(Protocol.HTTP_2)
            .code(0)
            .body(EMPTY_RESPONSE)
            .build()

        return chain.proceed(chain.request().newBuilder().build())
    }

    // ---
    // PRIVATE
    // ---

    private fun isOnline(): Boolean = connectivityManager
        .activeNetworkInfo?.isConnected ?: false
}