package ru.kpfu.itis.t_travel.presentation.common

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingsManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val themeManager: ThemeManager,
    private val languageManager: LanguageManager
) {
    fun initializeAppSettings() {
        applyTheme()
        applyLanguage()
    }

    private fun applyTheme() {
        val theme = AppTheme.valueOf(themeManager.getTheme())
        when (theme) {
            AppTheme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            AppTheme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            AppTheme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun applyLanguage() {
        Log.i("AppSettingsManager", "${Locale("ru")} ${Locale.ENGLISH}")
        Log.i("", "applyLanguage")
        val language = AppLanguage.valueOf(languageManager.getLanguage())
        Log.i("AppSettingsManager","$language")
        val locale = language.locale
        Log.i("AppSettingsManager","$locale")
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }
        val resources = context.resources
        resources.updateConfiguration(config, resources.displayMetrics)
        if (context is Activity) {
            recreateActivity(context)
        } else if (context is Application) {
            context.registerActivityLifecycleCallbacks(object :
                Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    activity.recreate()
                }
                override fun onActivityStarted(activity: Activity) {}
                override fun onActivityResumed(activity: Activity) {
                    activity.recreate()
                    context.unregisterActivityLifecycleCallbacks(this)
                }

                override fun onActivityPaused(activity: Activity) {}
                override fun onActivityStopped(activity: Activity) {}
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
                override fun onActivityDestroyed(activity: Activity) {}
            })
        }
        Log.i("AppSettingsManager", "Current locale: ${Locale.getDefault()}")
        Log.i("AppSettingsManager", "Configuration locale: ${context.resources.configuration.locale}")
    }

    fun recreateActivity(activity: Activity) {
        activity.recreate()
    }
}