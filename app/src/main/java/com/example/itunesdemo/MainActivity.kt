package com.example.itunesdemo

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ItunesViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(ItunesViewModel::class.java)

        progressDialog = ProgressDialog(this).apply {
            setMessage("加载中...")
            setCancelable(false)
        }

        viewModel.data.observe(this, {
            progressDialog.dismiss()
            Toast.makeText(this, it.results[0].artistViewUrl, 0).show()
            // 更新你的UI
        })

        viewModel.error.observe(this, { error ->
            progressDialog.dismiss()
            // 显示错误消息或其他反馈
        })

        fetchDataFromApi()
    }

    private fun fetchDataFromApi() {
        progressDialog.show()
        viewModel.fetchData()
    }
}