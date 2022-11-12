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

package com.piappstudio.pimodel.database.di

import android.content.Context
import androidx.room.Room
import com.piappstudio.pimodel.database.PiGiftDatabase
import com.piappstudio.pimodel.database.dao.IEventDao
import com.piappstudio.pimodel.database.dao.IGuestDao
import com.piappstudio.pimodel.database.dao.IMediaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// 2 class, 1. Interface, 2. Abstract.

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context):PiGiftDatabase {
        return Room.databaseBuilder(context,
            PiGiftDatabase::class.java,
            "pi_gift_database").fallbackToDestructiveMigration().build()
    }


    @Provides
    fun provideEventDao(database: PiGiftDatabase): IEventDao {
        return database.eventDao()
    }

    @Provides
    fun provideGuestDao(database: PiGiftDatabase):IGuestDao {
        return database.guestDao()
    }

    @Provides
    fun provideMedia(database: PiGiftDatabase):IMediaDao {
        return database.mediaDao()
    }
}

