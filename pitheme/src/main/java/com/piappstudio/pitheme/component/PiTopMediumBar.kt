/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pitheme.component

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.piappstudio.pinavigation.NavInfo
import com.piappstudio.pinavigation.NavManager
import com.piappstudio.pitheme.R
import com.piappstudio.pitheme.route.Route
import com.piappstudio.pitheme.theme.Dimen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberPiScrollBehaviour(): TopAppBarScrollBehavior {
    val decayAnimation = rememberSplineBasedDecay<Float>()
    return TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimation, rememberTopAppBarScrollState()
    )
}
@Composable
fun PiMediumTopAppBar(title:String, navManager: NavManager?=null, scrollBehavior: TopAppBarScrollBehavior?=null) {
    MediumTopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.padding(start = Dimen.space)
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.onPrimary),
        navigationIcon = {
            IconButton(onClick = {
                navManager?.navigate(routeInfo = NavInfo(Route.Control.Back))
            }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.acc_back)
                )

            }
        }, scrollBehavior = scrollBehavior)
}
