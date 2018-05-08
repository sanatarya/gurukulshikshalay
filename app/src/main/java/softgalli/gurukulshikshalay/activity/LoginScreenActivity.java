package softgalli.gurukulshikshalay.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;

import java.util.ArrayList;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.common.AppConstants;
import softgalli.gurukulshikshalay.common.ClsGeneral;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.model.UserDetailsDataModel;
import softgalli.gurukulshikshalay.model.UserDetailsModel;
import softgalli.gurukulshikshalay.preference.MyPreference;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class LoginScreenActivity extends AppCompatActivity implements KenBurnsView.TransitionListener {
    ProgressDialog dialog;
    EditText userId, password;
    EditText newPassword;
    EditText emailIDFP;
    Button senndOTP;
    RelativeLayout otpRL;
    boolean isNotification = false, isWelcomeNotification = false;
    Activity mActivity;
    private static final int TRANSITIONS_TO_SWITCH = 3;
    private ViewSwitcher mViewSwitcher;
    private int mTransitionsCount = 0;
    private RetrofitDataProvider retrofitDataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login_activity);
        mActivity = this;
<<<<<<< Updated upstream
        retrofitDataProvider = new RetrofitDataProvider(this);
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
=======
        mViewSwitcher = findViewById(R.id.viewSwitcher);
>>>>>>> Stashed changes

        KenBurnsView img1 = findViewById(R.id.img1);
        img1.setTransitionListener(this);

        KenBurnsView img2 = findViewById(R.id.img2);
        img2.setTransitionListener(this);

    }

    public void skipLoginSignup(View view) {
        MyPreference.setSignupSkipped(true);
        startActivity(new Intent(mActivity, HomeScreenActivity.class));
        this.finish();
    }

    public void principalLoginClick(View view) {
        loginDialog(AppConstants.PRINCIPAL);
    }

    public void teacherLoginClick(View view) {
        loginDialog(AppConstants.TEACHER);
    }

    public void studentLoginClick(View view) {
        loginDialog(AppConstants.STUDENT);
    }

    public void loginDialog(final String loginAs) {
        // custom dialog
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.login_dialog);
            dialog.setTitle(null);
            dialog.setCanceledOnTouchOutside(true);


            userId = dialog.findViewById(R.id.userId);
            password = dialog.findViewById(R.id.password);


            (dialog.findViewById(R.id.loginButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Utilz.isOnline(mActivity)) {
                        if (checkValidation()) {
                            dialog.dismiss();
                            userLogin(loginAs, userId.getText().toString().trim(), password.getText().toString().trim());

                           /* if (loginAs.equalsIgnoreCase(AppConstants.PRINCIPAL))
                                principalLogin(AppConstants.PRINCIPAL, userId.getText().toString().trim(), password.getText().toString().trim());
                            else if (loginAs.equalsIgnoreCase(AppConstants.TEACHER))
                                teacherLogin(AppConstants.TEACHER, userId.getText().toString().trim(), password.getText().toString().trim());
                            else
                                studentLogin(AppConstants.STUDENT, userId.getText().toString().trim(), password.getText().toString().trim());
                        */
                        }
                    } else {
                        Toast.makeText(mActivity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            (dialog.findViewById(R.id.forgotPassword)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    //forgotPassword();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void userLogin(final String loginAs, final String userIdStr, final String passwordStr) {
        Utilz.showDailog(mActivity, mActivity.getResources().getString(R.string.pleasewait));
        retrofitDataProvider.userLogin(loginAs, userIdStr, passwordStr, new DownlodableCallback<UserDetailsModel>() {
            @Override
            public void onSuccess(final UserDetailsModel result) {
                Utilz.closeDialog();
                if (result.getStatus().contains(PreferenceName.TRUE)) {
                    MyPreference.setLoginedAs(loginAs);
                    saveTeacherDetailsLocally(result.getData().get(0));
                }
            }

            @Override
            public void onFailure(String error) {
                Utilz.closeDialog();
                Toast.makeText(mActivity, R.string.something_went_wrong_error_message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onUnauthorized(int errorNumber) {
                Utilz.closeDialog();
                Toast.makeText(mActivity, R.string.something_went_wrong_error_message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveTeacherDetailsLocally(UserDetailsDataModel result) {

        ClsGeneral.setPreferences(mActivity,AppConstants.ID, result.getId());
        ClsGeneral.setPreferences(mActivity,AppConstants.USER_ID, result.getUser_id());
        ClsGeneral.setPreferences(mActivity,AppConstants.NAME, result.getName());
        ClsGeneral.setPreferences(mActivity,AppConstants.EMAIL, result.getEmail());
        ClsGeneral.setPreferences(mActivity,AppConstants.CLAS, result.getClas());
        ClsGeneral.setPreferences(mActivity,AppConstants.SEC, result.getSec());
        ClsGeneral.setPreferences(mActivity,AppConstants.JOINING_DATE, result.getJoining_date());
        ClsGeneral.setPreferences(mActivity,AppConstants.RESIDENTIAL_ADDRESS, result.getResidential_address());
        ClsGeneral.setPreferences(mActivity,AppConstants.PERMANENT_ADDRESS, result.getPermanent_address());
        ClsGeneral.setPreferences(mActivity,AppConstants.PROFILE_PIC, result.getProfile_pic());
        ClsGeneral.setPreferences(mActivity,AppConstants.STATUS, result.getStatus());
        ClsGeneral.setPreferences(mActivity,AppConstants.QUALIFICATION, result.getQualification());
        ClsGeneral.setPreferences(mActivity,AppConstants.ALTERNTE_NUMBER, result.getAlternate_number());
        ClsGeneral.setPreferences(mActivity,AppConstants.SUBJECT, result.getSubject());
        ClsGeneral.setPreferences(mActivity,AppConstants.CLASS_TEACHER_FOR, result.getClassteacher_for());
        ClsGeneral.setPreferences(mActivity,AppConstants.ADDRESS, result.getAddress());
        ClsGeneral.setPreferences(mActivity,AppConstants.FACEBOOK_ID, result.getFacebook_id());
        ClsGeneral.setPreferences(mActivity,AppConstants.DESIGNATION, result.getDesignation());

        startActivity(new Intent(mActivity, HomeScreenActivity.class));
    }



    public boolean checkValidation() {
        boolean ret = true;
        if (userId.getText().toString().trim().isEmpty()) {
            Toast.makeText(mActivity, "Please enter mobile no", Toast.LENGTH_LONG).show();
            ret = false;
        } else if (password.getText().toString().trim().isEmpty()) {
            Toast.makeText(mActivity, "Please enter password", Toast.LENGTH_LONG).show();
            ret = false;
        }
        return ret;
    }

    @Override
    public void onTransitionStart(Transition transition) {

    }


    @Override
    public void onTransitionEnd(Transition transition) {
        mTransitionsCount++;
        if (mTransitionsCount == TRANSITIONS_TO_SWITCH) {
            mViewSwitcher.showNext();
            mTransitionsCount = 0;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        KenBurnsView currentImage = (KenBurnsView) mViewSwitcher.getCurrentView();
        currentImage.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        KenBurnsView currentImage = (KenBurnsView) mViewSwitcher.getCurrentView();
        currentImage.resume();
    }
}