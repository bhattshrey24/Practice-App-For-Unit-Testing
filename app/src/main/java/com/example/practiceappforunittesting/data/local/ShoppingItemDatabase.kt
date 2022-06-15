package com.example.practiceappforunittesting.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShoppingItem::class],
    version = 1
)
abstract class ShoppingItemDatabase : RoomDatabase() { // Here I guess we are not taking care of asynchronously accessing of DB by concurrent threads since we are just testing but in real case scenario this code is Thread safe
    abstract fun shoppingDao(): ShoppingDao
}