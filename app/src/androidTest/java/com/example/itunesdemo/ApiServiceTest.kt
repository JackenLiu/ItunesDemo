package com.example.itunesdemo

import com.example.itunesdemo.net.ApiService
import com.example.itunesdemo.net.ItunesResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


@RunWith(JUnit4::class)
class ApiServiceTest {
    private var apiService: ApiService? = null
    private var server: MockWebServer? = null

    @Before
    @Throws(IOException::class)
    fun setUp() {
        server = MockWebServer()
        server?.start()
        apiService = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        server?.shutdown()
    }

    @Test
    @Throws(IOException::class)
    fun testApiRequest() {
        // 模拟网络响应
        server?.enqueue(MockResponse().setResponseCode(200).setBody("Response body"))

        // 执行网络请求
        val call: Call<ItunesResponse>? = apiService?.searchItunes("",0,100)
        val response: Response<ItunesResponse>? = call?.execute()

        // 检查响应
        assertTrue(response!!.isSuccessful)
        assertEquals("Response body", response.body())
    }
}

