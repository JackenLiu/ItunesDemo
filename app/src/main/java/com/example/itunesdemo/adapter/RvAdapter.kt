package com.example.itunesdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.bumptech.glide.Glide
import com.example.itunesdemo.MainActivity
import com.example.itunesdemo.R
import com.example.itunesdemo.db.Data
import com.example.itunesdemo.net.Result
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class RvAdapter(
    private val list: MutableList<Result> = mutableListOf(),
    private val originList: MutableList<Result> = mutableListOf()
) :
    RecyclerView.Adapter<RvAdapter.RvViewHolder>() {

    lateinit var activity: MainActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        if (viewType == list.size - 1) {
            val layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            layoutParams.bottomMargin = 150
            layout.layoutParams = layoutParams
        }
        return RvViewHolder(layout)
    }

    override fun onBindViewHolder(holder: RvViewHolder, position: Int) {
        // 加载预览图
        Glide.with(holder.itemView.context)
            .load(list[position].artworkUrl100)
            .placeholder(R.drawable.loading) // 加载时的占位图
            .error(R.drawable.error) // 加载失败时的图片
            .centerCrop()
            .into(holder.iv)

        val typeName = when (list[position].kind) {
            "feature-movie" -> "电影"
            "book" -> "电子书"
            "song" -> "歌曲"
            "album" -> "专辑"
            "coached-audio" -> "指导音频"
            "interactive-booklet" -> "互动手册"
            "music-video" -> "音乐视频"
            "pdf podcast" -> "pdf 播客"
            "podcast-episode" -> "播客剧集"
            "software-package" -> "软件包"
            "tv-episode" -> "电视剧集"
            "artist" -> "艺术家"
            else -> "其他"
        }


        val titleStr = if (list[position].trackName != null) list[position].trackName
        else list[position].collectionName
        holder.tvTitle.text = titleStr
        holder.tvDetail.text = typeName + " · " + list[position].artistName

        holder.ivR.setOnClickListener {
            list[position].isLike = !list[position].isLike
            notifyDataSetChanged()
            if (list[position].isLike) {
                activity.viewModel.insertData(
                    activity.db.dataDao(),
                    Data(
                        null,
                        list[position].artworkUrl100,
                        titleStr,
                        typeName + " · " + list[position].artistName,
                        list[position].kind
                    )
                )
            } else {
                activity.viewModel.deleteData(
                    activity.db.dataDao(),
                    list[position].artworkUrl100,
                    titleStr!!,
                    typeName + " · " + list[position].artistName,
                    list[position].kind!!
                )
            }
        }

        if (list[position].isLike) holder.ivR.setImageResource(R.drawable.ic_heart_like)
        else holder.ivR.setImageResource(R.drawable.ic_heart_unlike)
    }

    override fun getItemViewType(position: Int) = position
    override fun getItemCount() = list.size

    fun updateData(newItems: List<Result>) {
        thread {
            list.clear()
            originList.clear()
            list.addAll(newItems)
            for (item in newItems) {
                val data =
                    activity.viewModel.getDataByImgUrl(activity.db.dataDao(), item.artworkUrl100)
                if (data != null) item.isLike = true
                originList.add(item)
            }
            activity.runOnUiThread { notifyDataSetChanged() }
        }
    }

    fun filterList(selectFilterNum: Int, filterList: List<CatalogueAdapter.TextBean>) {
        val newList = mutableListOf<Result>()
        newList.addAll(originList)
        if (selectFilterNum != 0) {
            for (result in originList) {
                loop@ for (bean in filterList) {
                    // the filter is selected
                    if (bean.isSelect) {
                        if (bean.isCountry) { // country match
                            if (result.country != bean.name) if (newList.contains(result))
                                newList.remove(result)

                        } else { // kind match
                            if (result.kind != bean.name) if (newList.contains(result))
                                newList.remove(result)
                        }
                    }
                }

            }
        }

        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class RvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv = itemView.findViewById<ImageView>(R.id.iv)
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_1)
        val tvDetail = itemView.findViewById<TextView>(R.id.tv_2)
        val ivR = itemView.findViewById<ImageView>(R.id.iv_r)
    }
}