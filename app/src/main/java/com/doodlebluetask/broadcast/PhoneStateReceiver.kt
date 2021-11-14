package com.doodlebluetask.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.doodlebluetask.R


class PhoneStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        println("Receiver start")
        val state = intent.extras!!.getString(TelephonyManager.EXTRA_STATE)
        var incomingNumber = intent.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
        if (state == TelephonyManager.EXTRA_STATE_RINGING) {
            val preferences = context.getSharedPreferences("login", 0)
            val strMobile = preferences.getString("mobile", null)
            val strName = preferences.getString("name", null)

            if (!strMobile.isNullOrEmpty() && PhoneNumberUtils.compare(
                    context,
                    strMobile.trim(),
                    incomingNumber
                )
            ) {
                Log.i("mobile", strMobile.toString())
                Log.i("mobile1", incomingNumber.toString())
                //start activity which has dialog


                Toast.makeText(context,
                    HtmlCompat.fromHtml("<background color='black'><font color='red'>Name: $strName Mobile: $incomingNumber</font></background>", HtmlCompat.FROM_HTML_MODE_LEGACY),
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}