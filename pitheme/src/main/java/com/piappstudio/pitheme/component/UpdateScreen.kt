/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pitheme.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.piappstudio.pitheme.R
import com.piappstudio.pitheme.theme.Dimen

@Preview
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UpdateScreen(appUrl: String = "https://play.google.com/store/apps/details?id=com.piappstudio.giftregister") {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {

        Surface {


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimen.half_space),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(
                                start = Dimen.double_space,
                                end = Dimen.double_space
                            ),
                    ) {

                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier.height(Dimen.space))
                            Text(
                                text = stringResource(R.string.update_title),
                                modifier = Modifier.padding(Dimen.double_space),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Black,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            PILottie(resourceId = R.raw.update, modifier = Modifier.height(Dimen.slider_height))
                            Spacer(modifier = Modifier.height(Dimen.space))
                            Text(
                                text = stringResource(R.string.we_add_lost_of_new_features_and),
                                modifier = Modifier.padding(
                                    start = Dimen.double_space,
                                    end = Dimen.double_space
                                ),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium,
                            )

                            Spacer(modifier = Modifier.height(Dimen.double_space))

                            val uriHandler = LocalUriHandler.current

                            Button(onClick = {
                                uriHandler.openUri(appUrl)
                            }) {
                                Text(
                                    text = stringResource(R.string.update_app),
                                    modifier = Modifier.padding(
                                        start = Dimen.double_space,
                                        end = Dimen.double_space
                                    ),
                                    fontWeight = FontWeight.Medium,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Spacer(modifier = Modifier.height(Dimen.double_space))
                        }
                    }

                }

            }
        }
    }
}
