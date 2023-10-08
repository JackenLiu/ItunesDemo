package com.example.itunesdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.bumptech.glide.Glide
import com.example.itunesdemo.MainActivity
import com.example.itunesdemo.R
import com.example.itunesdemo.db.Data
import com.example.itunesdemo.net.Result
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

        val typeName = getKindStr(list[position].kind)
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
                    typeName + " · " + list[position].artistName
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
                val titleStr = item.trackName ?: item.collectionName
                val data =
                    activity.viewModel.getData(
                        activity.db.dataDao(), item.artworkUrl100,
                        titleStr, getKindStr(item.kind) + " · " + item.artistName
                    )
                if (data != null) item.isLike = true
                originList.add(item)
            }
            activity.runOnUiThread { notifyDataSetChanged() }
        }
    }

    private fun getKindStr(kind: String?) = when (kind) {
        "feature-movie" -> activity.resources.getString(R.string.movie)
        "book" -> activity.resources.getString(R.string.book)
        "song" -> activity.resources.getString(R.string.song)
        "album" -> activity.resources.getString(R.string.album)
        "coached-audio" -> activity.resources.getString(R.string.coached_audio)
        "interactive-booklet" -> activity.resources.getString(R.string.interactive_booklet)
        "music-video" -> activity.resources.getString(R.string.music_video)
        "pdf podcast" -> activity.resources.getString(R.string.pdf_podcast)
        "podcast-episode" -> activity.resources.getString(R.string.podcast_episode)
        "software-package" -> activity.resources.getString(R.string.software_package)
        "tv-episode" -> activity.resources.getString(R.string.tv_episode)
        "artist" -> activity.resources.getString(R.string.artist)
        else -> activity.resources.getString(R.string.other)
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