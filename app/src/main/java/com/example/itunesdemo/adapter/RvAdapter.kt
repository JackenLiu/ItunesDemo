package com.example.itunesdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.bumptech.glide.Glide
import com.example.itunesdemo.R
import com.example.itunesdemo.net.Result

class RvAdapter(private val list: List<Result>) : RecyclerView.Adapter<RvAdapter.RvViewHolder>() {

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

        holder.tvTitle.text = list[position].artistName
        holder.tvDetail.text = list[position].trackName

        holder.ivR.setOnClickListener {
            list[position].isLike = !list[position].isLike
            notifyDataSetChanged()
        }
        if (list[position].isLike) holder.ivR.setImageResource(R.drawable.ic_heart_like)
        else holder.ivR.setImageResource(R.drawable.ic_heart_unlike)
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