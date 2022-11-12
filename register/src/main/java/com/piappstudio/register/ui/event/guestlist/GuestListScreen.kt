/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.guestlist

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.piappstudio.pitheme.theme.Cash
import com.piappstudio.pitheme.theme.Diamond
import com.piappstudio.pitheme.theme.Gift
import com.piappstudio.pitheme.theme.People
import com.piappstudio.register.R
import com.piappstudio.pimodel.*
import com.piappstudio.pimodel.Constant.EMPTY_STRING
import com.piappstudio.pinavigation.NavInfo
import com.piappstudio.pitheme.component.PiProgressIndicator
import com.piappstudio.pitheme.component.getColor
import com.piappstudio.pitheme.component.piTopBar
import com.piappstudio.pitheme.route.Route
import com.piappstudio.pitheme.theme.Dimen
import com.piappstudio.register.ui.PiDialog
import com.piappstudio.register.ui.event.filter.SortScreen
import com.piappstudio.register.ui.event.list.EventEmptyScreen
import kotlinx.coroutines.launch

fun giftImage(guestInfo: GuestInfo): ImageVector {
    return when (guestInfo.giftType) {
        GiftType.GOLD -> {
            Icons.Default.Diamond
        }
        GiftType.CASH -> {
            Icons.Default.Payments
        }
        else -> {
            Icons.Default.Redeem
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun GuestListScreen(
    eventInfo: EventInfo?,
    viewModel: GuestListViewModel = hiltViewModel(),
    callback: () -> Unit,
    onClickGuestItem: (guestInfo: GuestInfo) -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchGuest()
    }

    val guestListState by viewModel.guestListState.collectAsState()

    if (guestListState.progress == Resource.Status.LOADING) {
        PiProgressIndicator()
    }

    val lstGuest = guestListState.lstGuest

    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val searchOption = guestListState.searchOption

    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0XFF0F9D58))
            ) {
                SortScreen(filerOption = searchOption.filterOption, onClickClose = {
                    coroutineScope.launch {
                        bottomSheetScaffoldState.hide()
                    }
                }, onClickViewResult = {
                    coroutineScope.launch {
                        bottomSheetScaffoldState.hide()

                    }

                    viewModel.updateFilter(it)
                })
            }
        }
    ) {
        Scaffold {

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(it)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .piTopBar()
                            .padding(top = Dimen.space, bottom = Dimen.space),
                    ) {
                        IconButton(onClick = { viewModel.navManager.navigate(NavInfo(Route.Control.Back)) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(
                                    id = R.string.back
                                )
                            )
                        }
                        Column(modifier = Modifier.padding(start = Dimen.space)) {
                            Text(
                                text = eventInfo?.title ?: stringResource(R.string.guestlist),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = eventInfo?.date ?: EMPTY_STRING,
                                style = MaterialTheme.typography.titleSmall
                            )

                        }
                    }

                    SearchWithFilterView(
                        modifier = Modifier.padding(Dimen.space),
                        viewModel
                    )
                    Surface {

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Dimen.space),
                            horizontalArrangement = Arrangement.spacedBy(Dimen.space)
                        ) {
                            item {

                                AssistChip(onClick = {
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.animateTo(ModalBottomSheetValue.Expanded)
                                    }
                                }, label = {
                                    Text(
                                        text = stringResource(R.string.filter),
                                        fontWeight = FontWeight.Black
                                    )

                                }, leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Tune,
                                        contentDescription = stringResource(R.string.acc_filter)
                                    )
                                })
                                Spacer(modifier = Modifier.width(Dimen.space))

                                ItemView(

                                    imageVector = Icons.Default.People,
                                    text = lstGuest.size.toString(),
                                    color = People
                                )
                                ItemView(
                                    modifier = Modifier.padding(start = Dimen.space),
                                    imageVector = Icons.Default.Payments,
                                    text = lstGuest.filter { it.giftType == GiftType.CASH }
                                        .sumOf { it.giftValue?.toDouble() ?: 0.0 }.toCurrency(),
                                    color = Cash
                                )
                                ItemView(
                                    modifier = Modifier.padding(start = Dimen.space),
                                    imageVector = Icons.Default.Diamond,
                                    text = lstGuest.filter { it.giftType == GiftType.GOLD }
                                        .sumOf { it.giftValue?.toDouble() ?: 0.0 }.toString()
                                        ?: "0",
                                    color = Diamond
                                )
                                ItemView(
                                    modifier = Modifier.padding(start = Dimen.space),
                                    imageVector = Icons.Default.Redeem,
                                    text = lstGuest.filter { it.giftType == GiftType.OTHERS }.size.toString(),
                                    color = Gift
                                )

                            }

                        }
                    }

                    if (lstGuest.isEmpty()) {
                        EventEmptyScreen()
                    }
                    LazyColumn(
                        modifier = Modifier
                            .padding(it)
                            .padding(start = Dimen.space, end = Dimen.space)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(Dimen.double_space),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        val filteredItem = guestListState.filteredItem
                        filteredItem.forEach { (key, list) ->

                            stickyHeader {
                                Surface(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = key,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall,
                                        modifier = Modifier.padding(Dimen.space)
                                    )

                                }
                            }

                            items(list) { guest ->
                                // Rendering the row
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
                                        title = stringResource(id = R.string.delete_title),
                                        message = stringResource(id = R.string.delete_message),
                                        lottieImages=R.raw.delete,

                                        onClick = { index ->
                                            showDeleteOption = false
                                            coroutineScope.launch {
                                                dismissState.animateTo(DismissValue.Default)
                                            }
                                            if (index == 1) {
                                                viewModel.delete(guest)
                                            }
                                        },
                                        enableCancel = true
                                    )
                                }


                                SwipeToDismiss(
                                    state = dismissState,
                                    directions = setOf(
                                        DismissDirection.StartToEnd,
                                        DismissDirection.EndToStart
                                    ),
                                    background = {
                                        val direction =
                                            dismissState.dismissDirection ?: return@SwipeToDismiss
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
                                                .padding(
                                                    start = Dimen.double_space,
                                                    end = Dimen.double_space
                                                )
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
                                                    IconButton(onClick = { showDeleteOption = true}) {
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
                                        Card{
                                            RenderGuestListView(
                                                guestInfo = guest,
                                                viewModel,
                                                onClickGuestItem
                                            )

                                        }

                                    }
                                )

                            }
                        }
                    }
                }


                ExtendedFloatingActionButton(
                    onClick = { callback.invoke() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(Dimen.double_space)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Guest list"
                    )

                }

            }

        }
    }
}

@Composable
fun SearchWithFilterView(
    modifier: Modifier, viewModel: GuestListViewModel
) {
    val guestListState by viewModel.guestListState.collectAsState()
    val searchOption = guestListState.searchOption
    Surface(modifier = modifier) {
        OutlinedTextField(value = searchOption.text ?: EMPTY_STRING,
            onValueChange = { text ->
                viewModel.updateSearchText(
                    text
                )
            },
            placeholder = {
                Text(text = stringResource(R.string.search))
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search)
                )
            }, trailingIcon = {
                IconButton(onClick = {
                    viewModel.updateSearchText("")
                }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close Icons")
                }
            }

        )


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderGuestListView(
    guestInfo: GuestInfo,
    viewModel: GuestListViewModel,
    onClickGuestItem: (guestInfo: GuestInfo) -> Unit
) {

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(start = Dimen.space, end = Dimen.space)
        .clickable { onClickGuestItem.invoke(guestInfo) }) {
        Row(
            modifier = Modifier
                .padding(start = Dimen.double_space, end = Dimen.double_space)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = guestInfo.name ?: EMPTY_STRING,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black
                )
                guestInfo.address?.let {
                    Spacer(modifier = Modifier.height(Dimen.space))
                    Text(
                        text = guestInfo.address ?: EMPTY_STRING,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                guestInfo.phone?.let {
                    Spacer(modifier = Modifier.height(Dimen.double_space))
                    Text(
                        text = guestInfo.phone ?: EMPTY_STRING,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }


            val image = giftImage(guestInfo)
            Column(
                modifier = Modifier.padding(top = Dimen.double_space, bottom = Dimen.double_space),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = image,
                    contentDescription = guestInfo.giftValue,
                    tint = guestInfo.getColor()
                )
                Text(
                    text = guestInfo.displayGiftValue() ?: "N/A",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemView(modifier: Modifier = Modifier, imageVector: ImageVector, text: String?, color: Color) {
    text?.let {
        ElevatedAssistChip(modifier = modifier, onClick = {
        }, label = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black
            )
        }, leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = text, tint = color)
        })
    }

}

