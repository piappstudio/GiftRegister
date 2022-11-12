/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pitheme.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.piappstudio.pimodel.pref.PiPreference
import com.piappstudio.pitheme.R


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PiPermissionRequired(
    permissionDescription: String,
    permissionState: PermissionState,
    permissionGranted: () -> Unit,
    noOfPreviousAttempt: Int = 0,
    updatePermissionAttemptCount: () -> Unit
) {
    val context = LocalContext.current
    when (permissionState.status) {
        PermissionStatus.Granted -> {
            SideEffect {
                permissionGranted()
            }
        }
        is PermissionStatus.Denied -> {
            Rationale(text = permissionDescription) {
                if (noOfPreviousAttempt <= 2) {
                    permissionState.launchPermissionRequest()
                    updatePermissionAttemptCount()
                } else {
                    context.openAppSettings()
                }
            }

        }
    }
}

@Composable
private fun Rationale(
    text: String,
    onRequestPermission: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Don't */ },
        title = {
            Text(text = stringResource(R.string.permission_title))
        },
        text = {
            Text(text)
        },
        confirmButton = {
            Button(onClick = onRequestPermission) {
                Text(stringResource(R.string.ok))
            }
        }
    )
}