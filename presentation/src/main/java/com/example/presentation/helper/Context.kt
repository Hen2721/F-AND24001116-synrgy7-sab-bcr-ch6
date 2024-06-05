package com.example.presentation.helper

import android.app.Activity
import android.content.Intent
import java.util.Locale

fun Activity.applyLanguage(language: String, intent: Intent) {
    val myLocale = Locale(language)
    val res = resources
    val dm = res.displayMetrics
    val conf = res.configuration
    conf.locale = myLocale
    res.updateConfiguration(conf, dm)
    startActivity(intent)
    finish()
}