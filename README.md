# BurnerAndroidSDKSample

### Introduction
This is the sample Android Burner SDK app. 

### IMPORTANT - Before you get started
In order to run the SDK sample project, you *must* apply for a key with the Burner team. The team will provide you with a `CLIENT_ID` and `CLIENT_SECRET` in order to make the calls. Otherwise this sample app will *not* run.

The other thing you will need is access to our Sonatype Maven repo to get the latest SDK from our custom Maven repository. Again please contact the Burner Dev team.

### Instructions to run the app
Once you have received your `CLIENT_ID` and `CLIENT_SECRET` from the Burner team, check out this project, then import it in the Android Studio. To do so, click on File -> New -> Import Project and select this repository.

Next, navigate to `SampleActivity`. In that activity you will need to enter the according `CLIENT_ID` and `CLIENT_SECRET`.

Now run the project, and you can click on the floating action button to start the process.

## Integrating the Burner SDK in your own app.

### Quick Start Guide to implementing the Burner SDK

0A. Download the SDK. The SDK should be available to you from the Burner team. In your app/build.gradle, add the following in your repositories block if you haven't done so already:
```
repositories {
    flatDir {
        dirs 'libs'
    }
}
```

0B. Alternatively, if you are using the maven repo approach: In your `allProjects` declaration in your build.gradle, add our custom maven repository:

```
maven {
  url "http://nexus.burnerapp.com:8080/content/repositories/AndroidSDK"
  credentials {
  username '<This will be provided to you>'
  password '<This will be provided to you>'
  }
}
```

1B. Add the following in your build.gradle. Get the latest version number from the Burner team.
```
implementation 'co.adhoclabs:burnersdk:<VERSION_NUMBER>@aar' 
```

2. *Add permissions and activities*. If you have not already, add the INTERNET permission to your manifest file. Also add the custom activity under the <application> tag:
```
<uses-permission android:name="android.permission.INTERNET" />
 
Please note you will need to add our activities in your app as well. Further note that the orientation should be portrait only as SDK/Oauth flow is optimized for portrait mode only.
// inside <application>
...
<activity
  android:name="co.adhoclabs.burnersdk.activities.AuthorizationActivity"
  android:screenOrientation="portrait"
  android:theme="@style/Theme.AppCompat.Light.NoActionBar"
>
</activity>
<activity
  android:screenOrientation="portrait"
  android:name="co.adhoclabs.burnersdk.activities.SelectNumberActivity"
  android:theme="@style/Theme.AppCompat.Light.NoActionBar"
/>
...
</application>
```

3. In either your extension of the `Application` or `Activity` class (depending on your use case), register the Burner SDK with the client secret:
```
BurnerSDK.getInstance().initialize("CLIENT_ID", "CLIENT_SECRET")
```

4. Now you can direct your users to the create Burner flow in your activity where we will verify the user and create a Burner for them before returning back to you:
```
BurnerSDK.getInstance().start(this); // this refers to an instance of Activity
```

5. In your onActivityResult, handle the results as follows. Note you will get the following fields for you to use from the `bundle`: (`BurnerSDK.IntentParams.AUTH_TOKEN`, `BurnerSDK.IntentParams.AUTH_TOKEN_EXPIRES_IN`, `BurnerSDK.IntentParams.PHONE_NUMBER`, `BurnerSDK.IntentParams.PHONE_NUMBER_EXPIRES_AT`). Please note that in certain (uncommon) situations, the `PHONE_NUMBER` might be null -- that's because the exising Burner user doesn't have an active Burner anymore. In that case please handle accordingly. New Burner users will always have a phone number since they would have gone through the number creation flow at the point of `onActivityResult`
```
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Check which request we're responding to
    if (requestCode == BurnerSDK.SIGNUP_FLOW_REQUEST && data != null) {
      Bundle bundle = data.getExtras();
      // Make sure the request was successful
      if (resultCode == RESULT_OK) {
        String phoneNumber = bundle.getString(BurnerSDK.IntentParams.PHONE_NUMBER);
        if (phoneNumber != null) { // do a check here, phoneNumber could be null
          field.setText(getString(R.string.phone_is, phoneNumber));
        }
      } else { // oauth failed, get reason
        field1.setText(bundle.getString("reason"));
      }
    }
  }
```

### Notes
The minimum SDK for this Android SDK is 16.

### Resources
Here are a list of the resources that pertain to developing with our API, once you have the access token:

API Reference: https://oauth20endpoints.docs.apiary.io/#reference/web-app-api

