package rikka.design.chooser.app

import android.os.Build
import android.os.Bundle
import android.util.Log
import rikka.design.app.MaterialActivity

class ChooserActivity : MaterialActivity() {

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

    override fun onApplyTranslucentSystemBars() {
        val window = window ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = true
        }
    }
}
