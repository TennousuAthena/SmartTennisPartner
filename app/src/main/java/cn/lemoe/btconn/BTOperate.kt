package cn.lemoe.btconn

import android.content.Context
import world.shanya.serialport.SerialPort
import world.shanya.serialport.SerialPortBuilder

class BTConn(private val context: Context) {

    private val serialPort: SerialPort = SerialPortBuilder
        .isDebug(true)
        .autoHexStringToString(true)
        .isIgnoreNoNameDevice(true)
        .setAutoReconnectAtIntervals(true, 10000)
        .build(context)

    fun openDiscoveryActivity() {
        serialPort.openDiscoveryActivity()
    }
}
