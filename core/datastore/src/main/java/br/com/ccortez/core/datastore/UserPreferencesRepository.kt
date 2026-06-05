package br.com.ccortez.core.datastore

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val lastUserId: Flow<String>
    val lastOriginAddress: Flow<String>
    val lastDestinyAddress: Flow<String>
    val lastDriverId: Flow<String>
    val themeMode: Flow<ThemeMode>

    suspend fun saveLastUserId(userId: String)
    suspend fun saveLastOriginAddress(origin: String)
    suspend fun saveLastDestinyAddress(destiny: String)
    suspend fun saveLastDriverId(driverId: String)
    suspend fun saveThemeMode(mode: ThemeMode)
}
