/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.guestlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piappstudio.register.R
import com.piappstudio.register.ui.event.filter.FilterOption
import com.piappstudio.pimodel.Constant.EMPTY_STRING
import com.piappstudio.pimodel.GuestInfo
import com.piappstudio.pimodel.Resource
import com.piappstudio.pimodel.ResourceHelper
import com.piappstudio.pimodel.database.PiDataRepository
import com.piappstudio.pinavigation.ErrorInfo
import com.piappstudio.pinavigation.ErrorManager
import com.piappstudio.pinavigation.ErrorState
import com.piappstudio.pinavigation.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


data class GuestListState(
    val lstGuest: List<GuestInfo>,
    val searchOption: SearchOption,
    val filteredItem: Map<String, List<GuestInfo>>,
    val progress:Resource.Status = Resource.Status.NONE,

)

@HiltViewModel
class GuestListViewModel @Inject constructor(
    private val piDataRepository: PiDataRepository,
    val navManager: NavManager,
    private val resourceHelper: ResourceHelper,
    private val errorManager: ErrorManager
) : ViewModel() {

    var selectedEventId: Long = 0


    private val _lstGuest: MutableStateFlow<List<GuestInfo>> = MutableStateFlow(emptyList())

    private val _guestListState =
        MutableStateFlow(
            GuestListState(
                lstGuest = emptyList(),
                searchOption = SearchOption(),
                emptyMap()
            )
        )
    val guestListState: StateFlow<GuestListState> = _guestListState


    private val _selectedGiftInfo: MutableStateFlow<GuestInfo> = MutableStateFlow(GuestInfo())
    val selectedGiftInfo: StateFlow<GuestInfo> = _selectedGiftInfo
    fun updateSelectedGiftInfo(guestInfo: GuestInfo?) {
        _selectedGiftInfo.update { guestInfo ?: GuestInfo() }
    }

    fun fetchGuest() {
        viewModelScope.launch(Dispatchers.IO) {
            Timber.d("Fetching event for $selectedEventId")
            piDataRepository.fetchGuest(selectedEventId).onEach { result ->
                Timber.d("Status ${result.status}")
                if (result.status == Resource.Status.SUCCESS) {
                    result.data?.let { guestList ->
                        Timber.d("Data size: ${guestList.size}")
                        _lstGuest.value = guestList
                        _guestListState.update { it.copy(lstGuest = guestList) }
                        applyFilter()
                    }
                }

            }.collect()

        }

    }

    fun updateSearchText(text: String) {
        _guestListState.update { it.copy(searchOption = it.searchOption.copy(text = text)) }
        val lstFiltered = _lstGuest.value.filter {
            it.name?.contains(text, true) == true
                    || it.address?.contains(text, true) == true
        }
        _guestListState.update { it.copy(lstGuest = lstFiltered) }
        applyFilter()
    }

    fun updateFilter(updatedOption: FilterOption) {
        _guestListState.update { it.copy(searchOption = it.searchOption.copy(filterOption = updatedOption)) }
        applyFilter()

    }

    private fun applyFilter() {
        val filterOption = _guestListState.value.searchOption.filterOption
        val fullList = _guestListState.value.lstGuest

        val giftType = resourceHelper.getString(R.string.gift_type)

        // Applied GiftType
        val groupedItem: Map<String, List<GuestInfo>> =
            if (filterOption.groupBy.title == giftType) {
                fullList.groupBy { it.giftType.toString() }
            } else {
                fullList.groupBy { it.address?.trim()?.uppercase() ?: EMPTY_STRING }
            }

        val finalSortedMap = mutableMapOf<String, List<GuestInfo>>()
        val sort = filterOption.sort.title

        for ((key, value) in groupedItem) {
            var sortedArray: List<GuestInfo> = emptyList()
            when (sort) {
                resourceHelper.getString(id = R.string.sort_name_by_ascending) -> {
                    sortedArray = value.sortedBy { it.name }
                }
                resourceHelper.getString(R.string.sort_name_by_descending) -> {
                    sortedArray = value.sortedByDescending { it.name }
                }

                resourceHelper.getString(R.string.sort_amount_by_ascending) -> {
                    sortedArray = value.sortedBy { it.giftValue?.toPiDouble() }
                }
                resourceHelper.getString(R.string.sort_amount_by_descending) -> {
                    sortedArray = value.sortedByDescending { it.giftValue?.toPiDouble() }
                }
            }
            finalSortedMap[key] = sortedArray
        }


        _guestListState.update { it.copy(filteredItem = finalSortedMap.toSortedMap()) }

    }

    private fun String.toPiDouble():Double {
        try {
            this.toDouble()
        } catch (ex:Exception) {
            return 0.0
        }
        return 0.0
    }

    fun delete(guest: GuestInfo) {
        viewModelScope.launch (Dispatchers.IO) {
            piDataRepository.delete(guest).onEach { result->
                if (result.status == Resource.Status.SUCCESS) {
                    fetchGuest()
                    _guestListState.update { it.copy(progress = result.status) }
                    errorManager.post(ErrorInfo(message = resourceHelper.getString(R.string.delete_guest_success), errorState = ErrorState.POSITIVE))
                } else {
                    _guestListState.update { it.copy(progress = result.status) }
                    if (result.status == Resource.Status.ERROR) {
                        errorManager.post(ErrorInfo(piError = result.error, action = {
                            delete(guest)
                        }, errorState = ErrorState.NEGATIVE))
                    }
                }

            }.collect()

        }

    }

}

data class SearchOption(val text: String? = null, val filterOption: FilterOption = FilterOption())

