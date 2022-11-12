/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.editguest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.piappstudio.register.R
import com.piappstudio.pimodel.MediaInfo
import com.piappstudio.pitheme.theme.Dimen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoSlider(
    lstMediaInfo: List<MediaInfo>,
    modifier: Modifier = Modifier,
    onClickDelete: (mediaInfo: MediaInfo) -> Unit,
    onClickZoom: (mediaInfo: MediaInfo) -> Unit
) {
    if (lstMediaInfo.isNotEmpty()) {
        LazyRow {
            items(lstMediaInfo) { mediaInfo ->
                Box(modifier = modifier.padding(Dimen.doubleSpace)) {

                    Card(onClick = { onClickZoom(mediaInfo) }) {
                        AsyncImage(
                            model = mediaInfo.path.toUri(),
                            contentDescription = "Slide show image",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .size(Dimen.small_lottie_icon)
                        )
                    }
                    IconButton(onClick = { onClickDelete(mediaInfo) }, modifier = Modifier.align(
                        Alignment.TopEnd)) {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.acc_delete),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }

            }
        }
    }

    /*
    if (lstMediaInfo.isNotEmpty()) {
        HorizontalPager(count = lstMediaInfo.size, itemSpacing = Dimen.doubleSpace, modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) { pageIndex->
            Timber.d("Rendering the image $pageIndex")

            AsyncImage(model = lstMediaInfo[pageIndex].path.toUri(), contentDescription = "Slide show image", contentScale = ContentScale.Fit)
        }
    } else {
    }*/

}