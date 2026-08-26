package com.plugin.safe.area.insets.css

import android.app.Activity
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin
import app.tauri.plugin.Invoke
import android.os.Build
import android.webkit.WebView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log

@InvokeArg
class PingArgs {
    var value: String? = null
}

@TauriPlugin
class InsetPlugin(private val activity: Activity) : Plugin(activity) {

    override fun load(webView: WebView) {
        super.load(webView)
        val rootView = activity.window.decorView

// On écoute les changements de WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

            if (imeHeight > 0) {
                trigger("keyboard_shown", JSObject())
            } else {
                trigger("keyboard_hidden", JSObject())
            }

            // New edge-insets event
            val systemInsets = insets.getInsets(
                WindowInsetsCompat.Type.statusBars() or
                        WindowInsetsCompat.Type.navigationBars() or
                        WindowInsetsCompat.Type.displayCutout()
            )

            val density = activity.resources.displayMetrics.density

            val result = JSObject()
            result.put("top", systemInsets.top / density)
            result.put("right", systemInsets.right / density)
            result.put("bottom", systemInsets.bottom / density)
            result.put("left", systemInsets.left / density)

            trigger("edge-insets-changed", result)

            insets

        }
    }

    @Command
    fun getTopInset(invoke: Invoke) {
        val topInset = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.decorView.rootWindowInsets?.getInsets(android.view.WindowInsets.Type.statusBars())?.top ?: 0
        } else {
            0
        }
        val topInsetDIP = toDIPFromPixel(topInset.toFloat()).toDouble()
        val result = JSObject()
        result.put("inset", topInsetDIP)
        invoke.resolve(result)
    }

    @Command
    fun getBottomInset(invoke: Invoke) {
        val bottomInset = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.decorView.rootWindowInsets?.getInsets(android.view.WindowInsets.Type.navigationBars())?.bottom
                ?: 0
        } else {
            0
        }
        val bottomInsetDIP = toDIPFromPixel(bottomInset.toFloat()).toDouble()
        val result = JSObject()
        result.put("inset", bottomInsetDIP)
        invoke.resolve(result)
    }

    @Command
    fun getEdgeInsets(invoke: Invoke) {
        val insets = ViewCompat.getRootWindowInsets(activity.window.decorView)

        if (insets == null) {
            invoke.resolve(JSObject().apply {
                put("top", 0.0)
                put("right", 0.0)
                put("bottom", 0.0)
                put("left", 0.0)
            })
            return
        }

        val systemInsets = insets.getInsets(
            WindowInsetsCompat.Type.statusBars() or
                    WindowInsetsCompat.Type.navigationBars() or
                    WindowInsetsCompat.Type.displayCutout()
        )

        val density = activity.resources.displayMetrics.density

        val result = JSObject()
        result.put("top", systemInsets.top / density)
        result.put("right", systemInsets.right / density)
        result.put("bottom", systemInsets.bottom / density)
        result.put("left", systemInsets.left / density)

        invoke.resolve(result)
    }

    private fun toDIPFromPixel(pixels: Float): Float {
        val density = activity.resources.displayMetrics.density

        return pixels / density
    }
}
