package ntk.android.estate.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import java9.util.function.Consumer;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.adapter.SpinnerAdapter;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.core.CoreLocationModel;
import ntk.android.base.services.core.CoreLocationService;
import ntk.android.estate.R;

public class SelectProviceDialog extends DialogFragment {

    private Consumer<CoreLocationModel> func;
    private CoreLocationModel selectedCountry;
    private CoreLocationModel selectedProvince;
    private CoreLocationModel selectedCity;
    private CoreLocationModel selectedArea;

    public static SelectProviceDialog START_DIALOG(Consumer<CoreLocationModel> func) {
        SelectProviceDialog fragment = new SelectProviceDialog();
        fragment.setCancelable(false);
        fragment.setInterface(func);
        return fragment;
    }

    private void setInterface(Consumer<CoreLocationModel> func) {
        this.func = func;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.select_provice_dialog, container, false);

        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getLayoutParams().width = (BaseRecyclerAdapter.getScreenWidth() / 3) * 2;
        view.findViewById(R.id.sendLocation).setOnClickListener(view1 -> {
            view1.setEnabled(false);
            sendLocation();
            view1.setEnabled(true);
        });
        view.findViewById(R.id.cancelLocation).setOnClickListener(view1 -> {
            func.accept(null);
            dismiss();
        });
        getCountry();
    }



    private void sendLocation() {
        if (selectedArea != null) {
            func.accept(selectedArea);
            dismiss();
        } else if (selectedCity != null) {
            func.accept(selectedCity);
            dismiss();
        } else if (selectedProvince != null) {
            func.accept(selectedProvince);
            dismiss();
        }else if (selectedCountry!=null){
            func.accept(selectedCountry);
            dismiss();
        } else{
            Toasty.error(getContext(), "مکانی انتخاب نشده است").show();
        }
    }

    private void getCountry() {
        View progress = getView().findViewById(R.id.progressView);
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
        FilterModel filterModel = new FilterModel().setRowPerPage(1000);

        ServiceExecute.execute(new CoreLocationService(getContext()).getAllCountry(filterModel)).subscribe(new NtkObserver<ErrorException<CoreLocationModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<CoreLocationModel> model) {
                try {
                    View progress = getView().findViewById(R.id.progressView);
                    if (progress != null) {
                        progress.setVisibility(View.GONE);
                    }
                    MaterialAutoCompleteTextView spinner = getView().findViewById(R.id.EstateCountryAutoComplete);
                    List<String> names = new ArrayList<>();
                    for (CoreLocationModel t : model.ListItems)
                        names.add(t.Title);
                    if (names.size() == 0)
                        names.add("موردی یافت نشد");
                    else
                        names.add(0, "انتخاب کنید");
                    SpinnerAdapter<CoreLocationModel> locationAdapter = new SpinnerAdapter<CoreLocationModel>(getContext(), names);
                    spinner.setOnItemClickListener((parent, view, position, id) -> {
                        if (position > 0) {
                            selectedCountry = model.ListItems.get(position - 1);
                            ((MaterialAutoCompleteTextView) getView().findViewById(R.id.EstateProvinceAutoComplete)).setAdapter(new SpinnerAdapter<>(getContext(), new ArrayList<>()));
                            ((MaterialAutoCompleteTextView) getView().findViewById(R.id.EstateCityAutoComplete)).setAdapter(new SpinnerAdapter<>(getContext(), new ArrayList<>()));
                            ((MaterialAutoCompleteTextView) getView().findViewById(R.id.EstateAreaAutoComplete)).setAdapter(new SpinnerAdapter<>(getContext(), new ArrayList<>()));
                            selectedProvince = null;
                            selectedCity = null;
                            selectedArea = null;
                            getProvince();
                        }
                    });
                    spinner.setAdapter(locationAdapter);
                    // Do something for lollipop and above versions
                    spinner.setText(locationAdapter.getItem(0), false);
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                View errorView = getView();
                if (errorView != null) {
                    View progress = errorView.findViewById(R.id.progressView);
                    if (progress != null) {
                        progress.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void getProvince() {
        View progress = getView().findViewById(R.id.progressView);
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
        FilterModel filterModel = new FilterModel().setRowPerPage(100);
        filterModel.addFilter(new FilterDataModel().setPropertyName("LinkParentId").setIntValue((long) selectedCountry.Id));

        ServiceExecute.execute(new CoreLocationService(getContext()).getAllProvinces(filterModel)).subscribe(new NtkObserver<ErrorException<CoreLocationModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<CoreLocationModel> model) {
                try {
                    View progress = getView().findViewById(R.id.progressView);
                    if (progress != null) {
                        progress.setVisibility(View.GONE);
                    }
                    MaterialAutoCompleteTextView spinner = getView().findViewById(R.id.EstateProvinceAutoComplete);
                    List<String> names = new ArrayList<>();
                    for (CoreLocationModel t : model.ListItems)
                        names.add(t.Title);
                    if (names.size() == 0)
                        names.add("موردی یافت نشد");
                    else
                        names.add(0, "انتخاب کنید");
                    SpinnerAdapter<CoreLocationModel> locationAdapter = new SpinnerAdapter<CoreLocationModel>(getContext(), names);
                    spinner.setOnItemClickListener((parent, view, position, id) -> {
                        if (position > 0) {
                            selectedProvince = model.ListItems.get(position - 1);
                            ((MaterialAutoCompleteTextView) getView().findViewById(R.id.EstateCityAutoComplete)).setAdapter(new SpinnerAdapter<>(getContext(), new ArrayList<>()));
                            ((MaterialAutoCompleteTextView) getView().findViewById(R.id.EstateAreaAutoComplete)).setAdapter(new SpinnerAdapter<>(getContext(), new ArrayList<>()));
                            selectedCity = null;
                            selectedArea = null;
                            getCity();
                        }
                    });
                    spinner.setAdapter(locationAdapter);
                    // Do something for lollipop and above versions
                    spinner.setText(locationAdapter.getItem(0), false);
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                View errorView = getView();
                if (errorView != null) {
                    View progress = errorView.findViewById(R.id.progressView);
                    if (progress != null) {
                        progress.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void getCity() {
        View progress = getView().findViewById(R.id.progressView);
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
        FilterModel filterModel = new FilterModel().setRowPerPage(1000);
        filterModel.addFilter(new FilterDataModel().setPropertyName("LinkParentId").setIntValue((long) selectedProvince.Id));
        ServiceExecute.execute(new CoreLocationService(getContext()).getAll(filterModel)).subscribe(new NtkObserver<ErrorException<CoreLocationModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<CoreLocationModel> model) {
                try {
                    View progress = getView().findViewById(R.id.progressView);
                    if (progress != null) {
                        progress.setVisibility(View.GONE);
                    }
                    MaterialAutoCompleteTextView spinner = getView().findViewById(R.id.EstateCityAutoComplete);
                    List<String> names = new ArrayList<>();
                    for (CoreLocationModel t : model.ListItems)
                        names.add(t.Title);
                    if (names.size() == 0)
                        names.add("موردی یافت نشد");
                    else
                        names.add(0, "انتخاب کنید");
                    SpinnerAdapter<CoreLocationModel> locationAdapter = new SpinnerAdapter<CoreLocationModel>(getContext(), names);
                    spinner.setOnItemClickListener((parent, view, position, id) -> {
                        if (position > 0) {
                            selectedCity = model.ListItems.get(position - 1);
                            ((MaterialAutoCompleteTextView) getView().findViewById(R.id.EstateAreaAutoComplete)).setAdapter(new SpinnerAdapter<>(getContext(), new ArrayList<>()));
                            selectedArea = null;
                            getArea();
                        }
                    });
                    spinner.setAdapter(locationAdapter);
                    // Do something for lollipop and above versions
                    spinner.setText(locationAdapter.getItem(0), false);
                } catch (Exception e) {
                }
            }


            @Override
            public void onError(@NonNull Throwable e) {
                View errorView = getView();
                if (errorView != null) {
                    View progress = errorView.findViewById(R.id.progressView);
                    if (progress != null) {
                        progress.setVisibility(View.GONE);
                    }
                }
            }

        });
    }

    private void getArea() {
        View progress = getView().findViewById(R.id.progressView);
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
        FilterModel filterModel = new FilterModel().setRowPerPage(1000);
        filterModel.addFilter(new FilterDataModel().setPropertyName("LinkParentId").setIntValue((long) selectedCity.Id));
        ServiceExecute.execute(new CoreLocationService(getContext()).getAll(filterModel)).subscribe(new NtkObserver<ErrorException<CoreLocationModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<CoreLocationModel> model) {
                try {
                    View progress = getView().findViewById(R.id.progressView);
                    if (progress != null) {
                        progress.setVisibility(View.GONE);
                    }
                    MaterialAutoCompleteTextView spinner = getView().findViewById(R.id.EstateAreaAutoComplete);
                    List<String> names = new ArrayList<>();
                    for (CoreLocationModel t : model.ListItems)
                        names.add(t.Title);
                    if (names.size() == 0)
                        names.add("موردی یافت نشد");
                    else
                        names.add(0, "انتخاب کنید");
                    SpinnerAdapter<CoreLocationModel> locationAdapter = new SpinnerAdapter<CoreLocationModel>(getContext(), names);
                    spinner.setOnItemClickListener((parent, view, position, id) -> {
                        if (position > 0) {
                            selectedArea = model.ListItems.get(position - 1);
                        }
                    });

                    spinner.setAdapter(locationAdapter);
                    // Do something for lollipop and above versions
                    spinner.setText(locationAdapter.getItem(0), false);
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                View errorView = getView();
                if (errorView != null) {
                    View progress = errorView.findViewById(R.id.progressView);
                    if (progress != null) {
                        progress.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

}