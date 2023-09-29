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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itunesdemo.adapter.RvAdapter
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ItunesViewModel
    private lateinit var progressDialog: ProgressDialog

    private var offset = 0
    private val offsetValue = 20
    private var limit = offsetValue

    private val adapter = RvAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewListener()

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        viewModel = ViewModelProvider(this).get(ItunesViewModel::class.java)
        progressDialog = ProgressDialog(this).apply {
            setMessage("加载中...")
            setCancelable(false)
        }

        viewModel.data.observe(this) {
            if (it.resultCount != 0) {
                adapter.updateData(it.results)
            }

            refreshLayout.finishLoadMore()
            refreshLayout.finishRefresh()

            window.decorView.postDelayed({
                progressDialog.dismiss()
                rv.smoothScrollBy(0, -10000)
            }, 800)

        }

        viewModel.error.observe(this) {
            progressDialog.dismiss()
            showErrorDialog(it)
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setViewListener() {
        // search result
        ed.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                fetchDataFromApi(ed.text.toString(), offset, limit)
                hideKeyboard(v)
                return@OnEditorActionListener true
            }
            false
        })
        // clear EditText icon
        ed.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) ed.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_search, 0, R.drawable.ic_clear, 0
                ) else ed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)
            }

            override fun afterTextChanged(s: Editable) {}
        })
        // clear EditText
        ed.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (ed.compoundDrawables[2] != null && event.rawX >= ed.right - ed.compoundDrawables[2].bounds.width()) {
                    ed.setText("")
                    return@OnTouchListener true
                }
            }
            false
        })
        // refresh or load previous page
        refreshLayout.setOnRefreshListener(OnRefreshListener { refreshlayout ->
            if (offset != 0) {
                offset -= offsetValue
                fetchDataFromApi(ed.text.toString(), offset, limit)
            } else refreshlayout.finishRefresh()
        })
        // refresh or load next page
        refreshLayout.setOnLoadMoreListener(OnLoadMoreListener { refreshlayout ->
            offset += offsetValue
            fetchDataFromApi(ed.text.toString(), offset, limit)
        })
    }

    private fun fetchDataFromApi(content: String, offset: Int, limit: Int) {
        progressDialog.show()
        viewModel.fetchData(content, offset, limit)
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(this).setTitle("Api Response Error").setMessage(errorMessage)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }


}