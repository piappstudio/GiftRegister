/*
 *
 *   *
 *   *  * Copyright 2022 All rights are reserved by Pi App Studio
 *   *  *
 *   *  * Unless required by applicable law or agreed to in writing, software
 *   *  * distributed under the License is distributed on an "AS IS" BASIS,
 *   *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   *  * See the License for the specific language governing permissions and
 *   *  * limitations under the License.
 *   *  *
 *   *
 *
 */

package com.piappstudio.pitheme.component

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.text.Html
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.piappstudio.pitheme.theme.Cash
import com.piappstudio.pitheme.theme.Diamond
import com.piappstudio.pitheme.theme.Gift
import com.piappstudio.pimodel.GiftType
import com.piappstudio.pimodel.GuestInfo
import com.piappstudio.pitheme.theme.Dimen
import timber.log.Timber
import java.text.DecimalFormat
import java.util.*


fun Modifier.piShimmer(visible: Boolean): Modifier = composed {
    placeholder(
        visible = visible,
        highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
        color = Color.Gray.copy(0.2f),
        shape = RoundedCornerShape(4.dp)
    )
}

fun Modifier.piImageCircle(size: Dp = 100.dp, color: Color = Color.Gray): Modifier = composed {
    this
        .size(size)
        .clip(CircleShape)
        .border(Dimen.half_space, color = color, CircleShape)
}

fun Modifier.piShadow(elevation: Dp = Dimen.space): Modifier = composed {
    shadow(elevation = elevation, shape = RoundedCornerShape(elevation), clip = true)
}

@Composable
fun ComposableLifecycle(
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PiTopBarScrollBehaviour(): TopAppBarScrollBehavior {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    return TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec,
        rememberTopAppBarScrollState()
    )
}


/**
 * To open the rul
 */
fun Context.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    if (intent.resolveActivity(packageManager) != null) {
        this.startActivity(intent)
    }

}

fun Context.composeEmail(emailAddress: String, subject: String? = null) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, emailAddress)
    subject?.let {
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    if (intent.resolveActivity(packageManager) != null) {
        this.startActivity(intent)
    }
}


fun Context.dialNumber(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_CALL)
    intent.data = android.net.Uri.parse("tel:$phoneNumber")
    this.startActivity(intent)

}

fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    this.startActivity(intent)
}

fun String.toHtml(): String {
    return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
}

fun Context.isPermissionAvailable(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun DashedDivider(
    thickness: Dp,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    phase: Float = 10f,
    intervals: FloatArray = floatArrayOf(10f, 10f),
    modifier: Modifier
) {
    Canvas(
        modifier = modifier
    ) {
        val dividerHeight = thickness.toPx()
        drawRoundRect(
            color = color,
            style = Stroke(
                width = dividerHeight,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = intervals,
                    phase = phase
                )
            )
        )
    }
}

val df = DecimalFormat("#.##")

fun String?.toPiDouble(): Double {
    if (this == null) return 0.00
    val value = this.toDoubleOrNull() ?: 0.00
    return String.format("%.2f", value).toDoubleOrNull() ?: 0.00
}

fun String.toCurrency(): String {
    //val currency = Currency.getInstance(Locale.getDefault())
    return "$$this"
}



fun String.toColor(): Color? {
    try {
        return Color(android.graphics.Color.parseColor("#$this"))
    } catch (ex: Exception) {
        Timber.e(ex)
    }

    return null
}

fun GuestInfo.getColor():Color {
    return when (this.giftType) {
        GiftType.CASH -> {
            Cash
        }
        GiftType.GOLD -> {
            Diamond
        }
        GiftType.OTHERS -> {
            Gift
        }
    }
}

@Composable
fun Modifier.piTopBar():Modifier {
    return this
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.outline.copy(0.1f))
}