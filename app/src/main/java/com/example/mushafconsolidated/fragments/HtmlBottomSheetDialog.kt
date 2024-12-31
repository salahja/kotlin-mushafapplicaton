package com.example.mushafconsolidated.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.databinding.VfourExpandableNewactivityShowAyahsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HtmlBottomSheetDialog : BottomSheetDialogFragment() {
  private var htmlContent: String? = null
  private var title: String? = null

  private lateinit var bottomsheetHtmlBehaviour: BottomSheetBehavior<RelativeLayout>
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val bundle = this.arguments
    htmlContent = bundle!!.getString("htmlContent")

    val view = inflater.inflate(R.layout.dialog_html, container, false)

    val webView = view.findViewById<WebView>(R.id.webView)
    val closeButton = view.findViewById<Button>(R.id.closeButton)
    val bottomsheetHtml: RelativeLayout =view.findViewById(R.id.htmlbottomsheet)
    bottomsheetHtmlBehaviour = BottomSheetBehavior.from(bottomsheetHtml)
    bottomsheetHtmlBehaviour.state = BottomSheetBehavior.STATE_HALF_EXPANDED


    webView.settings.javaScriptEnabled = true
    htmlContent?.let { webView.loadDataWithBaseURL(null, it, "text/html", "utf-8", null) }

    closeButton.setOnClickListener {
      dismiss()
    }
    return view
  }

  companion object {
    fun newInstance(htmlContent: String, title: String? = null): HtmlBottomSheetDialog {
      val fragment = HtmlBottomSheetDialog()
      val args = Bundle()
      args.putString("htmlContent", htmlContent)
      args.putString("title", title)
      fragment.arguments = args
      return fragment
    }
  }
}
