/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.piappstudio.pimodel.Constant.EMPTY_STRING
import com.piappstudio.pitheme.theme.Dimen
import com.piappstudio.register.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortScreen(
    viewModel: SortScreenViewModel = hiltViewModel(),
    filerOption: FilterOption,
    onClickClose: (() -> Unit)? = null,
    onClickViewResult: ((updatedOption: FilterOption) -> Unit)? = null
) {

    LaunchedEffect(key1 = filerOption) {
        viewModel.loadPreviousOption(filerOption)
    }

    Scaffold(topBar = {
        MediumTopAppBar(title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.sort_filter)
            )
        },
            colors = TopAppBarDefaults.mediumTopAppBarColors(MaterialTheme.colorScheme.onPrimary),
            actions = {
                IconButton(onClick = {
                    onClickClose?.invoke()
                }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close")

                }
            })

    }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {


            // List, Map
            val mapOfOption by viewModel.mapOfOption.collectAsState()

            val lstOfOption = mapOfOption.keys.toTypedArray()
            lstOfOption.sortBy { it.title }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimen.double_space),
                verticalArrangement = Arrangement.spacedBy(Dimen.space),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                items(lstOfOption) { option ->
                    TopOption(option = option, mapOfOption[option], onClickTopOption = {
                        viewModel.updateTopClick(option)
                    }, onClickSubList = { subOption ->
                        viewModel.updateSubListClick(option, subOption)
                    })
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                }
                item {
                    Spacer(modifier = Modifier.height(Dimen.triple_space))
                    Button(
                        onClick = { onClickViewResult?.invoke(viewModel.currentFilterOption) },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.view__result))
                    }
                }
            }

        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopOption(
    option: Option,
    subList: List<Option>?,
    onClickTopOption: (() -> Unit)? = null,
    onClickSubList: ((option: Option) -> Unit)? = null
) {
    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClickTopOption?.invoke()
                }
                .padding(Dimen.space),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = option.title ?: EMPTY_STRING,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = {
                onClickTopOption?.invoke()
            }) {
                Icon(
                    imageVector = if (option.isSelected) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = stringResource(R.string.navigate)
                )

            }
        }

        if (option.isSelected) {
            subList?.let { lstOption ->
                Column {
                    lstOption.forEach { subOption ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onClickSubList?.invoke(subOption)
                            }) {
                            RadioButton(selected = subOption.isSelected, onClick = {
                                onClickSubList?.invoke(subOption)
                            })
                            Text(text = subOption.title ?: EMPTY_STRING)
                        }
                    }
                }
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SortScreen(filerOption = FilterOption())

}






