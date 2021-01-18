
package com.feylabs.tahfidz.View.Teacher

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.API_Endpoint
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.activity_teacher_dashboard.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*


class TeacherDashboard : AppCompatActivity(), AdvancedWebView.Listener {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_teacher_dashboard)

    webview.setListener(this, this)
    webview.setMixedContentAllowed(false)
    webview.loadUrl(API_Endpoint.MENTOR_DASHBOARD)

  }

  override fun onPageStarted(url: String?, favicon: Bitmap?) {
    anim_loading.visibility = View.VISIBLE
  }

  override fun onPageFinished(url: String?) {
    anim_loading.visibility = View.GONE
  }

  override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
//        TODO("Not yet implemented")
  }

  override fun onDownloadRequested(
    url: String?,
    suggestedFilename: String?,
    mimeType: String?,
    contentLength: Long,
    contentDisposition: String?,
    userAgent: String?
  ) {
//        TODO("Not yet implemented")
  }

  override fun onExternalPageRequest(url: String?) {
//        TODO("Not yet implemented")
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    intent: Intent?
  ) {
    super.onActivityResult(requestCode, resultCode, intent)
    webview.onActivityResult(requestCode, resultCode, intent)
  }


}