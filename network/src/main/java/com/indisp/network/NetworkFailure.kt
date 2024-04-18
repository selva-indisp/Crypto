package com.indisp.network

sealed class NetworkFailure {
    object NoInternet: NetworkFailure()
    object ConnectionTimeOut: NetworkFailure()
    data class UnknownFailure(val error: Throwable): NetworkFailure()
}