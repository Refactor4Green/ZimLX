package com.android.launcher3;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;

import static com.android.launcher3.util.SystemUiController.FLAG_DARK_NAV;
import static com.android.launcher3.util.SystemUiController.UI_STATE_ROOT_VIEW;


public class LauncherRootView extends InsettableFrameLayout {
    private final Launcher mLauncher;
    private final Paint mOpaquePaint;
    @ViewDebug.ExportedProperty(category = "launcher")
    private final Rect mConsumedInsets = new Rect();
    private View mAlignedView;

    public LauncherRootView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mOpaquePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOpaquePaint.setColor(Color.BLACK);
        mOpaquePaint.setStyle(Paint.Style.FILL);

        mLauncher = Launcher.getLauncher(context);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            // LauncherRootView contains only one child, which should be aligned
            // based on the horizontal insets.
            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            mAlignedView = getChildAt(0);
        }
        super.onFinishInflate();
    }

    @TargetApi(23)
    @Override
    protected boolean fitSystemWindows(Rect insets) {
        boolean rawInsetsChanged = !mInsets.equals(insets);
        mConsumedInsets.setEmpty();
        boolean drawInsetBar = false;
        if (mLauncher.isInMultiWindowModeCompat()
                && (insets.left > 0 || insets.right > 0 || insets.bottom > 0)) {
            mConsumedInsets.left = insets.left;
            mConsumedInsets.right = insets.right;
            mConsumedInsets.bottom = insets.bottom;
            insets = new Rect(0, insets.top, 0, insets.bottom);
            drawInsetBar = true;
        } else if ((insets.right > 0 || insets.left > 0) &&
                (!Utilities.ATLEAST_MARSHMALLOW ||
                        getContext().getSystemService(ActivityManager.class).isLowRamDevice())) {
            mConsumedInsets.left = insets.left;
            mConsumedInsets.right = insets.right;
            insets = new Rect(insets.left, 0, insets.right, 0);
            drawInsetBar = true;
        }

        mLauncher.getSystemUiController().updateUiState(
                UI_STATE_ROOT_VIEW, drawInsetBar ? FLAG_DARK_NAV : 0);

        // Update device profile before notifying th children.
        mLauncher.getDeviceProfile().updateInsets(insets);
        boolean resetState = !insets.equals(mInsets);
        setInsets(insets);

        if (mAlignedView != null) {
            // Apply margins on aligned view to handle consumed insets.
            MarginLayoutParams lp = (MarginLayoutParams) mAlignedView.getLayoutParams();
            if (lp.leftMargin != mConsumedInsets.left ||
                    lp.rightMargin != mConsumedInsets.right ||
                    lp.bottomMargin != mConsumedInsets.bottom ||
                    lp.topMargin != mConsumedInsets.top) {
                lp.leftMargin = mConsumedInsets.left;
                lp.rightMargin = mConsumedInsets.right;
                lp.topMargin = mConsumedInsets.top;
                lp.bottomMargin = mConsumedInsets.bottom;
                mAlignedView.setLayoutParams(lp);
            }
        }

        if (rawInsetsChanged) {
            // Update the grid again
            Launcher launcher = Launcher.getLauncher(getContext());
            launcher.onInsetsChanged(insets);
        }

        return true; // I'll take it from here
    }

    @Override
    public void setInsets(Rect insets) {
        // If the insets haven't changed, this is a no-op. Avoid unnecessary layout caused by
        // modifying child layout params.
        if (!insets.equals(mInsets)) {
            super.setInsets(insets);
        }
    }

    public void dispatchInsets() {
        mLauncher.getDeviceProfile().updateInsets(mInsets);
        super.setInsets(mInsets);
    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        // If the right inset is opaque, draw a black rectangle to ensure that is stays opaque.
        if (mConsumedInsets.right > 0) {
            int width = getWidth();
            canvas.drawRect(width - mConsumedInsets.right, 0, width, getHeight(), mOpaquePaint);
        }
        if (mConsumedInsets.left > 0) {
            canvas.drawRect(0, 0, mConsumedInsets.left, getHeight(), mOpaquePaint);
        }
        if (mConsumedInsets.bottom > 0) {
            int height = getHeight();
            canvas.drawRect(0, height - mConsumedInsets.bottom, getWidth(), height, mOpaquePaint);
        }
    }
}