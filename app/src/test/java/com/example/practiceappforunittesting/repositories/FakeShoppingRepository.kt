package com.example.practiceappforunittesting.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practiceappforunittesting.data.local.ShoppingItem
import com.example.practiceappforunittesting.data.remote.responses.ImageResponse
import com.example.practiceappforunittesting.other.Resource

// Here we simulate the behaviour of Our Original repository to just check how our viewmodel is responding to the events that are coming from the repository
class FakeShoppingRepository :
    ShoppingRepository { // There are many types of Test doubles and one of the most used are "Fake" or "Mock" test double which we are using here. Basically they are very well suited for test cases but not for actual production.This class here will simulate behaviour like real repository but not compeletely

    private val shoppingItems = mutableListOf<ShoppingItem>() // since we are testing , we can simply use dummy data ie. like here we are using list.
    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)
    private val observableTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError =
        false // This will determine whether our function should return error or not

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData() {
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }

    private fun getTotalPrice(): Float {
        return shoppingItems.sumByDouble { it.price.toDouble() }.toFloat()
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return observableShoppingItems
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(
                ImageResponse(
                    listOf(),
                    0,
                    0
                )
            ) // just returning dummy data for sake of testing
        }
    }

}