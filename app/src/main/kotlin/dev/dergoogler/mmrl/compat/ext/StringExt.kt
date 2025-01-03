package dev.dergoogler.mmrl.compat.ext

import com.dergoogler.mmrl.app.Const
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.io.path.nameWithoutExtension


/**
 * Finds the first file in the given directory that matches the given glob pattern.
 * @param   prefix
 *          what the file should starts with
 * @param   patterns
 *          what should be searched for. Default is `*.apk`, `*.jar`, `*.dex`
 *
 * @return  a new `Path` object
 */
fun String.findFileGlob(
    prefix: String, vararg patterns: String = arrayOf("*.apk", "*.jar", "*.dex"),
): Path? {
    val dirPath: Path = Paths.get(this)

    Files.newDirectoryStream(dirPath).use { directoryStream ->
        for (path in directoryStream) {
            for (pattern in patterns) {
                val pathMatcher = FileSystems.getDefault().getPathMatcher("glob:$pattern")
                if (pathMatcher.matches(path.fileName) && path.fileName.nameWithoutExtension == prefix) {
                    return path
                }
            }
        }
    }

    return null
}

inline fun <R> String?.ifNotNullOrBlank(block: (String) -> R): R? {
    return if (!this.isNullOrBlank()) block(this) else null
}

@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNotNullOrBlank(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrBlank != null)
    }

    return !this.isNullOrBlank()
}

fun String.isLocalWifiUrl(): Boolean {
    return try {
        val uri = URI(this)
        val host = uri.host ?: return false
        val port = uri.port

        host.matches(Const.WEBUI_DOMAIN_REMOTE_SAFE_REGEX) && (port == -1 || port in 1..65535)
    } catch (e: Exception) {
        false
    }
}