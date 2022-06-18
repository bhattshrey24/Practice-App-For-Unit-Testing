package com.example.practiceappforunittesting.repositories

import androidx.lifecycle.LiveData
import com.example.practiceappforunittesting.data.local.ShoppingDao
import com.example.practiceappforunittesting.data.local.ShoppingItem
import com.example.practiceappforunittesting.data.remote.PixabayAPI
import com.example.practiceappforunittesting.data.remote.responses.ImageResponse
import com.example.practiceappforunittesting.other.Resource
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixelbayApi: PixabayAPI
) : ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

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