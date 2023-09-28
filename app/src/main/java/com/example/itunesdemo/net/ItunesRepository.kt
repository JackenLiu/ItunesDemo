package com.example.itunesdemo.net

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItunesRepository {

    private val _data = MutableLiveData<ItunesResponse>()
    val data: LiveData<ItunesResponse> get() = _data

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun searchItunes(term: String, offset: Int, limit: Int) {
        RetrofitBuilder.apiService.searchItunes(term, offset, limit).enqueue(object :
            Callback<ItunesResponse> {
            override fun onResponse(
                call: Call<ItunesResponse>,
                response: Response<ItunesResponse>
            ) {
                if (response.isSuccessful) {
                    _data.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                _error.postValue(t.message)
            }
        })
    }
}


