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
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.john.waveview.WaveView;
import com.rm.mywater.MainActivity;
import com.rm.mywater.OnFragmentInteractionListener;
import com.rm.mywater.R;
import com.rm.mywater.database.DrinkHistoryDatabase;
import com.rm.mywater.database.OnDataUpdatedListener;
import com.rm.mywater.model.Drink;
import com.rm.mywater.util.Prefs;
import com.rm.mywater.util.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment
        implements
        OnDrinkChosenListener,
        OnDataUpdatedListener {

    private OnFragmentInteractionListener mInterationListener;

    // data
    private int mPercentProgress;
    private float mCurrentVolume;
    private float mCurrentMaximum;

    // splash views
    private RelativeLayout mSplash;

    // data views
    private WaveView mWave;
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

        mSplash = (RelativeLayout) view.findViewById(R.id.splash_view);
        mSplash.animate()
                .setDuration(500)
                .setStartDelay(1000)
                .alpha(0)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        mSplash.setVisibility(View.GONE);
                    }
                })
                .start();

        mWave = (WaveView) view.findViewById(R.id.wave_view);
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

                mInterationListener.onFragmentAction(
                        null,
                        OnFragmentInteractionListener.KEY_OPEN_STATS
                );
            }
        });

        mNotifyButton = (ImageView) view.findViewById(R.id.main_notify);
        mNotifyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mInterationListener.onFragmentAction(
                        null,
                        OnFragmentInteractionListener.KEY_OPEN_NOTIFY
                );
            }
        });

        mSettingsButton = (ImageView) view.findViewById(R.id.main_settings);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mInterationListener.onFragmentAction(
                        null,
                        OnFragmentInteractionListener.KEY_OPEN_SETTINGS
                );
            }
        });
    }

    private void showChooserDialog() {

        ChooseDrinkDialog chooseDrinkDialog = new ChooseDrinkDialog(getActivity());
        chooseDrinkDialog.setOnDrinkChosenListener(this);
        chooseDrinkDialog.show();
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

        mWave.setProgress(mPercentProgress);
        mPercent.setText(getString(R.string.main_data_percent, mPercentProgress, "%"));

        mVolume.setText(
                getString(R.string.main_data_volume,
                        mCurrentVolume / 1000,
                        "л", // TODO add oz support
                        mCurrentMaximum / 1000
                )
        );
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mInterationListener = (MainActivity) activity;
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
