package adhoclabs.co.burnersamplesdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import co.adhoclabs.burnersdk.BurnerSDK;

public class SampleActivity extends AppCompatActivity {

  private static final String CLIENT_ID = BuildConfig.CLIENT_ID;
  private static final String CLIENT_SECRET = BuildConfig.CLIENT_SECRET;
  private static final String SCOPES = BuildConfig.SCOPES;

  private BurnerSDK burnerSDK;
  private TextView field1;
  private TextView field2;

  private Button removeCredentialsButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);
    burnerSDK = BurnerSDK.getInstance();
    burnerSDK.initialize(CLIENT_ID, CLIENT_SECRET, SCOPES);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    field1 = findViewById(R.id.phone_number);
    field2 = findViewById(R.id.sdk_version);
    removeCredentialsButton = findViewById(R.id.remove_credentials);

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        burnerSDK.start(SampleActivity.this);
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    field2.setText("Burner SDK Version: " + burnerSDK.getSdkVersion());

    if (burnerSDK.getToken(this) != null) {
      removeCredentialsButton.setVisibility(View.VISIBLE);
      removeCredentialsButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          field1.setText(R.string.no_number_selected);
          burnerSDK.removeCredentials(SampleActivity.this);
          Toast.makeText(SampleActivity.this, "Credentials Removed, now login again.", Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Check which request we're responding to
    if (requestCode == BurnerSDK.SIGNUP_FLOW_REQUEST && data != null) {
      Bundle bundle = data.getExtras();
      // Make sure the request was successful
      if (resultCode == RESULT_OK) {
        String phoneNumber = bundle.getString(BurnerSDK.IntentParams.PHONE_NUMBER);
        if (phoneNumber != null) {
          field1.setText(getString(R.string.phone_is, phoneNumber));
        }
      } else { // oauth failed, get reason
        field1.setText(bundle.getString("reason"));
      }
    }
  }
}
