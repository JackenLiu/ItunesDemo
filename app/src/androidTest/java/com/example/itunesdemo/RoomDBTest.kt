package com.example.itunesdemo

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.itunesdemo.db.AppDatabase
import com.example.itunesdemo.db.Data
import com.example.itunesdemo.db.DataDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomDBTest {

    private lateinit var dataDao: DataDao
    lateinit var appDB: AppDatabase

    @Before
    fun createDatabase() {
        appDB = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dataDao = appDB.dataDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        appDB.close()
    }

    @Test
    fun insertData() {
        runBlocking {
//            dataDao.insertData(Data(null, "1", "2", "3", "4"))
//            dataDao.insertData(Data(null, "11", "22", "33", "44"))
//            dataDao.insertData(Data(null, "111", "222", "333", "444"))
            val insertData = Data(null, "1111", "2222", "3333", "4444")
            dataDao.insertData(insertData)
            val getData = dataDao.getData("1111", "2222", "3333")
            assertEquals(insertData, getData)
        }
    }

    @Test
    fun findAllUsers() {
        runBlocking {
            for (data in dataDao.getAllData()) {
                println("Data { userId = ${data.id}, imgUrl = ${data.imgUrl}, title = ${data.title}, detail = ${data.detail}, kind = ${data.kind}}")
            }
        }
    }
}
