package com.example.artbookmvvmandtesting.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.artbookmvvmandtesting.getOrAwaitValueAndroidTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ArtDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("testDatabase")
    lateinit var database: ArtDatabase

    private lateinit var dao: ArtDao

    @Before
    fun setup(){
        /*
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),ArtDatabase::class.java
        ).allowMainThreadQueries().build()
*/
        hiltRule.inject()

        dao  =database.artDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertArtTesting() = runTest{

        val exampleArt = Art("Salvator Mundi","Da Vinci",1500,"imageTest",1)
        dao.insertArt(exampleArt)
        val list = dao.observeArts().getOrAwaitValueAndroidTest()
        assertThat(list).contains(exampleArt)
    }

    @Test
    fun deleteArtTesting() = runTest{

        val exampleArt = Art("Salvator Mundi","Da Vinci",1500,"imageTest",1)
        dao.insertArt(exampleArt)
        dao.deleteArt(exampleArt)

        val list = dao.observeArts().getOrAwaitValueAndroidTest()
        assertThat(list).doesNotContain(exampleArt)
    }
}