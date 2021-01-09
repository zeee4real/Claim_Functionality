package com.zayd.claimfunctionality.utils

import android.view.Window
import androidx.appcompat.widget.Toolbar

object AppUtils {

    fun dialogCustomise(window: Window?) {
        window?.let {
            it.setBackgroundDrawableResource(android.R.color.transparent)
            it.setLayout(
                Toolbar.LayoutParams.MATCH_PARENT,
                Toolbar.LayoutParams.WRAP_CONTENT
            )
        }
    }
}