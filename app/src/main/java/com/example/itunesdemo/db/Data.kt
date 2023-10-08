package com.example.itunesdemo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "data",
    indices = [Index(value = ["imgUrl", "title", "detail", "kind"], unique = true)]
)
data class Data(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    var imgUrl: String?,
    var title: String?,
    var detail: String?,
    var kind: String?,
    var musicUrl: String = ""
)
