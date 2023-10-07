package com.example.itunesdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itunesdemo.db.Data
import com.example.itunesdemo.db.DataDao
import com.example.itunesdemo.net.ItunesRepository
import com.example.itunesdemo.net.ItunesResponse
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = ItunesRepository() // 假设你有一个Repository类管理数据请求

    val data: LiveData<ItunesResponse> = repository.data
    val error: LiveData<String> = repository.error

    val favorData = MutableLiveData<List<Data>>()

    fun fetchData(content: String, offset: Int, limit: Int) {
        repository.searchItunes(content, offset, limit)
    }


    fun getAllData(dao: DataDao) {
        // Using viewModelScope to launch a coroutine
        viewModelScope.launch {
            favorData.value = dao.getAllData()
        }
    }

    fun insertData(dao: DataDao, data: Data) {
        viewModelScope.launch {
            dao.insertData(data)
        }
    }

    fun deleteData(dao: DataDao, data: Data) {
        viewModelScope.launch {
            dao.deleteData(data)
        }
    }
}



