package com.example.sms_receiver_channel

import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat;
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.BroadcastReceiver;
import android.provider.Telephony
import android.Manifest


class MainActivity : FlutterActivity() {
    private val CHANNEL: String = "sms.receiver.channel"
    private val REQUEST_CODE: Int = 1

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, result ->
            if (call.method == "receive_sms") {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.RECEIVE_SMS),
                        REQUEST_CODE
                    )
                } else {
                    val smsReceiver = object : BroadcastReceiver() {
                        override fun onReceive(p0: Context?, p1: Intent?) {
                            for (sms in Telephony.Sms.Intents.getMessagesFromIntent(p1)) {
                                result.success(sms.displayMessageBody)
                                unregisterReceiver(this)
                            }
                        }
                    }
                    registerReceiver(
                        smsReceiver,
                        IntentFilter("android.provider.Telephony.SMS_RECEIVED")
                    )
                }

            } else {
                result.notImplemented()
            }
        }
    }
}