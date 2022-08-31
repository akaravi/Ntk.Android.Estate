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
import java9.util.stream.StreamSupport;
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

    private List<CoreLocationModel> countries = new ArrayList<>();
    private List<CoreLocationModel> provinces = new ArrayList<>();
    private List<CoreLocationModel> cities = new ArrayList<>();
    private List<CoreLocationModel> areas = new ArrayList<>();

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
        } else if (selectedCountry != null) {
            func.accept(selectedCountry);
            dismiss();
        } else {
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

                    countries = model.ListItems;
                    MaterialAutoCompleteTextView spinner = getView().findViewById(R.id.EstateCountryAutoComplete);
                    spinner.setThreshold(1);
                    List<String> names = new ArrayList<>();
                    for (CoreLocationModel t : model.ListItems)
                        names.add(t.Title);
                    if (names.size() == 0)
                        names.add("موردی یافت نشد");

                    SpinnerAdapter<CoreLocationModel> locationAdapter = new SpinnerAdapter<CoreLocationModel>(getContext(), names);
                    spinner.setOnItemClickListener((parent, view, position, id) -> {
                        selectedCountry =
                                StreamSupport.stream(countries).filter(t -> t.Title.equals(parent.getItemAtPosition(position).toString())).findFirst().orElse(null);
                        if (selectedCountry != null) {
                            ((MaterialAutoCompleteTextView) getView().findViewById(R.id.EstateProvinceAutoComplete)).setAdapter(new SpinnerAdapter<>(getContext(), new ArrayList<>()));
                            ((MaterialAutoCompleteTextView) getView().findViewById(R.id.EstateCityAutoComplete)).setAdapter(new SpinnerAdapter<>(getContext(), new ArrayList<>()));
                            ((MaterialAutoCompleteTextView) getView().findViewById(R.id.EstateAreaAutoComplete)).setAdapter(new SpinnerAdapter<>(getContext(), new ArrayList<>()));
                            provinces = new ArrayList<>();
                            selectedProvince = null;
                            cities = new ArrayList<>();
                            selectedCity = null;
                            areas = new ArrayList<>();
                            selectedArea = null;
                            getProvince();
                        }
                    });
                    spinner.setAdapter(locationAdapter);
                    // Do something for lollipop and above versions
                    if ( model.ListItems.size() == 0)  spinner.setText(locationAdapter.getItem(0), false);
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
                    provinces = model.ListItems;
                    MaterialAutoCompleteTextView spinner = getView().findViewById(R.id.EstateProvinceAutoComplete);
                    spinner.setThreshold(1);
                    List<String> names = new ArrayList<>();
                    for (CoreLocationModel t : model.ListItems)
                        names.add(t.Title);
                    if (names.size() == 0) {
                        names.add("موردی یافت نشد");
                        getView().findViewById(R.id.EstateProvinceTextInput).setVisibility(View.GONE);
                        getView().findViewById(R.id.EstateCityTextInput).setVisibility(View.GONE);
                        getView().findViewById(R.id.EstateAreaTextInput).setVisibility(View.GONE);

                    } else {
                        getView().findViewById(R.id.EstateProvinceTextInput).setVisibility(View.VISIBLE);

                    }
                    SpinnerAdapter<CoreLocationModel> locationAdapter = new SpinnerAdapter<CoreLocationModel>(getContext(), names);
                    spinner.setOnItemClickListener((parent, view, position, id) -> {
                        selectedProvince = StreamSupport.stream(provinces).filter(t -> t.Title.equals(parent.getItemAtPosition(position).toString())).findFirst().orElse(null);
                        if (selectedProvince != null) {
                            ((MaterialAutoCompleteTextView) getView().findViewById(R.id.EstateCityAutoComplete)).setAdapter(new SpinnerAdapter<>(getContext(), new ArrayList<>()));
                            ((MaterialAutoCompleteTextView) getView().findViewById(R.id.EstateAreaAutoComplete)).setAdapter(new SpinnerAdapter<>(getContext(), new ArrayList<>()));
                            cities = new ArrayList<>();
                            selectedCity = null;
                            areas = new ArrayList<>();
                            selectedArea = null;
                            getCity();
                        }
                    });
                    spinner.setAdapter(locationAdapter);
                    // Do something for lollipop and above versions
                    if ( model.ListItems.size() == 0)   spinner.setText(locationAdapter.getItem(0), false);
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
                    cities = model.ListItems;
                    MaterialAutoCompleteTextView spinner = getView().findViewById(R.id.EstateCityAutoComplete);
                    spinner.setThreshold(1);
                    List<String> names = new ArrayList<>();
                    for (CoreLocationModel t : model.ListItems)
                        names.add(t.Title);
                    if (names.size() == 0) {
                        names.add("موردی یافت نشد");
                        getView().findViewById(R.id.EstateCityTextInput).setVisibility(View.GONE);
                        getView().findViewById(R.id.EstateAreaTextInput).setVisibility(View.GONE);

                    } else {
                        getView().findViewById(R.id.EstateCityTextInput).setVisibility(View.VISIBLE);

                    }
                    SpinnerAdapter<CoreLocationModel> locationAdapter = new SpinnerAdapter<CoreLocationModel>(getContext(), names);
                    spinner.setOnItemClickListener((parent, view, position, id) -> {
                        selectedCity = StreamSupport.stream(cities).filter(t -> t.Title.equals(parent.getItemAtPosition(position).toString())).findFirst().orElse(null);
                        if (selectedCity != null) {
                            ((MaterialAutoCompleteTextView) getView().findViewById(R.id.EstateAreaAutoComplete)).setAdapter(new SpinnerAdapter<>(getContext(), new ArrayList<>()));
                            areas = new ArrayList<>();
                            selectedArea = null;
                            getArea();
                        }
                    });
                    spinner.setAdapter(locationAdapter);
                    // Do something for lollipop and above versions
                    if ( model.ListItems.size() == 0)  spinner.setText(locationAdapter.getItem(0), false);
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
                    if (names.size() == 0) {
                        names.add("موردی یافت نشد");
                        getView().findViewById(R.id.EstateAreaTextInput).setVisibility(View.GONE);

                    } else {
                        getView().findViewById(R.id.EstateAreaTextInput).setVisibility(View.VISIBLE);

                    }
                    SpinnerAdapter<CoreLocationModel> locationAdapter = new SpinnerAdapter<CoreLocationModel>(getContext(), names);
                    spinner.setOnItemClickListener((parent, view, position, id) -> {
                        selectedArea = StreamSupport.stream(areas).filter(t -> t.Title.equals(parent.getItemAtPosition(position).toString())).findFirst().orElse(null);

                    });

                    spinner.setAdapter(locationAdapter);
                    // Do something for lollipop and above versions
                    if ( model.ListItems.size() == 0)   getView().findViewById(R.id.EstateProvinceTextInput).setVisibility(View.VISIBLE);
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