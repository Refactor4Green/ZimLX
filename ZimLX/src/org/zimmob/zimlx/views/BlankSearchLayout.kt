/*
 *     This file is part of Lawnchair Launcher.
 *
 *     Lawnchair Launcher is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Lawnchair Launcher is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Lawnchair Launcher.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.zimmob.zimlx.views

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.animation.Interpolator
import com.android.launcher3.allapps.AllAppsContainerView
import com.android.launcher3.allapps.SearchUiManager
import com.android.launcher3.anim.PropertySetter

class BlankSearchLayout(context: Context, attrs: AttributeSet?) : View(context, attrs), SearchUiManager {

    var topMargin = 0
        set(value) {
            if (value != field) {
                field = value
                layoutParams.height = value
            }
        }

    override fun initialize(containerView: AllAppsContainerView) {
        containerView.isVerticalFadingEdgeEnabled = false
    }

    override fun resetSearch() {

    }

    override fun preDispatchKeyEvent(keyEvent: KeyEvent?) {

    }

    override fun startSearch() {

    }

    override fun setContentVisibility(visibleElements: Int, setter: PropertySetter?, interpolator: Interpolator?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScrollRangeDelta(insets: Rect?): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
