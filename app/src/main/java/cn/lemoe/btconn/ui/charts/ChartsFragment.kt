package cn.lemoe.btconn.ui.charts

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import cn.lemoe.btconn.R
import cn.lemoe.btconn.databinding.FragmentChartsBinding


class ChartsFragment : Fragment() {

    private var _binding: FragmentChartsBinding? = null

    private val binding get() = _binding!!

    private lateinit var webView: WebView
    private var isWebViewAvailable:Boolean = false

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val webView: WebView = binding.webviewCharts

        // 启用JavaScript支持
        val settings: WebSettings = webView.settings

        settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE;
        // 加载Echarts图表页面
        webView.loadUrl("file:///android_asset/html/chart.html")

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                // 加载完成
                webView.visibility = View.VISIBLE
                binding.progressWebView.visibility = View.GONE
                isWebViewAvailable = true

                val stringVal = getString(R.string.webview_waiting_for_connection)
                webView.evaluateJavascript("document.querySelector(\"#json_area\").innerText = '$stringVal'", null)
            }
        }
        webView.setOnTouchListener { _, event ->
            // 禁止左右滑动
            if (event.pointerCount == 1) {
                when (event.action) {
                    MotionEvent.ACTION_MOVE -> true
                    else -> false
                }
            } else {
                false
            }
        }


        return root
    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                // 处理接收到的蓝牙消息
                val message = intent.getStringExtra("message")
                if (message != null) {
                    val jsCode = "document.querySelector(\"#json_area\").innerText = '$message'"
                    val newWebView : WebView = binding.webviewCharts
                    newWebView.evaluateJavascript(jsCode, null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 注册广播接收器
        val intentFilter = IntentFilter("cn.lemoe.btconn.bluetooth.MESSAGE_RECEIVED")
        context?.let {
            ContextCompat.registerReceiver(
                it,
                messageReceiver,
                intentFilter,
                ContextCompat.RECEIVER_EXPORTED
            )
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        context?.unregisterReceiver(messageReceiver)
    }
}