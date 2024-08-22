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
    fun refreshConnection(){
        if(btSocket.isConnected){
            try {
                btSocket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

                    // Accumulate the received data
                    completeData += data

                    // Check if we have received a complete message
                    val startIndex = completeData.indexOf("<DATA>")
                    val endIndex = completeData.indexOf("</DATA>")

                    if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                        // Extract the full message
                        val fullMessage = completeData.substring(startIndex + "<DATA>".length, endIndex)

                        // Clear the completeData buffer up to the end of the current message
                        completeData = completeData.substring(endIndex + "</DATA>".length)

                        // Pass the complete message to the callback
                        onDataReceived(fullMessage)

                        // Optionally write the full message to a file
                        writeToFile(fullMessage)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    break
                }
            }
        }.start()
    }
    fun copyFileToExternalStorage(context: Context, fileName: String) {
        val internalFile = File(context.filesDir, fileName)
        val externalFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)

        try {
            FileInputStream(internalFile).use { input ->
                FileOutputStream(externalFile).use { output ->
                    input.copyTo(output)
                }
            }
            println("File copied to: ${externalFile.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
