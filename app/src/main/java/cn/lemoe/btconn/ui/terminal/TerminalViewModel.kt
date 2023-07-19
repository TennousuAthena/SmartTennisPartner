package cn.lemoe.btconn.ui.terminal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TerminalViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Waiting·for·dataflow...⤵\n"
    }
    val text: LiveData<String> = _text
}