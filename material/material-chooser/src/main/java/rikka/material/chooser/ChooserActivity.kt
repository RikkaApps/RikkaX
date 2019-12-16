package rikka.material.chooser

import android.os.Bundle
import android.util.Log
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
}
