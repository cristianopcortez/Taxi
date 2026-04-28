package br.com.ccortez.taxi.provider

import br.com.ccortez.taxi.BuildConfig
import br.com.ccortez.core.common.utils.BuildConfigFields
import br.com.ccortez.core.common.utils.BuildConfigFieldsProvider

class ApplicationBuildConfigFieldsProvider : BuildConfigFieldsProvider {

    override fun get(): BuildConfigFields = BuildConfigFields(
        buildType = BuildConfig.BUILD_TYPE,
        versionCode = BuildConfig.VERSION_CODE,
        versionName = BuildConfig.VERSION_NAME
    )
}