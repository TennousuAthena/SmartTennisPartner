package cn.lemoe.btconn.ui.slideshow

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.lemoe.btconn.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun getVer(context: Context): String {
        val packageManager = context.packageManager
        val packageName = context.packageName

        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val appVersion = packageInfo.versionName
            val appVersionCode = packageInfo.versionCode

            return "$appVersion ($appVersionCode)"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "Unknown"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val appVer = getVer(requireContext());
        val textView: TextView = binding.textSlideshow
        textView.text = "BTConn\n\n" +
                "Ver. $appVer\n"+
                "This app is used to connect to a Bluetooth device and send data to it.\n" +
                "The app is developed by Lemoe.cn\n" +
                "The source code is available at https://github.com/TennousuAthena/SmartTennisPartner\n"+
                "The app is licensed under the MIT License:\n\n"+"Copyright 2023 TennousuAthena.\n" +
                "\n" +
                "Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:\n" +
                "\n" +
                "The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.\n" +
                "\n" +
                "THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE."
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}