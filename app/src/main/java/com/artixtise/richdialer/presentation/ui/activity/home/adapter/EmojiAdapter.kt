package com.artixtise.richdialer.presentation.ui.activity.home.adapter

import android.content.Context
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseAdapter
import com.artixtise.richdialer.base.BaseViewHolder
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.databinding.ItemEmojiBinding
import com.artixtise.richdialer.presentation.ui.activity.home.EmojiInterface

class EmojiAdapter constructor(context: Context, val emojiData: EmojiInterface) :
    BaseAdapter<Int, ItemEmojiBinding>(context, R.layout.item_emoji) {

    private var emojies = ArrayList<Int>()

    override fun onViewHolderBind(
        viewHolder: BaseViewHolder<ItemEmojiBinding>,
        binding: ItemEmojiBinding,
        position: Int,
        emoji: Int,
        list: ArrayList<Int>
    ) {

        binding.apply {
            tvEmoji.text = getEmoji(emoji)
            tvEmoji.setOnClickListener {
                emojiData.onEmojiSelect(emoji)
            }
        }
    }

    fun getEmoji(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

    override fun onClickItemListner( binding: ItemEmojiBinding,data: Int, position: Int) {
        //Toast.makeText(context, "Click on item : " +( position + 1), Toast.LENGTH_SHORT).show()
    }
}