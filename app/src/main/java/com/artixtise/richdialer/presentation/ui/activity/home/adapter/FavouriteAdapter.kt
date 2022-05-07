package com.artixtise.richdialer.presentation.ui.activity.home.adapter

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import androidx.core.view.isVisible
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseAdapter
import com.artixtise.richdialer.base.BaseViewHolder
import com.artixtise.richdialer.data.call.CallInterface
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.databinding.ItemFavouriteBinding
import java.util.ArrayList

class FavouriteAdapter constructor(context: Context,val callData: CallInterface) :
    BaseAdapter<ContactList, ItemFavouriteBinding>(context, R.layout.item_favourite) {

    override fun onViewHolderBind(
        viewHolder: BaseViewHolder<ItemFavouriteBinding>,
        binding: ItemFavouriteBinding,
        position: Int,
        data: ContactList,
        list: ArrayList<ContactList>
    ) {
        if (position % 2 == 0) {
            binding.contactBackground.setBackgroundColor(context.resources.getColor(R.color.white))
        } else {
            binding.contactBackground.setBackgroundColor(context.resources.getColor(R.color.light_gray))
        }
        binding.ExpandNow.setOnClickListener {
            if(binding.expandView.isVisible){
                binding.expandView.visibility= View.GONE
            }else{
                binding.expandView.visibility= View.VISIBLE
            }
        }

        with(binding) {
            nameOfContact.text = data.name
          //  alphabaticaName.text = data.name
            phoneOfContact.text = data.phoneNumber
        }
        binding.cloudCallNow.setOnClickListener {
           callData.onCallStart(data.phoneNumber)
        }
        binding.richCall.setOnClickListener {
            if(isNetworkAvailable(context)){
                callData.onRichCallStart(data)
            }

        }
    }

    override fun onClickItemListner(data: ContactList, position: Int) {
        //Toast.makeText(context, "Click on item : " +( position + 1), Toast.LENGTH_SHORT).show()
    }
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

}