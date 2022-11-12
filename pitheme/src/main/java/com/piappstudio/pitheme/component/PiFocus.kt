/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pitheme.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
data class PiFocus  constructor(
    val scope: CoroutineScope,
    val bringIntoViewRequester:BringIntoViewRequester,
    val keyboardController: SoftwareKeyboardController?,
    val focusManager: FocusManager
)


@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun rememberPiFocus(): PiFocus {
    val bringIntoViewRequester = remember {
        BringIntoViewRequester()
    }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    return remember {
        PiFocus(scope, bringIntoViewRequester, keyboardController, focusManager)
    }
}

fun Modifier.piFocus(
    focusRequester: FocusRequester,
    piFocus: PiFocus
): Modifier {
    return this
        .focusRequester(focusRequester)

}
