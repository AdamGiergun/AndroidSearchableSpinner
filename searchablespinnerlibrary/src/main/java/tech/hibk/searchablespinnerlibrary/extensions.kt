package tech.hibk.searchablespinnerlibrary

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.toActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.toActivity()
        else -> null
    }