package com.relaxed.passwordGenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private TextView mTextViewPassword;
    private SwitchCompat mSwitchLowerCase;
    private SwitchCompat mSwitchUpperCase;
    private SwitchCompat mSwitchNumbers;
    private SwitchCompat mSwitchSpecialSymbols;
    private SwitchCompat mSwitchBasicSymbols;
    private Button mButtonGenerate;
    private SeekBar mSeekBarPasswordLength;
    private TextView mTextViewPasswordLength;
    private MenuItem mMenuItemOptions;
    private FloatingActionButton fabShare;
    private FloatingActionButton fabGo;

    private int passwordLength = 25;
    private int minPasswordLength = 5;

    private String mPassword;

    private final String PASSWORD_KEY = "password key";

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PASSWORD_KEY, mPassword);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        } else {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (savedInstanceState != null) {
            mPassword = savedInstanceState.getString(PASSWORD_KEY,"");
        }

        mTextViewPassword = findViewById(R.id.textViewPassword);

        mSwitchLowerCase = findViewById(R.id.switch_lower);
        mSwitchBasicSymbols = findViewById(R.id.switch_basic_symbols);
        mSwitchNumbers = findViewById(R.id.switch_numbers);
        mSwitchUpperCase = findViewById(R.id.switch_upper);
        mSwitchSpecialSymbols = findViewById(R.id.switch_special_symbols);
        mButtonGenerate = findViewById(R.id.button_run);
        mSeekBarPasswordLength = findViewById(R.id.seekBarPasswordLength);
        mTextViewPasswordLength = findViewById(R.id.textViewPasswordLength);
        fabShare = findViewById(R.id.fabShare);


        if (mPassword!=""){
            mTextViewPassword.setText(mPassword);
            fabShare.setClickable(true);
        }

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mTextViewPassword.getText());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        mButtonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mSwitchUpperCase.isChecked()&&
                    !mSwitchLowerCase.isChecked()&&
                    !mSwitchNumbers.isChecked()&&
                    !mSwitchBasicSymbols.isChecked()&&
                    !mSwitchSpecialSymbols.isChecked()) {
                    Toast.makeText(getApplicationContext(),"Select at least one character type",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    mPassword = PasswordGenerator.passwordGenerate(
                            mSwitchUpperCase.isChecked(),
                            mSwitchLowerCase.isChecked(),
                            mSwitchNumbers.isChecked(),
                            mSwitchBasicSymbols.isChecked(),
                            mSwitchSpecialSymbols.isChecked(),
                            passwordLength, 2);
                    mTextViewPassword.setText(mPassword);
                    fabShare.setClickable(true);
                }
            }
        });
        mTextViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTextViewPassword.getText()+"" != "" ) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", mTextViewPassword.getText() + "");
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(MainActivity.this, R.string.saved_to_clipboard, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mTextViewPasswordLength.setText(passwordLength+"");

        mSeekBarPasswordLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                passwordLength = i+minPasswordLength;
                mTextViewPasswordLength.setText(passwordLength+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mMenuItemOptions = menu.findItem(R.id.menuItemOptions);
        mMenuItemOptions.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = prefs.getString("theme","");
        String dark_colour = prefs.getString("dark_colour","");
        String light_colour = prefs.getString("light_colour","");

        if (theme.equals("system_default")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        else if (theme.equals("light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if (theme.equals("dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        Toast.makeText(getApplicationContext(), "config change", Toast.LENGTH_SHORT).show();
        super.onConfigurationChanged(newConfig);
    }
}