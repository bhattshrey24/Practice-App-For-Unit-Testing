package com.example.practiceappforunittesting.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.practiceappforunittesting.getOrAwaitValue
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi // this annotation should be used when you are using coroutines in test cases like we used "runBlockingTest" here. I guess its not necessary to add this notation but you should add it
@RunWith(AndroidJUnit4::class) // Now since here we are usig android specific libraries like context etc therefore JUnit cannot run it directly on JVM like it did in "test" package , therefore we have to apply this annotation to tell JUnit to runt these tests in Emulator
@SmallTest // There are 3 annotation for “androidTest” package, “@SmallTest” if you want to do Unit Testing , “@MediumTest” for Integerated Testing and “@LargeTest” annotation for UI testing. Since here we are doing Unit testing therefore I used this annotation
class ShoppingDaoTest {

    @get:Rule // This annotation tells JVM to apply the Rule for all the test cases , Like here we are telling JVM to Apply the rule that all test cases should run synchronously
    var instantTaskExecutorRule = InstantTaskExecutorRule() // This as the name suggest tells JVM to run all the test cases synchronously

    private lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before // This annotation tells JVM to execute this function everytime before executing a test case(Mtlb har test case execute hone se pehle yeh wala function chalega) , so here you can do initialization like we did here
    fun setup() {
       // database=Room.databaseBuilder()// Usually we do this in our source code but in testing since we only need to test therefore we use a different approach as shown below

        // In testing we use "inMemoryDatabaseBuilder()" instead of "databaseBuilder()" to make Db cause it will only make database in RAM which will get erased after each test case execution this is useful because we don't want to create multiple Databases in our persistent memory just for the sake of testing cause that will just waste memory
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), // This is how we get context
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build() //"allowMainThreadQueries" as the name suggest this function will allow  allow this Room database to be accessed by Main thread , now generally we access DB using background thread which runs asynchronously but we do not want async behaviour in test cases therefore we let main thread execute it

        dao = database.shoppingDao() // just initializing the Dao with Database so that we can use the function present in it
    }

    @After// This annotation tells JVM to run this function after each test case is executed (Mtlb har test case execute hone ke baad yeh wala function chalega) , so here you can close connections like the way we closed the db
    fun teardown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runBlockingTest { // runBlockingTest is a coroutine . This is optimised for test cases like it will skip the "delay" function cause we dont want our test case to delay ie. they should be fast

        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()  //Now here is a problem , this function returns a liveData and liveData is async and we don't want async behaviour in our testcases so the solution is this "getOrAwaitValue" function which is from the google class "LiveDataUtilAndroidTest" that we added.

        Truth.assertThat(allShoppingItems).contains(shoppingItem)// this function will check if "allShoppingItems" list contains "shoppingItem" or not
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        Truth.assertThat(allShoppingItems).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name", 2, 10f, "url", id = 1)
        val shoppingItem2 = ShoppingItem("name", 4, 5.5f, "url", id = 2)
        val shoppingItem3 = ShoppingItem("name", 0, 100f, "url", id = 3)

        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

        Truth.assertThat(totalPriceSum).isEqualTo(2 * 10f + 4 * 5.5f)
    }
}