/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.authentication

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.piappstudio.authentication.ui.forget.ForgetPasswordScreen
import com.piappstudio.authentication.ui.login.LoginScreen
import com.piappstudio.authentication.ui.register.RegisterScreen
import com.piappstudio.pitheme.route.Root
import com.piappstudio.pitheme.route.Route

fun NavGraphBuilder.authNavGraph() {

    navigation(startDestination = Root.Auth.LOGIN, route = Root.AUTH) {

        composable(Route.Auth.REGISTER) {
            RegisterScreen()
        }
        loginNavGraph()
    }
}


fun NavGraphBuilder.loginNavGraph() {

    navigation(startDestination = Route.Auth.Login.LOGIN, route = Root.Auth.LOGIN) {

        composable(Route.Auth.Login.LOGIN) {
            LoginScreen()
        }
        composable(Route.Auth.Login.FORGET_PASSWORD) {
            ForgetPasswordScreen()
        }
    }
}