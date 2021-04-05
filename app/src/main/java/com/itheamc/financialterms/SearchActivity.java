package com.itheamc.financialterms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.itheamc.financialterms.adapters.TermsAdapter;
import com.itheamc.financialterms.interfaces.TermsInterface;
import com.itheamc.financialterms.models.Terms;
import com.itheamc.financialterms.viewmodel.DataModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements TermsInterface, Filterable {
    private static final String TAG = "SearchActivity";
    private TermsAdapter termsAdapter;
    private AdView mAdView;
    private DataModel dataModel;

    private boolean isSearched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dataModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(DataModel.class);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(TAG, "onInitializationComplete: " + initializationStatus.toString());
            }
        });

        mAdView = findViewById(R.id.bannerAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        initRecyclerView();
    }


    // Function to init recycler view
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        termsAdapter = new TermsAdapter(this);
        recyclerView.setAdapter(termsAdapter);

        if (dataModel.getTermsList() == null) {
            loadTerms();
        } else {
            termsAdapter.submitList(dataModel.getTermsList());
        }
    }


    // Function to load data from api
    private void loadTerms() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request getRequest = new Request.Builder().url("https://financial-terms-3e5ff.web.app/terms.json").build();

        okHttpClient.newCall(getRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                SearchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this, "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "loadTerms: ", e.getCause());

                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("terms");
                    Log.d(TAG, "onResponse: " + jsonArray.toString());
                    createList(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // Function to create postList from the jsonArray
    private void createList(JSONArray jsonArray) {
        SearchActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Terms> termsList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);


                        Terms terms = new Terms(jsonObject.getInt("_id"), jsonObject.getString("_title"), jsonObject.getString("_body"));
                        termsList.add(terms);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dataModel.setTermsList(termsList);
                termsAdapter.submitList(termsList);
            }
        });

    }


    // Overriding onCreateOptionMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the filter
                isSearched = true;
                getFilter().filter(query);
                hideKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    isSearched = false;
                    termsAdapter.submitList(dataModel.getTermsList());
                    findViewById(R.id.no_result_text).setVisibility(View.GONE);
                    return false;
                }
                isSearched = true;
                getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void handleClick(View[] views, int position) {
        Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
        intent.putExtra("title", isSearched ? dataModel.getFilteredList().get(position).get_title() : dataModel.getTermsList().get(position).get_title());
        intent.putExtra("desc", isSearched ? dataModel.getFilteredList().get(position).get_body() : dataModel.getTermsList().get(position).get_body());
//        These are for shared transition
        Pair<View, String> title_pair = Pair.create(views[0], "title");
        Pair<View, String> desc_pair = Pair.create(views[1], "description");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SearchActivity.this, title_pair, desc_pair);
        startActivity(intent, options.toBundle());
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        // This will runOnBackGroundThreads
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Terms> filteredLists = new ArrayList<>();

            // Searching the query in the title
            for (Terms term : dataModel.getTermsList()) {
                if (term.get_title().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    filteredLists.add(term);
                }
            }

            // If filtered list is empty after matching the title
            if (filteredLists.isEmpty()) {
                for (Terms term : dataModel.getTermsList()) {
                    if (term.get_body().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredLists.add(term);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredLists;
            return filterResults;
        }

        // This will runOnUiThreads
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results == null || results.values.equals(null) || results.values.equals(new ArrayList<>())) {
                findViewById(R.id.no_result_text).setVisibility(View.VISIBLE);
                termsAdapter.submitList((List<Terms>) results.values);
            } else {
                findViewById(R.id.no_result_text).setVisibility(View.GONE);
                List<Terms> tempLists = (List<Terms>) results.values;
                dataModel.setFilteredList(tempLists);
                termsAdapter.submitList(tempLists);
            }

        }
    };


    // Function to hide the keyboard
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}