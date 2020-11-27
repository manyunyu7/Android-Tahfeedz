package com.feylabs.tahfidz.Model

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.MotBottomSheet
import kotlinx.android.synthetic.main.list_motivation.view.*

class MotAdapter : RecyclerView.Adapter<MotAdapter.MotHolder>() {

    var listData = mutableListOf<MotModel>()


    fun setData(listSetter: MutableList<MotModel>) {
        listData.clear()
        listData.addAll(listSetter)
        listData.shuffle()
        notifyDataSetChanged()
    }

    inner class MotHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mot_cover = itemView.mot_cover
        var mot_title = itemView.mot_title
        var mot_content = itemView.mot_content
        var mot_more = itemView.mot_content
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_motivation, parent, false)
        return MotHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: MotHolder, position: Int) {

        holder.mot_title.text = listData[position].title
        holder.mot_content.text = listData[position].content

        Glide.with(holder.itemView.context)
            .load(listData[position].img)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.no_img)
            .into(holder.mot_cover)

        holder.itemView.setOnClickListener {
            val activity = holder.itemView.context as Activity
            val motDetailBottomSheet = MotBottomSheet(activity)
            val motDetTitle = motDetailBottomSheet.findViewById(R.id.tv_det_mot_title) as TextView
            val motDetContent =
                motDetailBottomSheet.findViewById(R.id.tv_det_mot_content) as TextView
            val motDetCover = motDetailBottomSheet.findViewById(R.id.iv_det_mot_img) as ImageView
            val motDetCloseBtn =
                motDetailBottomSheet.findViewById(R.id.btnCloseDetMot) as ImageButton

            motDetTitle.text = listData[position].title
            motDetContent.text = listData[position].content
            Glide.with(activity)
                .load(listData[position].img)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.no_img)
                .into(motDetCover)
            motDetCloseBtn.setOnClickListener {
                motDetailBottomSheet.animation = loadAnimation(activity, R.anim.fragment_close_exit)
                motDetailBottomSheet.visibility = View.GONE
            }
            motDetailBottomSheet.show()

        }


    }

}