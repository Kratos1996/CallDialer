package com.artixtise.richdialer.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.artixtise.richdialer.R
import com.artixtise.richdialer.database.prefrence.SharedPre

abstract class BaseFragment(View:Int) : Fragment(View){

    abstract fun WorkStation();


    open fun onBackPressed(){
       requireActivity().onBackPressed()
    }

    open fun showCustomAlert(msg: String?, v: View?) {
       val snackBar:Snackbar = Snackbar.make(v!!, msg!!, Snackbar.LENGTH_LONG)
        snackBar.setActionTextColor(Color.BLUE)
        val snackBarView: View = snackBar.getView()
        val textView = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackBar.show()
    }


    fun getSharedPre():SharedPre{
        return SharedPre.getInstance(requireContext())!!
    }


}