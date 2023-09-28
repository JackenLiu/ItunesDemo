package com.example.itunesdemo

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itunesdemo.adapter.RvAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ItunesViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListener()

        viewModel = ViewModelProvider(this).get(ItunesViewModel::class.java)
        progressDialog = ProgressDialog(this).apply {
            setMessage("加载中...")
            setCancelable(false)
        }

        viewModel.data.observe(this) {
            progressDialog.dismiss()
            rv.layoutManager = LinearLayoutManager(this)
            val adapter = RvAdapter(it.results)
            rv.adapter = adapter
        }

        viewModel.error.observe(this) {
            progressDialog.dismiss()
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListener() {

        ed.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                fetchDataFromApi(ed.text.toString())

                hideKeyboard(v)
                return@OnEditorActionListener true
            }
            false
        })

        ed.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) ed.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_search, 0, R.drawable.ic_clear, 0
                ) else ed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        ed.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (ed.compoundDrawables[2] != null &&
                    event.rawX >= ed.right - ed.compoundDrawables[2].bounds.width()
                ) {
                    ed.setText("")
                    return@OnTouchListener true
                }
            }
            false
        })

    }

    private fun fetchDataFromApi(content: String) {
        progressDialog.show()
        viewModel.fetchData(content)
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

}