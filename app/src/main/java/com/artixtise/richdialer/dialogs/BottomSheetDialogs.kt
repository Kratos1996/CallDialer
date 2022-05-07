package com.artixtise.richdialer.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.artixtise.richdialer.R
import com.google.android.material.bottomsheet.BottomSheetDialog


object BottomSheetDialogs {

    fun getDialpad(context: Context): Dialog {
        val dialog = BottomSheetDialog(context, R.style.DialogFragmentTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialpad)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
    fun selectSimDialog(context: Context): Dialog {
        val dialog = BottomSheetDialog(context, R.style.DialogFragmentTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.sim_selection_dialog)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    fun getRichCallDialog(context: Context): Dialog {
        val dialog = BottomSheetDialog(context, R.style.DialogFragmentTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.rich_call_options)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

}