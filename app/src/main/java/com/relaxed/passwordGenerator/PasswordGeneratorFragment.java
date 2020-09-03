package com.relaxed.passwordGenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PasswordGeneratorFragment extends Fragment {

    private TextView mTextViewPassword;
    private SwitchCompat mSwitchLowerCase;
    private SwitchCompat mSwitchUpperCase;
    private SwitchCompat mSwitchNumbers;
    private SwitchCompat mSwitchSpecialSymbols;
    private SwitchCompat mSwitchBasicSymbols;
    private Button mButtonGenerate;
    private SeekBar mSeekBarPasswordLength;
    private TextView mTextViewPasswordLength;
    private String mPassword;
    private int passwordLength = 25;
    private int minPasswordLength = 5;

    private FloatingActionButton fabShare;

    private final String PASSWORD_KEY = "password key";

    public PasswordGeneratorFragment() {
        // Required empty public constructor
    }


    public static PasswordGeneratorFragment newInstance(String param1, String param2) {
        PasswordGeneratorFragment fragment = new PasswordGeneratorFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_password_generator, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mPassword = savedInstanceState.getString(PASSWORD_KEY,"");
        }

        mTextViewPassword = getView().findViewById(R.id.textViewPassword);
        mSwitchLowerCase = getView().findViewById(R.id.switch_lower);
        mSwitchBasicSymbols = getView().findViewById(R.id.switch_basic_symbols);
        mSwitchNumbers = getView().findViewById(R.id.switch_numbers);
        mSwitchUpperCase = getView().findViewById(R.id.switch_upper);
        mSwitchSpecialSymbols = getView().findViewById(R.id.switch_special_symbols);
        mButtonGenerate = getView().findViewById(R.id.button_run);
        mSeekBarPasswordLength = getView().findViewById(R.id.seekBarPasswordLength);
        mTextViewPasswordLength = getView().findViewById(R.id.textViewPasswordLength);

        if (mPassword!=""){
            mTextViewPassword.setText(mPassword);
        }
        fabShare = getView().findViewById(R.id.fabShare);
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
                    Toast.makeText(getContext(),"Select at least one character type",
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
                }
            }
        });
        mTextViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTextViewPassword.getText()+"" != "" ) {
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", mTextViewPassword.getText() + "");
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), R.string.saved_to_clipboard, Toast.LENGTH_SHORT).show();
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PASSWORD_KEY, mPassword);
    }
}