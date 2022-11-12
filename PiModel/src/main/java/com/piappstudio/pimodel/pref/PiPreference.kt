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

package com.piappstudio.pimodel.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

object PiPrefKey {
    const val PROFILE = "profile"
    const val CALL_PERMISSION = "call_permission"
    const val FILE_PERMISSION = "file_permission"
    const val CAMERA_PERMISSION = "file_permission"
    const val LAST_SYNC_TIME = "last_sync_time"
    const val GOOGLE_DRIVE_FOLDER_ID = "google_drive_folder_id"
    const val JSON_FILE_ID = "json_file_id"
    const val IS_USER_LOGGED_IN = "is_user_logged_in"

}
@Singleton
class PiPreference @Inject constructor(@ApplicationContext val context: Context) {

    private val sharedPrefsFile: String = "aladdin"

    // Although you can define your own key generation parameter specification, it's
    // recommended that you use the value specified here.
    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        sharedPrefsFile,
        mainKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )


    fun <T> save(key: String, value: T) {
        with(sharedPreferences.edit()) {
            when (value) {
                is Boolean -> {
                    putBoolean(key, value)
                }
                is Float -> {
                    putFloat(key, value)
                }
                is Long -> {
                    putLong(key, value)
                }
                is String -> {
                    putString(key, value)
                }
                is Int -> {
                    putInt(key, value)
                }
            }
            apply()
        }
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }
    fun remove(key:String) {
        with(sharedPreferences.edit()) {
            remove(key)
            apply()
        }
    }

}