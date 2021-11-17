package ntk.android.estate.fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.nostra13.universalimageloader.core.ImageLoader;

import es.dmoral.toasty.Toasty;
import io.reactivex.annotations.NonNull;
import java9.util.function.Consumer;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.file.FileUploadModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.service.FileManagerService;
import ntk.android.base.services.file.FileUploaderService;
import ntk.android.base.utill.AppUtill;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewEstateActivity;

public class NewEstateFragment5 extends BaseFragment {

    private static final int MAIN_IMAGE_REQ = 213;

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_estate_5);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(R.id.selectMainImage).setOnClickListener(t -> selectMainImage());

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void selectMainImage() {
        ClickAttach(MAIN_IMAGE_REQ);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void ClickAttach(int REQ) {
        new FileManagerService().clickAttach(estateActivity(), result -> {
            Uri uri;
            if (result.getData() != null) {
                uri = result.getData().getData();
                if (uri != null) {
                    ImageLoader.getInstance().displayImage(uri.toString(), (ImageView) findViewById(R.id.selectedImageView));
                    UploadFileToServer(FileManagerService.getFilePath(getContext(), uri),
                            fileUploadModel -> {
                                estateActivity().MainImage_GUID=fileUploadModel.FileKey;
                                estateActivity().MainImage_FilePath=uri.toString();
                            });
                }
            }
        });
    }

    private void UploadFileToServer(String url, Consumer<FileUploadModel> consumer) {
        if (AppUtill.isNetworkAvailable(getContext())) {
            ServiceExecute.execute(new FileUploaderService(getContext()).uploadFile(url))
                    .subscribe(new NtkObserver<FileUploadModel>() {
                        @Override
                        public void onNext(@NonNull FileUploadModel fileUploadModel) {
                            consumer.accept(fileUploadModel);
                        }

                        @Override
                        public void onError(Throwable e) {
                            //todo show error
                            Toasty.error(getContext(), "خطا در آپلود فایل").show();
                        }
                    });
        } else {
            Toasty.error(getContext(), "انترنت در دسترس نیست").show();

        }
    }

    private NewEstateActivity estateActivity() {
        return ((NewEstateActivity) getActivity());
    }

    public boolean isValidForm() {
        return true;
    }

}
