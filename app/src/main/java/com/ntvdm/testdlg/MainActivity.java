package com.ntvdm.testdlg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
    private int currentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        currentTheme = prefs.getInt("theme_id", android.R.style.Theme_DeviceDefault);

        setTheme(currentTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final EditText etTitle = (EditText) findViewById(R.id.edit_title);
        final EditText etMsg = (EditText) findViewById(R.id.edit_msg);
        final EditText etOk = (EditText) findViewById(R.id.edit_ok);
        final EditText etCancel = (EditText) findViewById(R.id.edit_cancel);

        Button btnToast = (Button) findViewById(R.id.btn_toast);
        Button btnNotif = (Button) findViewById(R.id.btn_notif);
        Button btnDialog2 = (Button) findViewById(R.id.btn_dialog_2);
        Button btnDialog1 = (Button) findViewById(R.id.btn_dialog_1);

        final Spinner spinner = (Spinner) findViewById(R.id.theme_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.theme_names, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        int selectionIndex = (currentTheme == android.R.style.Theme_Holo) ? 1 : 0;
        spinner.setSelection(selectionIndex);

        Button btnApply = (Button) findViewById(R.id.btn_apply_theme);
        btnApply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = spinner.getSelectedItemPosition();
                int selectedTheme;

                if (pos == 1) {
                    selectedTheme = android.R.style.Theme_DeviceDefault_Light;
                } else if (pos == 2) {
                    selectedTheme = android.R.style.Theme_Holo;
                } else if (pos == 3) {
                    selectedTheme = android.R.style.Theme_Holo_Light;
                } else if (pos == 4) {
                    selectedTheme = android.R.style.Theme_Material;
                } else if (pos == 5) {
                    selectedTheme = android.R.style.Theme_Material_Light;
                } else if (pos == 6) {
                    selectedTheme = android.R.style.Theme_Black;
                } else {
                    selectedTheme = android.R.style.Theme_DeviceDefault;
                }

                if (selectedTheme != currentTheme) {
                    prefs.edit().putInt("theme_id", selectedTheme).apply();
                    finish();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Theme already active", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnToast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, etMsg.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btnNotif.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String msg = etMsg.getText().toString();
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                String CHANNEL_ID = "my_channel_01";

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    android.app.NotificationChannel channel = new android.app.NotificationChannel(
                            CHANNEL_ID, "Channel Name", android.app.NotificationManager.IMPORTANCE_DEFAULT);
                    nm.createNotificationChannel(channel);

                    android.app.Notification.Builder builder = new android.app.Notification.Builder(MainActivity.this, CHANNEL_ID)
                            .setContentTitle(title)
                            .setContentText(msg)
                            .setSmallIcon(android.R.drawable.stat_notify_chat);
                    nm.notify(1, builder.build());
                } else {
                    Notification note = new Notification(android.R.drawable.stat_notify_chat, "Ticker", System.currentTimeMillis());
                    PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, new Intent(), 0);
                    try {
                        java.lang.reflect.Method method = note.getClass().getMethod("setLatestEventInfo",
                                Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                        method.invoke(note, MainActivity.this, title, msg, pi);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    nm.notify(1, note);
                }
            }
        });

        btnDialog2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(etTitle.getText().toString())
                        .setMessage(etMsg.getText().toString())
                        .setPositiveButton(etOk.getText().toString(), null)
                        .setNegativeButton(etCancel.getText().toString(), null)
                        .show();
            }
        });

        btnDialog1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(etTitle.getText().toString())
                        .setMessage(etMsg.getText().toString())
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }
}