package com.example.practiceappforunittesting.repositories

import androidx.lifecycle.LiveData
import com.example.practiceappforunittesting.data.local.ShoppingItem
import com.example.practiceappforunittesting.other.Resource
import com.example.practiceappforunittesting.data.remote.responses.ImageResponse
import retrofit2.Response

interface ShoppingRepository {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}