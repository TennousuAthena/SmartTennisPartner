package cn.lemoe.btconn

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import world.shanya.serialport.SerialPort
import world.shanya.serialport.SerialPortBuilder


class BTOperate (private val context: Context) : BroadcastReceiver() {
    //Default constructor
    public constructor() : this(context = null!!) {
        heartbeatHandler.post(heartbeatRunnable)
    }

    private var isBluetoothConnected: Boolean = false
    private var bluetoothDeviceName = ""

    private val logTag = "BTActivity"
    private val heartbeatHandler : Handler = Handler()

    @SuppressLint("MissingPermission")
    private val serialPort: SerialPort = SerialPortBuilder
        .isDebug(true)
        .autoHexStringToString(true)
        .isIgnoreNoNameDevice(true)
        .setAutoReconnectAtIntervals(true, 10000)
        .setSendDataType(SerialPort.SEND_STRING )
        .setReceivedDataCallback { data ->
            Log.d(logTag, data)
            // 广播消息给所有界面
            val broadcastIntent = Intent("cn.lemoe.btconn.bluetooth.MESSAGE_RECEIVED")
            broadcastIntent.putExtra("message", data)
            context.sendBroadcast(broadcastIntent)
        }.setConnectionStatusCallback { status, bluetoothDevice ->
            heartbeatHandler.post(heartbeatRunnable)
            isBluetoothConnected = status
            try {
                if (bluetoothDevice != null) {
                    bluetoothDeviceName = bluetoothDevice.name
                }
            } catch (e: Exception) {
                bluetoothDeviceName = ""
            }
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

    fun isBluetoothConnected(): Boolean {
        return isBluetoothConnected
    }

    fun getBluetoothDeviceName(): String {
        return bluetoothDeviceName
    }

    fun sendHeartbeat() {
        Log.d(logTag, "sendHeartbeat")
        serialPort.sendData("ok")
    }

    // 每5秒发送一次心跳包
    private val heartbeatRunnable = object : Runnable {
        override fun run() {
            if (isBluetoothConnected) {
                sendHeartbeat()
            }
            heartbeatHandler.postDelayed(this, 5000)
        }
    }

}

