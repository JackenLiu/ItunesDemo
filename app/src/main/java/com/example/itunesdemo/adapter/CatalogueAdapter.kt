package com.example.itunesdemo.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itunesdemo.R

class CatalogueAdapter(
    private var rvAdapter: RvAdapter, private val list: MutableList<TextBean> = mutableListOf()
) :
    RecyclerView.Adapter<CatalogueAdapter.RvViewHolder>() {

    private var selectFilterNum = 0
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

                selectFilterNum++
            } else {
                holder.tvTitle.setTextColor(Color.BLACK)
                holder.tvTitle.setBackgroundResource(R.drawable.shape_circle_rectangle_red_empty)

                selectFilterNum--
            }

            rvAdapter.filterList(selectFilterNum, list)
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

    data class TextBean(
        var name: String = "",
        var isSelect: Boolean = false,
        var isCountry: Boolean = false
    )
}