package com.aphoh.muser.network

import retrofit.RetrofitError

/**
 * Created by Will on 1/14/16.
 */

public class NetworkException(message: String?, public val parsedMessage: String) : Exception(message) {

    companion object {
        public fun from(retrofitError: RetrofitError): NetworkException {
            val error = "Network Error ${retrofitError.response.status}, check your connection"
            return NetworkException(retrofitError.message, error)
        }

        public fun from(error: String?) = NetworkException(null, error ?: "No Message")

        public fun from(throwable: Throwable): NetworkException {
            if (throwable is NetworkException)
                return throwable
            if (throwable is RetrofitError)
                return from(throwable)
            else
                return from("Non-network error refreshing, this is a bug")
        }
    }
}
