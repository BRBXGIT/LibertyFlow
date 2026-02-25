package com.example.data

import com.example.data.utils.network.network_caller.NetworkCaller
import com.example.data.utils.network.network_caller.NetworkErrors
import com.example.data.utils.network.network_caller.NetworkResult
import com.example.network.auth.api.AuthApi
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response
import java.lang.NullPointerException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkCallerTest {

    private val networkCaller = NetworkCaller()

    @Test
    fun `safeApiCall returns Success and map result when response is successful`() = runTest {
        val rawData = "Raw Data"
        val mappedData = "Mapped Data"
        val response = Response.success(rawData)

        val result = networkCaller.safeApiCall(
            call = { response },
            map = { _ -> mappedData }
        )

        assertTrue(result is NetworkResult.Success)
        assertEquals(mappedData, (result as NetworkResult.Success).data)
    }

    @Test
    fun `safeApiCall returns Error and throw resId when response is error with code`() = runTest {
        val errorsMap = mapOf(
            401 to NetworkErrors.INCORRECT_CREDENTIALS,
            403 to NetworkErrors.UNAUTHORIZED,
            422 to NetworkErrors.NO_EMAIL_OR_PASSWORD,
            408 to NetworkErrors.REQUEST_TIMEOUT,
            409 to NetworkErrors.CONFLICT,
            413 to NetworkErrors.PAYLOAD_TOO_LARGE,
            429 to NetworkErrors.TOO_MANY_REQUESTS,
            500 to NetworkErrors.SERVER_ERROR,
            430 to NetworkErrors.UNKNOWN
        )

        errorsMap.forEach { (code, error) ->
            val errorResponseBody = "{}".toResponseBody("application/json".toMediaTypeOrNull())
            val response = Response.error<String>(code, errorResponseBody)

            val result = networkCaller.safeApiCall(
                call = { response },
                map = { it }
            )

            assertTrue(result is NetworkResult.Error)
            val errorResult = result as NetworkResult.Error

            assertEquals(error, errorResult.error)

            val resId = when(error) {
                NetworkErrors.CONFLICT -> R.string.conflict_message
                NetworkErrors.TOO_MANY_REQUESTS -> R.string.too_many_requests_message
                NetworkErrors.PAYLOAD_TOO_LARGE -> R.string.payload_too_large_message
                NetworkErrors.SERVER_ERROR -> R.string.server_error_message
                NetworkErrors.INCORRECT_CREDENTIALS -> R.string.incorrect_email_or_password_message
                NetworkErrors.UNAUTHORIZED -> R.string.unauthoried_message
                NetworkErrors.NO_EMAIL_OR_PASSWORD -> R.string.no_email_or_password_message
                NetworkErrors.REQUEST_TIMEOUT -> R.string.request_timeout_message
                NetworkErrors.INTERNET -> R.string.internet_message
                NetworkErrors.SERIALIZATION -> R.string.serialization_message
                NetworkErrors.UNKNOWN -> R.string.unknown_message
            }

            assertEquals(resId, errorResult.messageRes)
        }
    }

    @Test
    fun `saveApiCall returns Error and message res when response with Exception`() = runTest {
        val api = mockk<AuthApi>()

        val errorsMap = mapOf(
            UnknownHostException() to NetworkErrors.INTERNET,
            SocketException() to NetworkErrors.INTERNET,
            SocketTimeoutException() to NetworkErrors.REQUEST_TIMEOUT,
            NullPointerException() to NetworkErrors.UNKNOWN
        )

        errorsMap.forEach { (exception, error) ->
            coEvery { api.logout(any()) } throws exception

            val result = networkCaller.safeApiCall(
                call = { api.logout("") },
                map = { it }
            )

            val resId = when(error) {
                NetworkErrors.INTERNET -> R.string.internet_message
                NetworkErrors.REQUEST_TIMEOUT -> R.string.request_timeout_message
                NetworkErrors.UNKNOWN -> R.string.unknown_message
                else -> R.string.server_error_message
            }

            assertTrue(result is NetworkResult.Error)
            val errorResult = result as NetworkResult.Error
            assertEquals(resId, errorResult.messageRes)
            assertEquals(error, result.error)
        }
    }
}