package cn.lemoe.btconn

import android.content.Context
import android.util.Log
import world.shanya.serialport.SerialPort
import world.shanya.serialport.SerialPortBuilder

class BTConn(private val context: Context) {

    private val TAG = "BTActivity"

    private val serialPort: SerialPort = SerialPortBuilder
        .isDebug(true)
        .autoHexStringToString(true)
        .isIgnoreNoNameDevice(true)
        .setAutoReconnectAtIntervals(true, 10000)
        .setReceivedDataCallback { data ->
            Log.d(TAG, data)
        }
        .build(context)

    fun openDiscoveryActivity() {
        serialPort.openDiscoveryActivity()
    }
}
