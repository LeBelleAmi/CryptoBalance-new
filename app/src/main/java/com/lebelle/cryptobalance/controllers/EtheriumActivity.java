package com.lebelle.cryptobalance.controllers;

import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.lebelle.cryptobalance.R;
import com.lebelle.cryptobalance.api.Client;
import com.lebelle.cryptobalance.api.Service;
import com.lebelle.cryptobalance.model.Currency;
import com.lebelle.cryptobalance.model.CurrencyAdapter;
import com.lebelle.cryptobalance.model.DividerItemDecorator;
import com.lebelle.cryptobalance.model.EthExchange;
import com.lebelle.cryptobalance.model.EtheriumResponse;
import com.lebelle.cryptobalance.model.RecyclerviewItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EtheriumActivity extends AppCompatActivity implements RecyclerviewItemTouchHelper.RecyclerItemTouchHelperListener {
    /* strings array for currency names, symbols, cryptocoins, unicodes and exchange rates*/
    public String[] currencyNames, currencySymbols, coinNames, currencyUnicodes, rates;
    /*byte arrays for the country flags and coin images*/
    public TypedArray countryFlags, coinImages;
    /*position of spinner selected item*/
    public int spinnerPosition;
    /*recycler view*/
    LinearLayoutManager llm;
    /*ethereum java class*/
    EthExchange ethExchanges;
    /*bitcoin array java class*/
    EtheriumResponse etheriumResponse;
    /*recycler view*/
    private RecyclerView recyclerView;
    //currency spinner
    public Spinner mCurrencySpinner;
    /*currency list*/
    private List<Currency> currencies;
    /*currency adapter*/
    private CurrencyAdapter mAdapter;
    //activity parent layout
    CoordinatorLayout coordinatorLayout;
    /*network state boolean*/
    private boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etherium);

        /*initialize arrays*/
        currencyNames = getResources().getStringArray(R.array.array_currency_options);
        currencySymbols = getResources().getStringArray(R.array.array_symbol_options);
        countryFlags = getResources().obtainTypedArray(R.array.array_flag_options);
        coinImages = getResources().obtainTypedArray(R.array.array_coin_img);
        coinNames = getResources().getStringArray(R.array.array_coins);
        currencyUnicodes = getResources().getStringArray(R.array.array_currency_unicodes);

        //setup toolbar and parent layout for activity
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //coordinator layout id
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.ethereum_layout);

        //setup currencies arraylist
        currencies = new ArrayList<>();

        //initialize recyclerview
        initViews();

        //setup adapter for activity main
        mAdapter = new CurrencyAdapter(getApplicationContext(), currencies);
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerviewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        //FAB leads to editor dialogue for currencies selection
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EtheriumActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.layout_add, null);
                mBuilder.setTitle("Please select a Currency");
                mBuilder.setIcon(R.drawable.alert_bell_notification);
                mCurrencySpinner = (Spinner) mView.findViewById(R.id.fullname_text);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<String> currencyArrayAdapter = new ArrayAdapter<>(EtheriumActivity.this,
                        android.R.layout.simple_spinner_item, currencyNames);
                // Specify the layout to use when the list of choices appears
                currencyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //apply the adapter to the spinner
                mCurrencySpinner.setAdapter(currencyArrayAdapter);
                mCurrencySpinner.setOnItemSelectedListener(new onSpinnerItemClicked());

                mBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        addCard();
                        dialog.dismiss();
                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
            }
        });
        //call network state and network call
        connected();
    }

    public void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.list_view);
        // set up recycler view
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecorator(this, LinearLayoutManager.VERTICAL));
    }

    private void connected(){
        connected = checkInternetConnection();
        if (!connected){
            // showing snack bar with network option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "You seem to be Offline, please check connection!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // retry is selected, refresh the app
                    loadCurrencyRates();
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }else {
            loadCurrencyRates();
        }
    }

    /*method to handle network calls*/
    private void loadCurrencyRates() {
        try {
            Service apiService =
                    Client.getClient().create(Service.class);
            Call<EtheriumResponse> call = apiService.getEthCurrencyRate();
            call.enqueue(new Callback<EtheriumResponse>() {
                @Override
                public void onResponse(Call<EtheriumResponse> call, Response<EtheriumResponse> response) {
                    if (response != null) {
                        etheriumResponse = response.body();
                        ethExchanges = etheriumResponse.getEthRates();
                        if (ethExchanges != null) {

                            //creating a string array for the currency rates
                            rates = new String[25];
                            rates[0] = ethExchanges.getAED().toString();
                            rates[1] = ethExchanges.getARS().toString();
                            rates[2] = ethExchanges.getAUD().toString();
                            rates[3] = ethExchanges.getBTC().toString();
                            rates[4] = ethExchanges.getBRL().toString();
                            rates[5] = ethExchanges.getGBP().toString();
                            rates[6] = ethExchanges.getXAF().toString();
                            rates[7] = ethExchanges.getCAD().toString();
                            rates[8] = ethExchanges.getDKK().toString();
                            rates[9] = ethExchanges.getETH().toString();
                            rates[10] = ethExchanges.getEUR().toString();
                            rates[11] = ethExchanges.getINR().toString();
                            rates[12] = ethExchanges.getILS().toString();
                            rates[13] = ethExchanges.getJPY().toString();
                            rates[14] = ethExchanges.getKES().toString();
                            rates[15] = ethExchanges.getKRW().toString();
                            rates[16] = ethExchanges.getNGN().toString();
                            rates[17] = ethExchanges.getPLN().toString();
                            rates[18] = ethExchanges.getZAR().toString();
                            rates[19] = ethExchanges.getCNY().toString();
                            rates[20] = ethExchanges.getRUB().toString();
                            rates[21] = ethExchanges.getSAR().toString();
                            rates[22] = ethExchanges.getCHF().toString();
                            rates[23] = ethExchanges.getTRY().toString();
                            rates[24] = ethExchanges.getUSD().toString();

                        }
                    }
                }

                @Override
                public void onFailure(Call<EtheriumResponse> call, Throwable t) {
                    Log.e("Error", t.getMessage());
                    // showing snack bar with response failure option
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Exchange rates loading failed, please refresh!", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("REFRESH", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // refresh is selected, refresh the app
                            loadCurrencyRates();
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();

                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    /*
    * check if the app has network connection*/
    public boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo connectedNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isActive = (connectedNetwork != null && connectedNetwork.isConnectedOrConnecting());
        return isActive;
    }

    /*method to handle user card creation*/
    public void addCard() {
        int coinIndex = 0;

        currencies.add(new Currency(currencyNames[spinnerPosition],
                currencySymbols[spinnerPosition], rates[spinnerPosition], coinNames[1], ethExchanges.getETH().toString(),
                currencyUnicodes[spinnerPosition], currencyUnicodes[9], countryFlags.getDrawable(spinnerPosition), coinImages.getDrawable(coinIndex + 1)));

        mAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

    }

    /*method to handle dialogue spinner selection*/
    public class onSpinnerItemClicked implements AdapterView.OnItemSelectedListener {
        // This method is supposed to call the on item selected listener on the spinner class
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //this method gets called automatically when the user selects an item so we need to
            // retrieve what the use has clicked
            spinnerPosition = position;
            parent.getItemAtPosition(position);
            countryFlags.getResourceId(parent.getSelectedItemPosition(), -1);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //Do nothing
        }
    }


    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CurrencyAdapter.CurrencyViewHolder) {
            // get the removed item name to display it in snack bar
            String fullname = currencies.get(viewHolder.getAdapterPosition()).getFullname();

            // backup of removed item for undo purpose
            final Currency deletedItem = currencies.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, fullname
                            + " removed from Currency list!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }


    //onCreate boolean for search icon on appbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        // Associate searchable configuration with the SearchView
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        return true;
    }

    //action bar icons functionalities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.refresh:
                loadCurrencyRates();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //recycler view search filter for currencies
    private void search(final SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

}
