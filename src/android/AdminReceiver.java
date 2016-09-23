package com.commontime.plugin;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "Disable Requested";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
    }

    @Override
    public void onLockTaskModeEntering(Context context, Intent intent, String pkg) {
    }

    @Override
    public void onLockTaskModeExiting(Context context, Intent intent) {
    }
}
