package com.gitlab.kordlib.rest.ratelimit

import com.gitlab.kordlib.rest.request.Request
import java.time.Instant

/**
 * A rate limiter that follows [Discord's rate limits](https://discordapp.com/developers/docs/topics/rate-limits) for
 * the REST api.
 */
interface RequestRateLimiter {

    /**
     * Awaits all active rate limits for the [request], returning a [RequestToken] used to process the result of the
     * [request].
     */
    suspend fun await(request: Request<*, *>): RequestToken

}

/**
 * [Awaits][RequestRateLimiter.await] the rate limits for the [request] and then runs [consumer].
 * Throws an [IllegalStateException] if the supplied [RequestToken] was not completed.
 */
suspend inline fun <T> RequestRateLimiter.consume(
        request: Request<*, *>,
        consumer: (token: RequestToken) -> T
): T {
    val token = await(request)
    val result = consumer(token)
    check(token.completed) { "token was not completed" }
    return result
}

data class RateLimit(val total: Total, val remaining: Remaining) {
    val isExhausted: Boolean get() = remaining.value == 0L

    companion object
}

inline class Total(val value: Long) {
    companion object
}


inline class Remaining(val value: Long) {
    companion object
}

/**
 * The unique identifier of this bucket.
 */
inline class BucketKey(val value: String) {
    companion object
}

/**
 * The [instant][value] when the current bucket gets reset.
 */
inline class Reset(val value: Instant) {
    companion object
}

sealed class RequestResponse {
    abstract val bucketKey: BucketKey?
    abstract val rateLimit: RateLimit?
    abstract val reset: Reset

    /**
     * The request returned a non-rate limit error code.
     */
    data class Error(override val bucketKey: BucketKey?, override val rateLimit: RateLimit?, override val reset: Reset) : RequestResponse()

    /**
     * The request returned a response without errors.
     */
    data class Accepted(override val bucketKey: BucketKey?, override val rateLimit: RateLimit?, override val reset: Reset) : RequestResponse()

    /**
     * The request returned a global rate limit error.
     */
    data class GlobalRateLimit(override val bucketKey: BucketKey?, override val rateLimit: RateLimit?, override val reset: Reset) : RequestResponse()

    /**
     * The request returned a bucket rate limit error.
     */
    data class BucketRateLimit(override val bucketKey: BucketKey, override val rateLimit: RateLimit?, override val reset: Reset) : RequestResponse()

    companion object

}

/**
 * A completable token linked to a [Request].
 */
interface RequestToken {

    /**
     * Whether [complete] has been called.
     */
    val completed: Boolean

    /**
     * Completes the [Request], updating the rate limit data from the [response].
     */
    suspend fun complete(response: RequestResponse)
}