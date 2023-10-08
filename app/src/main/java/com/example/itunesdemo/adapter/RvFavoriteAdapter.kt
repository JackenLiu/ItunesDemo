package com.example.itunesdemo.adapter

import android.content.Intent
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.bumptech.glide.Glide
import com.example.itunesdemo.DetailActivity
import com.example.itunesdemo.MainActivity
import com.example.itunesdemo.R
import com.example.itunesdemo.db.Data

class RvFavoriteAdapter(private val list: List<Data> = mutableListOf()) :
    RecyclerView.Adapter<RvFavoriteAdapter.RvViewHolder>() {
    lateinit var activity: MainActivity

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

        holder.tvTitle.text = list[position].title
        holder.tvDetail.text = list[position].detail
        holder.ivR.visibility = View.GONE

        holder.itemView.setOnClickListener {
            if (list[position].kind != "song") return@setOnClickListener
            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                holder.iv,
                "shared_element"
            )
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("artworkUrl_key", list[position].imgUrl)
            intent.putExtra("tv_title_key", list[position].title)
            intent.putExtra("tv_detail_key", list[position].detail)
            intent.putExtra("music_url_key", list[position].musicUrl)
            activity.startActivity(
                intent,
                activityOptions.toBundle()
            )
        }
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