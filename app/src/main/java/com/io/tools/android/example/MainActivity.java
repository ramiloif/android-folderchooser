package com.io.tools.android.example;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.io.tools.android.ramiloif.folderchooser.ChooseDirectoryDialog;

public class MainActivity extends AppCompatActivity {

    public final static int READ_EXTERNAL_STORAGE = 15;
    private Button mButtonWithNeverAsk;
    private Button mButtonWithout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView resultTV =  (TextView) findViewById(R.id.result);
        setSupportActionBar(toolbar);
        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity. this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //fragmentsCreation();
        }else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE);

        }


        findViewById(R.id.bt_with_never_ask_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDirectoryDialog dialog =
                        ChooseDirectoryDialog.builder(MainActivity.this).

                                titleText("Choose directory").
                                startDir(Environment.getExternalStorageDirectory().getAbsoluteFile()).
                                showNeverAskAgain(true).
                                neverAskAgainText("Never ask again").
                                onPickListener(new ChooseDirectoryDialog.DirectoryChooseListener() {

                                    @Override
                                    public void onDirectoryPicked(ChooseDirectoryDialog.DialogResult result) {
                                        resultTV.setText("You picked \n " + result.getPath() + "\n Never ask again = " + result.isAskAgain());
                                    }

                                    @Override
                                    public void onCancel() {
                                        resultTV.setText("operation canceled");
                                    }
                                }).build();
                dialog.show();
            }
        });

        findViewById(R.id.bt_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ChooseDirectoryDialog dialog =
                        ChooseDirectoryDialog.builder(MainActivity.this).

                                titleText("Choose directory").
                                startDir(Environment.getExternalStorageDirectory().getAbsoluteFile())
                                .onPickListener(new ChooseDirectoryDialog.DirectoryChooseListener() {

                                    @Override
                                    public void onDirectoryPicked(ChooseDirectoryDialog.DialogResult result) {
                                        resultTV.setText("You picked \n " + result.getPath());
                                    }

                                    @Override
                                    public void onCancel() {
                                        resultTV.setText("operation canceled");
                                    }
                                }).build();
                dialog.show();
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }else {
                    final android.app.AlertDialog dialog =  new android.app.AlertDialog.Builder(MainActivity.this).setTitle("Fuck").setMessage("Shit").setPositiveButton
                            ("By By", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create();
                    dialog.show();

                }
            }
        }
    }

}
