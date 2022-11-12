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

import com.piappstudio.pimodel.error.PIError

enum class ErrorState {
    NONE,
    NEGATIVE,
    POSITIVE,
    SERVER_FAIL
}

data class ErrorInfo(
    val message: String? = null,
    val errorState: ErrorState = ErrorState.NONE,
    val piError: PIError?= null,
    val action: (() -> Unit)? = null
)
