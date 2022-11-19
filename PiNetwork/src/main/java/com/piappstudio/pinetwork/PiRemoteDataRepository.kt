/*
 *
 *  * Copyright 2021 All rights are reserved by Pi App Studio
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *
 */

package com.piappstudio.pinetwork

import android.content.Context
import com.piappstudio.pimodel.AppConfig
import com.piappstudio.pimodel.Resource
import com.piappstudio.pimodel.error.ErrorCode
import com.piappstudio.pimodel.error.PIError
import com.piappstudio.pinetwork.di.isNetworkAvailable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PiRemoteDataRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val networkRepo: PiRemoteDataSource) {

    suspend fun fetchAppConfig(): Flow<Resource<AppConfig?>> {
        return makeSafeApiCall {
            networkRepo.fetchAppConfig()
        }
    }

    private suspend fun <T> makeSafeApiCall(api: suspend () -> Resource<T?>) = flow {
        try {
            emit(Resource.loading())
            if (context.isNetworkAvailable()) {
                val response = api.invoke()
                emit(Resource.success(response.data))
            } else {
                emit(Resource.error(error = PIError(code = ErrorCode.NETWORK_NOT_AVAILABLE)))
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            emit(Resource.error(error = PIError(code = ErrorCode.NETWORK_CONNECTION_FAILED)))
        }
    }
}