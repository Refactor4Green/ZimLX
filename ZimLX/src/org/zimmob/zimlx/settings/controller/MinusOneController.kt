/*
 * Copyright (c) 2020 Zim Launcher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package org.zimmob.zimlx.settings.controller

import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import androidx.annotation.Keep
import androidx.preference.Preference
import com.android.launcher3.R
import org.zimmob.zimlx.settings.PreferenceController
import org.zimmob.zimlx.smartspace.FeedBridge

@Keep
class MinusOneController(context: Context) : PreferenceController(context) {

    override val title get() = getDisplayGoogleTitle()

    override val onChange = Preference.OnPreferenceChangeListener { pref, newValue ->
        if (newValue == true && !FeedBridge.getInstance(context).isInstalled()) {
            pref.preferenceManager.showDialog(pref)
            false
        } else {
            true
        }
    }

    private fun getDisplayGoogleTitle(): String {
        var charSequence: CharSequence? = null
        try {
            val resourcesForApplication = context.packageManager.getResourcesForApplication("com.google.android.googlequicksearchbox")
            val identifier = resourcesForApplication.getIdentifier("title_google_home_screen", "string", "com.google.android.googlequicksearchbox")
            if (identifier != 0) {
                charSequence = resourcesForApplication.getString(identifier)
            }
        } catch (ex: PackageManager.NameNotFoundException) {
        }

        if (TextUtils.isEmpty(charSequence)) {
            charSequence = context.getString(R.string.google_app)
        }
        return context.getString(R.string.title_show_google_app, charSequence)
    }
}