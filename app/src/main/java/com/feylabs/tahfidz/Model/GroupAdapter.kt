package com.feylabs.tahfidz.Model

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.tahfidz.R
import kotlinx.android.synthetic.main.list_group.view.*

class GroupAdapter : RecyclerView.Adapter<GroupAdapter.HolderGroupAdapter>() {
    var listGroup = mutableListOf<GroupModel>()

    fun setData(data: MutableList<GroupModel>) {
        listGroup.clear()
        listGroup.addAll(data)
        notifyDataSetChanged()
    }

    inner class HolderGroupAdapter(view: View) : RecyclerView.ViewHolder(view) {
        var name = itemView.tvGroupName
        var card_container = itemView.card_data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderGroupAdapter {
        return HolderGroupAdapter(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_group,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listGroup.size
    }

    override fun onBindViewHolder(holder: HolderGroupAdapter, position: Int) {
        val context = holder.itemView.context
        holder.name.text = listGroup[position].group_name.toString()
        val red = ContextCompat.getColor(context, R.color.colorRedPastel)
        val blue = ContextCompat.getColor(context, R.color.colorBlueWaves)

        if (listGroup[position].group_type=="L"){
            holder.card_container.setCardBackgroundColor(red)
        }else{
            holder.card_container.setCardBackgroundColor(blue)
        }

        holder.itemView.setOnClickListener {

        }
    }
}




