/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.list

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.piappstudio.pitheme.theme.Dimen
import com.piappstudio.register.R

@Composable
fun EventEmptyScreen() {


    Box(modifier = Modifier. fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(Dimen.fourth_space)
                .align(alignment = Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Default.Search,
                contentDescription ="Empty Events" ,
                modifier = Modifier.size(Dimen.person_image),
                tint = MaterialTheme.colorScheme.primary )
            Spacer(modifier = Modifier.height(Dimen.double_space))

            Text(text = stringResource(R.string.your_event_is_empty),
                style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(Dimen.space))

            Text(text = stringResource(R.string.lets_add_your_event),
                style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(all = Dimen.space),
                color = MaterialTheme.colorScheme.outline)

        }
    }
}