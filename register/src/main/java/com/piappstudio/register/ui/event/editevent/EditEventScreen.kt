/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.editevent

import PiDateVisualTransform
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.piappstudio.register.R
import com.piappstudio.pimodel.Resource
import com.piappstudio.pitheme.component.PiErrorView
import com.piappstudio.pitheme.component.PiProgressIndicator
import com.piappstudio.pitheme.theme.Dimen
import com.piappstudio.pitheme.theme.LocalAnalytic

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EditEventScreen(viewModel: EditEventViewModel ,callback:()->Unit) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(topBar = {
        SmallTopAppBar(title = {
            Text(text = stringResource(R.string.edit_event))
        },  actions = {
            IconButton(onClick = { callback.invoke()}) {
                Icon(imageVector = Icons.Default.Close, contentDescription ="close" )

            }
        })

    }) {

        val eventInfo by viewModel.eventInfo.collectAsState()
        val errorInfo by viewModel.errorInfo.collectAsState()

        val piTracker = LocalAnalytic.current

        piTracker?.logEvent("Calling from HOME")
        if (errorInfo.progress.status == Resource.Status.LOADING) {
            PiProgressIndicator()
        } else if (errorInfo.progress.status == Resource.Status.SUCCESS) {
            callback.invoke()
        }

        Box {

            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .padding(
                        start = Dimen.double_space,
                        top = Dimen.double_space,
                        end = Dimen.double_space
                    )
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .padding(Dimen.double_space)
                    ) {

                        Text(
                            text = stringResource(id = R.string.title_name),
                            fontWeight = FontWeight.Normal
                        )

                        Spacer(modifier = Modifier.height(Dimen.double_space))
                        OutlinedTextField(
                            value = eventInfo.title ?: "", onValueChange = { name ->
                                viewModel.updateTitle(name)
                            },
                            isError = errorInfo.nameError.isError,
                            placeholder = {
                                Text(text = stringResource(R.string.please_enter_your_event))
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.People,
                                    contentDescription = stringResource(R.string.people)
                                )
                            }, modifier = Modifier
                                .fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                        PiErrorView(uiError = errorInfo.nameError)

                        Spacer(modifier = Modifier.height(Dimen.double_space))
                        Text(
                            text = stringResource(id = R.string._date),
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(Dimen.double_space))
                        OutlinedTextField(
                            value = eventInfo.date ?: "", onValueChange = { eventDate ->
                                viewModel.updateDate(eventDate)

                            },
                            isError = errorInfo.dateError.isError,
                            placeholder = {
                                Text(text = "DD/MM/YYYY")
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.CalendarToday,
                                    contentDescription = "Date"
                                )
                            }, modifier = Modifier
                                .fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
                            visualTransformation = PiDateVisualTransform(),
                            keyboardActions = KeyboardActions(onDone = {
                                keyboardController?.hide()
                                viewModel.onClickSubmit()
                            })
                        )
                        PiErrorView(uiError = errorInfo.dateError)

                        Spacer(modifier = Modifier.height(Dimen.triple_space))

                        Button(onClick = {
                            viewModel.onClickSubmit()
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(R.string.submit),
                                modifier = Modifier.padding(Dimen.space)
                            )
                        }
                    }
                }

            }
        }
    }

}


