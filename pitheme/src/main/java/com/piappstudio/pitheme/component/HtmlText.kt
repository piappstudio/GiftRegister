/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pitheme.component

import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.fromHtml


@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    html: String,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
) {
    AndroidView(
        modifier = modifier,
        update = { it.text = fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY) },
        factory = { context ->
           val gravity = when (textStyle.textAlign) {
                TextAlign.Center -> Gravity.CENTER
                TextAlign.End -> Gravity.END
                else -> Gravity.START
            }

            TextView(context).apply {
                // general style
                textSize = textStyle.fontSize.value
                setGravity(gravity)
                movementMethod = LinkMovementMethod.getInstance()
                // links
            }
        }
    )
}