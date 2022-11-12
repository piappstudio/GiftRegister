/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pitheme.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*

@Composable
fun PILottie(resourceId: Int, modifier: Modifier = Modifier):Float {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resourceId))
    val progress by animateLottieCompositionAsState(
        composition, iterations = 1
    )
    LottieAnimation(
        composition,
        progress,
        modifier = modifier
    )
    return progress
}