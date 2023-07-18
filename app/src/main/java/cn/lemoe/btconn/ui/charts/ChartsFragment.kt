package cn.lemoe.btconn.ui.charts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.fragment.app.Fragment
import cn.lemoe.btconn.databinding.FragmentChartsBinding

class ChartsFragment : Fragment() {

    private var _binding: FragmentChartsBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("SetJavaScriptEnabled")
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

        // 加载Echarts图表页面
        webView.loadUrl("file:///android_asset/chart.html")


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}