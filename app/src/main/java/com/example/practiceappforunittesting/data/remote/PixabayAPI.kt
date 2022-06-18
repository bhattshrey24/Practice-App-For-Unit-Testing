package com.example.practiceappforunittesting.data.remote

import com.example.practiceappforunittesting.data.remote.responses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI { // this just an api that returns an image based on the query
    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = "BuildConfig.API_KEY" // actually you will pass here your api in our case our api is in build config but Im getting some error so im just commenting it
    ): Response<ImageResponse>
}