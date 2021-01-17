package com.feylabs.tahfidz.Model

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.tahfidz.R
import kotlinx.android.synthetic.main.list_admin.view.*
import java.net.URLEncoder

class AdminAdapter : RecyclerView.Adapter<AdminAdapter.HolderAdminAdapter>() {
    var listAdmin = mutableListOf<AdminModel>()
    lateinit var mContext: Context

    fun setData(data: MutableList<AdminModel>) {
        listAdmin.clear()
        listAdmin.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        listAdmin.clear()
        notifyDataSetChanged()
    }

    inner class HolderAdminAdapter(view: View) : RecyclerView.ViewHolder(view) {
        var name = itemView.adminName
        var contact = itemView.adminContact
        var email = itemView.adminEmail
        var desc = itemView.descAdmin
        var btnContact = itemView.cvAdmin
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderAdminAdapter {
        return HolderAdminAdapter(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_admin,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listAdmin.size
    }

    override fun onBindViewHolder(holder: HolderAdminAdapter, position: Int) {
        mContext = holder.itemView.context
        var checkPhone = ""

        if (listAdmin[position].contact.startsWith("0",true)){
            listAdmin[position].contact=listAdmin[position].contact.replaceFirst("0","62")
        }

        holder.name.text = listAdmin[position].name
        holder.email.text = listAdmin[position].email
        holder.desc.text = listAdmin[position].desc
        holder.contact.text = listAdmin[position].contact

        holder.btnContact.setOnClickListener {
            Toast.makeText(mContext,
                "Anda Akan Dialihkan Menuju Chat Whatsapp Admin "+listAdmin[position].name,
                Toast.LENGTH_LONG).show()

            onClickWhatsApp(listAdmin[position].contact)
        }



        holder.itemView.setOnClickListener {
            Toast.makeText(mContext,
                "Anda Akan Dialihkan Menuju Chat Whatsapp Admin"+listAdmin[position].name,
                Toast.LENGTH_LONG).show()
            onClickWhatsApp(listAdmin[position].contact)
        }
    }

    fun onClickWhatsApp(phone: String) {
        val pm: PackageManager = mContext.packageManager
        try {
            val packageManager: PackageManager = mContext.packageManager
            val i = Intent(Intent.ACTION_VIEW)
            val message =
                ""
            val phone: String = phone
            try {
                val url =
                    "https://api.whatsapp.com/send?phone=$phone&text=" + URLEncoder.encode(
                        message,
                        "UTF-8"
                    )
                i.setPackage("com.whatsapp")
                i.data = Uri.parse(url)
                if (i.resolveActivity(packageManager) != null) {
                    mContext.startActivity(i)
                }
            } catch (e: Exception) {
                Toast.makeText(mContext,
                    "Gagal Membuka Whatsapp",
                    Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        } catch (e: Exception) {
            Toast.makeText(mContext,
                "Gagal Membuka Whatsapp",
                Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}




