package edu.temple.nfcapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ReadActivity extends Activity {

    PendingIntent pi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        // Pending intent to have tag delivered to this activity
        
        Intent intent = new Intent(ReadActivity.this, ReadActivity.class);
        pi = PendingIntent.getActivity(ReadActivity.this, 0, intent, 0);

        findViewById(R.id.writeToTagTextView).setOnClickListener((View v) ->
            startActivity(new Intent(this, WriteActivity.class))
        );
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Intercept all NFC tags (filter is null)
        NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this, pi, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Stop intercepting NFC tags
        NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        // Only processing NDEF formatted tags
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            readPayload(intent);
        }
    }

    void readPayload(Intent intent) {

        // If data is text/plain, a language code will be retrieved
        // boilerplate code can remove it
        String payload = new String(
                ((NdefMessage) intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)[0])
                        .getRecords()[0]
                        .getPayload());

        ((TextView) findViewById(R.id.payloadDisplay))
                .setText(payload);
    }
}
