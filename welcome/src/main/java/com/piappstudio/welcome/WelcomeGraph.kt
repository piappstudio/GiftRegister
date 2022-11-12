/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.welcome

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.piappstudio.pitheme.route.Root
import com.piappstudio.pitheme.route.Route
import com.piappstudio.welcome.ui.feature.FeatureScreen
import com.piappstudio.welcome.ui.splash.SplashScreen
import com.piappstudio.welcome.ui.tnc.TermsAndConditionScreen

fun NavGraphBuilder.welcomeNavGraph() {

    navigation(startDestination = Route.Welcome.SPLASH, route = Root.WELCOME) {

        composable(Route.Welcome.SPLASH) {
            SplashScreen()
        }

        composable(Route.Welcome.FEATURE) {
            FeatureScreen()

        }
        composable(Route.Welcome.TNC) {
            TermsAndConditionScreen()

        }


    }
}