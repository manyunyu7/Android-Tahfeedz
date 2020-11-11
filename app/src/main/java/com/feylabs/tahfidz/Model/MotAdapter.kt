package com.feylabs.tahfidz.Model

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.View.SubmissionDetailStudent
import com.feylabs.tahfidz.ViewModel.MotDetail
import kotlinx.android.synthetic.main.list_motivation.view.*

class MotAdapter() : RecyclerView.Adapter<MotAdapter.MotHolder>() {

    var listData = mutableListOf<MotModel>()


        fun setData(listSetter : MutableList<MotModel>){
        listData.clear()
        listData.addAll(listSetter)
        notifyDataSetChanged()
    }

    inner class MotHolder(view:View) : RecyclerView.ViewHolder(view){
        var mot_cover = itemView.mot_cover
        var mot_title = itemView.mot_title
        var mot_content = itemView.mot_content
        var mot_more = itemView.mot_content
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_motivation,parent,false)
        return MotHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: MotHolder, position: Int) {

        holder.mot_title.text=listData[position].title
        holder.mot_content.text=listData[position].content

        Glide.with(holder.itemView.context)
            .load(listData[position].img)
            .placeholder(R.drawable.mot_placeholder_1)
            .error(R.drawable.mot_placeholder_2)
            .into(holder.mot_cover);

        holder.itemView.setOnClickListener {
            Toast.makeText(it.context,listData[position].title,Toast.LENGTH_LONG).show()
        }

        val intent =  Intent(holder.itemView.context, MotDetail::class.java)
        holder.mot_more.setOnClickListener { v->
            intent.putExtra("data",listData[position])
            v.context.startActivity(intent)
        }

    }

}