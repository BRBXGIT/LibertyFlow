package com.example.data

import com.example.data.models.releases.mappers.toUiAnimeId
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.onError
import com.example.data.utils.remote.network_request.onSuccess
import com.example.network.releases.api.ReleasesApi
import com.example.network.releases.models.anime_id_item_response.AnimeIdItem
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkRequestTest {

    private val api: ReleasesApi = mockk(relaxed = true)

    @Test
    fun `saveApiCall returns success when call successful`() = runTest {
        coEvery { api.getRandomAnime() } returns Response.success(AnimeIdItem(1))

        NetworkRequest.safeApiCall(
            call = { api.getRandomAnime() },
            map = { it.toUiAnimeId() }
        ).onSuccess { result ->
            assertEquals(1, result.id)
        }
    }

    @Test
    fun `saveApiCall returns error when call returns http error`() = runTest {
        val body = ResponseBody.create("application/json".toMediaType(), "")
        coEvery { api.getRandomAnime() } returns Response.error(600, body)

        NetworkRequest.safeApiCall(
            call = { api.getRandomAnime() },
            map = { it.toUiAnimeId() }
        ).onError { _, message ->
            assertEquals(R.string.unknown_message.toString(), message)
        }
    }

    @Test
    fun `saveApiCall returns error when call return exception`() = runTest {
        coEvery { api.getRandomAnime() } throws UnknownHostException()

        NetworkRequest.safeApiCall(
            call = { api.getRandomAnime() },
            map = { it.toUiAnimeId() }
        ).onError { _, message ->
            assertEquals(R.string.internet_message.toString(), message)
        }
    }

    // === Next tests check how NetworkRequest helpers work ===
    private suspend fun assertErrorMessage(code: Int, expected: String) {
        val body = "".toResponseBody("application/json".toMediaType())
        coEvery { api.getRandomAnime() } returns Response.error(code, body)

        NetworkRequest.safeApiCall(
            call = { api.getRandomAnime() },
            map = { it.toUiAnimeId() }
        ).onError { _, message ->
            assertEquals(expected, message)
        }
    }

    @Test
    fun `code 401 returns correct message`() = runTest {
        assertErrorMessage(401, R.string.incorrect_email_or_password_message.toString())
    }

    @Test
    fun `code 422 returns correct message`() = runTest {
        assertErrorMessage(422, R.string.no_email_or_password_message.toString())
    }

    @Test
    fun `code 408 returns correct message`() = runTest {
        assertErrorMessage(408, R.string.request_timeout_message.toString())
    }

    @Test
    fun `code 409 returns correct message`() = runTest {
        assertErrorMessage(409, R.string.conflict_message.toString())
    }

    @Test
    fun `code 413 returns correct message`() = runTest {
        assertErrorMessage(413, R.string.payload_too_large_message.toString())
    }

    @Test
    fun `code 429 returns correct message`() = runTest {
        assertErrorMessage(429, R.string.too_many_requests_message.toString())
    }

    @Test
    fun `code 500 returns correct message`() = runTest {
        assertErrorMessage(500, R.string.server_error_message.toString())
    }

    private suspend fun assertErrorException(e: Exception, expected: String) {
        coEvery { api.getRandomAnime() } throws e

        NetworkRequest.safeApiCall(
            call = { api.getRandomAnime() },
            map = { it.toUiAnimeId() }
        ).onError { _, message ->
            assertEquals(expected, message)
        }
    }

    @Test
    fun `UnknownHostException returns correct message`() = runTest {
        assertErrorException(UnknownHostException(), R.string.internet_message.toString())
    }

    @Test
    fun `SocketException returns correct message`() = runTest {
        assertErrorException(SocketException(), R.string.internet_message.toString())
    }

    @Test
    fun `SocketTimeoutException returns correct message`() = runTest {
        assertErrorException(SocketTimeoutException(), R.string.request_timeout_message.toString())
    }

    @Test
    fun `ArrayIndexOutOfBoundsException returns correct message`() = runTest {
        assertErrorException(ArrayIndexOutOfBoundsException(), R.string.unknown_message.toString())
    }
}