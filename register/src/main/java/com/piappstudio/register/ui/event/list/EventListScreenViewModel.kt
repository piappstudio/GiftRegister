/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piappstudio.register.R
import com.piappstudio.pimodel.EventSummary
import com.piappstudio.pimodel.Resource
import com.piappstudio.pimodel.ResourceHelper
import com.piappstudio.pimodel.database.PiDataRepository
import com.piappstudio.pinavigation.ErrorInfo
import com.piappstudio.pinavigation.ErrorManager
import com.piappstudio.pinavigation.ErrorState
import com.piappstudio.pinavigation.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel, Model-View-ViewModel
@HiltViewModel
class EventListScreenViewModel @Inject constructor(private val piDataRepository: PiDataRepository,
                                                   val navManager: NavManager,
                                                   private val errorManager: ErrorManager, private val resourceHelper: ResourceHelper) : ViewModel() {

    private val _eventList = MutableStateFlow(emptyList<EventSummary>())
    val eventList:StateFlow<List<EventSummary>> = _eventList

    private val _progress = MutableStateFlow(Resource.Status.NONE)
    val progress:StateFlow<Resource.Status> = _progress

    fun fetchEventList() {
        viewModelScope.launch {
            _eventList.update { piDataRepository.fetchEvents() }
        }
    }

    fun delete(eventSummary: EventSummary) {
        viewModelScope.launch {
            piDataRepository.delete(eventSummary).onEach { result->

                if (result.status == Resource.Status.SUCCESS) {
                    fetchEventList()
                    _progress.update { Resource.Status.SUCCESS }

                    errorManager.post(
                        ErrorInfo(
                            message = resourceHelper.getString(R.string.event_delete_success),
                            errorState = ErrorState.POSITIVE
                        )
                    )
                } else {
                    if (result.status == Resource.Status.ERROR) {
                        errorManager.post(ErrorInfo(piError = result.error, action = {
                            delete(eventSummary)
                        }, errorState = ErrorState.NEGATIVE))
                    }
                    _progress.update { result.status }
                }


            }.collect()
        }

    }
}