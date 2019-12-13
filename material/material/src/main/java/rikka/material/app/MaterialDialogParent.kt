package rikka.material.app

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.MainThread
import rikka.material.app.BackFragment

interface MaterialDialogParent : BackFragment {

    fun hasDialogOptionsMenu(): Boolean

    fun setHasDialogOptionsMenu(hasMenu: Boolean)

    @MainThread
    fun onCreateDialogOptionsMenu(menu: Menu, inflater: MenuInflater) {
    }

    @MainThread
    fun onPrepareDialogOptionsMenu(menu: Menu) {
    }

    @MainThread
    fun onDialogOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

    @MainThread
    fun onDialogOptionsMenuClosed(menu: Menu) {
    }

    @MainThread
    fun openDialogOptionsMenu() {
    }

    @MainThread
    fun closeDialogOptionsMenu() {
    }

    @MainThread
    fun invalidateDialogOptionsMenu() {
    }
}