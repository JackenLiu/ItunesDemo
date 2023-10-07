package com.example.itunesdemo.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataDao {
    @Query("SELECT * FROM data")
    suspend fun getAllData(): List<Data>

    @Insert
    suspend fun insertData(data: Data)

    @Delete
    suspend fun deleteData(data: Data)
}
