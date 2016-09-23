package com.commontime.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

public class KioskPlugin extends CordovaPlugin {
        
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        return true;
    }
}