package ntk.android.estate.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.xiaofeng.flowlayoutmanager.Alignment;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import java9.util.stream.Collectors;
import java9.util.stream.StreamSupport;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeUsageModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.estate.EstatePropertyTypeLanduseService;
import ntk.android.base.services.estate.EstatePropertyTypeService;
import ntk.android.base.services.estate.EstatePropertyTypeUsageService;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewEstateActivity;
import ntk.android.estate.adapter.EstatePropertyLandUseAdapterSelector;
import ntk.android.estate.adapter.EstatePropertyTypeAdapterSelector;
import ntk.android.estate.view.NumberTextWatcherForThousand;

public class NewEstateFragment1 extends BaseFragment {
    //share variable between edit and new prevent change adapter
    boolean isAllAdapterClickable = true;
    //to remove all selected value if landUse change
    EstatePropertyTypeLanduseModel lastSelectedLandUse;
    int count;
    List<EstatePropertyTypeUsageModel> typeUsages;
    List<EstatePropertyTypeModel> propertyType;
    List<EstatePropertyTypeLanduseModel> landUses;

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_estate_1);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        count = 0;
        //set editText separator
        TextInputEditText AreaEditText = findViewById(R.id.EstateAreaEditText);
        AreaEditText.addTextChangedListener(new NumberTextWatcherForThousand(AreaEditText));
        if (estateActivity().model().Area != 0) {
            AreaEditText.setText(String.valueOf(estateActivity().model().Area));
        }
        if (estateActivity().model().PropertyTypeLanduse != null) {
            changeUi();
            if (estateActivity().model().CreatedYaer != 0)
                ((TextInputEditText) findViewById(R.id.EstatePropertyOneEditText)).setText(String.valueOf(estateActivity().model().CreatedYaer));
            if (estateActivity().model().Partition != 0)
                ((TextInputEditText) findViewById(R.id.EstatePropertyTowEditText)).setText(String.valueOf(estateActivity().model().Partition));
        }
        setFont();
        getData();
    }

    private void setFont() {
        //textView
        Typeface t1 = FontManager.T1_Typeface(getContext());
        ((TextView) findViewById(R.id.tv1)).setTypeface(t1);
        ((TextView) findViewById(R.id.tv2)).setTypeface(t1);
        //textInputLayout
        ((TextInputLayout) findViewById(R.id.EstatePropertyOneTextInput)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.EstatePropertyTowTextInput)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.EstateAreaTextInput)).setTypeface(t1);
        //TextInputEditText
        ((TextInputEditText) findViewById(R.id.EstateAreaEditText)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.EstatePropertyOneEditText)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.EstatePropertyTowEditText)).setTypeface(t1);
    }

    private void getData() {
        estateActivity().showProgress();
        getPropertyType();
        getTypeUsage();
        getTypeLandUse();

    }

    private void getTypeLandUse() {
        ServiceExecute.execute(new EstatePropertyTypeLanduseService(getContext()).getAll(new FilterModel().setRowPerPage(100)))
                .subscribe(new NtkObserver<ErrorException<EstatePropertyTypeLanduseModel>>() {
                    @Override
                    public void onNext(@NonNull ErrorException<EstatePropertyTypeLanduseModel> response) {
                        landUses = response.ListItems;
                        if (estateActivity().model().LinkPropertyTypeLanduseId != null) {
                            EstatePropertyTypeLanduseModel find =
                                    StreamSupport.stream(landUses).filter(t -> t.Id.equals(estateActivity().model().LinkPropertyTypeLanduseId)).findFirst().orElse(null);
                            estateActivity().model().PropertyTypeLanduse = (find);
                            lastSelectedLandUse = find;
                        }
                        showData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        estateActivity().showErrorView();
                    }
                });
    }

    private void getPropertyType() {
        ServiceExecute.execute(new EstatePropertyTypeService(getContext()).getAll(new FilterModel().setRowPerPage(100))).subscribe(new NtkObserver<ErrorException<EstatePropertyTypeModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstatePropertyTypeModel> response) {
                propertyType = response.ListItems;

                showData();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (estateActivity() != null) estateActivity().showErrorView();
            }
        });
    }

    private void getTypeUsage() {

        ServiceExecute.execute(new EstatePropertyTypeUsageService(getContext()).getAll(new FilterModel().setRowPerPage(100)))
                .subscribe(new NtkObserver<>() {
                    @Override
                    public void onNext(@NonNull ErrorException<EstatePropertyTypeUsageModel> response) {
                        typeUsages = response.ListItems;

                        if (estateActivity().model().LinkPropertyTypeUsageId != null) {
                            EstatePropertyTypeUsageModel find =
                                    StreamSupport.stream(typeUsages).filter(t -> t.Id.equals(estateActivity().model().LinkPropertyTypeUsageId)).findFirst().orElse(null);
                            estateActivity().model().PropertyTypeUsage = (find);

                        }
                        showData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        estateActivity().showErrorView();
                    }
                });
    }


    protected synchronized void showData() {
        if (count == 2) {
            EstatePropertyTypeAdapterSelector adapter = new EstatePropertyTypeAdapterSelector(isAllAdapterClickable, typeUsages, estateActivity().model().PropertyTypeUsage,
                    estatePropertyTypeUsageModel -> {
                        findViewById(R.id.cardLandUsesView).setVisibility(View.VISIBLE);
                        estateActivity().model().PropertyTypeUsage = estatePropertyTypeUsageModel;
                        estateActivity().model().PropertyTypeLanduse = null;
                        setTypeUsage(estatePropertyTypeUsageModel);
                    });
            RecyclerView rc = findViewById(R.id.estateTypeRc);
            rc.setAdapter(adapter);
            FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
            flowLayoutManager.setAutoMeasureEnabled(true);
            flowLayoutManager.setAlignment(Alignment.RIGHT);
            rc.setLayoutManager(flowLayoutManager);
            if (estateActivity().model().PropertyTypeUsage != null) {
                findViewById(R.id.cardLandUsesView).setVisibility(View.VISIBLE);
                setTypeUsage(estateActivity().model().PropertyTypeUsage);
            }
            estateActivity().showContent();
        } else ++count;
    }

    void setTypeUsage(EstatePropertyTypeUsageModel estatePropertyTypeUsageModel) {
        changeUi();
        List<EstatePropertyTypeModel> mappers = StreamSupport.stream(propertyType)
                .filter(t -> t.LinkPropertyTypeUsageId.equals(estatePropertyTypeUsageModel.Id))
                .collect(Collectors.toList());
        List<EstatePropertyTypeLanduseModel> models = StreamSupport.stream(landUses).
                filter(t -> StreamSupport.stream(mappers)
                        .anyMatch(k -> k.LinkPropertyTypeLanduseId.equals(t.Id)))
                .collect(Collectors.toList());
        RecyclerView rc = findViewById(R.id.EstateLandUsedRc);
        rc.setAdapter(new EstatePropertyLandUseAdapterSelector(isAllAdapterClickable, models, estateActivity().model().PropertyTypeLanduse,
                t -> {
                    estateActivity().model().PropertyTypeLanduse = t;
                    changeUi();
                }));
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        flowLayoutManager.maxItemsPerLine(4);
        flowLayoutManager.setAlignment(Alignment.RIGHT);
        rc.setLayoutManager(flowLayoutManager);


    }

    void changeUi() {
        EstatePropertyTypeLanduseModel lastSelectedLandUse = estateActivity().model().PropertyTypeLanduse;
        if (lastSelectedLandUse == null) {
            findViewById(R.id.EstatePropertyOneTextInput).setVisibility(View.GONE);
            findViewById(R.id.EstatePropertyTowTextInput).setVisibility(View.GONE);
            ((EditText) findViewById(R.id.EstatePropertyOneEditText)).setText("");
            ((EditText) findViewById(R.id.EstatePropertyTowEditText)).setText("");
        } else {
            if (lastSelectedLandUse.TitleCreatedYaer != null && !lastSelectedLandUse.TitleCreatedYaer.equals("") && !lastSelectedLandUse.TitleCreatedYaer.equals("---")) {
                findViewById(R.id.EstatePropertyOneTextInput).setVisibility(View.VISIBLE);
                ((TextInputLayout) findViewById(R.id.EstatePropertyOneTextInput)).setHint(lastSelectedLandUse.TitleCreatedYaer);
            } else {
                findViewById(R.id.EstatePropertyOneTextInput).setVisibility(View.GONE);
                ((EditText) findViewById(R.id.EstatePropertyOneEditText)).setText("");
            }
            if (lastSelectedLandUse.TitlePartition != null && !lastSelectedLandUse.TitlePartition.equals("") && !lastSelectedLandUse.TitlePartition.equals("---")) {
                findViewById(R.id.EstatePropertyTowTextInput).setVisibility(View.VISIBLE);
                ((TextInputLayout) findViewById(R.id.EstatePropertyTowTextInput)).setHint(lastSelectedLandUse.TitlePartition);
            } else {
                findViewById(R.id.EstatePropertyTowTextInput).setVisibility(View.GONE);
                ((EditText) findViewById(R.id.EstatePropertyTowEditText)).setText("");
            }
        }
    }

    public boolean isValidForm() {

        if (estateActivity().model().PropertyTypeUsage == null) {
            Toasty.error(getContext(), "نوع کاربری را انتخاب نمایید", Toasty.LENGTH_LONG, true).show();
            return false;
        }
        if (estateActivity().model().PropertyTypeLanduse == null) {
            Toasty.error(getContext(), "نوع ملک را انتخاب نمایید", Toasty.LENGTH_LONG, true).show();
            return false;
        }
        estateActivity().model().LinkPropertyTypeLanduseId = estateActivity().model().PropertyTypeLanduse.Id;
        estateActivity().model().LinkPropertyTypeUsageId = estateActivity().model().PropertyTypeUsage.Id;
        if (!((TextInputEditText) findViewById(R.id.EstateAreaEditText)).getText().toString().trim().equals(""))
            estateActivity().model().Area = Double.parseDouble(NumberTextWatcherForThousand.trimCommaOfString(((TextInputEditText) findViewById(R.id.EstateAreaEditText)).getText().toString().trim()));
        else {
            Toasty.error(getContext(), "متراژ را وارد نمایید", Toasty.LENGTH_LONG, true).show();
            return false;
        }
        if (!(((TextInputEditText) findViewById(R.id.EstatePropertyOneEditText)).getText().toString().trim().equals("")))
            try {
                estateActivity().model().CreatedYaer = Integer.parseInt(((TextInputEditText) findViewById(R.id.EstatePropertyOneEditText)).getText().toString().trim());
            } catch (Exception e) {
                Toasty.error(getContext(), "فرمت عدد وارده در قسمت " + lastSelectedLandUse.TitleCreatedYaer + "اشتباه است", Toasty.LENGTH_LONG, true).show();

                return false;
            }
        else
            estateActivity().model().CreatedYaer = 0;

        if (!((TextInputEditText) findViewById(R.id.EstatePropertyTowEditText)).getText().toString().trim().equals(""))
            try {
                estateActivity().model().Partition = Integer.parseInt(((TextInputEditText) findViewById(R.id.EstatePropertyTowEditText)).getText().toString().trim());
            } catch (Exception e) {
                Toasty.error(getContext(), "فرمت عدد وارده در قسمت " + lastSelectedLandUse.TitlePartition + "اشتباه است", Toasty.LENGTH_LONG, true).show();
                return false;
            }
        else
            estateActivity().model().Partition = 0;
        if (lastSelectedLandUse != null && !lastSelectedLandUse.Id.equals(estateActivity().model().PropertyTypeLanduse.Id)) {
            estateActivity().model().PropertyDetailGroups = null;
            estateActivity().model().PropertyDetailValues = null;
        }
        return true;
    }

    NewEstateActivity estateActivity() {
        return ((NewEstateActivity) getActivity());
    }


}