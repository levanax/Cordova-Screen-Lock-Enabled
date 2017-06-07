package com.abarak64.screenlock;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.*;
import android.provider.*;
import android.app.*;

public class ScreenLock extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        Context context=this.cordova.getActivity().getApplicationContext(); 
        boolean hasSecurity = doesDeviceHaveSecuritySetup(context);
        callbackContext.success(hasSecurity ? 1 : 0);

        return true;
    }
    
    public static boolean doesDeviceHaveSecuritySetup(Context context)
    {
        return isPatternSet(context) || isPassOrPinSet(context);
    }

    private static boolean isPatternSet(Context context)
    {
        boolean result = false;
        if(android.os.Build.VERSION.SDK_INT < 23)
        {
            ContentResolver cr = context.getContentResolver();
            try
            {
                int lockPatternEnable = Settings.Secure.getInt(cr, Settings.Secure.LOCK_PATTERN_ENABLED);
                result = lockPatternEnable == 1;
            }
            catch (Settings.SettingNotFoundException e)
            {
                result = false;
            }
            finally
            {
                return result;
            }
        }
        else
        {
            return result;
        }
    }

    private static boolean isPassOrPinSet(Context context)
    {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE); //api 16+
        return keyguardManager.isKeyguardSecure();
    }
}
