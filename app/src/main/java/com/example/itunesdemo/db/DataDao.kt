package com.example.itunesdemo.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DataDao {
    @Query("SELECT * FROM data")
    suspend fun getAllData(): List<Data>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(data: Data)

    @Delete
    suspend fun deleteData(data: Data)

    @Query("DELETE FROM data WHERE imgUrl = :imgUrl AND title = :title AND detail = :detail AND kind = :kind")
    suspend fun deleteData(imgUrl: String, title: String, detail: String, kind: String)

    @Query("SELECT * FROM data WHERE imgUrl = :imgUrl AND title = :title AND detail = :detail AND kind = :kind")
    fun getData(imgUrl: String, title: String, detail: String, kind: String): Data?
}
