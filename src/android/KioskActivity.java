package com.commontime.plugin;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import org.apache.cordova.*;

import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.*;
import android.view.View;
import android.view.WindowManager;
import android.view.KeyEvent;

public class KioskActivity extends CordovaActivity {
    
    public static boolean running = false;
    private DevicePolicyManager mDpm;
    private View customView;
    public final static int REQUEST_CODE = -1010101;

    protected void onStart() {
        super.onStart();
        running = true;
    }
    
    protected void onStop() {
        super.onStop();
        running = false;
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init();
        loadUrl(launchUrl);

        if(Build.VERSION.SDK_INT >= 21) {
            ComponentName deviceAdmin = new ComponentName(this, AdminReceiver.class);
            mDpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            if (!mDpm.isAdminActive(deviceAdmin)) {
                Toast.makeText(this, "Not device admin", Toast.LENGTH_SHORT).show();
            }

            if (mDpm.isDeviceOwnerApp(getPackageName())) {
                mDpm.setLockTaskPackages(deviceAdmin, new String[]{getPackageName()});
            } else {
                Toast.makeText(this, "Not device owner", Toast.LENGTH_SHORT).show();
            }

            setupMainWindowDisplayMode();

            startLockTask();

            checkDrawOverlayPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupMainWindowDisplayMode();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    return true;
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    public void checkDrawOverlayPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            /** check if we already  have permission to draw over other apps */
            if (!Settings.canDrawOverlays(this)) {
                /** if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                /** request permission via start activity for result */
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                disableNotificationDrawer(true);
            }
        } else {
            disableNotificationDrawer(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if(Build.VERSION.SDK_INT >= 23) {
            /** check if received result code
             is equal our requested code for draw permission  */
            if (requestCode == REQUEST_CODE) {
            /* if so check once again if we have permission */
                if (Settings.canDrawOverlays(this)) {
                    disableNotificationDrawer(true);
                }
            }
        }
    }

    private void disableNotificationDrawer(boolean b) {

        class CustomViewGroup extends ViewGroup {

            public CustomViewGroup(Context context) {
                super(context);
            }

            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {
            }

            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                return true;
            }
        }

        WindowManager manager = ((WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (1 * getResources()
                .getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;

        if(b) {
            if( customView == null) {
                customView = new CustomViewGroup(this);
            }
            manager.addView(customView, localLayoutParams);
        } else {
            manager.removeView(customView);
        }
    }

    private void setupMainWindowDisplayMode() {
        View decorView = setSystemUiVisilityMode();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                setSystemUiVisilityMode(); // Needed to avoid exiting immersive_sticky when keyboard is displayed
            }
        });
    }

    private View setSystemUiVisilityMode() {
        View decorView = getWindow().getDecorView();
        int options;
        options =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(options);
        return decorView;
    }
}