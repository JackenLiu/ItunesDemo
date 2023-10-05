package com.example.itunesdemo.adapter

import android.graphics.Color
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

class CatalogueAdapter(
    private var rvAdapter: RvAdapter, private val list: MutableList<TextBean> = mutableListOf()
) :
    RecyclerView.Adapter<CatalogueAdapter.RvViewHolder>() {

    private val filterList = mutableListOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_list, parent, false)
        return RvViewHolder(layout)
    }

    override fun onBindViewHolder(holder: RvViewHolder, position: Int) {
        holder.tvTitle.text = list[position].name

        holder.tvTitle.setOnClickListener {
            list[position].isSelect = !list[position].isSelect

            if (list[position].isSelect) {
                holder.tvTitle.setTextColor(Color.WHITE)
                holder.tvTitle.setBackgroundResource(R.drawable.shape_circle_rectangle_red)

                // add filter name
                if (!filterList.contains(list[position].name)) filterList.add(list[position].name)
            } else {
                holder.tvTitle.setTextColor(Color.BLACK)
                holder.tvTitle.setBackgroundResource(R.drawable.shape_circle_rectangle_red_empty)

                // remove filter name
                if (filterList.contains(list[position].name)) filterList.remove(list[position].name)
            }

            rvAdapter.filterList(filterList)
        }
    }

    override fun getItemViewType(position: Int) = position
    override fun getItemCount() = list.size

    fun updateData(newItems: List<TextBean>) {
        list.clear()
        list.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class RvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tv)
    }

    class TextBean {
        var name = ""
        var isSelect = false
    }
}