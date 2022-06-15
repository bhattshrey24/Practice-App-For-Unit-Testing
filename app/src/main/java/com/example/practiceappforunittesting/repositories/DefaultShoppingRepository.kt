package com.example.practiceappforunittesting.repositories

import com.example.practiceappforunittesting.other.Resource
import com.example.practiceappforunittesting.remote.PixabayAPI
import com.example.practiceappforunittesting.remote.responses.ImageResponse
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val pixelbayApi: PixabayAPI
) : ShoppingRepository {

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> { // Observe this function returns object of our "Resource" class
        return try {

            val response = pixelbayApi.searchForImage(imageQuery) // doing the API Call

            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it) // see we encapsulated result in "Success" response using the success function of "Resource" class
                } ?: Resource.error("An unknown exception occured", null)
            } else {
                Resource.error(
                    "An unknown exception occured",
                    null
                ) // if data returned is null then we encapsulated it will "error" response
            }

        } catch (e: Exception) {
            Resource.error("Couldn't reach the server.Check your internet connection", null)
        }
    }
}