package ntk.android.estate.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.google.android.material.textfield.TextInputEditText;

import es.dmoral.toasty.Toasty;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.coremain.CoreUserModel;
import ntk.android.base.services.core.CoreUserService;
import ntk.android.base.utill.Regex;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.estate.R;

public class ProfileActivity extends BaseActivity {
    CoreUserModel user;
    long id = 2;
    private boolean uploadInProgress = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        id = getIntent().getExtras().getLong(Extras.EXTRA_FIRST_ARG);
        ViewCompat.setTranslationZ(findViewById(R.id.rv), 1);
        ViewCompat.setTranslationZ(findViewById(R.id.cardView), .03f);
        findViewById(R.id.saveBtn).bringToFront();
        getUser();
        findViewById(R.id.saveBtn).setOnClickListener(v -> updateUserData());
    }

    private void updateUserData() {
        if (uploadInProgress)
            Toasty.error(this, "لطفا تا اتمام بارگذاری تصویر انتخابی صبر کنید").show();
        else {
            String email = ((TextInputEditText) findViewById(R.id.emailEt)).getText().toString();
            if (Regex.ValidateEmail(email)) {
                Toasty.error(this, "آدرس ایمیل خود را به صورت صحیح وارد کنید").show();
                return;
            }
            user.Name = ((TextInputEditText) findViewById(R.id.nameEt)).getText().toString();
            user.LastName = ((TextInputEditText) findViewById(R.id.lastNameEt)).getText().toString();
            user.Email = ((TextInputEditText) findViewById(R.id.emailEt)).getText().toString();
            user.CompanyName = ((TextInputEditText) findViewById(R.id.companyEt)).getText().toString();
        }
    }

    private void getUser() {
        switcher.showProgressView();
        ServiceExecute.execute(new CoreUserService(this).getOne(id)).subscribe(new NtkObserver<ErrorException<CoreUserModel>>() {
            @Override
            public void onNext(ErrorException<CoreUserModel> coreUserModelErrorException) {
                switcher.showContentView();
                if (coreUserModelErrorException.IsSuccess) {
                    user = coreUserModelErrorException.Item;
                    showUserData();
                } else {
                    Toast.makeText(ProfileActivity.this, coreUserModelErrorException.ErrorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(ProfileActivity.this, "خطا رخ داد مجددا تلاش کنید", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void showUserData() {
        if (user.Mobile != null)
            ((TextInputEditText) findViewById(R.id.mobileEt)).setText(user.Mobile);
        else
            ((TextInputEditText) findViewById(R.id.mobileEt)).setText(Preferences.with(this).UserInfo().mobile());

        if (user.Name != null) {
            ((TextInputEditText) findViewById(R.id.nameEt)).setText(user.Name);
        }
        if (user.LastName != null) {
            ((TextInputEditText) findViewById(R.id.lastNameEt)).setText(user.LastName);
        }
        if (user.Email != null) {
            ((TextInputEditText) findViewById(R.id.emailEt)).setText(user.LastName);
        }
        if (user.CompanyName != null) {
            ((TextInputEditText) findViewById(R.id.companyEt)).setText(user.CompanyName);
        }
    }
}
