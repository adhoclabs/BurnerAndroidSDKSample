# BurnerAndroidSDKSample

### Introduction
This is the sample Android Burner SDK app. 

### IMPORTANT - Before you get started
In order to run the SDK sample project, you *must* apply for a key with the Burner team. The team will provide you with a `CLIENT_ID` and `CLIENT_SECRET` in order to make the calls. Otherwise this sample app will *not* run.

### Instructions to run the app
Once you have received your `CLIENT_ID` and `CLIENT_SECRET` from the Burner team, check out this project, then import it in the Android Studio. To do so, click on File -> New -> Import Project and select this repository.

Next, navigate to `SampleActivity`. In that activity you will need to enter the according `CLIENT_ID` and `CLIENT_SECRET`.

Now run the project, and you can click on the floating action button to start the process.

## Integrating the Burner SDK in your own app.

### Quick Start Guide to implementing the Burner SDK

0. Download the SDK. The SDK should be available to you from the Burner team.
1. In your app/build.gradle, add the following in your repositories block if you haven't done so already:
```
repositories {
    flatDir {
        dirs 'libs'
    }
}
```
2. *Add permissions and activities*. If you have not already, add the INTERNET permission to your manifest file. Also add the custom activity under the <application> tag:
```
<uses-permission android:name="android.permission.INTERNET" />
 
// inside <application>
...
    <activity
        android:name="co.adhoclabs.burnersdk.activities.AuthorizationActivity"
        >
    </activity>
...
</application>
```

3. In either your extension of the `Application` or `Activity` class (depending on your use case), register the Burner SDK with the client secret:
```
BurnerSDK.getInstance().initialize("CLIENT_ID", "CLIENT_SECRET")
```

4. Now you can direct your users to the create Burner flow in your activity where we will verify the user and create a sample Burner for them before returning back to you:
```
BurnerSDK.getInstance().launchSignupFlow(this); // this refers to an instance of Activity
```

5. In your onActivityResult, handle the results as follows. Note you will get the following fields for you to use from the `bundle`: (`BurnerSDK.IntentParams.AUTH_TOKEN`, `BurnerSDK.IntentParams.AUTH_TOKEN_EXPIRES_IN`, `BurnerSDK.IntentParams.PHONE_NUMBER`, `BurnerSDK.IntentParams.PHONE_NUMBER_EXPIRES_AT`)
```
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Check which request we're responding to
    if (requestCode == BurnerSDK.SIGNUP_FLOW_REQUEST && data != null) {
      Bundle bundle = data.getExtras();
      // Make sure the request was successful
      if (resultCode == RESULT_OK) {
        field1.setText(getString(R.string.token_is, bundle.getString(BurnerSDK.IntentParams.AUTH_TOKEN)));
        field2.setText(getString(R.string.token_expires_in, bundle.getInt(BurnerSDK.IntentParams.AUTH_TOKEN_EXPIRES_IN) / 1000));
        field3.setText(getString(R.string.phone_is, bundle.getString(BurnerSDK.IntentParams.PHONE_NUMBER)));
        field4.setText(getString(R.string.phone_expires_at, new Date(1000 * bundle.getLong(BurnerSDK.IntentParams.PHONE_NUMBER_EXPIRES_AT))));
      } else { // oauth failed, get reason
        field1.setText(bundle.getString("reason"));
      }
    }
  }
```

### Notes
The min SDK for this Android SDK is 16.

### Resources
Here are a list of the resources that pertain to developing with our API, once you have the access token:

API Reference: https://oauth20endpoints.docs.apiary.io/#reference/web-app-api

