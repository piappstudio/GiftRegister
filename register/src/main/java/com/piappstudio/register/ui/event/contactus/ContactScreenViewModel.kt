/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.contactus

import androidx.lifecycle.ViewModel
import com.piappstudio.pimodel.PiSession
import com.piappstudio.pinavigation.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactScreenViewModel @Inject constructor(val navManager: NavManager, val piSession: PiSession):ViewModel() {
    fun getAppVersion():String {
        return "${piSession.appVersion} (${piSession.buildNumber})"
    }
}