package com.artixtise.richdialer.utility

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.artixtise.richdialer.R
import com.artixtise.richdialer.databinding.ItemHomeTabBinding
import com.artixtise.richdialer.databinding.ItemRichCallTabBinding

object Utility {

    const val LOWER_ALPHA = 0.25f
    const val MEDIUM_ALPHA = 0.5f
    const val HIGHER_ALPHA = 0.75f

    private val tabIconsActive = intArrayOf(
        R.drawable.fav_selector,
        R.drawable.recent_checked,
        R.drawable.contact_selector,
        R.drawable.history_selector,
        R.drawable.confrence_call_selector
    )
    private val tabIconsUnActive = intArrayOf(
        R.drawable.fav_selector,
        R.drawable.recent_checked,
        R.drawable.contact_selector,
        R.drawable.history_selector,
        R.drawable.confrence_call_selector
    )

    fun getSelectedCustomTab(context: Context, position: Int,inflater : LayoutInflater): View {
        val binding:ItemHomeTabBinding = DataBindingUtil.inflate(inflater,R.layout.item_home_tab,null,false)
        when (position) {
            0 -> {
                binding.ivIcon.setImageDrawable(context.resources.getDrawable(tabIconsActive[0]))
            }
            1 -> {
                binding.ivIcon.setImageDrawable(context.resources.getDrawable(tabIconsActive[1]))
            }
            2 -> {
                binding.ivIcon.setImageDrawable(context.resources.getDrawable(tabIconsActive[2]))
            }
            3 -> {
                binding.ivIcon.setImageDrawable(context.resources.getDrawable(tabIconsActive[3]))
            }
            4 -> {
                binding.ivIcon.setImageDrawable(context.resources.getDrawable(tabIconsActive[4]))
            }
        }
        return binding.root
    }

    fun getCustomTab(context: Context, position: Int,inflater : LayoutInflater): View {
        val binding:ItemHomeTabBinding = DataBindingUtil.inflate(inflater,R.layout.item_home_tab,null,false)
        when (position) {
            0 -> {
                binding.ivIcon.setImageDrawable(context.resources.getDrawable(tabIconsUnActive[0]))
            }
            1 -> {
                binding.ivIcon.setImageDrawable(context.resources.getDrawable(tabIconsUnActive[1]))
            }
            2 -> {
                binding.ivIcon.setImageDrawable(context.resources.getDrawable(tabIconsUnActive[2]))
            }
            3 -> {
                binding.ivIcon.setImageDrawable(context.resources.getDrawable(tabIconsUnActive[3]))
            }
            4 -> {
                binding.ivIcon.setImageDrawable(context.resources.getDrawable(tabIconsUnActive[4]))
            }
        }
        return binding.root
    }

    fun getSelectedRichCallCustomTab(context: Context, position: Int,inflater : LayoutInflater): View {
        val binding:ItemRichCallTabBinding = DataBindingUtil.inflate(inflater,R.layout.item_rich_call_tab,null,false)
        when (position) {
            0 -> {
                binding.tvText.text = "Emoji"
                binding.tvText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }
            1 -> {
                binding.tvText.text = "GIF"
                binding.tvText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }
            2 -> {
                binding.tvText.text = "Gallery"
                binding.tvText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }
            3 -> {
                binding.tvText.text = "Text"
                binding.tvText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }
            4 -> {
                binding.tvText.text = "Location"
                binding.tvText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }
        }
        return binding.root
    }

    fun getRichCallCustomTab(context: Context, position: Int,inflater : LayoutInflater): View {
        val binding:ItemRichCallTabBinding = DataBindingUtil.inflate(inflater,R.layout.item_rich_call_tab,null,false)
        when (position) {
            0 -> {
                binding.tvText.text = "Emoji"
                binding.tvText.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            1 -> {
                binding.tvText.text = "GIF"
                binding.tvText.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            2 -> {
                binding.tvText.text = "Gallery"
                binding.tvText.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            3 -> {
                binding.tvText.text = "Text"
                binding.tvText.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            4 -> {
                binding.tvText.text = "Location"
                binding.tvText.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
        return binding.root
    }

    //constants
    const val GROUP_ID="groupId"

    const val RECEIVER_ID = "receiverUserId"
    const val SENDER_ID = "senderUserId"
    const val RECEIVER_NAME = "receiverName"
    const val SENDER_NAME = "senderName"
    const val RECEIVER_DEVEICE_ID ="receiverDeveiceId"
    const val EMOJI ="emoji"
    const val IMAGE ="image"
    const val GIF ="gif"
    const val TEXT_MSG ="textMsg"
    const val LAT = "lat"
    const val LNG = "lng"
    const val IS_RICH_CALL = "isRichcall"
    const val SIM_NUMBER = "simNumber"
    const val RECEIVER_NUMBER = "receiverNumber"
    const val SENDER_NUMBER = "senderNumber"
    const val INSTAGRAM_ID ="instagramId"
    const val FACEBOOK_ID ="facebookId"
    const val TWITTER_ID ="twitterID"
    const val LINKED_ID = "linkedID"
}