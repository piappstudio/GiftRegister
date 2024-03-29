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

package com.piappstudio.pinavigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * To maintin the global error at Main Activity
 */
@Singleton
class ErrorManager @Inject constructor() {
    private val _errorInfo = MutableStateFlow(ErrorInfo())
    val errorInfo: StateFlow<ErrorInfo> = _errorInfo

    fun post(errorInfo: ErrorInfo?) {
        _errorInfo.update { errorInfo ?: ErrorInfo() }
    }
}