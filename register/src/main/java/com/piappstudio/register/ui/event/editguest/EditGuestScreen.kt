/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.editguest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.piappstudio.pitheme.theme.Cash
import com.piappstudio.pitheme.theme.Diamond
import com.piappstudio.pitheme.theme.Gift
import com.piappstudio.register.R
import com.piappstudio.pimodel.Constant.EMPTY_STRING
import com.piappstudio.pimodel.GiftType
import com.piappstudio.pimodel.MediaInfo
import com.piappstudio.pimodel.Resource
import com.piappstudio.pitheme.component.PiErrorView
import com.piappstudio.pitheme.component.PiProgressIndicator
import com.piappstudio.pitheme.component.piFocus
import com.piappstudio.pitheme.component.rememberPiFocus
import com.piappstudio.pitheme.theme.Dimen
import com.piappstudio.register.ui.PiImagePreviewDialog
import com.piappstudio.register.ui.event.guestlist.giftImage


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EditGuestScreen(
    viewModel: EditGuestViewModel,
    callback: () -> Unit
) {

    var capturePhoto by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        SmallTopAppBar(title = {
            Text(text = stringResource(R.string.title_edit_guest))

        }, actions = {
            IconButton(onClick = { capturePhoto = true }) {
                Icon(imageVector = Icons.Default.Camera, contentDescription =  stringResource(R.string.acc_add_photo))
            }
            IconButton(onClick = { callback.invoke() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "close")

            }
        })

    }) {

        val piFocus = rememberPiFocus()

        // READ
        val guestInfo by viewModel.guestInfo.collectAsState()
        val errorInfo by viewModel.errorInfo.collectAsState()
        val lstPhotos by viewModel.lstMedias.collectAsState()
        val keyboardController = LocalSoftwareKeyboardController.current

        if (errorInfo.progress.status == Resource.Status.LOADING) {
            PiProgressIndicator()
        } else if (errorInfo.progress.status == Resource.Status.SUCCESS) {
            callback.invoke()
        }

        var zoomImage by remember { mutableStateOf(false) }
        var selectedMedia by remember { mutableStateOf(MediaInfo(path = EMPTY_STRING))}

        if (zoomImage) {
            PiImagePreviewDialog(imagePath = selectedMedia.path) {
                zoomImage = false
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(start = Dimen.double_space, end = Dimen.double_space)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimen.double_space),
        ) {
            item {
                PhotoSlider(lstMediaInfo = lstPhotos, onClickDelete = { media->
                                                                      viewModel.deleteImage(media)
                }, onClickZoom = { media->
                    selectedMedia = media
                    zoomImage = true
                })
                Spacer(modifier = Modifier.height(Dimen.double_space))
                Column {
                    if (capturePhoto) {
                        CapturePhoto(callback = { path->
                            viewModel.addMedia(path)
                            capturePhoto = false
                        }, noOfPreviousAttempt = viewModel.previousCameraPermissionAttempt(), piSession = viewModel.piSession) {
                            viewModel.updateCameraPermissionAttempt()
                            capturePhoto = false
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimen.double_space))
                    FieldTitle(title = stringResource(R.string.name))

                    Spacer(modifier = Modifier.height(Dimen.space))
                    OutlinedTextField(
                        value = guestInfo.name ?: EMPTY_STRING, onValueChange = { name ->
                            viewModel.updateName(name)

                        }, isError = errorInfo.nameError.isError,
                        placeholder = {
                            Text(text = stringResource(R.string.type_name))
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Person"
                            )
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .piFocus(errorInfo.nameError.focusRequester, piFocus),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    PiErrorView(uiError = errorInfo.nameError)

                    Spacer(modifier = Modifier.height(Dimen.double_space))


                    FieldTitle(title = stringResource(R.string._address))
                    Spacer(modifier = Modifier.height(Dimen.space))
                    OutlinedTextField(
                        value = guestInfo.address ?: EMPTY_STRING, onValueChange = { address ->
                            viewModel.updateAddress(address)
                        }, isError = errorInfo.addressError.isError,
                        placeholder = {
                            Text(text = stringResource(R.string.type_address))
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = stringResource(R.string.icon_address)
                            )
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .piFocus(errorInfo.addressError.focusRequester, piFocus),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    PiErrorView(uiError = errorInfo.addressError)
                    Spacer(modifier = Modifier.height(Dimen.double_space))

                    GiftTypeOption(guestInfo.giftType) { giftType ->
                        viewModel.updateGiftType(giftType)
                    }

                    val keyboardType = when (guestInfo.giftType) {
                        GiftType.GOLD, GiftType.CASH -> {
                            KeyboardType.Number
                        }
                        else -> {
                            KeyboardType.Text
                        }
                    }

                    val description = when (guestInfo.giftType) {
                        GiftType.GOLD -> {
                            stringResource(id = R.string.gold_in_gram)
                        }
                        GiftType.CASH -> {
                            stringResource(id = R.string.cash)
                        }
                        else -> {
                            stringResource(id = R.string.gift_detail)
                        }
                    }
                    FieldTitle(title = description)
                    Spacer(modifier = Modifier.height(Dimen.space))

                    OutlinedTextField(
                        value = guestInfo.giftValue?: EMPTY_STRING, onValueChange = { quantity ->
                            viewModel.updateQuantity(quantity)
                        }, isError = errorInfo.quantity.isError,
                        placeholder = {
                            Text(text = description)
                        },
                        leadingIcon = {
                            Icon(
                                giftImage(guestInfo),
                                contentDescription = stringResource(R.string.icon_address)
                            )
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .piFocus(errorInfo.quantity.focusRequester, piFocus),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done, keyboardType =
                            keyboardType
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                            viewModel.onClickSubmit()
                        })
                    )
                    PiErrorView(uiError = errorInfo.quantity)

                    Spacer(modifier = Modifier.height(Dimen.fourth_space))

                    Button(onClick = {
                        keyboardController?.hide()
                        viewModel.onClickSubmit()
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.submit),
                            modifier = Modifier.padding(Dimen.space),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimen.double_space))

                }
            }

        }
    }

}

@Composable
fun FieldTitle(title:String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Medium
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftTypeOption(type: GiftType, callback: (selectedType: GiftType) -> Unit) {
    Column {
        FieldTitle(title = stringResource(R.string.select_gift_type))
        Spacer(modifier = Modifier.height(Dimen.space))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(modifier = Modifier.padding(Dimen.space), selected = type == GiftType.CASH, onClick = { callback.invoke(GiftType.CASH) }, label = {
                Text(stringResource(R.string.cash), fontWeight = FontWeight.Medium, style = MaterialTheme.typography.titleSmall)
            }, leadingIcon = {
                Icon(imageVector = Icons.Default.Payments, contentDescription = stringResource(R.string.cash))
            }, selectedIcon = {
                Icon(imageVector = Icons.Default.Payments, contentDescription = stringResource(R.string.cash), tint = Cash)
            })

            FilterChip(selected = type == GiftType.GOLD, onClick = { callback.invoke(GiftType.GOLD) }, label = {
                Text(stringResource(R.string.gold), fontWeight = FontWeight.Medium, style = MaterialTheme.typography.titleSmall)
            }, leadingIcon = {
                Icon(imageVector = Icons.Default.Diamond, contentDescription = stringResource(R.string.gold))
            }, selectedIcon = {
                Icon(imageVector = Icons.Default.Diamond, contentDescription = stringResource(R.string.gold), tint = Diamond)
            })


            FilterChip(selected = type == GiftType.OTHERS, onClick = { callback.invoke(GiftType.OTHERS) }, label = {
                Text(stringResource(R.string.others), fontWeight = FontWeight.Medium, style = MaterialTheme.typography.titleSmall)
            }, leadingIcon = {
                Icon(imageVector = Icons.Default.Redeem, contentDescription = stringResource(R.string.others))
            }, selectedIcon = {
                Icon(imageVector = Icons.Default.Redeem, contentDescription = stringResource(R.string.others), tint = Gift)
            })

        }
        Spacer(modifier = Modifier.height(Dimen.space))
    }
}

