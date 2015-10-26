package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Randall on 27/09/2015.
 */
public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler{

    public static final String BAR_CODE_KEY = "barCode";

    private static final String LOG_TAG = ScannerActivity.class.getSimpleName();


    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);    // Programmatically initialize the scanner view
        scannerView.setResultHandler(this);
        setContentView(scannerView);                // Set the scanner view as the content view
    }


    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        scannerView.startCamera();          // Start camera on resume
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra(BAR_CODE_KEY, rawResult.getText());
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
