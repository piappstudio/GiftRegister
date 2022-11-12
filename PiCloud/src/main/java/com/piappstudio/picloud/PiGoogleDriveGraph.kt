/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.picloud

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.piappstudio.picloud.ui.auth.AuthIntroScreen
import com.piappstudio.pitheme.route.Root

fun NavGraphBuilder.driveGraph() {
    
    navigation(startDestination = Root.Drive.INTRO, route = Root.DRIVE) {
        composable(route = Root.Drive.INTRO) {
            AuthIntroScreen()
        }
    }

}