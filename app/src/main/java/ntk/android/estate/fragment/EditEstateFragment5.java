package ntk.android.estate.fragment;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaofeng.flowlayoutmanager.Alignment;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;

import es.dmoral.toasty.Toasty;
import ntk.android.estate.R;
import ntk.android.estate.adapter.OtherImageAdapter;

public class EditEstateFragment5 extends NewEstateFragment5{
    @Override
    protected void showPrevImages() {
        if (estateActivity().model().LinkMainImageIdSrc != null && !estateActivity().model().LinkMainImageIdSrc.equals("")){
            ImageLoader.getInstance().displayImage(estateActivity().model().LinkMainImageIdSrc, (ImageView) findViewById(R.id.selectedImageView));
            findViewById(R.id.deleteImage).setVisibility(View.VISIBLE);
            findViewById(R.id.extraImageCardView).setVisibility(View.VISIBLE);
            findViewById(R.id.extraImagePadding).setVisibility(View.VISIBLE);
        }

        if (estateActivity().OtherImageIds!=null&&estateActivity().OtherImageIds.size()>0){
            RecyclerView rc = findViewById(R.id.imageRecycler);
            FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
            flowLayoutManager.setAutoMeasureEnabled(true);
            flowLayoutManager.setAlignment(Alignment.RIGHT);
            rc.setLayoutManager(flowLayoutManager);
            rc.setAdapter(new OtherImageAdapter(estateActivity(),
                    estateActivity().OtherImageIds, estateActivity().OtherImageSrc));
        }
    }
}
