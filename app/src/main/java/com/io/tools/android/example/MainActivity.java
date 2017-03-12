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

import com.io.tools.android.ramiloif.folderchooser.ChooseDirectoryDialog;

public class MainActivity extends AppCompatActivity {

    public final static int READ_EXTERNAL_STORAGE = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseDirectoryDialog dialog =
                new ChooseDirectoryDialog(MainActivity.this).

                        setmTitleText("Hello jcenter").
                setStartDir(Environment.getExternalStorageDirectory().getAbsoluteFile())
                .setOnPickListener(new ChooseDirectoryDialog.DirectoryChooseListener() {
                    @Override
                    public void onDirectoryPicked(String path) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                dialog.show();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
