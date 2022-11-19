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

import com.piappstudio.pimodel.AppConfig
import com.piappstudio.pimodel.Resource
import com.piappstudio.pimodel.error.PIError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
// Class that helps you to fetch the data from server (network)
 class PiRemoteDataSource @Inject constructor(private val iMoiConfig: IMoiConfig){
    suspend fun fetchAppConfig(): Resource<AppConfig?> {
        return withContext(Dispatchers.IO) {
            val response = iMoiConfig.fetchAppConfig()
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error(null, error = PIError(response.code()))
            }
        }
    }
}