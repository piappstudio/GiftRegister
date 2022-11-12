/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register

import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.gson.Gson
import com.piappstudio.pimodel.EventInfo
import com.piappstudio.pitheme.route.Root
import com.piappstudio.pitheme.route.Route
import com.piappstudio.register.ui.event.EventHome
import com.piappstudio.register.ui.event.about.AboutScreen
import com.piappstudio.register.ui.event.contactus.ContactScreen
import com.piappstudio.register.ui.event.filter.FilterOption
import com.piappstudio.register.ui.event.filter.SortScreen
import com.piappstudio.register.ui.event.guestlist.GuestHome
import com.piappstudio.register.ui.event.list.EventEmptyScreen


fun NavGraphBuilder.homeGraph() {
    navigation(startDestination = Root.Home.EVENTS, route = Root.HOME) {
        eventGraph()
        giftGraph()
    }

}


@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.eventGraph() {

    navigation(startDestination = Route.Home.EVENT.LIST, route = Root.Home.EVENTS) {

        composable(Route.Home.EVENT.LIST) {
            EventHome()
        }
        composable(Route.Home.EVENT.SORTSCREEN) {
           SortScreen(filerOption = FilterOption())
        }
        composable(Route.Home.EVENT.EVENT_EMPTY_SCREEN) {
            EventEmptyScreen()
        }
        composable(Route.Home.EVENT.ABOUT) {
            AboutScreen()
        }
        composable(Route.Home.EVENT.CONTACT_US) {
            ContactScreen()
        }


    }
}

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.giftGraph() {

    navigation(startDestination = Route.Home.GUEST.LIST, route = Root.Home.GIFTS) {

        composable(Route.Home.GUEST.LIST, arguments = listOf(navArgument(Route.Home.GUEST.Argument.query){type = NavType.StringType})) {
            val strEventInfo = it.arguments?.getString(Route.Home.GUEST.Argument.query)
            val eventInfo = Gson().fromJson(strEventInfo, EventInfo::class.java)
            GuestHome(eventInfo = eventInfo)
        }
    }
}

