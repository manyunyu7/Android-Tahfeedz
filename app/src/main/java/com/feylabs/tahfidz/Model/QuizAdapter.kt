package com.feylabs.tahfidz.Model

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.Util.API_Endpoint
import com.feylabs.tahfidz.View.SharedView.WebsiteActivity
import kotlinx.android.synthetic.main.list_quiz.view.*
import org.json.JSONObject

class QuizAdapter :
    RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {


    private val listData = mutableListOf<QuizModel>()

    lateinit var mContext: Context

    fun addData(setter: MutableList<QuizModel>) {
        listData.clear()
        listData.addAll(setter)
        notifyDataSetChanged()
    }


    inner class QuizViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quizTitle = view.quizTitle
        val quizDesc = view.quizDesc
        val quizDate = view.quizDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val razkun = listData[position]
        mContext = holder.itemView.context
        holder.quizTitle.text = razkun.title
        holder.quizDesc.text = razkun.desc
        holder.quizDate.text = razkun.created_at

        holder.itemView.setOnClickListener {
            if (Preference(mContext).getPrefString("login_type").toString()
                == "student"
            ) {
                onStudentClick(position)
            } else {
                onTeacherClick(razkun,position)
            }

        }

    }

    fun deleteQuiz(id: String, position: Int, dialog: DialogInterface): Int {
        var tempReturn = 0
        AndroidNetworking.post(API_Endpoint.DELETE_QUIZ)
            .addBodyParameter("quiz_id", id)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.i("fan-delete-quiz", response.toString())
                    if (response.getInt("response_code") == 1) {
                        tempReturn = 1
                        makeToast("Berhasil Menghapus Quiz")
                        listData.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, listData.size)
                        dialog.dismiss()
                    } else {
                        makeToast("Gagal Menghapus Quiz")
                        tempReturn = 2
                        dialog.dismiss()
                    }
                }

                override fun onError(anError: ANError) {
                    tempReturn = 0
                    makeToast("Gagal Menghapus Quiz , Periksa Koneksi Internet Anda Dan Coba Lagi Nanti")
                    Log.i("fan-delete-quiz", anError.toString())
                    dialog.dismiss()
                }

            })
        return tempReturn
    }

    private fun makeToast(text: String) {
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show()
    }

    private fun onStudentClick(position: Int) {
        val cfAlert = CFAlertDialog.Builder(mContext)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
            .setTitle(listData[position].title)
            .setMessage(listData[position].desc)
            .addButton(
                "Buka Google Form", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT,
                CFAlertDialog.CFAlertActionAlignment.START
            ) { dialog, which ->
                val intent = Intent(mContext,WebsiteActivity::class.java)
                intent.putExtra("url",listData[position].gform_link)
                mContext.startActivity(intent)
                dialog.dismiss()
            }
            .addButton(
                "Kembali", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT,
                CFAlertDialog.CFAlertActionAlignment.START
            ) { dialog, which ->
                dialog.dismiss()
            }
        cfAlert.show()
    }

    private fun onTeacherClick(razkun: QuizModel, position: Int) {
        val cfAlert = CFAlertDialog.Builder(mContext)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
            .setTitle(listData[position].title)
            .setMessage(listData[position].desc)
            .addButton(
                "Hapus Quiz",
                -1,
                -1,
                CFAlertDialog.CFAlertActionStyle.DEFAULT,
                CFAlertDialog.CFAlertActionAlignment.START
            ) { dialog, which ->
                deleteQuiz(razkun.id_quiz, position, dialog)
            }
            .addButton(
                "Buka Google Form", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT,
                CFAlertDialog.CFAlertActionAlignment.START
            ) { dialog, which ->
                val intent = Intent(mContext,WebsiteActivity::class.java)
                intent.putExtra("url",listData[position].gform_link)
                mContext.startActivity(intent)
                dialog.dismiss()
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