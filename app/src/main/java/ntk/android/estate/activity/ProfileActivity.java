package ntk.android.estate.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import es.dmoral.toasty.Toasty;
import io.reactivex.annotations.NonNull;
import java9.util.function.Consumer;
import ntk.android.base.Extras;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.coremain.CoreUserModel;
import ntk.android.base.entitymodel.file.FileUploadModel;
import ntk.android.base.service.FileManagerService;
import ntk.android.base.services.core.CoreUserService;
import ntk.android.base.services.file.FileUploaderService;
import ntk.android.base.utill.AppUtil;
import ntk.android.base.utill.FontManager;
import ntk.android.base.utill.Regex;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.estate.R;

public class ProfileActivity extends BaseActivity {
    CoreUserModel user;
    long id = 2;
    private boolean uploadInProgress = false;
    private String image_GUID = "";
    private String image_FilePath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        id = getIntent().getExtras().getLong(Extras.EXTRA_FIRST_ARG);
        ViewCompat.setTranslationZ(findViewById(R.id.rv), 1);
        ViewCompat.setTranslationZ(findViewById(R.id.cardView), .03f);
        MaterialButton saveBtn = findViewById(R.id.saveBtn);
        MaterialButton backBtn = findViewById(R.id.backBtn);
        getUser();
        saveBtn.setOnClickListener(v -> updateUserData());
        backBtn.setOnClickListener(v -> finish());
        findViewById(R.id.image).setOnClickListener(v -> ClickAttach());
        ////set font
        saveBtn.setTypeface(FontManager.T1_Typeface(this));
        backBtn.setTypeface(FontManager.T1_Typeface(this));
        ((TextInputEditText) findViewById(R.id.mobileEt)).setTypeface(FontManager.T1_Typeface(this));
        ((TextInputEditText) findViewById(R.id.nameEt)).setTypeface(FontManager.T1_Typeface(this));
        ((TextInputEditText) findViewById(R.id.lastNameEt)).setTypeface(FontManager.T1_Typeface(this));
        ((TextInputEditText) findViewById(R.id.emailEt)).setTypeface(FontManager.T1_Typeface(this));
        ((TextInputEditText) findViewById(R.id.companyEt)).setTypeface(FontManager.T1_Typeface(this));

        ((TextInputLayout) findViewById(R.id.mobileTl)).setTypeface(FontManager.T1_Typeface(this));
        ((TextInputLayout) findViewById(R.id.nameTl)).setTypeface(FontManager.T1_Typeface(this));
        ((TextInputLayout) findViewById(R.id.lastNameTl)).setTypeface(FontManager.T1_Typeface(this));
        ((TextInputLayout) findViewById(R.id.emailTl)).setTypeface(FontManager.T1_Typeface(this));
        ((TextInputLayout) findViewById(R.id.companyTl)).setTypeface(FontManager.T1_Typeface(this));

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
            ServiceExecute.execute(new CoreUserService(this).edit(user)).subscribe(new NtkObserver<ErrorException<CoreUserModel>>() {
                @Override
                public void onNext(ErrorException<CoreUserModel> coreUserModelErrorException) {
                    if (coreUserModelErrorException.IsSuccess)
                        Toasty.success(ProfileActivity.this, "اطلاعات حساب کاربری شما ذخیره شد").show();
                    else
                        Toasty.error(ProfileActivity.this, coreUserModelErrorException.ErrorMessage).show();
                }

                @Override
                public void onError(Throwable e) {
                    Toasty.error(ProfileActivity.this, "مجددا تلاش نمایید").show();
                }
            });
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
            ((TextInputEditText) findViewById(R.id.emailEt)).setText(user.Email);
        }
        if (user.CompanyName != null) {
            ((TextInputEditText) findViewById(R.id.companyEt)).setText(user.CompanyName);
        }
        if (user.LinkMainImageIdSrc != null && !user.LinkMainImageIdSrc.equals("")) {
            findViewById(R.id.noImage).setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(user.LinkMainImageIdSrc, (ImageView) findViewById(R.id.image));
            findViewById(R.id.image).setVisibility(View.VISIBLE);
        }
    }

    private void ClickAttach() {
        if (uploadInProgress)
            Toasty.info(this, "فایل انتخابی قبلی در حال بارگزاری است...", Toasty.LENGTH_LONG).show();
        else {
            new FileManagerService().clickAttach(this, result -> {
                Uri uri;
                if (result.getData() != null) {
                    uri = result.getData().getData();
                    if (uri != null) {
                        ImageLoader.getInstance().displayImage(uri.toString(), (ImageView) findViewById(R.id.image));
                        UploadFileToServer(FileManagerService.getFilePath(this, uri), fileUploadModel -> {
                            image_GUID = fileUploadModel.FileKey;
                            image_FilePath = uri.toString();
                        });
                    }
                }
            });
        }
    }

    private void UploadFileToServer(String url, Consumer<FileUploadModel> consumer) {
        if (AppUtil.isNetworkAvailable(this)) {
            uploadInProgress = true;
            Toasty.info(this, "در حال بارگذاری...", Toasty.LENGTH_LONG).show();
            ServiceExecute.execute(new FileUploaderService(this).uploadFile(url)).subscribe(new NtkObserver<FileUploadModel>() {
                @Override
                public void onNext(@NonNull FileUploadModel fileUploadModel) {
                    uploadInProgress = false;
                    consumer.accept(fileUploadModel);
                }

                @Override
                public void onError(Throwable e) {
                    uploadInProgress = false;
                    Toasty.error(ProfileActivity.this, "خطا در آپلود فایل").show();
                }
            });
        } else {
            Toasty.error(this, "انترنت در دسترس نیست").show();
        }
    }

}
