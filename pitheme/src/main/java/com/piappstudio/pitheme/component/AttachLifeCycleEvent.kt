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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import timber.log.Timber

@Composable
fun AttachLifeCycleEvent(onCreate: (() -> Unit)? = null,
                         onStart: (() -> Unit)? = null,
                         onResume: (() -> Unit)? = null) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifeCycleOwner) {
        val eventObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    Timber.d("OnCreate")
                    onCreate?.invoke()
                }
                Lifecycle.Event.ON_START -> {
                    Timber.d("On Start")
                    onStart?.invoke()
                }
                Lifecycle.Event.ON_RESUME -> {
                    Timber.d("On Resume")
                    onResume?.invoke()
                }
                else -> {

                }
            }

        }

        lifeCycleOwner.lifecycle.addObserver(eventObserver)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(eventObserver)
        }
    }

}