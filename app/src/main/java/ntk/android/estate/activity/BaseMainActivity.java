package ntk.android.estate.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import ntk.android.base.activity.abstraction.AbstractMainActivity;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.enums.EnumSortType;
import ntk.android.base.utill.prefrense.Preferences;

abstract class BaseMainActivity extends AbstractMainActivity {
    FilterModel row1;
    FilterModel row2;
    FilterModel row3;
    FilterModel row4;
    FilterModel row5;
    FilterModel row6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        generateFilterModel();
    }

    private void generateFilterModel() {
        row1=new FilterModel();
        row2=new FilterModel();
        row3=new FilterModel();
        row4=new FilterModel();
        row5=new FilterModel();
        row6=new FilterModel();
        //base on last item
        row1.setSortType(EnumSortType.Descending.index());
        row1.setSortColumn("CreatedDate");

        //special list item
        new FilterDataModel();
        row2.setSortType(EnumSortType.Random.index());
        row2.addFilter(new FilterDataModel()
                .setPropertyName("PropertyAds")
                .setPropertyAnyName("ViewLevel")
                .setStringValue("1,2,3,4,5,6"));
        row2.addFilter(new FilterDataModel()
                .setPropertyName("PropertyAds")
                .setPropertyAnyName("StationLevel")
                .setStringValue("212"));
        //ejare roozane
        row3.setSortType(EnumSortType.Descending.index());
        row3.addFilter(new FilterDataModel().setPropertyName("Contracts")
                .setPropertyAnyName("LinkEstateContractTypeId")
                .setStringValue("68dc5e3b-7c34-4412-c071-08d972b7fc67"));
        //forosh
        row4.setSortType(EnumSortType.Descending.index());
        row4.addFilter(new FilterDataModel().setPropertyName("Contracts")
                .setPropertyAnyName("LinkEstateContractTypeId")
                .setStringValue("6c2ccf97-2bc6-4a79-19a1-08d92cf7c414"));
        //rahn ejare
       row5.setSortType(EnumSortType.Descending.index());
       row5.addFilter(new FilterDataModel().setPropertyName("Contracts")
                .setPropertyAnyName("LinkEstateContractTypeId")
                .setStringValue("db4bf96d-f485-410f-12e5-08d92cf7fe11"));
        // articles
        row6.setSortType(EnumSortType.Descending.index());

    }
    public boolean isLogin(){
        return (Preferences.with(this).appVariableInfo().isLogin() && Preferences.with(this).UserInfo().userId() > 0);
    }
}
