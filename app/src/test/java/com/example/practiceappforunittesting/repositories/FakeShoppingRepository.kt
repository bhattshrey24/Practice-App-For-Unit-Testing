package com.example.practiceappforunittesting.repositories

import com.example.practiceappforunittesting.other.Resource
import com.example.practiceappforunittesting.remote.responses.ImageResponse

// Here we simulate the behaviour of Our Original repository to just check how our viewmodel is responding to the events that are coming from the repository
class FakeShoppingRepository :
    ShoppingRepository { // There are many types of Test doubles and one of the most used are "Fake" or "Mock" test double which we are using here. Basically they are very well suited for test cases but not for actual production.This class here will simulate behaviour like real repository but not compeletely
    private var shouldReturnNetworkError =
        false // This will determine whether our function should return error or not

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(ImageResponse(listOf(), 0, 0)) // just returning dummy data for sake of testing
        }
    }

}