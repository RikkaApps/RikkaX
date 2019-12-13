package rikka.material.view.menu

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.ActionProvider
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.MenuItem.OnMenuItemClickListener
import android.view.View

open class MenuItemImpl : MenuItem {

    override fun getItemId() = 0

    override fun getGroupId() = 0

    override fun getOrder() = 0

    override fun setTitle(title: CharSequence) = this

    override fun setTitle(title: Int) = this

    override fun getTitle() = null

    override fun setTitleCondensed(title: CharSequence) = this

    override fun getTitleCondensed() = null

    override fun setIcon(icon: Drawable) = this

    override fun setIcon(iconRes: Int) = this

    override fun getIcon() = null

    override fun setIntent(intent: Intent) = this

    override fun getIntent() = null

    override fun setShortcut(numericChar: Char, alphaChar: Char) = this

    override fun setNumericShortcut(numericChar: Char) = this

    override fun getNumericShortcut() = 0.toChar()

    override fun setAlphabeticShortcut(alphaChar: Char) = this

    override fun getAlphabeticShortcut() = 0.toChar()

    override fun setCheckable(checkable: Boolean) = this

    override fun isCheckable() = false

    override fun setChecked(checked: Boolean) = this

    override fun isChecked() = false

    override fun setVisible(visible: Boolean) = this

    override fun isVisible() = false

    override fun setEnabled(enabled: Boolean) = this

    override fun isEnabled() = false

    override fun hasSubMenu() = false

    override fun getSubMenu() = null

    override fun setOnMenuItemClickListener(menuItemClickListener: OnMenuItemClickListener) = this

    override fun getMenuInfo() = null

    override fun setShowAsAction(actionEnum: Int) {}

    override fun setShowAsActionFlags(actionEnum: Int) = this

    override fun setActionView(view: View) = this

    override fun setActionView(resId: Int) = this

    override fun getActionView() = null

    override fun setActionProvider(actionProvider: ActionProvider) = this

    override fun getActionProvider() = null

    override fun expandActionView() = false

    override fun collapseActionView() = false

    override fun isActionViewExpanded() = false

    override fun setOnActionExpandListener(listener: OnActionExpandListener) = this

}
