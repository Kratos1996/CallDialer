package com.artixtise.richdialer.application

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate


object ThemeHelper {
    const val LIGHT_MODE = "light"
    const val DARK_MODE = "dark"
    const val DEFAULT_MODE = "default"

    fun applyTheme(themePref: String) {
        when (themePref) {
            LIGHT_MODE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            DARK_MODE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }
            }
        }
    }
    fun adjustFontScale(context:Context, scale: Float):Context  {
        val configuration = context.resources.configuration
        // This will apply to all text like -> Your given text size * fontScale
        // This will apply to all text like -> Your given text size * fontScale
        configuration.fontScale = scale
        context.createConfigurationContext(configuration)
        return context.createConfigurationContext(configuration)
    }
}