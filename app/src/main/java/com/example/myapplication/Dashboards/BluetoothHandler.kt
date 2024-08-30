package com.example.myapplication.Dashboards

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

class BluetoothHandler(private val context: Context, private val deviceAddress: String) {

    private val btAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var btSocket: BluetoothSocket
    private lateinit var inputStream: InputStream

    private val hc05UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    @SuppressLint("MissingPermission")
    fun connect(): Boolean {
        return try {
            val hc05: BluetoothDevice = btAdapter.getRemoteDevice(deviceAddress)
            btSocket = hc05.createRfcommSocketToServiceRecord(hc05UUID)
            btSocket.connect()
            inputStream = btSocket.inputStream
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun readData(onDataReceived: (String) -> Unit) {
        val buffer = ByteArray(256)
        var bytes: Int
        var completeData = ""

        Thread {
            while (true) {
                try {
                    bytes = inputStream.read(buffer)
                    val data = String(buffer, 0, bytes)

                    completeData += data


                    val startIndex = completeData.indexOf("<DATA>")
                    val endIndex = completeData.indexOf("</DATA>")

                    if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {

                        val fullMessage = completeData.substring(startIndex + "<DATA>".length, endIndex)


                        completeData = completeData.substring(endIndex + "</DATA>".length)


                        onDataReceived(fullMessage)


                        writeToFile(fullMessage)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    break
                }
            }
        }.start()
    }
    private fun writeToFile(data: String) {
        val fileName = "bluetooth_data1.txt"
        val file = File(context.filesDir, fileName)

        try {
            FileOutputStream(file, true).use { fos ->
                fos.write((data).toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun closeConnection() {
        try {
            btSocket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
