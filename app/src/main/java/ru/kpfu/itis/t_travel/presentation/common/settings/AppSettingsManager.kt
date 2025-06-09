package ru.kpfu.itis.t_travel.presentation.common.settings

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
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
    private var currentLocale: Locale? = null

    fun initializeAppSettings() {
        applyTheme()
        applyLanguage()
        observeAppLifecycle()
    }

    private fun observeAppLifecycle() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    currentLocale?.let { applyLanguage(it) }
                }
            }
        )
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
        Log.i("AppSettingsManager", "$language")
        val locale = language.locale
        Log.i("AppSettingsManager", "$locale")
        currentLocale = locale
        applyLanguage(locale)
    }

    private fun applyLanguage(locale: Locale) {
        Log.i("AppSettingsManager", "Applying language: $locale")
        Locale.setDefault(locale)
        val appConfig = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            appConfig.setLocales(android.os.LocaleList(locale))
        } else {
            @Suppress("DEPRECATION")
            appConfig.locale = locale
        }
        context.resources.updateConfiguration(appConfig, context.resources.displayMetrics)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
        if (context is Application) {
            context.registerActivityLifecycleCallbacks(object :
                Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    updateActivityLocale(activity, locale)
                    context.unregisterActivityLifecycleCallbacks(this) // <--- ВАЖНО!
                }
                override fun onActivityStarted(activity: Activity) {}
                override fun onActivityResumed(activity: Activity) {}
                override fun onActivityPaused(activity: Activity) {}
                override fun onActivityStopped(activity: Activity) {}
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
                override fun onActivityDestroyed(activity: Activity) {}
            })
        }
        Log.i("AppSettingsManager", "Current locale: ${Locale.getDefault()}")
        Log.i("AppSettingsManager", "Configuration locale: ${context.resources.configuration.locale}")
    }

    private fun updateActivityLocale(activity: Activity, locale: Locale) {
        val activityConfig = Configuration(activity.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activityConfig.setLocales(android.os.LocaleList(locale))
        } else {
            @Suppress("DEPRECATION")
            activityConfig.locale = locale
        }
        activity.resources.updateConfiguration(activityConfig, activity.resources.displayMetrics)
    }

    fun recreateActivity(activity: Activity) {
        currentLocale?.let { updateActivityLocale(activity, it) }
    }
}

