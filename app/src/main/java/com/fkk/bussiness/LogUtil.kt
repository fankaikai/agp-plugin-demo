package com.fkk.bussiness

import android.util.Log

/**
 * @author fan.kaikai
 * @date 2023/9/9
 *
 **/
object LogUtil {
    @JvmStatic
    fun w(msg: String?) {
        Log.w("CustomPluginLog", "$msg")
    }
}