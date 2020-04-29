package rikka.material.chooser

import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import rikka.core.res.resolveColor
import rikka.material.app.MaterialActivity

open class ChooserActivity : MaterialActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent == null) {
            Log.d("ChooserActivity", "ChooserActivity needs a intent.")
            finish()
            return
        }

        if (savedInstanceState != null) {
            return
        }

        val fragment = ChooserFragment()
        fragment.arguments = intent.getBundleExtra(ChooserFragment.EXTRA_ARGUMENTS)

        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit()
    }

    /*override fun shouldApplyTranslucentSystemBars(): Boolean {
        return true
    }

    override fun onApplyTranslucentSystemBars() {
        super.onApplyTranslucentSystemBars()

        val window = window
        window?.statusBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = true
            }
        }
    }*/
}
