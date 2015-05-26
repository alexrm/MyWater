package com.rm.mywater.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.rm.mywater.R;
import com.rm.mywater.adapter.ChooserDrinkAdapter;
import com.rm.mywater.model.Drink;
import com.rm.mywater.util.Dimen;
import com.rm.mywater.util.DrinkUtil;
import com.rm.mywater.util.Prefs;

/**
 * Created by alex
 */
public class ChooseDrinkDialog extends Dialog
        implements
        DialogInterface.OnShowListener,
        ChooserDrinkAdapter.OnItemClickListener,
        AdapterView.OnItemSelectedListener,
        DialogInterface.OnDismissListener {

    // root
    private boolean mIsFirstPage = true;
    private boolean mIsSelected = false;
    private OnDrinkChosenListener mDrinkChosenListener;
    private FrameLayout mContent;
    private RelativeLayout mRoot;

    // first
    private RelativeLayout mFirst;
    private RecyclerView mDrinksGrid;
    private ImageView mCloseButton;

    // second
    private RelativeLayout mSecond;
    private ImageView mBackButton;
    private ImageView mSuccessButton;

    private ImageView mDrinkChosenIcon;
    private TextView mDrinkChosenName;
    private TextView mVolumeText;
    private Spinner mVolumeSpinner;
    private ArrayAdapter<CharSequence> mSpinnerAdapter;

    // data
    private int mDrinkType;
    private int mVolume;

    // animators
    private AnimatorSet mCombineAnimSet;

    public ChooseDrinkDialog(Activity activity) {
        super(activity, R.style.AppTheme_ChooseDrinkDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_drink);

        Log.d("DIALOG", "CREATE");
        setOnShowListener(this);
        setOnDismissListener(this);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

        mRoot = (RelativeLayout) findViewById(R.id.dialog_root);

        mContent = (FrameLayout) findViewById(R.id.chooser_content);
        mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }); // stub

        mFirst = (RelativeLayout) findViewById(R.id.chooser_first);
        initFirstPage();

        mSecond = (RelativeLayout) findViewById(R.id.chooser_second);
        initSecondPage();

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
                .setInterpolator(new OvershootInterpolator(1.1F))
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
    public void onDismiss(DialogInterface dialog) {

        Log.d("ChooseDrinkDialog", "onDismiss");
//        if (mIsSelected) {
//            mDrinkChosenListener.onChose(new Drink(mDrinkType, mVolume));
//        }
    }

    @Override
    public void onBackPressed() {

        if (!mIsFirstPage) {

            processTransition();

        } else {

            closeDialog();
        }

    }

    @Override
    public <T> void onItemClick(T data, int position) {

        mDrinkType = (Integer) data;

        processTransition();

        mDrinkChosenName.setText(
                DrinkUtil.getTitle(mDrinkType)
        );

        mDrinkChosenIcon.setColorFilter(
                DrinkUtil.getDrinkColor(mDrinkType),
                PorterDuff.Mode.MULTIPLY
        );
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {

        int value = Integer.parseInt(
                parent
                        .getAdapter()
                        .getItem(position)
                        .toString()
                        .split(" ")[0]);

        setVolumeValue(value);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setOnDrinkChosenListener(OnDrinkChosenListener drinkChosenListener) {
        mDrinkChosenListener = drinkChosenListener;
    }

    private void initFirstPage() {

        mCloseButton = (ImageView) findViewById(R.id.chooser_close);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeDialog();
            }
        });

        mDrinksGrid = (RecyclerView) findViewById(R.id.chooser_drinks);
        ChooserDrinkAdapter adapter = new ChooserDrinkAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);

        adapter.setOnItemClickListener(this);

        mDrinksGrid.setLayoutManager(layoutManager);
        mDrinksGrid.setHasFixedSize(true);
        mDrinksGrid.setAdapter(adapter);
    }

    private void initSecondPage() {

        mBackButton = (ImageView) findViewById(R.id.chooser_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processTransition();
            }
        });

        mSuccessButton = (ImageView) findViewById(R.id.chooser_success);
        mSuccessButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mIsSelected) return;

                mDrinkChosenListener.onChose(new Drink(mDrinkType, mVolume));

                mIsSelected = true;
            }
        });

        mVolumeText = (TextView) findViewById(R.id.chooser_chosen_volume);

        mSpinnerAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.volume_spinner_euro,
                R.layout.item_chooser_spinner
        );

        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mVolumeSpinner = (Spinner) findViewById(R.id.chooser_volume_spinner);
        mVolumeSpinner.setOnItemSelectedListener(this);
        mVolumeSpinner.setAdapter(mSpinnerAdapter);
        mVolumeSpinner.setSelection(4);

        mDrinkChosenIcon = (ImageView) findViewById(R.id.chooser_chosen_drink_icon);
        mDrinkChosenName = (TextView) findViewById(R.id.chooser_chosen_drink_name);
    }

    public void closeDialog() {

        Log.d("DIALOG", "CLOSE");

        mContent.removeView(mIsFirstPage ? mSecond : mFirst);

        mContent.animate()
                .translationYBy(Dimen.get(R.dimen.dialog_y_down))
                .setInterpolator(new AnticipateInterpolator(1.1F))
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

    private void processTransition() {

        if (mCombineAnimSet != null && mCombineAnimSet.isRunning())  return;

        ObjectAnimator firstViewAnimAlpha =
                ObjectAnimator.ofFloat(
                        mFirst,
                        "alpha",
                        mIsFirstPage ? 0 : 1
                );

        ObjectAnimator firstViewAnimTrans =
                ObjectAnimator.ofFloat(
                        mFirst,
                        "translationX",
                        mIsFirstPage ? -Dimen.get(R.dimen.dialog_slide_x) : 0
                );

        ObjectAnimator secondViewAnimAlpha =
                ObjectAnimator.ofFloat(
                        mSecond,
                        "alpha",
                        mIsFirstPage ? 1 : 0
                );

        ObjectAnimator secondViewAnimTrans =
                ObjectAnimator.ofFloat(
                        mSecond,
                        "translationX",
                        mIsFirstPage ? 0 : Dimen.get(R.dimen.dialog_slide_x)
                );

        AnimatorSet firstViewAnimSet = new AnimatorSet();
        firstViewAnimSet.setDuration(100).playTogether(firstViewAnimAlpha, firstViewAnimTrans);
        firstViewAnimSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (mIsFirstPage) {

                    mFirst.setVisibility(View.GONE);

                } else {

                    mSecond.setVisibility(View.GONE);
                }
            }
        });

        AnimatorSet secondViewAnimSet = new AnimatorSet();
        secondViewAnimSet.setDuration(100) .playTogether(secondViewAnimAlpha, secondViewAnimTrans);
        secondViewAnimSet.setStartDelay(100);
        secondViewAnimAlpha.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                if (mIsFirstPage) {

                    mSecond.setVisibility(View.VISIBLE);

                } else {

                    mFirst.setVisibility(View.VISIBLE);
                }
            }
        });

        mCombineAnimSet = new AnimatorSet();

        mCombineAnimSet.setInterpolator(new DecelerateInterpolator());
        mCombineAnimSet.playSequentially(

                mIsFirstPage ? firstViewAnimSet : secondViewAnimSet,
                mIsFirstPage ? secondViewAnimSet : firstViewAnimSet
        );

        mCombineAnimSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                mIsFirstPage = !mIsFirstPage;
            }
        });

        mCombineAnimSet.start();
    }

    private void setVolumeValue(int value) {

        mVolume = value;

        mVolumeText.setText(Html.fromHtml(
                getContext().getString(
                        R.string.chooser_volume_text,
                        mVolume,
                        Prefs.getUnit()
                )
        ));
    }

}
