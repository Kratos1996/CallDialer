package com.artixtise.richdialer.presentation.ui.activity.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseAdapter
import com.artixtise.richdialer.base.BaseViewHolder
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.databinding.ItemContactsBinding
import java.util.ArrayList

class ContactsAdapter(context:Context,val openProcess:OpenContactDetails) : BaseAdapter<ContactList,ItemContactsBinding>(context = context,R.layout.item_contacts) {

    override fun onViewHolderBind(
        viewHolder: BaseViewHolder<ItemContactsBinding>,
        binding: ItemContactsBinding,
        position: Int,
        data: ContactList,
        list: ArrayList<ContactList>
    ) {
        if (position % 2 == 0) {
            binding.recentBackground.setBackgroundColor(context.resources.getColor(R.color.white))
        } else {
            binding.recentBackground.setBackgroundColor(context.resources.getColor(R.color.light_gray))
        }
        with(binding) {
            nameOfContact.text = data.name
            alphabaticaName.text = data.name
            NumberOfContact.text = data.phoneNumber
        }
    }

    override fun onClickItemListner(get: ContactList, position: Int) {
        openProcess.openContactDetail(get)
    }
    interface OpenContactDetails{
        fun openContactDetail(data:ContactList)
    }
}