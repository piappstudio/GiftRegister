/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.editguest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piappstudio.register.R
import com.piappstudio.pimodel.*
import com.piappstudio.pimodel.database.PiDataRepository
import com.piappstudio.pimodel.pref.PiPrefKey
import com.piappstudio.pimodel.pref.PiPreference
import com.piappstudio.pinavigation.ErrorInfo
import com.piappstudio.pinavigation.ErrorManager
import com.piappstudio.pinavigation.ErrorState
import com.piappstudio.pitheme.component.UiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditGuestViewModel @Inject constructor(
    private val piDataRepository: PiDataRepository,
    private val piPreference: PiPreference,
    val piSession: PiSession,
    private val errorManager: ErrorManager,
    private val resourceHelper: ResourceHelper
) :
    ViewModel() {

    var selectedEventId: Long = 0

    // READ & write
    private val _guestInfo = MutableStateFlow(GuestInfo())

    // READ
    val guestInfo: StateFlow<GuestInfo> = _guestInfo

    private val _medias = MutableStateFlow<List<MediaInfo>>(emptyList())
    val lstMedias: StateFlow<List<MediaInfo>> = _medias

    private val _errorInfo = MutableStateFlow(GuestError())
    val errorInfo: StateFlow<GuestError> = _errorInfo

    fun updateGuestInfo(guestInfo: GuestInfo?) {
        _guestInfo.value = guestInfo ?: GuestInfo()
        _medias.value = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            if (_guestInfo.value.id != 0L) {
                _guestInfo.value.id.let {
                    piDataRepository.fetchGuestMedia(it).onEach { result ->
                        if (result.status == Resource.Status.SUCCESS) {
                            result.data?.let {
                                _medias.value = it
                            }

                        }
                    }.collect()
                }
            }
        }
    }

    fun updateName(name: String) {
        _guestInfo.update { it.copy(name = name) }
    }

    fun updateAddress(address: String) {
        _guestInfo.update { it.copy(address = address) }
    }

    fun updateQuantity(quantity: String) {
        _guestInfo.update { it.copy(giftValue = quantity) }
    }

    //region Camera Permission
    fun addMedia(path: String) {
        val media = MediaInfo(path = path, guestId = _guestInfo.value.id)
        val lstMedia = mutableListOf<MediaInfo>()
        lstMedia.addAll(_medias.value)
        lstMedia.add(media)
        _medias.update { lstMedia }
    }

    fun previousCameraPermissionAttempt(): Int {
        return piPreference.getInt(PiPrefKey.CAMERA_PERMISSION)
    }

    fun updateCameraPermissionAttempt() {
        val prev = piPreference.getInt(PiPrefKey.CAMERA_PERMISSION)
        piPreference.save(PiPrefKey.CAMERA_PERMISSION, prev + 1)
    }
    //endregion


    fun onClickSubmit() {
        val guestInfo = _guestInfo.value
        if (guestInfo.name == null || guestInfo.name?.isBlank() == true) {
            _errorInfo.update { it.copy(nameError = it.nameError.copy(isError = true)) }
            return
        } else {
            _errorInfo.update { it.copy(nameError = it.nameError.copy(isError = false)) }
        }

        if (guestInfo.address == null || guestInfo.address?.isBlank() == true) {
            _errorInfo.update { it.copy(addressError = it.addressError.copy(isError = true)) }
            return
        } else {
            _errorInfo.update { it.copy(addressError = it.addressError.copy(isError = false)) }
        }

        if (guestInfo.giftValue == null || guestInfo.giftValue?.isBlank() == true) {
            _errorInfo.update { it.copy(quantity = it.quantity.copy(isError = true)) }
            return
        } else {
            _errorInfo.update { it.copy(quantity = it.quantity.copy(isError = false)) }
        }

        val updatedGuestInfo = guestInfo.copy(eventId = selectedEventId)
        viewModelScope.launch {
            if (updatedGuestInfo.id != 0L) {
                // Perform update operation
                piDataRepository.update(updatedGuestInfo, lstMediaInfo = lstMedias.value)
                    .onEach { response ->
                        _errorInfo.update { it.copy(progress = response) }
                        if (response.status == Resource.Status.SUCCESS) {
                            _guestInfo.update { GuestInfo() }
                            errorManager.post(
                                ErrorInfo(
                                    message = resourceHelper.getString(R.string.guest_update_success),
                                    errorState = ErrorState.POSITIVE
                                )
                            )
                        }

                        // Handle failure  case
                        if (response.status == Resource.Status.ERROR) {
                            errorManager.post(ErrorInfo(piError = response.error, action = {
                                onClickSubmit()
                            }, errorState = ErrorState.NEGATIVE))
                        }

                    }.collect()
            } else {
                piDataRepository.insert(updatedGuestInfo, lstMediaInfo = lstMedias.value)
                    .onEach { response ->
                        _errorInfo.update { it.copy(progress = response) }
                        if (response.status == Resource.Status.SUCCESS) {
                            _guestInfo.update { GuestInfo() }
                            errorManager.post(
                                ErrorInfo(
                                    message = resourceHelper.getString(R.string.guest_insert_success),
                                    errorState = ErrorState.POSITIVE
                                )
                            )
                        }
                        if (response.status == Resource.Status.ERROR) {
                            errorManager.post(ErrorInfo(piError = response.error, action = {
                                onClickSubmit()
                            }, errorState = ErrorState.NEGATIVE))
                        }
                    }.collect()
            }

        }
        Timber.d("Save event information")


    }

    fun updateGiftType(giftType: GiftType) {
        _guestInfo.update { it.copy(giftType = giftType) }
    }

    fun deleteImage(media: MediaInfo) {
        // Not saved into DB yet, so just remove from he list
        val lstMedia = mutableListOf<MediaInfo>()
        lstMedia.addAll(_medias.value)
        lstMedia.remove(media)
        _medias.value = lstMedia
        viewModelScope.launch {
            piDataRepository.delete(media).onEach { result ->
                Timber.d("Result status: ${result.status}")
            }.collect()
        }

    }
}

data class GuestError(
    val nameError: UiError = UiError(errorText = R.string.error_name),
    val addressError: UiError = UiError(errorText = R.string.error_address),
    val quantity: UiError = UiError(errorText = R.string.error_quantity),
    val progress: Resource<Any?> = Resource.idle()
)

