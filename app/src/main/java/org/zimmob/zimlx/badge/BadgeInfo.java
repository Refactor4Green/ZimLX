package org.zimmob.zimlx.badge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import org.zimmob.zimlx.notification.NotificationInfo;
import org.zimmob.zimlx.notification.NotificationKeyData;
import org.zimmob.zimlx.util.PackageUserKey;

import java.util.ArrayList;
import java.util.List;

public class BadgeInfo {
    private static final int MAX_COUNT = 999;
    private int mTotalCount;
    private List<NotificationKeyData> mNotificationKeys;
    private NotificationInfo mNotificationInfo;
    private PackageUserKey mPackageUserKey;
    private Shader mNotificationIcon;

    public BadgeInfo(PackageUserKey packageUserKey) {
        mPackageUserKey = packageUserKey;
        mNotificationKeys = new ArrayList<>();
    }

    public boolean addOrUpdateNotificationKey(NotificationKeyData notificationKey) {
        int indexOfPrevKey = mNotificationKeys.indexOf(notificationKey);
        NotificationKeyData prevKey = indexOfPrevKey == -1 ? null
                : mNotificationKeys.get(indexOfPrevKey);
        if (prevKey != null) {
            if (prevKey.count == notificationKey.count) {
                return false;
            }
            // Notification was updated with a new count.
            mTotalCount -= prevKey.count;
            mTotalCount += notificationKey.count;
            prevKey.count = notificationKey.count;
            return true;
        }
        boolean added = mNotificationKeys.add(notificationKey);
        if (added) {
            mTotalCount += notificationKey.count;
        }
        return added;
    }

    /**
     * Returns whether the notification was removed (false if it didn't exist).
     */
    public boolean removeNotificationKey(NotificationKeyData notificationKey) {
        boolean removed = mNotificationKeys.remove(notificationKey);
        if (removed) {
            mTotalCount -= notificationKey.count;
        }
        return removed;
    }

    public List<NotificationKeyData> getNotificationKeys() {
        return mNotificationKeys;
    }

    public int getNotificationCount() {
        return Math.min(mTotalCount, MAX_COUNT);
    }

    public void setNotificationToShow(@Nullable NotificationInfo notificationInfo) {
        mNotificationInfo = notificationInfo;
       // mNotificationIcon = null;
    }

    public boolean hasNotificationToShow() {
        return mNotificationInfo != null;
    }

    /**
     * Returns a shader to set on a Paint that will draw the notification icon in a badge.
     *
     * The shader is cached until {@link #setNotificationToShow(NotificationInfo)} is called.
     */
    public Shader getNotificationIconForBadge(Context context, int badgeColor,
                                       int badgeSize, int badgePadding) {
        if (mNotificationInfo == null) {
            return null;
        }
        if (mNotificationIcon == null) {
            Drawable icon = mNotificationInfo.getIconForBackground(context, badgeColor)
                    .getConstantState().newDrawable();
            int iconSize = badgeSize - badgePadding * 2;
            icon.setBounds(0, 0, iconSize, iconSize);
            Bitmap iconBitmap = Bitmap.createBitmap(badgeSize, badgeSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(iconBitmap);
            canvas.translate(badgePadding, badgePadding);
            icon.draw(canvas);
            mNotificationIcon = new BitmapShader(iconBitmap, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);
        }
        return mNotificationIcon;
    }

    public boolean isIconLarge() {
        return mNotificationInfo != null && mNotificationInfo.isIconLarge();
    }

    /**
     * Whether newBadge represents the same PackageUserKey as this badge, and icons with
     * this badge should be invalidated. So, for instance, if a badge has 3 notifications
     * and one of those notifications is updated, this method should return false because
     * the badge still says "3" and the contents of those notifications are only retrieved
     * upon long-click. This method always returns true when adding or removing notifications,
     * or if the badge has a notification icon to show.
     */
    public boolean shouldBeInvalidated(BadgeInfo newBadge) {
        return mPackageUserKey.equals(newBadge.mPackageUserKey)
                && (getNotificationCount() != newBadge.getNotificationCount()
                || hasNotificationToShow());
    }
}