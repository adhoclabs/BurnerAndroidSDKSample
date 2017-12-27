package adhoclabs.co.burnersamplesdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import co.adhoclabs.burnersdk.BurnerSDK;

public class SampleActivity extends AppCompatActivity {

  private static final String CLIENT_ID = "<YOUR CLIENT ID HERE>";
  private static final String CLIENT_SECRET = "<YOUR CLIENT SECRET HERE>";

  private BurnerSDK burnerSDK;
  private TextView field1;
  private TextView field2;
  private TextView field3;
  private TextView field4;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);
    burnerSDK = BurnerSDK.getInstance();
    burnerSDK.initialize(CLIENT_ID, CLIENT_SECRET);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    field1 = findViewById(R.id.access_token);
    field2 = findViewById(R.id.access_token_expires_in);
    field3 = findViewById(R.id.phone_number);
    field4 = findViewById(R.id.phone_number_expires_at);

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        burnerSDK.launchSignupFlow(SampleActivity.this);
      }
    });
  }

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
}
