package rikka.material.app

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.MaterialViewInflater
import androidx.appcompat.widget.Toolbar
import rikka.material.internal.ThemedAppCompatActivity
import rikka.material.widget.AppBarLayout

open class MaterialActivity : ThemedAppCompatActivity(), AppBarOwner, TranslucentSystemBars {

    private var appBar: AppBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installViewFactory(LayoutInflater.from(this))
        super.onCreate(savedInstanceState)

        if (shouldApplyTranslucentSystemBars()) {
            onApplyTranslucentSystemBars()
        }
    }

    open fun installViewFactory(layoutInflater: LayoutInflater) {
        layoutInflater.factory2 = MaterialViewInflater(this)
    }

    override fun getAppBar(): AppBar? {
        return appBar
    }

    override fun setAppBar(appBarLayout: AppBarLayout, toolbar: Toolbar) {
        super.setSupportActionBar(toolbar)
        appBar = AppBar(supportActionBar!!, appBarLayout)
    }
}