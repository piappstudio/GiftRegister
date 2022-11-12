/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.guestlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.piappstudio.pimodel.EventInfo
import com.piappstudio.register.ui.event.editguest.EditGuestScreen
import com.piappstudio.register.ui.event.editguest.EditGuestViewModel
import kotlinx.coroutines.launch
import timber.log.Timber


@ExperimentalMaterialApi
@Composable
fun GuestHome(
    eventInfo: EventInfo?,
    guestListViewModel: GuestListViewModel = hiltViewModel(),
    editGuestViewModel: EditGuestViewModel = hiltViewModel()
) {

    Timber.d("EventId: ${eventInfo?.id}")
    LaunchedEffect(key1 = eventInfo) {
        guestListViewModel.selectedEventId = eventInfo?.id ?: 0
        editGuestViewModel.selectedEventId = eventInfo?.id ?: 0
    }

    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0XFF0F9D58))
            ) {
                EditGuestScreen(
                    viewModel = editGuestViewModel
                ) {
                    guestListViewModel.fetchGuest()
                    coroutineScope.launch {
                        Timber.d("Collapse called: $bottomSheetScaffoldState")
                        bottomSheetScaffoldState.hide()
                    }
                }
            }
        }
    ) {
        GuestListScreen(eventInfo = eventInfo, viewModel = guestListViewModel, callback = {
            coroutineScope.launch {
                // This is new event
                Timber.d("Expand called: $bottomSheetScaffoldState")
                editGuestViewModel.updateGuestInfo(null)
                bottomSheetScaffoldState.animateTo(ModalBottomSheetValue.Expanded)
            }
        }) {
            coroutineScope.launch {
                Timber.d("Expand called: $bottomSheetScaffoldState")
                // This is old event
                editGuestViewModel.updateGuestInfo(it)
                bottomSheetScaffoldState.animateTo(ModalBottomSheetValue.Expanded)
            }
        }

    }
}