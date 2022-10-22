package ntk.android.estate.fragment;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaofeng.flowlayoutmanager.Alignment;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.annotations.NonNull;
import java9.util.function.Consumer;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.file.FileUploadModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.service.FileManagerService;
import ntk.android.base.services.file.FileUploaderService;
import ntk.android.base.utill.AppUtil;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewEstateActivity;
import ntk.android.estate.adapter.OtherImageAdapter;

public class NewEstateFragment5 extends BaseFragment {

    private static final int MAIN_IMAGE_REQ = 213;

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_estate_5);
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFont();
        findViewById(R.id.selectMainImage).setOnClickListener(t -> selectMainImage());
        findViewById(R.id.deleteImage).setOnClickListener(t -> deleteMainPage());
        findViewById(R.id.addOtherImageBtn).setOnClickListener(t -> selectOtherImage());
    }

    private void setFont() {
        Typeface t1 = FontManager.T1_Typeface(getContext());
        ((TextView) findViewById(R.id.tv1)).setTypeface(t1);
        ((TextView) findViewById(R.id.tv2)).setTypeface(t1);
        ((MaterialButton) findViewById(R.id.deleteImage)).setTypeface(t1);
    }


    private void deleteMainPage() {
        if (estateActivity().isUploaded()) {
            estateActivity().MainImage_GUID = "";
            ((ImageView) findViewById(R.id.selectedImageView)).setImageResource(0);
            findViewById(R.id.deleteImage).setVisibility(View.GONE);
        } else {
            Toasty.info(getContext(), "فایل انتخابی قبلی در حال بارگزاری است...", Toasty.LENGTH_LONG).show();
        }
    }


    private void selectMainImage() {
        if (estateActivity().isUploaded()) {
            ClickAttach(MAIN_IMAGE_REQ);
        } else {
            Toasty.info(getContext(), "فایل انتخابی قبلی در حال بارگزاری است...", Toasty.LENGTH_LONG).show();
        }
    }

    private void selectOtherImage() {
        if (estateActivity().isUploaded()) {
            ClickAttachOther();
        } else {
            Toasty.info(getContext(), "فایل انتخابی قبلی در حال بارگزاری است...", Toasty.LENGTH_LONG).show();
        }
    }

    public void ClickAttach(int REQ) {
        new FileManagerService().clickAttach(estateActivity(), result -> {
            Uri uri;
            if (result.getData() != null) {
                uri = result.getData().getData();
                if (uri != null) {
                    ImageLoader.getInstance().displayImage(uri.toString(), (ImageView) findViewById(R.id.selectedImageView));
                    findViewById(R.id.deleteImage).setVisibility(View.VISIBLE);
                    UploadFileToServer(FileManagerService.getFilePath(getContext(), uri),
                            fileUploadModel -> {
                                estateActivity().MainImage_GUID = fileUploadModel.FileKey;
                                estateActivity().MainImage_FilePath = uri.toString();
                            });
                }
            }
        });
    }

    public void ClickAttachOther() {
        new FileManagerService().clickAttach(estateActivity(), result -> {
            Uri uri;
            if (result.getData() != null) {
                uri = result.getData().getData();
                if (uri != null) {
                    UploadFileToServer(FileManagerService.getFilePath(getContext(), uri),
                            fileUploadModel -> {
                                estateActivity().OtherImageIds.add(fileUploadModel.FileKey);
                                estateActivity().OtherImageSrc.add(uri.toString());
                                if (isSafeFragment(this)) {
                                    RecyclerView rc = (RecyclerView) findViewById(R.id.imageRecycler);
                                    FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
                                    flowLayoutManager.setAutoMeasureEnabled(true);
                                    flowLayoutManager.setAlignment(Alignment.RIGHT);
                                    rc.setLayoutManager(flowLayoutManager);
                                    rc.setAdapter(new OtherImageAdapter(estateActivity(),
                                            estateActivity().OtherImageIds, estateActivity().OtherImageSrc));
                                }
                            });
                }
            }
        });
    }

    private void UploadFileToServer(String url, Consumer<FileUploadModel> consumer) {
        if (AppUtil.isNetworkAvailable(getContext())) {
            estateActivity().onUploadingStep();
            Toasty.info(getContext(), "در حال بارگذاری...", Toasty.LENGTH_LONG).show();
            ServiceExecute.execute(new FileUploaderService(getContext()).uploadFile(url))
                    .subscribe(new NtkObserver<FileUploadModel>() {
                        @Override
                        public void onNext(@NonNull FileUploadModel fileUploadModel) {
                            estateActivity().uploadFinished();
                            consumer.accept(fileUploadModel);
                        }

                        @Override
                        public void onError(Throwable e) {
                            estateActivity().uploadFinished();
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
