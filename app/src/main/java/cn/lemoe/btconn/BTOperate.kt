package cn.lemoe.btconn

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import world.shanya.serialport.SerialPort
import world.shanya.serialport.SerialPortBuilder


class BTOperate (private val context: Context) : BroadcastReceiver() {

    private val TAG = "BTActivity"

    private val serialPort: SerialPort = SerialPortBuilder
        .isDebug(true)
        .autoHexStringToString(true)
        .isIgnoreNoNameDevice(true)
        .setAutoReconnectAtIntervals(true, 10000)
        .setReceivedDataCallback { data ->
            Log.d(TAG, data)
            // 广播消息给所有界面
            val broadcastIntent = Intent("cn.lemoe.btconn.bluetooth.MESSAGE_RECEIVED")
            broadcastIntent.putExtra("message", data)
            context.sendBroadcast(broadcastIntent)
        }
        .build(context)

    override fun onReceive(context: Context, intent: Intent) {
        // 在这里处理接收到的蓝牙消息
        val message = intent.getStringExtra("message")

        // 广播消息给所有界面
        val broadcastIntent = Intent("cn.lemoe.btconn.bluetooth.MESSAGE_RECEIVED")
        broadcastIntent.putExtra("serialMessage", message)
        context.sendBroadcast(broadcastIntent)
    }

    fun openDiscoveryActivity() {
        serialPort.openDiscoveryActivity()
    }
}
