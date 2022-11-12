/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui

import com.piappstudio.register.R
import com.piappstudio.pitheme.component.PILottie
import com.piappstudio.pitheme.component.piShadow
import com.piappstudio.pitheme.theme.Dimen
import com.piappstudio.pitheme.theme.Dimen.double_space
import com.piappstudio.pitheme.theme.Dimen.space
import com.piappstudio.pitheme.theme.Dimen.triple_space
import androidx.annotation.RawRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog


@Composable
fun PositiveButton(onclick: () -> Unit, modifier: Modifier = Modifier, text: String) {
    Button(
        onClick = onclick,
        modifier = modifier
    ) {
        Text(text = text, Modifier.padding(space))
    }
}

@Composable
fun NegativeButton(onclick: () -> Unit, modifier: Modifier = Modifier, text: String) {
    TextButton(
        onClick = onclick,
        modifier = modifier
    ) {
        Text(text = text, modifier = Modifier.padding(space))
    }
}

@Composable
fun PiDialog(
    title: String? = null,
    message: String,
    enableCancel: Boolean = false,
    @RawRes lottieImages: Int? = null,
    negativeLabel:String?= null,
    positiveLabel:String? = null,
    onClick: (index: Int) -> Unit
) {
    val isDisplay = remember { mutableStateOf(true) }

    if (isDisplay.value) {
        Dialog(onDismissRequest = {}) {
            Surface(modifier = Modifier.piShadow()) {
                Column(
                    modifier = Modifier.padding(
                        triple_space
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    lottieImages?.let { img ->
                        PILottie(
                            resourceId = img,
                            modifier = Modifier
                                .size(Dimen.small_lottie_icon)
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(space))
                    }

                    title?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(space))
                    }


                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(double_space))
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        var okay = stringResource(id = R.string.btn_ok)
                        if (enableCancel) {
                            okay = stringResource(id = R.string.title_yes)

                            NegativeButton(
                                onclick = {
                                    isDisplay.value = false
                                    onClick.invoke(0)
                                }, text = negativeLabel?: stringResource(id = R.string.btn_no),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = space, end = double_space)
                            )

                        }
                        PositiveButton(
                            onclick = {
                                isDisplay.value = false
                                onClick.invoke(1)
                            },
                            text = positiveLabel?:okay,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = space)
                        )
                    }


                }
            }

        }
    }

}
