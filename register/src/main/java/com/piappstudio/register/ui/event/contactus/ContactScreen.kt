/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.contactus

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.piappstudio.register.R
import com.piappstudio.pitheme.component.PiMediumTopAppBar
import com.piappstudio.pitheme.component.rememberPiScrollBehaviour
import com.piappstudio.pitheme.theme.Dimen

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(viewModel: ContactScreenViewModel = hiltViewModel()) {

    val scrollBehavior = rememberPiScrollBehaviour()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PiMediumTopAppBar(
                title = stringResource(id = R.string.contact_us),
                navManager = viewModel.navManager,
                scrollBehavior = scrollBehavior
            )
        }
    ) {

        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            val (list,appversion) = createRefs()

            Text(
                text = stringResource(R.string.app_version) + viewModel.getAppVersion(),
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(Dimen.double_space)
                    .constrainAs(appversion) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
            )

            val scroll = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(Dimen.double_space)
                    .verticalScroll(scroll)
                    .constrainAs(list) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(appversion.top)
                        height = Dimension.fillToConstraints
                    },
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = stringResource(R.string.email_id),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.piappstudio_gmail),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(Dimen.triple_space))

                Text(
                    text = "Our Apps",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                )



                CardDetails(
                    text = stringResource(R.string.digital_diary),
                    painter = painterResource(id = R.drawable.diary), appUrl = stringResource(R.string.digital_diary_url)
                )

                CardDetails(
                    text = "Fresh Look",
                    painter = painterResource(id = R.drawable.freshlook), appUrl = stringResource(R.string.fresh_look_url)
                )
                CardDetails(
                    text = "Travel Insurance",
                    painter = painterResource(id = R.drawable.travelinsurance), appUrl = stringResource(
                                            R.string.travel_insurance_url)
                )

                Spacer(modifier = Modifier.height(Dimen.fourth_space))

                Text(
                    text = "Social Media",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(Dimen.double_space))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    SocialMediaCard(
                        painter = painterResource(id = R.drawable.youtube), url = stringResource(
                            R.string.youtube_link
                        )
                    )
                    SocialMediaCard(
                        painter = painterResource(id = R.drawable.twitter), url = stringResource(
                            R.string.twitter_link
                        )
                    )
                    SocialMediaCard(
                        painter = painterResource(id = R.drawable.linkedin), url = stringResource(
                            R.string.linkedin_link
                        )
                    )
                    SocialMediaCard(
                        painter = painterResource(id = R.drawable.facebook), url = stringResource(
                            R.string.facebook_link
                        )
                    )
                }

            }
        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetails(painter: Painter, text: String,appUrl: String) {
    val uriHandler = LocalUriHandler.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimen.space)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    uriHandler.openUri(appUrl)
                }
                .padding(Dimen.space),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Row (verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painter,
                    contentDescription = text,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(Dimen.space),
                    contentScale = ContentScale.Crop
                )
                Text(
                    modifier = Modifier.padding(start = Dimen.double_space),
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            IconButton(onClick = {
                uriHandler.openUri(appUrl)
            }) {
                Icon(
                    Icons.Filled.NavigateNext,
                    contentDescription = text,
                )

            }


        }

    }


}

@Composable
fun SocialMediaCard(painter: Painter, url: String) {
    val uriHandler = LocalUriHandler.current
    IconButton(onClick = {
        // Code
        uriHandler.openUri(url)
    })
    {
        Image(
            painter = painter,
            contentDescription = url,
            modifier = Modifier
                .size(55.dp)
                .padding(Dimen.space),
            contentScale = ContentScale.Crop
        )


    }
}
