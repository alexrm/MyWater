package com.rm.mywater.ui.main;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rm.mywater.MainActivity;
import com.rm.mywater.OnFragmentInteractionListener;
import com.rm.mywater.R;
import com.rm.mywater.database.DrinkHistoryDatabase;
import com.rm.mywater.database.OnDataUpdatedListener;
import com.rm.mywater.model.Drink;
import com.rm.mywater.util.Prefs;
import com.rm.mywater.util.base.BaseFragment;
import com.rm.mywater.views.wave.Wave;
import com.rm.mywater.views.wave.WaveView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment
        implements
        OnDrinkChosenListener,
        OnDataUpdatedListener {

    private OnFragmentInteractionListener mInteractionListener;
    private RelativeLayout mRoot;

    // data
    private int mPercentProgress;
    private float mCurrentVolume;
    private float mCurrentMaximum;

    // picker
    private ChooseDrinkDialog mChooserDialog;

    // splash views
    private RelativeLayout mSplash;

    // data views
    private WaveView mWaveView;
    private Wave mWave;
    private TextView mPercent;
    private TextView mVolume;

    // action views
    private ImageView mAddButton;
    private ImageView mStatButton;
    private ImageView mNotifyButton;
    private ImageView mSettingsButton;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mToolbar != null) mToolbar.setVisibility(View.GONE);

        DrinkHistoryDatabase.setUpdateListener(this);

        showSplash(view);

        mWaveView = (WaveView) view.findViewById(R.id.wave_view);
        mPercent = (TextView) view.findViewById(R.id.main_text_percent);
        mVolume = (TextView) view.findViewById(R.id.main_text_volume);

        updateData();

        mAddButton = (ImageView) view.findViewById(R.id.main_add);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showChooserDialog();
            }
        });

        mStatButton = (ImageView) view.findViewById(R.id.main_stats);
        mStatButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mInteractionListener.onFragmentAction(
                        null,
                        OnFragmentInteractionListener.KEY_OPEN_STATS
                );
            }
        });

        mNotifyButton = (ImageView) view.findViewById(R.id.main_notify);
        mNotifyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mInteractionListener.onFragmentAction(
                        null,
                        OnFragmentInteractionListener.KEY_OPEN_NOTIFY
                );
            }
        });

        mSettingsButton = (ImageView) view.findViewById(R.id.main_settings);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mInteractionListener.onFragmentAction(
                        null,
                        OnFragmentInteractionListener.KEY_OPEN_SETTINGS
                );
            }
        });
    }

    private void showSplash(View root) {

        mRoot = (RelativeLayout) root.findViewById(R.id.main_root_view);

        mSplash = (RelativeLayout) root.findViewById(R.id.splash_view);
        mSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }); // stub

        mSplash.animate()
                .setDuration(300)
                .setStartDelay(800)
                .alpha(0)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        mSplash.setVisibility(View.GONE);
                        mRoot.removeView(mSplash);
                    }
                })
                .start();
    }

    private void showChooserDialog() {

        mChooserDialog = new ChooseDrinkDialog(getActivity());
        mChooserDialog.setOnDrinkChosenListener(MainFragment.this);
        mChooserDialog.show();
    }

    private void updateData() {

        mCurrentVolume = Prefs.getCurrentVol();
        mCurrentMaximum = Prefs.getCurrentMax();

        Log.d("MainFragment", "updateData - mCurrentVolume: "
                + mCurrentVolume);

        Log.d("MainFragment", "updateData - mCurrentMaximum: "
                + mCurrentMaximum);

        mPercentProgress = (int) ((mCurrentVolume / mCurrentMaximum) * 100);
        if (mPercentProgress > 100) mPercentProgress = 100;

        Log.d("MainFragment", "updateData - mPercentProgress: "
                + mPercentProgress);

        mWaveView.setProgress(mPercentProgress);
        mPercent.setText(getString(R.string.main_data_percent, mPercentProgress, "%"));

        mVolume.setText(
                getString(R.string.main_data_volume,
                        mCurrentVolume / 1000,
                        "л", // TODO add oz support
                        mCurrentMaximum / 1000
                )
        );

        if (mChooserDialog != null) {

            mChooserDialog.closeDialog();
            mChooserDialog = null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mInteractionListener = (MainActivity) activity;
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d("MainFragment", "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();

        mWaveView.getWave().stopWave();
    }

    @Override
    public void onResume() {
        super.onResume();

        mWaveView.getWave().restartWave();
    }

    @Override
    public void onChose(Drink d) {

        DrinkHistoryDatabase.addDrink(getActivity(), d);
    }

    @Override
    public void onUpdate() {

        updateData();
    }
}
