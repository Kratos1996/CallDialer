package com.artixtise.richdialer.presentation.managers

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import com.artixtise.richdialer.presentation.services.TService


class DeviceAdminManger : DeviceAdminReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        /*context.stopService(Intent(context, TService::class.java))
        val myIntent = Intent(context, TService::class.java)
        context.startService(myIntent)*/
    }

    override fun onEnabled(context: Context, intent: Intent) {}
    override fun onDisabled(context: Context, intent: Intent) {}
}