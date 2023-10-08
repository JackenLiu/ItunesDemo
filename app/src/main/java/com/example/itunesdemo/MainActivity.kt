package com.example.itunesdemo

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.itunesdemo.adapter.CatalogueAdapter
import com.example.itunesdemo.adapter.CatalogueAdapter.TextBean
import com.example.itunesdemo.adapter.RvAdapter
import com.example.itunesdemo.adapter.RvFavoriteAdapter
import com.example.itunesdemo.db.AppDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    private lateinit var progressDialog: ProgressDialog

    private var offset = 0
    private val offsetValue = 20
    private var limit = offsetValue

    private val adapter = RvAdapter()
    private val catalogueAdapter = CatalogueAdapter(adapter)

    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewListener()

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "favorite").build()
        adapter.activity = this

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        rv_catalogue.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_catalogue.adapter = catalogueAdapter

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        progressDialog = ProgressDialog(this).apply {
            setMessage("加载中...")
            setCancelable(false)
        }

        viewModel.data.observe(this) {
            if (it.resultCount != 0) {
                iv_empty.visibility = View.GONE
                adapter.updateData(it.results)

                val catalogueList = mutableListOf<TextBean>()
                val nameList = mutableListOf<String>() // 已经添加过的目录名
                for (result in it.results) {
                    if (result.kind != null && !nameList.contains(result.kind)) {
                        nameList.add(result.kind)
                        val textBean = TextBean()
                        textBean.name = result.kind
                        textBean.isCountry = false
                        catalogueList.add(textBean)
                    }
                    if (result.country != null && !nameList.contains(result.country)) {
                        val textBean = TextBean()
                        nameList.add(result.country)
                        textBean.name = result.country
                        textBean.isCountry = true
                        catalogueList.add(textBean)
                    }
                }
                val catalogueAdapter = CatalogueAdapter(adapter)
                rv_catalogue.adapter = catalogueAdapter
                catalogueAdapter.updateData(catalogueList)
            }

            refreshLayout.finishLoadMore()
            refreshLayout.finishRefresh()

            window.decorView.postDelayed({
                progressDialog.dismiss()
                rv.smoothScrollBy(0, -10000)
                rv_catalogue.smoothScrollBy(-10000, 0)
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
                    showKeyboard(v)
                    return@OnTouchListener true
                }
            }
            false
        })
        // refresh or load previous page
        refreshLayout.setOnRefreshListener { refreshlayout ->
            if (offset != 0) {
                offset -= offsetValue
                fetchDataFromApi(ed.text.toString(), offset, limit)
            } else refreshlayout.finishRefresh()
        }
        // refresh or load next page
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            offset += offsetValue
            fetchDataFromApi(ed.text.toString(), offset, limit)
        }
        // favorite
        iv_favorites.setOnClickListener { showPopupWindow() }
    }

    private fun fetchDataFromApi(content: String, offset: Int, limit: Int) {
        progressDialog.show()
        viewModel.fetchData(content, offset, limit)
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(this).setTitle("Api Response Error").setMessage(errorMessage)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private var popupWindow: PopupWindow? = null

    private fun showPopupWindow() {
        if (popupWindow != null) return
        else {
            // 初始化 PopupWindow
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.layout_popup, null)
            popupWindow = PopupWindow(
                popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            viewModel.getAllData(db.dataDao())
            viewModel.favorData.observe(this) {
                // 从 XML 布局文件中获取 PopupWindow 的内容
                popupWindow?.isFocusable = true
                popupWindow?.isOutsideTouchable = true

                // 如果 API >= 21, 设置 PopupWindow 的进入和退出动画
                popupWindow?.enterTransition = android.transition.TransitionInflater.from(this)
                    .inflateTransition(android.R.transition.fade)
                popupWindow?.exitTransition = android.transition.TransitionInflater.from(this)
                    .inflateTransition(android.R.transition.fade)

                popupView.findViewById<ImageView>(R.id.iv_close)
                    .setOnClickListener {
                        popupWindow?.dismiss()
                        popupWindow = null
                    }
                popupView.findViewById<View>(R.id.v)
                    .setOnClickListener {
                        popupWindow?.dismiss()
                        popupWindow = null
                    }
                if (it.isNotEmpty()) popupView.findViewById<ImageView>(R.id.iv_empty).visibility =
                    View.GONE else popupView.findViewById<ImageView>(R.id.iv_empty).visibility =
                    View.VISIBLE
                val recyclerView = popupView.findViewById<RecyclerView>(R.id.rv_)
                recyclerView.layoutManager = LinearLayoutManager(popupView.context)
                recyclerView.visibility = View.VISIBLE
                val adapter = RvFavoriteAdapter(it)
                recyclerView.adapter = adapter
                popupWindow?.showAtLocation(
                    cl_parent,  // 需要一个 parent view，通常可以使用整个内容视图
                    Gravity.NO_GRAVITY,  // 指定无重力效应
                    0,  // x 坐标
                    0   // y 坐标
                )
            }
        }
    }


}