/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piappstudio.pianalytic.PiAnalyticConstant
import com.piappstudio.pimodel.Resource
import com.piappstudio.pinavigation.NavInfo
import com.piappstudio.pitheme.component.PiProgressIndicator
import com.piappstudio.pitheme.route.Route
import com.piappstudio.register.ui.event.editevent.EditEventScreen
import com.piappstudio.register.ui.event.editevent.EditEventViewModel
import com.piappstudio.register.ui.event.list.EventListScreen
import com.piappstudio.register.ui.event.list.EventListScreenViewModel
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Composable
fun EventHome(viewModel: EditEventViewModel = hiltViewModel(), eventListScreenViewModel: EventListScreenViewModel = hiltViewModel()) {

    LaunchedEffect(key1 = Unit) {
        viewModel.piTracker.logEvent(PiAnalyticConstant.SCREEN_VIEW , PiAnalyticConstant.Page.EVENT_LIST)
    }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    val coroutineScope = rememberCoroutineScope()

    val lstEvents  by eventListScreenViewModel.eventList.collectAsState()

    val progress by eventListScreenViewModel.progress.collectAsState()
    if (progress == Resource.Status.LOADING) {
        PiProgressIndicator()
    }
    LaunchedEffect(key1 =Unit) {
        eventListScreenViewModel.fetchEventList()
    }
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.6f)) {
                EditEventScreen (viewModel = viewModel) {
                    eventListScreenViewModel.fetchEventList()
                    coroutineScope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                        viewModel.piTracker.logEvent(PiAnalyticConstant.COLLAPSED , PiAnalyticConstant.Page.EVENT_LIST)

                    }
                }
            }

        },
        sheetPeekHeight = 0.dp
    ) {
        //Content
        EventListScreen (lstEvents = lstEvents, onClickSetting = {
            eventListScreenViewModel.navManager.navigate(routeInfo = NavInfo(id = Route.Home.EVENT.ABOUT))
        }, onClickFloatingAction =   {
            coroutineScope.launch {
                bottomSheetScaffoldState.bottomSheetState.expand()
                viewModel.piTracker.logEvent(PiAnalyticConstant.EXPENDED , PiAnalyticConstant.Page.EVENT_LIST)
            }
        },
        onClickDeleteItem = {eventInfo->
            eventListScreenViewModel.delete(eventInfo)
        }) {
            eventListScreenViewModel.navManager.navigate(routeInfo = NavInfo(id = Route.Home.GUEST.guestList(it)))
        }
    }
}