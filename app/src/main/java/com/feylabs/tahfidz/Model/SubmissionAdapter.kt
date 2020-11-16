package com.feylabs.tahfidz.Model

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.DownloadListener
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SubBottomSheet
import com.feylabs.tahfidz.Util.URL
import com.feylabs.tahfidz.View.SubmissionDetailStudent
import kotlinx.android.synthetic.main.layout_detail_submission.view.*
import kotlinx.android.synthetic.main.list_submission.view.*
import kotlinx.android.synthetic.main.list_submission.view.sub_start_end

class SubmissionAdapter : RecyclerView.Adapter<SubmissionAdapter.SubmissionHolder>() {

    var listData = mutableListOf<SubmissionModel>()


    fun setData(listSetter: MutableList<SubmissionModel>) {
        listData.clear()
        listData.addAll(listSetter)
        notifyDataSetChanged()
    }

    inner class SubmissionHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sub_id = itemView.sub_id

        //        var sub_student_ID
        var sub_date = itemView.sub_date
        var sub_student_name = itemView.sub_student_name
        var sub_status = itemView.sub_status
        var sub_start_end = itemView.sub_start_end
        var btn_sub_detail = itemView.btnSeeDetail

        val submissionModel = listData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmissionHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_submission, parent, false)
        return SubmissionHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: SubmissionHolder, position: Int) {
        holder.sub_student_name.text = listData[position].studentName
        listData[position].id="SE"+listData[position].id
        holder.sub_id.text = listData[position].id
        holder.sub_date.text = listData[position].date
        var status = listData[position].status
        var checkStatus =0
        when (status) {
            "0" -> {
                checkStatus=0
                status = "Menunggu Dinilai"
                listData[position].status=status
            }
            "1" -> {
                checkStatus=1
                status = "Sudah Dinilai"
                listData[position].status=status
            }
        }
        holder.sub_status.text = status
        holder.sub_start_end.text = listData[position].start + "-\n" + listData[position].end

        val intent = Intent(holder.itemView.context, SubmissionDetailStudent::class.java)
        holder.itemView.setOnClickListener { v ->
            val activity = holder.itemView.context as Activity
            val subDetailBottomSheet = SubBottomSheet(activity)
            val subDetId = subDetailBottomSheet.sub_det_id
            val subStartEnd = subDetailBottomSheet.sub_start_end
            val subStartStatus = subDetailBottomSheet.sub_det_status
            val subDetScoreSmall = subDetailBottomSheet.sub_det_score_small
            val subDetScoreBig = subDetailBottomSheet.sub_det_score_big
            val subDetMp3 = subDetailBottomSheet.sub_det_mp3View

            when(checkStatus){
                0 ->{
                    subDetScoreBig.textSize=12f
                    subDetScoreSmall.text=holder.itemView.context.getString(R.string.not_graded)
                    subDetScoreBig.text=holder.itemView.context.getString(R.string.not_graded)
                }
                1->{
                    subDetScoreBig.textSize=60f
                }
            }

            var mp3Path = listData[position].audio
            mp3Path = mp3Path.replaceFirst(".", "")
            mp3Path = mp3Path.replaceFirst("/", "")
            mp3Path = mp3Path.replace(" ", "%20")

            subDetId.text=listData[position].id
            subStartStatus.text=listData[position].status
            subStartEnd.text="${listData[position].start} - ${listData[position].end}"
            subDetScoreBig.text=listData[position].score
            subDetScoreSmall.text=listData[position].score
            subDetailBottomSheet.show()

            subDetMp3.settings.javaScriptEnabled = true
            subDetMp3.setDownloadListener(object : DownloadListener {
                override fun onDownloadStart(
                    url: String?, userAgent: String?,
                    contentDisposition: String?, mimetype: String?,
                    contentLength: Long
                ) {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    holder.itemView.context.startActivity(i)
                }
            })
            subDetMp3.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    view?.loadUrl(URL.MP3_MOBILE+mp3Path)
                    return true
                }
            }
            subDetMp3.loadUrl(URL.MP3_MOBILE+mp3Path)
            Log.i("MP3View URL", URL.MP3_MOBILE+mp3Path)
        }

    }
}