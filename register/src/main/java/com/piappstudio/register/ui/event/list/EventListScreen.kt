/*
* **
* Pi App Studio. All rights reserved.Copyright (c) 2022.
*
*/

package com.piappstudio.register.ui.event.list

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.piappstudio.register.R
import com.piappstudio.pimodel.Constant.EMPTY_STRING
import com.piappstudio.pimodel.EventInfo
import com.piappstudio.pimodel.EventSummary
import com.piappstudio.pimodel.GiftType
import com.piappstudio.pimodel.toCurrency
import com.piappstudio.pitheme.component.piShadow
import com.piappstudio.pitheme.component.rememberPiScrollBehaviour
import com.piappstudio.pitheme.theme.Diamond
import com.piappstudio.pitheme.theme.Dimen
import com.piappstudio.pitheme.theme.Gift
import com.piappstudio.pitheme.theme.People
import com.piappstudio.register.ui.PiDialog
import kotlinx.coroutines.launch

// MVVM = Model- View- ViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun EventListScreen(
    lstEvents: List<EventSummary>,
    onClickSetting: () -> Unit,
    onClickFloatingAction: () -> Unit,
    onClickDeleteItem: ((eventSummary: EventSummary) -> Unit)? = null,
    onClickEventItem: ((eventInfo: EventInfo?) -> Unit)? = null
) {

    val coroutine = rememberCoroutineScope()

    val scrollBehaviour = rememberPiScrollBehaviour()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(title = {
                Text(text = stringResource(R.string.title_events))
            }, actions = {
                IconButton(onClick = { onClickSetting() }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(R.string.acc_setting)
                    )
                }
            }, scrollBehavior = scrollBehaviour)
        }) {


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(
                        start = Dimen.double_space,
                        end = Dimen.double_space,
                        bottom = Dimen.double_space
                    )
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimen.double_space),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                items(lstEvents) { event ->


                    var unread by remember { mutableStateOf(false) }
                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            if (it == DismissValue.DismissedToEnd) unread = !unread
                            it != DismissValue.DismissedToEnd
                        }
                    )

                    var showDeleteOption by remember { mutableStateOf(false) }
                    if (showDeleteOption) {
                        PiDialog(
                            title = (stringResource(R.string.delete_title)),
                            message = (stringResource(R.string.delete_message)),
                            lottieImages = R.raw.delete,
                            onClick = { index ->
                                showDeleteOption = false
                                coroutine.launch {
                                    dismissState.reset()
                                }
                                if (index == 1) {
                                    onClickDeleteItem?.invoke(event)
                                }
                            },
                            enableCancel = true
                        )
                    }



                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier.padding(vertical = 4.dp),
                        directions = setOf(
                            DismissDirection.StartToEnd,
                            DismissDirection.EndToStart
                        ),
                        background = {
                            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    DismissValue.Default -> Color.LightGray
                                    DismissValue.DismissedToEnd -> Color.Green
                                    DismissValue.DismissedToStart -> Color.Red
                                }
                            )
                            val alignment = when (direction) {
                                DismissDirection.StartToEnd -> Alignment.CenterStart
                                DismissDirection.EndToStart -> Alignment.CenterEnd

                            }
                            val icon = when (direction) {
                                DismissDirection.StartToEnd -> Icons.Default.Done
                                DismissDirection.EndToStart -> Icons.Default.Delete

                            }
                            val scale by animateFloatAsState(
                                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                            )

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .padding(start = Dimen.double_space, end = Dimen.double_space)
                                    .background(color),
                                contentAlignment = alignment
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically

                                ) {


                                    Column(
                                        modifier = Modifier,
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        IconButton(onClick = {
                                            showDeleteOption = true
                                            showDeleteOption = true
                                        }) {
                                            Icon(
                                                icon,
                                                contentDescription = stringResource(R.string.icons),
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier
                                                    .scale(scale)
                                                    .padding(all = Dimen.space)
                                            )

                                        }
                                        Text(
                                            text = stringResource(id = R.string.delete),
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier
                                                .scale(scale)
                                                .padding(
                                                    start = Dimen.double_space,
                                                    end = Dimen.double_space,
                                                )
                                        )

                                    }
                                }

                            }
                        },
                        dismissContent = {
                            Card(
                                elevation = animateDpAsState(
                                    if (dismissState.dismissDirection != null) 4.dp else 0.dp
                                ).value

                            ) {

                                RenderEventView(eventSummary = event) {
                                    onClickEventItem?.invoke(event.eventInfo)

                                }


                            }

                        }
                    )

                }

            }

            if (lstEvents.isEmpty()) {
                EventEmptyScreen()
            }


            ExtendedFloatingActionButton(
                onClick = { onClickFloatingAction.invoke() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(Dimen.double_space)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.acc_add_new_event)
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderEventView(eventSummary: EventSummary, callBack: (() -> Unit)? = null) {

    val model = eventSummary.eventInfo

    Card(modifier = Modifier
        .fillMaxWidth()
        .piShadow()
        .clickable {
            callBack?.invoke()
        }) {
        Column(modifier = Modifier.padding(Dimen.double_space)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(0.6f, true)) {
                    Text(
                        text = model.title ?: EMPTY_STRING,
                        style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(Dimen.space))
                    Text(
                        text = model.date ?: EMPTY_STRING,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(modifier = Modifier.padding(start = Dimen.double_space),
                    text = eventSummary.lstGuestInfo?.filter { it.giftType == GiftType.CASH }
                        ?.sumOf { it.giftValue?.toDouble() ?: 0.0 }?.toCurrency() ?: "0",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(Dimen.double_space))
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimen.space),
                modifier = Modifier.fillMaxWidth()
            ) {
                ItemCountView(
                    modifier = Modifier.weight(1f, true),
                    imageVector = Icons.Default.People,
                    text = eventSummary.lstGuestInfo?.size?.toString() ?: "0",
                    color = People
                )
                ItemCountView(
                    modifier = Modifier.weight(1f, true),
                    imageVector = Icons.Default.Diamond,
                    text = eventSummary.lstGuestInfo?.filter { it.giftType == GiftType.GOLD }
                        ?.sumOf { it.giftValue?.toDouble() ?: 0.0 }?.toString() ?: "0",
                    color = Diamond
                )
                ItemCountView(
                    modifier = Modifier.weight(1f, true),
                    imageVector = Icons.Default.Redeem,
                    text = eventSummary.lstGuestInfo?.filter { it.giftType == GiftType.OTHERS }?.size?.toString()
                        ?: "0",
                    color = Gift
                )
            }
        }
    }

}


@Composable
fun ItemCountView(
    imageVector: ImageVector,
    text: String?,
    modifier: Modifier = Modifier,
    color: Color,
    isShadowEnabled: Boolean = true
) {

    var updatedModifier = modifier.fillMaxSize()

    if (isShadowEnabled) {
        updatedModifier = updatedModifier.piShadow(Dimen.half_space)
    }
    Column(
        modifier = updatedModifier
            .background(color)
            .padding(Dimen.space),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = text,
            modifier = Modifier.padding(top = Dimen.half_space),
            tint = Color.White
        )
        Text(
            text = text ?: "$100",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }


}

@Composable
fun RenderEmptyScreen() {


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {


}
