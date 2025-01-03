package com.dergoogler.mmrl.ui.screens.settings.updates

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.dergoogler.mmrl.R
import com.dergoogler.mmrl.ui.component.ListButtonItem
import com.dergoogler.mmrl.ui.component.ListHeader
import com.dergoogler.mmrl.ui.component.ListRadioCheckItem
import com.dergoogler.mmrl.ui.component.ListSwitchItem
import com.dergoogler.mmrl.ui.component.SettingsScaffold
import com.dergoogler.mmrl.ui.providable.LocalUserPreferences
import com.dergoogler.mmrl.viewmodel.SettingsViewModel

private val optionsOfHours = listOf(1, 2, 3, 4, 5, 10, 12, 16, 24, 48, 72)

@Composable
fun UpdatesScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val userPreferences = LocalUserPreferences.current
    val context = LocalContext.current

    SettingsScaffold(
        title = R.string.settings_updates,
    ) {
        ListButtonItem(
            icon = R.drawable.notification,
            title = stringResource(id = R.string.settings_open_notification_settings),
            onClick = {
                val intent =
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    }

                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent, null)
                } else {
                    Toast.makeText(
                        context,
                        "Cannot open notification settings",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        ListHeader(
            title = stringResource(id = R.string.settings_app)
        )

        ListSwitchItem(
            icon = R.drawable.launcher_outline,
            title = stringResource(id = R.string.settings_check_app_updates),
            desc = stringResource(id = R.string.settings_check_app_updates_desc),
            checked = userPreferences.checkAppUpdates,
            onChange = viewModel::setCheckAppUpdates
        )

        ListSwitchItem(
            icon = R.drawable.launcher_outline,
            title = stringResource(id = R.string.settings_include_preleases),
            enabled = userPreferences.checkAppUpdates,
            checked = userPreferences.checkAppUpdatesPreReleases,
            onChange = viewModel::setCheckAppUpdatesPreReleases
        )

        ListHeader(
            title = stringResource(id = R.string.page_repository)
        )

        ListSwitchItem(
            icon = R.drawable.refresh,
            title = stringResource(id = R.string.settings_auto_update_repos),
            desc = stringResource(id = R.string.settings_auto_update_repos_desc),
            checked = userPreferences.autoUpdateRepos,
            onChange = viewModel::setAutoUpdateRepos
        )

        ListRadioCheckItem(
            title = stringResource(R.string.settings_repo_update_interval),
            icon = R.drawable.clock_24,
            desc = stringResource(
                R.string.settings_repo_update_interval_desc,
                userPreferences.autoUpdateReposInterval
            ),
            enabled = userPreferences.autoUpdateRepos,
            suffix = stringResource(id = R.string.settings_repo_update_interval_suffix),
            value = userPreferences.autoUpdateReposInterval,
            options = optionsOfHours,
            onConfirm = {
                viewModel.setAutoUpdateReposInterval(it)
            })

        ListHeader(
            title = stringResource(id = R.string.page_modules)
        )

        ListSwitchItem(
            icon = R.drawable.box,
            title = stringResource(id = R.string.settings_check_modules_update),
            desc = stringResource(id = R.string.settings_check_modules_update_desc),
            checked = userPreferences.checkModuleUpdates,
            onChange = viewModel::setCheckModuleUpdates
        )

        ListRadioCheckItem(
            title = stringResource(R.string.settings_check_modules_update_interval),
            icon = R.drawable.device_mobile_code,
            desc = stringResource(
                R.string.settings_check_modules_update_interval_desc,
                userPreferences.checkModuleUpdatesInterval
            ),
            enabled = userPreferences.checkModuleUpdates,
            suffix = stringResource(id = R.string.settings_check_modules_update_interval_suffix),
            value = userPreferences.checkModuleUpdatesInterval,
            options = optionsOfHours,
            onConfirm = {
                viewModel.setCheckModuleUpdatesInterval(it)
            }
        )
    }
}