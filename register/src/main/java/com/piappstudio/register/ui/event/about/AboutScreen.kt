/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.piappstudio.pinavigation.NavInfo
import com.piappstudio.pitheme.component.PiMediumTopAppBar
import com.piappstudio.pitheme.route.Root
import com.piappstudio.pitheme.route.Route
import com.piappstudio.pitheme.theme.Dimen
import com.piappstudio.register.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(viewModel: AboutViewModel = hiltViewModel()) {
    Scaffold(topBar = {
       PiMediumTopAppBar(title = stringResource(id = R.string.about_screen), navManager = viewModel.navManager)
    }) {

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CardDetails(
                text = stringResource(id = R.string.google_drive_backup),
                imageVector = Icons.Default.NavigateNext, onClick = {
                    viewModel.navManager.navigate(routeInfo = NavInfo(id = Root.DRIVE))
                }

            )
            Divider(color = MaterialTheme.colorScheme.outline.copy(.35f))
            CardDetails(
                text = stringResource(R.string.contact_us),

                imageVector = Icons.Default.NavigateNext, onClick = {
                    viewModel.navManager.navigate(routeInfo = NavInfo(id = Route.Home.EVENT.CONTACT_US))
                }
            )
            Divider(color = MaterialTheme.colorScheme.outline.copy(.35f))

        }
    }
}

@Composable
fun CardDetails(
    text: String,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick.invoke() }
                .padding(Dimen.double_space),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = Dimen.double_space),
                fontWeight = FontWeight.SemiBold
            )
            IconButton(onClick = { onClick.invoke() }) {
                Icon(
                    imageVector = imageVector,
                    modifier = Modifier
                        .padding(start = Dimen.space),
                    contentDescription = "$text Detail"
                )

            }
        }
    }

}