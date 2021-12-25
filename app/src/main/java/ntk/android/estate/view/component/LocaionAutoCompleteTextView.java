package ntk.android.estate.view.component;

import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.adapter.SpinnerAdapter;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.core.CoreLocationModel;
import ntk.android.base.services.core.CoreLocationService;
import ntk.android.estate.MyApplication;


public class LocaionAutoCompleteTextView {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void addOnAutoCompleteTextViewTextChangedObserver(final AutoCompleteTextView autoCompleteTextView) {
        Observable<ErrorException<CoreLocationModel>> autocompleteResponseObservable =
                RxTextView.textChangeEvents(autoCompleteTextView)
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .map(textViewTextChangeEvent -> textViewTextChangeEvent.text().toString())
                        .filter(s -> s.length() >= 3)
                        .observeOn(Schedulers.io())
                        .switchMap(s -> new CoreLocationService(MyApplication.getAppContext()).getAllTree(s)
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry();

        compositeDisposable.add(
                autocompleteResponseObservable.subscribe(
                        placeAutocompleteResult -> {
                            List<String> names = new ArrayList<>();
                            for (CoreLocationModel t : placeAutocompleteResult.ListItems)
                                names.add(t.Title);
                            SpinnerAdapter<CoreLocationModel> locationAdapter = new SpinnerAdapter<CoreLocationModel>(autoCompleteTextView.getContext(), names);
                            autoCompleteTextView.setAdapter(locationAdapter);
                            String enteredText = autoCompleteTextView.getText().toString();
                            if (names.size() >= 1 && enteredText.equals(names.get(0))) {
                                autoCompleteTextView.dismissDropDown();
                            } else {
                                autoCompleteTextView.showDropDown();
                            }
                        },
                        e -> Log.e("LOCATION", "onError", e),
                        () -> {}));
    }
//    void addOnAutoCompleteTextViewItemClickedSubscriber(final AutoCompleteTextView autoCompleteTextView) {
//        Observable<ErrorException<CoreLocationModel>> adapterViewItemClickEventObservable =
//                RxAutoCompleteTextView.itemClickEvents(autoCompleteTextView)
//                        .map(adapterViewItemClickEvent -> {
//                            CoreLocationModel  item = (  CoreLocationModel ) autoCompleteTextView.getAdapter()
//                                    .getItem(adapterViewItemClickEvent.position());
//                            return item;
//                        })
//                        .observeOn(Schedulers.io())
//                        .switchMap(placeId -> RestClient.INSTANCE.getGooglePlacesClient().details(placeId))
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .retry();
//
//        compositeDisposable.add(
//                adapterViewItemClickEventObservable.subscribe(
//                        placeDetailsResult -> {
//                            Log.i("PlaceAutocomplete", placeDetailsResult.toString());
//                            updateMap(placeDetailsResult);
//                        },
//                        throwable -> Log.e(TAG, "onError", throwable),
//                        () -> Log.i(TAG, "onCompleted")));
//    }

}
