package cn.lemoe.btconn.ui.terminal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.lemoe.btconn.databinding.FragmentTerminalBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TerminalFragment : Fragment() {

    private var _binding: FragmentTerminalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    internal lateinit var callback: OnHeadlineSelectedListener

    fun setOnHeadlineSelectedListener(callback: OnHeadlineSelectedListener) {
        Log.v("TerminalFragment", "setOnHeadlineSelectedListener")
        this.callback = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnHeadlineSelectedListener {
        fun onSerialMsgReceived(content: String)
    }

    fun onSerialMsgReceived(v: View, content: String) {
        callback.onSerialMsgReceived(content)
    }

    fun terminalAppend(text: String) {
        val textView: TextView = binding.textGallery
        val currentTimeMillis = System.currentTimeMillis()
        val formattedTime = formatTimeToMilliseconds(currentTimeMillis)
        var newText = text.replace("\n", "⤵\n")
        newText = newText.replace("\r", "⤵\r")
        newText = newText.replace(" ", "·")
        textView.append("$formattedTime->$newText⤵\n")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chartsViewModel =
            ViewModelProvider(this).get(TerminalViewModel::class.java)

        _binding = FragmentTerminalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        textView.isSelected = true;
        textView.movementMethod = ScrollingMovementMethod.getInstance()
        chartsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val testButton = binding.buttonSend
        testButton.setOnClickListener {
            val terminalInputBinding = binding.TerminalInput
            val terminalText = terminalInputBinding.text.toString()
            terminalAppend(terminalText)
        }

        return root
    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // 处理接收到的蓝牙消息
            val message = intent.getStringExtra("message")

            if (message != null) {
                terminalAppend(message)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 注册广播接收器
        val intentFilter = IntentFilter("cn.lemoe.btconn.bluetooth.MESSAGE_RECEIVED")
        context?.let { registerReceiver(it, messageReceiver, intentFilter, RECEIVER_EXPORTED) }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun formatTimeToMilliseconds(time: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        return dateFormat.format(Date(time))
    }
}