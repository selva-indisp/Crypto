package com.indisp.network

import com.indisp.core.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.errors.IOException
import java.nio.channels.UnresolvedAddressException

class NetworkApiService(
    val client: HttpClient
) {
    suspend inline fun <reified S> get(url: String): Result<S, NetworkFailure> {
        return try {
            Result.Success(client.get(url).body<S>())
        } catch (e: UnresolvedAddressException) {
            Result.Error(NetworkFailure.NoInternet)
        } catch (e: IOException) {
            Result.Error(NetworkFailure.NoInternet)
        } catch (e: Throwable) {
            Result.Error(NetworkFailure.UnknownFailure(e))
        }
    }
}