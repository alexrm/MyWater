package com.rm.mywater.ui;


import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.rm.mywater.R;
import com.rm.mywater.util.Prefs;
import com.rm.mywater.util.base.BaseFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment {

    private RadioGroup mUnits;
    private RadioGroup mGender;

    private RadioButton mMaleRadio;
    private RadioButton mFemaleRadio;
    private RadioButton mMetricRadio;
    private RadioButton mEnglishRadio;

    private Spinner mWeight;
    private ArrayList<String> mWeightList = new ArrayList<>();
    private ArrayAdapter<String> mWeightAdapter;

    private CompoundButton mTraining;

    private SeekBar mVolumeSeek;
    private TextView mVolumeText;
    private int mVolume;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMaleRadio = (RadioButton) view.findViewById(R.id.radio_male);
        mFemaleRadio = (RadioButton) view.findViewById(R.id.radio_female);

        mMetricRadio = (RadioButton) view.findViewById(R.id.radio_metric);
        mEnglishRadio = (RadioButton) view.findViewById(R.id.radio_english);

        mUnits = (RadioGroup) view.findViewById(R.id.unit_radio);
        mUnits.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.radio_metric:
                        Log.d("SettingsFragment", "onCheckedChanged: METRIC");
                        break;

                    case R.id.radio_english:
                        Log.d("SettingsFragment", "onCheckedChanged: ENGLISH");
                        break;
                }
            }
        });

        mGender = (RadioGroup) view.findViewById(R.id.gender_radio);
        mGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.radio_male:
                        Log.d("SettingsFragment", "onCheckedChanged: MALE");
                        break;

                    case R.id.radio_female:
                        Log.d("SettingsFragment", "onCheckedChanged: FEMALE");
                        break;
                }
            }
        });

        //region Weight
        mWeightAdapter =  new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                getWeightList()
        );
        mWeightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mWeight = (Spinner) view.findViewById(R.id.set_weight_spinner);
        mWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Prefs.putAndCommit(Prefs.KEY_USER_WEIGHT, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Log.d("SettingsFragment", "onNothingSelected");
            }
        });
        mWeight.setAdapter(mWeightAdapter);
        mWeight.setSelection(Prefs.get().getInt(Prefs.KEY_USER_WEIGHT, 7));
        //endregion

        //region Training
        mTraining = (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                ?
                (Switch) view.findViewById(R.id.training_switch)
                :
                (SwitchCompat) view.findViewById(R.id.training_switch)
        );
        mTraining.setChecked(Prefs.get().getBoolean(Prefs.KEY_TRAINING, false));
        mTraining.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Prefs.putAndCommit(Prefs.KEY_TRAINING, isChecked);
            }
        });
        //endregion

        //region Volume
        mVolumeText = (TextView) view.findViewById(R.id.volume_text);
        mVolumeSeek = (SeekBar) view.findViewById(R.id.volume_seek);
        mVolumeSeek.setMax(6700); // TODO stub
        mVolumeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mVolume = progress + 500;
                mVolumeText.setText(String.format("%d %s", mVolume, " мл")); // TODO stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Prefs.putAndCommit(Prefs.KEY_BASE_TARGET_VOL, mVolume);
            }
        });
        mVolumeSeek.setProgress(Prefs.getBaseVol() - 500);  // TODO stub
        //endregion
    }

    public ArrayList<String> getWeightList() {

        int w = 30;

        while (w <= 130) {

            mWeightList.add(String.format("%d %s", w, " кг")); // TODO stub

            w += 5;
        }

        return mWeightList;
    }
}
