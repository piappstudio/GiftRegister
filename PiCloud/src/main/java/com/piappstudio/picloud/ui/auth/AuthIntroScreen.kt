/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.picloud.ui.auth

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.AddToDrive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.piappstudio.picloud.R
import com.piappstudio.pimodel.error.PIError
import com.piappstudio.pitheme.component.PiMediumTopAppBar
import com.piappstudio.pitheme.component.PiProgressIndicator
import com.piappstudio.pitheme.theme.Dimen
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AuthIntroScreen(viewModel: AuthIntroViewModel = hiltViewModel()) {

    var errorInfo by remember { mutableStateOf(PIError()) }

    fun handleSignData(data: Intent?) {
        errorInfo = PIError()
        // The Task returned from this call is always completed, no need to attach
        // a listener.
        GoogleSignIn.getSignedInAccountFromIntent(data)
            .addOnCompleteListener {
                Timber.d("isSuccessful ${it.isSuccessful}")
                if (it.isSuccessful) {
                    viewModel.updateAccountInfo(it.result)
                    // user successfully logged-in
                    Timber.d("account ${it.result?.account}")
                    Timber.d("displayName ${it.result?.displayName}")
                    Timber.d("Email ${it.result?.email}")
                } else {
                    // authentication failed
                    Timber.e("exception ${it.exception}")
                    errorInfo = PIError(message = it.exception?.message)
                }
            }

    }

    val launcherActivity =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            handleSignData(result.data)
        }

    fun performLogin(viewModel: AuthIntroViewModel) {
        val signInIntent = viewModel.googleSignInClient.signInIntent
        launcherActivity.launch(signInIntent)
    }

    val uiState by viewModel.uiState.collectAsState()

    val scrollState = rememberScrollState()
    Scaffold (topBar = {
        PiMediumTopAppBar(title = stringResource(R.string.title_drive_back), navManager = viewModel.navManager)
    }){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(it)
                .padding(Dimen.doubleSpace),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Outlined.AddToDrive,
                contentDescription = stringResource(R.string.acc_sync_google_drive),
                modifier = Modifier.size(Dimen.person_image),
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(Dimen.doubleSpace))
            Text(
                stringResource(R.string.title_google_drive),

                style = MaterialTheme.typography.headlineMedium
            )
            var description =  stringResource(R.string.desc_google_drive_login)
            var buttonTitle = stringResource(R.string.btn_login)

            if (uiState.isUserSignIn) {
                buttonTitle = stringResource(R.string.btn_logout)
                description = stringResource(R.string.desc_google_drive_logout)
            }
            Spacer(modifier = Modifier.height(Dimen.doubleSpace))
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(Dimen.doubleSpace))

            if (!uiState.isUserSignIn) {
                errorInfo.message?.let {
                    Card(modifier = Modifier.padding(top = Dimen.doubleSpace)) {
                        Text(
                            stringResource(R.string.desc_auth_failed),
                            modifier = Modifier.padding(Dimen.doubleSpace),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                }
            } else {
                Card(modifier = Modifier.padding(Dimen.doubleSpace)) {
                    Column(
                        modifier = Modifier.padding(Dimen.doubleSpace),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.currentUser?.displayName
                                ?: stringResource(R.string.user_name),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = uiState.currentUser?.email
                                ?: stringResource(R.string.user_name),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimen.doubleSpace))

            Button(onClick = {
                if (uiState.isUserSignIn) {
                    viewModel.performLogout()
                } else {
                    performLogin(viewModel = viewModel)
                }

            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = buttonTitle)
            }

            Spacer(modifier = Modifier.height(Dimen.triple_space))
            SyncView(viewModel = viewModel, uiState = uiState)

        }
    }

}

@Composable
fun SyncView(viewModel: AuthIntroViewModel, uiState: AuthState) {
    if (uiState.isUserSignIn) {
        Text(
            text = stringResource(R.string.title_sync_summary),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(Dimen.space))
        Text(
            text = stringResource(R.string.sync_schedule),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(Dimen.triple_space))
        Text(
            text = stringResource(R.string.last_sync_time),
            style = MaterialTheme.typography.titleSmall,
        )
        Spacer(modifier = Modifier.height(Dimen.space))
        val lastSyncDate = uiState.syncDate
        if (lastSyncDate.isNullOrEmpty()) {
            Text(text = stringResource(R.string.no_sync_yet), style = MaterialTheme.typography.bodyMedium)
        } else {
            Text(text = lastSyncDate, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
        }
        Spacer(modifier = Modifier.height(Dimen.doubleSpace))
        if (uiState.isLoading) {
            PiProgressIndicator()
        }
        OutlinedButton(onClick = { viewModel.syncNow() } ) {
            Icon(
                imageVector = Icons.Filled.Sync,
                contentDescription = stringResource(R.string.acc_sync_file)
            )
        }
    }


}
