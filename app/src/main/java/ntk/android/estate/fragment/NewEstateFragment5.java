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
import java9.util.stream.StreamSupport;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.file.FileUploadModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.service.FileManagerService;
import ntk.android.base.services.file.FileUploaderService;
import ntk.android.base.utill.AppUtil;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.MyApplication;
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
        findViewById(R.id.deleteImage).setOnClickListener(t -> deleteMainImage());
        findViewById(R.id.addOtherImageBtn).setOnClickListener(t -> selectOtherImage());
        showPrevImages();

    }

    protected void showPrevImages() {
        if (estateActivity().MainImage_FilePath != null && !estateActivity().MainImage_FilePath.equals("")){
            ImageLoader.getInstance().displayImage(estateActivity().MainImage_FilePath, (ImageView) findViewById(R.id.selectedImageView));
            findViewById(R.id.deleteImage).setVisibility(View.VISIBLE);
            findViewById(R.id.extraImageCardView).setVisibility(View.VISIBLE);
            findViewById(R.id.extraImagePadding).setVisibility(View.VISIBLE);
        }
        if (estateActivity().OtherImageIds!=null&&estateActivity().OtherImageIds.size()>0){
            RecyclerView rc = (RecyclerView) findViewById(R.id.imageRecycler);
            FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
            flowLayoutManager.setAutoMeasureEnabled(true);
            flowLayoutManager.setAlignment(Alignment.RIGHT);
            rc.setLayoutManager(flowLayoutManager);
            rc.setAdapter(new OtherImageAdapter(estateActivity(),
                    estateActivity().OtherImageIds, estateActivity().OtherImageSrc));
        }
    }

    private void setFont() {
        Typeface t1 = FontManager.T1_Typeface(getContext());
        ((TextView) findViewById(R.id.tv1)).setTypeface(t1);
        ((TextView) findViewById(R.id.tv2)).setTypeface(t1);
        ((MaterialButton) findViewById(R.id.deleteImage)).setTypeface(t1);
    }


    protected void deleteMainImage() {
        if (estateActivity().isUploaded()) {

            if (estateActivity().OtherImageIds.size() > 0) {
                Toasty.error(getContext(), "قادر به حذف عکس اصلی نیستید در حالی که در قسمت عکس های بیشتر موردی اضافه کرده اید ").show();
                return;
            }
            estateActivity().MainImage_GUID = "";
            estateActivity().MainImage_FilePath = "";
            estateActivity().model().LinkMainImageId=0;
            estateActivity().model().LinkMainImageIdSrc="";
            ((ImageView) findViewById(R.id.selectedImageView)).setImageResource(0);
            findViewById(R.id.deleteImage).setVisibility(View.GONE);
            findViewById(R.id.extraImageCardView).setVisibility(View.GONE);
            findViewById(R.id.extraImagePadding).setVisibility(View.GONE);
        } else {
            Toasty.info(getContext(), "فایل انتخابی قبلی در حال بارگزاری است...", Toasty.LENGTH_LONG).show();
        }
    }


    protected void selectMainImage() {
        if (estateActivity().isUploaded()) {
            ClickAttach(MAIN_IMAGE_REQ);
        } else {
            Toasty.info(getContext(), "فایل انتخابی قبلی در حال بارگزاری است...", Toasty.LENGTH_LONG).show();
        }
    }

    protected void selectOtherImage() {
        if (estateActivity().isUploaded()) {
            ClickAttachOther();
        } else {
            Toasty.info(getContext(), "فایل انتخابی قبلی در حال بارگزاری است...", Toasty.LENGTH_LONG).show();
        }
    }

    public void ClickAttach(int REQ) {
        new FileManagerService().clickAttach(estateActivity(), result -> {
            Uri uri;
            uri = result;
            if (uri != null) {
                if (uploadedBefore(uri.toString()))
                    Toasty.error(getContext(), "این فایل قبلا انتخاب شده است").show();
                else {
                    ImageLoader.getInstance().displayImage(uri.toString(), (ImageView) findViewById(R.id.selectedImageView));
                    findViewById(R.id.extraImageCardView).setVisibility(View.VISIBLE);
                    findViewById(R.id.extraImagePadding).setVisibility(View.VISIBLE);
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
            Uri uri = result;
            if (uri != null) {
                if (uploadedBefore(uri.toString()))
                    Toasty.error(getContext(), "این فایل قبلا انتخاب شده است").show();
                else
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
        });
    }

    private boolean uploadedBefore(String newSelectedPath) {
        return (StreamSupport.stream(estateActivity().OtherImageSrc).anyMatch(s1 -> s1 != null && s1.equals(newSelectedPath)) || estateActivity().MainImage_FilePath.equals(newSelectedPath));
    }

    private void UploadFileToServer(String url, Consumer<FileUploadModel> consumer) {
        if (url == null)
            Toasty.error(MyApplication.get(), "خطا در انتخاب فایل مجددا تلاش فرمایید").show();
        else {
            boolean error = false;
            try {
                String s = String.valueOf(Uri.parse(url));
            } catch (Exception e) {
                error = true;
                Toasty.error(MyApplication.get(), "خطا در انتخاب فایل مجددا تلاش فرمایید").show();
            }
            if (!error)
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
    }

    protected NewEstateActivity estateActivity() {
        return ((NewEstateActivity) getActivity());
    }

    public boolean isValidForm() {
        return true;
    }

}
