package com.rm.mywater.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rm.mywater.R;
import com.rm.mywater.adapter.ChooserDrinkAdapter;
import com.rm.mywater.util.Dimen;

/**
 * Created by alex
 */
public class ChooseDrinkDialog extends Dialog implements DialogInterface.OnShowListener, ChooserDrinkAdapter.OnItemClickListener {

    private FrameLayout mContent;
    private RelativeLayout mRoot;

    private RelativeLayout mFirst;
    private RecyclerView mDrinksGrid;
    private ImageView mDrinksGridClose;

    private LinearLayout mSecond;

    public ChooseDrinkDialog(Activity activity) {
        super(activity, R.style.AppTheme_ChooseDrinkDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_drink);
        Log.d("DIALOG", "CREATE");
        setOnShowListener(this);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

        mRoot = (RelativeLayout) findViewById(R.id.dialog_root);

        mContent = (FrameLayout) findViewById(R.id.chooser_content);
        mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });

        mFirst = (RelativeLayout) findViewById(R.id.chooser_first);
        initFirst();

        mSecond = (LinearLayout) findViewById(R.id.second);

        mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeDialog();
            }
        });
    }


    @Override
    public void onShow(DialogInterface dialog) {

        Log.d("DIALOG", "SHOW");

        mContent.animate()
                .translationYBy(-Dimen.get(R.dimen.dialog_y_up))
                .setInterpolator(new OvershootInterpolator(1.2F))
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        mDrinksGrid.animate()
                                .alpha(1)
                                .setDuration(200)
                                .setListener(
                                        new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {
                                                super.onAnimationStart(animation);

                                                mDrinksGrid.setVisibility(View.VISIBLE);
                                            }
                                        })
                                .start();
                    }
                })
                .start();
    }

    @Override
    public void onBackPressed() {
        closeDialog();
    }

    private void initFirst() {

        mDrinksGridClose = (ImageView) findViewById(R.id.chooser_close);

        mDrinksGridClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeDialog();
            }
        });

        mDrinksGrid = (RecyclerView) findViewById(R.id.chooser_drinks);
        ChooserDrinkAdapter adapter = new ChooserDrinkAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);

        adapter.setOnClickListener(this);

        mDrinksGrid.setLayoutManager(layoutManager);
        mDrinksGrid.setHasFixedSize(true);
        mDrinksGrid.setAdapter(adapter);
    }

    private void closeDialog() {

        Log.d("DIALOG", "CLOSE");

        mContent.animate()
                .translationYBy(Dimen.get(R.dimen.dialog_y_down))
                .setInterpolator(new AnticipateInterpolator(1.2F))
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {


                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        ChooseDrinkDialog.this.cancel();
                    }
                })
                .start();
    }

    @Override
    public <T> void onItemClick(T data, int position) {

    }
}
