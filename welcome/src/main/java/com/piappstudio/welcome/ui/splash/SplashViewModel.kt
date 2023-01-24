/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.welcome.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piappstudio.pianalytic.IPiAnalyticProvider
import com.piappstudio.pianalytic.PiAnalyticConstant
import com.piappstudio.pianalytic.PiTracker
import com.piappstudio.pimodel.Resource
import com.piappstudio.pinavigation.NavManager
import com.piappstudio.pinetwork.PiRemoteDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(val navManager: NavManager, val piRemoteDataRepository: PiRemoteDataRepository, val piTracker: PiTracker):ViewModel() {

    init {
        viewModelScope.launch {
            piRemoteDataRepository.fetchAppConfig().onEach {
                when (it.status) {
                    Resource.Status.SUCCESS -> {

                    }
                    Resource.Status.ERROR -> {
                        piTracker.logEvent("Exception", PiAnalyticConstant.Page.SPLASH)
                        Timber.e(Throwable("Error while fetch config: ${it.error?.message}"))
                    } else -> {

                }
                }
            }.collect()
        }
    }
}