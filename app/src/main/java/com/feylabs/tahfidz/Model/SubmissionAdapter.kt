package com.feylabs.tahfidz.Model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.tahfidz.R
import kotlinx.android.synthetic.main.list_submission.view.*

class SubmissionAdapter(list: MutableList<SubmissionModel>) : RecyclerView.Adapter<SubmissionAdapter.SubmissionHolder>() {

    var listData = mutableListOf<SubmissionModel>()

    fun setData(listSetter : MutableList<SubmissionModel>){
        listData.clear()
        listData.addAll(listSetter)
        notifyDataSetChanged()
    }

    inner class SubmissionHolder(view:View) : RecyclerView.ViewHolder(view){
        var sub_id = itemView.sub_id
//        var sub_student_ID
        var sub_date = itemView.sub_date
        var sub_student_name = itemView.sub_id
        var sub_status = itemView.sub_status
        var sub_start_end = itemView.sub_start_end
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmissionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_submission,parent,false)
        return SubmissionHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: SubmissionHolder, position: Int) {
//        TODO("Not yet implemented")
        holder.sub_student_name.text = listData[position].studentName
        holder.sub_id.text=listData[position].id
        holder.sub_date.text=listData[position].date
        holder.sub_start_end
        var status = listData[position].status
        when(status){
            "0"->{
                status = "Menunggu Dinilai"
            }
            "1"->{
                status = "Sudah Dinilai"
            }
        }
        holder.sub_status.text=status
    }

}