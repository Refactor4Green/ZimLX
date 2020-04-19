package org.zimmob.zimlx.globalsearch.providers

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.annotation.Keep
import com.android.launcher3.R
import com.android.launcher3.util.PackageManagerHelper
import org.zimmob.zimlx.globalsearch.SearchProvider

@Keep
class DuckDuckGoSearchProvider(context: Context) : SearchProvider(context) {

    override val name = context.getString(R.string.search_provider_ddg)
    override val supportsVoiceSearch: Boolean
        get() = false
    override val supportsAssistant: Boolean
        get() = false
    override val supportsFeed = false

    override val isAvailable: Boolean
        get() = PackageManagerHelper.isAppEnabled(context.packageManager, PACKAGE, 0)

    override fun startSearch(callback: (intent: Intent) -> Unit) = callback(Intent(Intent.ACTION_ASSIST).setPackage(PACKAGE))

    override fun getIcon(): Drawable = context.resources.getDrawable(R.drawable.ic_ddg)

    companion object {
        private const val PACKAGE = "com.duckduckgo.mobile.android"
    }
}
