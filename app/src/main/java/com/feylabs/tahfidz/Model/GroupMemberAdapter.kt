package com.feylabs.tahfidz.Model

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.URL
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_member.view.*


class GroupMemberAdapter : RecyclerView.Adapter<GroupMemberAdapter.HolderGroupMemberAdapter>() {
    var listGroup = mutableListOf<GroupMemberModel>()

    fun setData(data: MutableList<GroupMemberModel>) {
        listGroup.clear()
        listGroup.addAll(data)
        notifyDataSetChanged()
    }

    inner class HolderGroupMemberAdapter(view: View) : RecyclerView.ViewHolder(view) {
        var name = itemView.user_name
        var nisn = itemView.user_nisn
        var u_class = itemView.user_class
        var studentPhoto = itemView.usr_profile
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderGroupMemberAdapter {
        return HolderGroupMemberAdapter(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_member,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listGroup.size
    }

    override fun onBindViewHolder(holder: HolderGroupMemberAdapter, position: Int) {
        val context = holder.itemView.context
        holder.name.text = listGroup[position].name
        holder.nisn.text = listGroup[position].student_nisn
        holder.u_class.text = listGroup[position].student_class

        var studentProfileUrl = URL.STUDENT_PHOTO + listGroup[position].student_photo

        if (listGroup[position].student_gender == "L") {
            Picasso.get()
                .load(studentProfileUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit()
                .placeholder(R.drawable.ic_student_moslem)
                .error(R.drawable.ic_student_moslem)
                .into(holder.studentPhoto, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        Log.i("fan-url-teacher-photo", studentProfileUrl)
                    }

                    override fun onError(e: java.lang.Exception?) {
                        Log.i("fan-url-teacher-photo", studentProfileUrl)
                    }
                })
        } else {
            Picasso.get()
                .load(studentProfileUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit()
                .placeholder(R.drawable.ic_student_moslemah)
                .error(R.drawable.ic_student_moslemah)
                .into(holder.studentPhoto, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        Log.i("fan-url-teacher-photo", studentProfileUrl)
                    }

                    override fun onError(e: java.lang.Exception?) {
                        Log.i("fan-url-teacher-photo", studentProfileUrl)
                    }
                })
        }

        val mContext = holder.itemView.context
        holder.itemView.setOnClickListener {
            val cfAlert = CFAlertDialog.Builder(mContext)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Pilih Fitur")
                .addButton(
                    "Lihat Setoran Siswa",
                    -1,
                    -1,
                    CFAlertDialog.CFAlertActionStyle.DEFAULT,
                    CFAlertDialog.CFAlertActionAlignment.START
                ) { dialog, which ->
                    dialog.dismiss()
                    val bundle = Bundle()
                    bundle.putString("student_id", listGroup[position].student_id)
                    bundle.putBoolean("isIndividually", true)
                    holder.itemView.findNavController().navigate(R.id.mentorSubmission, bundle)
//                        Navigation.findNavController(holder.itemView).navigate(R.id.submissionHistoryFragment, bundle)
                }
                .addButton(
                    "Kembali", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT,
                    CFAlertDialog.CFAlertActionAlignment.START
                ) { dialog, which ->
                    dialog.dismiss()
                }
            cfAlert.show()
        }
    }
}




