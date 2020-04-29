@file:Suppress("unused")

package rikka.html.text

import android.text.Spanned

fun CharSequence.toHtml(flags: Int = 0): Spanned {
    return HtmlCompat.fromHtml(this.toString(), flags)
}