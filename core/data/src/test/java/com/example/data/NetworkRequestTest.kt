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
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response
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
        val body = ResponseBody.create(MediaType.get("application/json"), "")
        coEvery { api.getRandomAnime() } returns Response.error(600, body)

        NetworkRequest.safeApiCall(
            call = { api.getRandomAnime() },
            map = { it.toUiAnimeId() }
        ).onError { message ->
            assertEquals(R.string.unknown_message.toString(), message)
        }
    }

    @Test
    fun `saveApiCall returns error when call return exception`() = runTest {
        coEvery { api.getRandomAnime() } throws UnknownHostException()

        NetworkRequest.safeApiCall(
            call = { api.getRandomAnime() },
            map = { it.toUiAnimeId() }
        ).onError { message ->
            assertEquals(R.string.internet_message.toString(), message)
        }
    }
}