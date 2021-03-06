package edu.temple.nfcapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.widget.EditText;

public class WriteActivity extends Activity {

    PendingIntent pi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        Intent intent = new Intent(WriteActivity.this, WriteActivity.class);
        pi = PendingIntent.getActivity(WriteActivity.this, 0, intent, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this, pi, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            writePayload(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
        }
    }

    private void writePayload (Tag tag) {
        // Create data payload
        // Using createTextRecord() requires >= API 21
        NdefRecord dataRecord = NdefRecord.createTextRecord(null
                , ((EditText) findViewById(R.id.payloadEntry)).getText().toString());

        // Create NDEF Record
        NdefRecord appRecord = NdefRecord.createApplicationRecord(getPackageName());

        // Create NDEF Message
        NdefMessage message = new NdefMessage(new NdefRecord[]{dataRecord, appRecord});

        // Preempt exceptions by checking for problems
        // such as sufficient space, formatted, etc.
        try {
            Ndef ndef = Ndef.get(tag);
            ndef.connect();
            ndef.writeNdefMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
