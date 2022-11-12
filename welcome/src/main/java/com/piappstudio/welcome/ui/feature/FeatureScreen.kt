package com.piappstudio.welcome.ui.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.piappstudio.pitheme.theme.Dimen

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun FeatureScreen() {
    @Composable
    fun ItemView() {
        Row(modifier = Modifier.padding(start = Dimen.double_space, end = Dimen.double_space)) {
            Text(text = "A")
            Text(text = "B")
            Text(text = "Element C")
        }
    }


    LazyColumn (verticalArrangement = Arrangement.spacedBy(Dimen.double_space), modifier = Modifier.fillMaxWidth()) {
        stickyHeader { 
            Text(text = "Sticky Header",
                modifier = Modifier.fillMaxWidth().background(Color.Red). padding(Dimen.double_space))
        }
        items(100) {
            ItemView()
        }
    }
}