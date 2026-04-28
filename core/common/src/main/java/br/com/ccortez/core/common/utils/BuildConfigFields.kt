package br.com.ccortez.core.common.utils

data class BuildConfigFields(
    val buildType: String,
    val versionCode: Int,
    val versionName: String
)

interface BuildConfigFieldsProvider {

    fun get(): BuildConfigFields
}