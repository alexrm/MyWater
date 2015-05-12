package com.rm.mywater.ui.main;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.john.waveview.WaveView;
import com.rm.mywater.R;
import com.rm.mywater.util.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment {

    int mProgress = 50;
    WaveView mWave;
    TextView mPercent;

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

        mWave = (WaveView) view.findViewById(R.id.wave_view);

        mPercent = (TextView) view.findViewById(R.id.main_text_percent);
        mPercent.setText(getString(R.string.main_data_percent, mProgress, "%"));

        TextView volume = (TextView) view.findViewById(R.id.main_text_volume);
        volume.setText(getString(R.string.main_data_volume, 2.24F, "л", 4.44F));

        ImageView add = (ImageView) view.findViewById(R.id.main_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showChooserDialog();
            }
        });
    }

    private void showChooserDialog() {

        ChooseDrinkDialog chooseDrinkDialog = new ChooseDrinkDialog(getActivity());

        chooseDrinkDialog.show();
    }
}
