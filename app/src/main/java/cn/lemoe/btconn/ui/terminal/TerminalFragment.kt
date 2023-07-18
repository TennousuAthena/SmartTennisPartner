package cn.lemoe.btconn.ui.terminal

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
            val currentTimeMillis = System.currentTimeMillis()
            val formattedTime = formatTimeToMilliseconds(currentTimeMillis)
            val terminalInputBinding = binding.TerminalInput
            val terminalText = terminalInputBinding.text.toString()
            textView.append("$formattedTime->$terminalText⤵\n")
        }

        return root
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