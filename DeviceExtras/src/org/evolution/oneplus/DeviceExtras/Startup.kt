/*
 * Copyright (C) 2013 The OmniROM Project
 * Copyright (C) 2021-2022 The Evolution X Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.evolution.oneplus.DeviceExtras

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import androidx.preference.PreferenceManager
import android.os.UserHandle

import org.evolution.oneplus.DeviceExtras.FileUtils
import org.evolution.oneplus.DeviceExtras.modeswitch.*
import org.evolution.oneplus.DeviceExtras.preferences.*
import org.evolution.oneplus.DeviceExtras.services.FPSInfoService
class Startup : BroadcastReceiver() {
    private var mHBM = false
    override fun onReceive(context: Context, bootintent: Intent) {
        DeviceExtras.restoreSliderStates(context)
        EarGainPreference.restore(context)
        org.evolution.oneplus.DeviceExtras.doze.DozeUtils.checkDozeService(context)
        MicGainPreference.restore(context)
        org.evolution.oneplus.DeviceExtras.touch.TouchscreenGestureSettings.MainSettingsFragment.restoreTouchscreenGestureStates(
            context
        )
        VibratorCallStrengthPreference.restore(context)
        VibratorNotifStrengthPreference.restore(context)
        VibratorStrengthPreference.restore(context)
        RedPreference.restore(context)
        GreenPreference.restore(context)
        BluePreference.restore(context)
        SaturationPreference.restore(context)
        HuePreference.restore(context)
        ValuePreference.restore(context)
        ContrastPreference.restore(context)
        var enabled = false
        val sharedPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_DC_SWITCH, false)
        if (enabled) {
            restore(DCModeSwitch.getFile(context), enabled)
        }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_P3_SWITCH, false)
        if (enabled) {
            mHBM = false
            restore(P3ModeSwitch.getFile(context), enabled)
        }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_CUSTOMER_P3_SWITCH, false)
        if (enabled) {
            mHBM = false
            restore(CustomerP3ModeSwitch.getFile(context), enabled)
        }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_HBM_SWITCH, false)
        if (enabled) {
            restore(HBMModeSwitch.getFile(context), enabled)
        }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_FPS_INFO, false)
        if (enabled) {
            context.startService(Intent(context, FPSInfoService::class.java))
        }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_GAME_SWITCH, false)
        if (enabled) {
            restore(GameModeSwitch.getFile(context), enabled)
        }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_SRGB_SWITCH, false)
        if (enabled) {
            mHBM = false
            restore(SRGBModeSwitch.getFile(context), enabled)
        }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_CUSTOMER_SRGB_SWITCH, false)
        if (enabled) {
            mHBM = false
            restore(CustomerSRGBModeSwitch.getFile(context), enabled)
        }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_LOADING_EFFECT_SWITCH, false)
        if (enabled) {
            mHBM = false
            restore(LoadingEffectModeSwitch.getFile(context), enabled)
        }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_TP_EDGE_LIMIT_SWITCH, false)
        if (enabled) {
            restore(TPEdgeLimitModeSwitch.getFile(context), enabled)
        }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_USB2_SWITCH, false)
        if (enabled) {
            restore(USB2FastChargeModeSwitch.getFile(context), enabled)
        }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_OTG_SWITCH, false)
        if (enabled) {
            restore(OTGModeSwitch.getFile(context), enabled)
        }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_WIDE_SWITCH, false)
        if (enabled) {
            mHBM = false
            restore(WideModeSwitch.getFile(context), enabled)
        }
        if (bootintent.action == Intent.ACTION_BOOT_COMPLETED) {
            context.startServiceAsUser(
                Intent(
                    context,
                    ClientPackageObserverService::class.java
                ),
                UserHandle.SYSTEM
            )
    }
}

    private fun restore(file: String?, enabled: Boolean) {
        if (file == null) {
            return
        }
        if (enabled) {
            FileUtils.writeValue(file, "1")
        }
    }

    private fun restore(file: String?, value: String) {
        if (file == null) {
            return
        }
        FileUtils.writeValue(file, value)
    }

    companion object {
        private const val TAG = "BootReceiver"
    }
}