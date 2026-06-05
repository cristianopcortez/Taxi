package br.com.ccortez.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    companion object {
        private val KEY_LAST_USER_ID = stringPreferencesKey("last_user_id")
        private val KEY_LAST_ORIGIN_ADDRESS = stringPreferencesKey("last_origin_address")
        private val KEY_LAST_DESTINY_ADDRESS = stringPreferencesKey("last_destiny_address")
        private val KEY_LAST_DRIVER_ID = stringPreferencesKey("last_driver_id")
        private val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
    }

    override val lastUserId: Flow<String> =
        dataStore.data.map { it[KEY_LAST_USER_ID] ?: "" }

    override val lastOriginAddress: Flow<String> =
        dataStore.data.map { it[KEY_LAST_ORIGIN_ADDRESS] ?: "" }

    override val lastDestinyAddress: Flow<String> =
        dataStore.data.map { it[KEY_LAST_DESTINY_ADDRESS] ?: "" }

    override val lastDriverId: Flow<String> =
        dataStore.data.map { it[KEY_LAST_DRIVER_ID] ?: "" }

    override val themeMode: Flow<ThemeMode> =
        dataStore.data.map {
            runCatching {
                ThemeMode.valueOf(it[KEY_THEME_MODE] ?: ThemeMode.SYSTEM.name)
            }.getOrDefault(ThemeMode.SYSTEM)
        }

    override suspend fun saveLastUserId(userId: String) {
        dataStore.edit { it[KEY_LAST_USER_ID] = userId }
    }

    override suspend fun saveLastOriginAddress(origin: String) {
        dataStore.edit { it[KEY_LAST_ORIGIN_ADDRESS] = origin }
    }

    override suspend fun saveLastDestinyAddress(destiny: String) {
        dataStore.edit { it[KEY_LAST_DESTINY_ADDRESS] = destiny }
    }

    override suspend fun saveLastDriverId(driverId: String) {
        dataStore.edit { it[KEY_LAST_DRIVER_ID] = driverId }
    }

    override suspend fun saveThemeMode(mode: ThemeMode) {
        dataStore.edit { it[KEY_THEME_MODE] = mode.name }
    }
}
