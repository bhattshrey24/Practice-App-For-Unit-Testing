package com.example.practiceappforunittesting.repositories

import com.example.practiceappforunittesting.other.Resource
import com.example.practiceappforunittesting.remote.responses.ImageResponse
import retrofit2.Response

interface ShoppingRepository {
    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}