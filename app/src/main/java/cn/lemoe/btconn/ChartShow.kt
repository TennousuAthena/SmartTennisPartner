package cn.lemoe.btconn

import android.content.Context
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView

class ChartShow(private val context: Context, private val webView: WebView) {
    init {
        // 启用JavaScript支持
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // 设置WebChromeClient以支持JavaScript的alert、confirm和prompt对话框
        webView.webChromeClient = WebChromeClient()
    }

    fun loadChart() {
        // 加载本地的HTML文件
        webView.loadUrl("file:///asset/chart.html")
    }

    fun updateChart(newData: String) {
        webView.post {
            // 在WebView的JavaScript代码中执行数据传递操作
            webView.evaluateJavascript("updateChart('$newData');", null)
        }
    }
}
