package com.dergoogler.mmrl.model.online

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import com.dergoogler.mmrl.Platform
import com.dergoogler.mmrl.ui.providable.LocalUserPreferences
import com.dergoogler.mmrl.utils.Utils
import com.squareup.moshi.JsonClass
import dev.dergoogler.mmrl.compat.ext.isNotNullOrBlank
import dev.dergoogler.mmrl.compat.ext.isNotNullOrEmpty

@JsonClass(generateAdapter = true)
data class OnlineModule(
    val id: String,
    val name: String,
    val version: String,
    val versionCode: Int,
    val author: String,
    val description: String,
    val track: TrackJson,
    val versions: List<VersionItem>,

    val maxApi: Int? = null,
    val minApi: Int? = null,

    val size: Int? = null,
    val categories: List<String>? = null,
    val icon: String? = null,
    val homepage: String? = null,
    val donate: String? = null,
    val support: String? = null,
    val cover: String? = null,
    val screenshots: List<String>? = null,
    val license: String? = "",
    val readme: String? = null,
    val verified: Boolean? = null,

    val require: List<String>? = null,
    val arch: List<String>? = null,
    val devices: List<String>? = null,

    val manager: ModuleManager? = null,
    val root: ModuleRoot? = null,
    val note: ModuleNote? = null,
    val features: ModuleFeatures? = null,
) {
    val versionDisplay get() = Utils.getVersionDisplay(version, versionCode)
    val hasLicense
        get() = license.orEmpty().isNotBlank()
                && license.orEmpty().uppercase() != "UNKNOWN"

    val hasRequire = require.orEmpty().isNotEmpty()
    val hasIcon = icon.orEmpty().isNotEmpty()
    val hasHomepage = homepage.orEmpty().isNotEmpty()
    val hasDonate = donate.orEmpty().isNotEmpty()
    val hasSupport = support.orEmpty().isNotEmpty()
    val hasCover = cover.orEmpty().isNotEmpty()
    val hasScreenshots = screenshots.orEmpty().isNotEmpty()
    val hasRoot = root != null
    val hasNote = note != null
    val hasReadme = readme.orEmpty().isNotEmpty()
    val hasCategories = categories.orEmpty().isNotEmpty()
    val hasMaxApi = maxApi != null
    val hasMinApi = minApi != null
    val hasSize = size != null
    val isVerified = verified != null && verified

    inline fun <R> hasScreenshots(block: (List<String>) -> R): R? {
        return if (!screenshots.isNullOrEmpty()) block(screenshots) else null
    }

    private fun <T : List<String>> getFromModuleManagerOrDefault(
        default: T?,
        platform: Platform,
        extractor: (ModuleManagerSolution?) -> T?,
    ): T? {
        return default.takeIf { it.isNotNullOrEmpty() }
            ?: manager?.let { extractor(it[platform]) }.takeIf { it.isNotNullOrEmpty() }
    }

    private fun requires(platform: Platform) =
        getFromModuleManagerOrDefault(require, platform) { it?.require }

    private fun devices(platform: Platform) =
        getFromModuleManagerOrDefault(devices, platform) { it?.devices }

    private fun arch(platform: Platform) =
        getFromModuleManagerOrDefault(arch, platform) { it?.arch }

    fun manager(platform: Platform) = ModuleManagerSolution(
        min = manager?.get(platform)?.min,
        devices = devices(platform),
        arch = arch(platform),
        require = requires(platform)
    )

    @Composable
    inline fun <R> hasIcon(block: (String?) -> R): R? {
        val userPreferences = LocalUserPreferences.current
        val menu = userPreferences.repositoryMenu

        return if (menu.showIcon) block(icon) else null
    }

    @Composable
    inline fun <R> hasCover(block: (String) -> R): R? {
        val userPreferences = LocalUserPreferences.current
        val menu = userPreferences.repositoryMenu

        return if (!cover.isNullOrBlank() && menu.showCover) block(cover) else null
    }

    @SuppressLint("ComposableNaming")
    @Composable
    inline fun <R> hasCoverOrScreenshots(block: (String?, List<String>?) -> R): R? {
        val userPreferences = LocalUserPreferences.current
        val menu = userPreferences.repositoryMenu

        return if ((cover.isNotNullOrBlank() && menu.showCover) || screenshots.isNotNullOrEmpty()) {
            block(cover, screenshots)
        } else {
            null
        }
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is OnlineModule -> id == other.id
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        fun example() = OnlineModule(
            id = "online_example",
            name = "Example",
            version = "2022.08.16",
            versionCode = 1703,
            author = "Sanmer",
            description = "This is an example!",
            license = "GPL-3.0",
            track = TrackJson(
                typeName = "ONLINE_JSON",
                added = 0f,
                antifeatures = emptyList()
            ),
            versions = emptyList()
        )
    }
}