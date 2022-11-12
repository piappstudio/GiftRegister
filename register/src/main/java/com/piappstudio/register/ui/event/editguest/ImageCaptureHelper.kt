/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.editguest

import android.content.Context
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.piappstudio.pimodel.Constant
import com.piappstudio.pimodel.PiSession
import com.piappstudio.pitheme.component.PiPermissionRequired
import com.piappstudio.register.R
import timber.log.Timber
import java.io.File
import java.util.*


@OptIn(ExperimentalPermissionsApi::class)
@Composable

fun CapturePhoto(callback:(imagePath:String)->Unit,
                 noOfPreviousAttempt:Int, piSession: PiSession, updatePermissionAttempt:()->Unit) {

    fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp = Constant.PiFormat.mediaDateTimeFormat.format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDir?.path+"/${piSession.appName}_JPEG_${timeStamp}.jpg")
    }

    val context = LocalContext.current
    val file = createImageFile(context = context)
    val imageUri = FileProvider.getUriForFile(
        context,
        "${piSession.packageName}.provider", file
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess ->
            if (isSuccess) {
                Timber.d("Successfully saved the image :${imageUri}")
                callback.invoke(imageUri.toString())
            }
        })

    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )
    PiPermissionRequired(
        permissionDescription = stringResource(R.string.camera_permission_desc),
        permissionState = cameraPermissionState,
        permissionGranted = {
            Timber.d("Launching camera")
            cameraLauncher.launch(imageUri)
        },
        noOfPreviousAttempt = noOfPreviousAttempt,
        updatePermissionAttemptCount = updatePermissionAttempt
    )
}