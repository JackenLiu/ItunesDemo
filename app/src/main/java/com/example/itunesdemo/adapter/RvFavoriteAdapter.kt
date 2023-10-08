package com.example.itunesdemo.adapter

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.bumptech.glide.Glide
import com.example.itunesdemo.R
import com.example.itunesdemo.db.Data

class RvFavoriteAdapter(private val list: List<Data> = mutableListOf()) :
    RecyclerView.Adapter<RvFavoriteAdapter.RvViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        if (viewType == list.size - 1) {
            val layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            layoutParams.bottomMargin = 50
            layout.layoutParams = layoutParams
        }
        return RvViewHolder(layout)
    }

    override fun onBindViewHolder(holder: RvViewHolder, position: Int) {
        // 加载预览图
        Glide.with(holder.itemView.context)
            .load(list[position].imgUrl)
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


        holder.tvTitle.text = list[position].title
        holder.tvDetail.text = typeName + " · " + list[position].detail
        holder.ivR.visibility = View.GONE
    }

    override fun getItemViewType(position: Int) = position
    override fun getItemCount() = list.size


    inner class RvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv = itemView.findViewById<ImageView>(R.id.iv)
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_1)
        val tvDetail = itemView.findViewById<TextView>(R.id.tv_2)
        val ivR = itemView.findViewById<ImageView>(R.id.iv_r)
    }
}