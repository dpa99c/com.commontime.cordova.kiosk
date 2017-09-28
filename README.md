Cordova Kiosk Mode
==================

Cordova plugin to create Cordova application looked down into a "kiosk" mode. This is achieved by using Lollipop's screen pinning feature along with stopping the user from being able to drag down the notification area at the top.

For more information on the screen pinning functionality please go to [https://developer.android.com/about/versions/android-5.0.html#Enterprise](https://developer.android.com/about/versions/android-5.0.html#Enterprise)

**Important:** This plugin uses fully takes advantage of new features introduced in Lollipop, therefore if this plugin is used on a device running an older version of Android no locking down of the app will occur.

**Note for iOS:** This plugin is for Android only for now. Support of iOS would be useless, because this feature is built in iOS as Guided Access (see Settings - General - Accessibility - Guided Access)

Usage
=====

The kiosk mode is not active by default. It is only activated if the app is set as a launcher once installed on the device. Once this has been done and the device is restarted the app will be launched in a locked down state. On launch the user will be shown how to unlock the app so they can exit it.

To fully lock the device down to the app the plugin fully supports being used alongside setting the app as a device owner introduced in Android Lollipop. Please refer to the link in the section above to learn more about the device owner feature. Using the plugin in this way will provide the benefit of not having instructions shown as how to exit the screen pinning along with removing the home and recent apps button in the navigation bar.

**Provisioning note**  
The general command to provision the device owner should look like this:  
`./adb dpm set-device-owner com.yourorg.yourpackage/com.commontime.plugin.AdminReceiver`

Fork Hacks
==========
- Adjusted the plugin.xml to handle intents properly for Lenovo tablets.
- Adjusted HomeActivity.java to support the above intent changes.