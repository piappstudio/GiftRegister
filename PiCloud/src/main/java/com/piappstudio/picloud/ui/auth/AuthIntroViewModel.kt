/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.picloud.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.piappstudio.picloud.worker.GoogleDriveSyncWorker
import com.piappstudio.picloud.worker.PiDriveManager
import com.piappstudio.pimodel.Constant
import com.piappstudio.pimodel.Resource
import com.piappstudio.pimodel.pref.PiPrefKey
import com.piappstudio.pimodel.pref.PiPreference
import com.piappstudio.pinavigation.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class AuthState(
    val isUserSignIn: Boolean = false,
    val currentUser: GoogleSignInAccount? = null,
    val syncDate: String? = null,
    val isLoading:Boolean = false
)

fun PiPreference.isUserLoggedIn():Boolean {
    return GoogleSignIn.getLastSignedInAccount(context)!=null
}

@HiltViewModel
class AuthIntroViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val piPreference: PiPreference,
    private val piDriveManager: PiDriveManager,
    val navManager:NavManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    private val gso = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestProfile()
        .requestScopes(Scope(DriveScopes.DRIVE_FILE), Scope(DriveScopes.DRIVE_APPDATA))
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    init {
        initializeSignIn()
    }

    private fun initializeSignIn() {
        viewModelScope.launch {
            val account = GoogleSignIn.getLastSignedInAccount(context)
            piPreference.save(PiPrefKey.IS_USER_LOGGED_IN, account!=null)
            _uiState.update { it.copy(currentUser = account, isUserSignIn = account != null) }
            lastSyncDate()
        }
    }

    fun updateAccountInfo(account: GoogleSignInAccount?) {

        _uiState.update { it.copy(currentUser = account, isUserSignIn = account != null) }

    }

    fun performLogout() {
        piPreference.remove(PiPrefKey.LAST_SYNC_TIME)
        googleSignInClient.signOut()
        initializeSignIn()
    }

    private fun lastSyncDate() {
        val lstSyncTime = piPreference.getString(PiPrefKey.LAST_SYNC_TIME)
        _uiState.update { it.copy(syncDate = lstSyncTime) }
    }

    fun syncNow() {

        viewModelScope.launch {

            if (piPreference.isUserLoggedIn()) {
                val workManager = WorkManager.getInstance(context)
                // User is logged in, schedule the background operation
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.UNMETERED)
                    .build()
                val work =
                    OneTimeWorkRequest.Builder(GoogleDriveSyncWorker::class.java)
                        .setConstraints(constraints).build()
                workManager.getWorkInfoByIdLiveData(work.id).observeForever {  workInfo->
                    when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            _uiState.update { it.copy(isLoading = false) }
                            lastSyncDate()
                        }
                        WorkInfo.State.FAILED -> {
                            _uiState.update { it.copy(isLoading = false, syncDate = "Last sync was failed") }
                        }
                        else -> {
                            _uiState.update { it.copy(isLoading = false, syncDate = "Sync is in progress") }
                        }
                    }
                }
                workManager.enqueue(work)
            }

        }
    }


}