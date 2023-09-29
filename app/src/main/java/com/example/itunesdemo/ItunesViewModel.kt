package com.example.itunesdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.itunesdemo.net.ItunesRepository
import com.example.itunesdemo.net.ItunesResponse

class ItunesViewModel : ViewModel() {

    private val repository = ItunesRepository() // 假设你有一个Repository类管理数据请求

    val data: LiveData<ItunesResponse> = repository.data
    val error: LiveData<String> = repository.error

    fun fetchData(content: String, offset: Int, limit: Int) {
        repository.searchItunes(content, offset, limit)
    }
}



