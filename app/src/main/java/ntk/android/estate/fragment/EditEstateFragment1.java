package ntk.android.estate.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EditEstateFragment1 extends NewEstateFragment1 {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isAllAdapterClickable = false;
    }
//    @Override
//    protected synchronized void showData() {
//        if (count == 2) {
//            EstatePropertyTypeAdapterSelector adapter = new EstatePropertyTypeAdapterSelector(typeUsages, estateActivity().model().PropertyTypeUsage,
//                    estatePropertyTypeUsageModel -> {
//
//                    });
//            RecyclerView rc = findViewById(R.id.estateTypeRc);
//            rc.setAdapter(adapter);
//
//            FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
//            flowLayoutManager.setAutoMeasureEnabled(true);
//            flowLayoutManager.setAlignment(Alignment.RIGHT);
//            rc.setLayoutManager(flowLayoutManager);
//            if (estateActivity().model().PropertyTypeUsage != null) {
//                findViewById(R.id.cardLandUsesView).setVisibility(View.VISIBLE);
//                setTypeUsage(estateActivity().model().PropertyTypeUsage);
//            }
//            estateActivity().showContent();
//        } else ++count;
//    }
//
//    @Override
//    void setTypeUsage(EstatePropertyTypeUsageModel estatePropertyTypeUsageModel) {
//        changeUi();
//        List<EstatePropertyTypeModel> mappers = StreamSupport.stream(propertyType)
//                .filter(t -> t.LinkPropertyTypeUsageId.equals(estatePropertyTypeUsageModel.Id))
//                .collect(Collectors.toList());
//        List<EstatePropertyTypeLanduseModel> models = StreamSupport.stream(landUses).
//                filter(t -> StreamSupport.stream(mappers)
//                        .anyMatch(k -> k.LinkPropertyTypeLanduseId.equals(t.Id)))
//                .collect(Collectors.toList());
//        RecyclerView rc = findViewById(R.id.EstateLandUsedRc);
//        rc.setAdapter(new EstatePropertyLandUseAdapterSelector(models, estateActivity().model().PropertyTypeLanduse,
//                t -> {
//
//                }));
//        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
//        flowLayoutManager.setAutoMeasureEnabled(true);
//        flowLayoutManager.maxItemsPerLine(4);
//        flowLayoutManager.setAlignment(Alignment.RIGHT);
//        rc.setLayoutManager(flowLayoutManager);
//
//
//    }
}
