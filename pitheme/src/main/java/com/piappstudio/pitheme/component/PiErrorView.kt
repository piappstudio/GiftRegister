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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.piappstudio.pitheme.theme.Dimen

@Composable
fun PiErrorView(uiError: UiError, modifier: Modifier = Modifier) {
    if (uiError.isError) {
        Spacer(modifier = Modifier.height(Dimen.half_space))
        Text(
            modifier = modifier,
            text = uiError.dynamicText?: stringResource(id = uiError.errorText),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}