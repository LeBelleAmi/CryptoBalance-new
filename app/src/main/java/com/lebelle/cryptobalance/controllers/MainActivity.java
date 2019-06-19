package com.lebelle.cryptobalance.controllers;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.Button;
import android.widget.Spinner;

import com.lebelle.cryptobalance.R;
import com.lebelle.cryptobalance.api.Client;
import com.lebelle.cryptobalance.api.Service;
import com.lebelle.cryptobalance.model.BitcoinResponse;
import com.lebelle.cryptobalance.model.BtcExchange;
import com.lebelle.cryptobalance.model.Currency;
import com.lebelle.cryptobalance.model.CurrencyAdapter;
import com.lebelle.cryptobalance.model.CustomSpinnerAdapter;
import com.lebelle.cryptobalance.model.DividerItemDecorator;
import com.lebelle.cryptobalance.model.RecyclerviewItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerviewItemTouchHelper.RecyclerItemTouchHelperListener {
    /* strings array for currency names, symbols, cryptocoins, unicodes and exchange rates*/
    public String[] currencyNames, currencySymbols, coinNames, currencyUnicodes, rates;
    /*byte arrays for the country flags and coin images*/
    public TypedArray countryFlags, coinImages;
    /*position of spinner selected item*/
    public int spinnerPosition;
    /*bitcoin java class*/
    BtcExchange exchanges;
    /*bitcoin array java class*/
    BitcoinResponse bitcoinResponse;
    /*recycler view layout manager*/
    LinearLayoutManager llm;
    /*recycler view*/
    private RecyclerView recyclerView;
    /*currency list*/
    private List<Currency> currencies;
    /*currency adapter*/
    private CurrencyAdapter mAdapter;
    /*network state boolean*/
    private boolean connected;
    /*progress dialog for network call*/
    ProgressDialog progressDialog;

    //navigation drawer
    private DrawerLayout mDrawerLayout;

    //drawer toogle
    private ActionBarDrawerToggle drawerToggle;

    // Toolbar
    private Toolbar toolbar;
    // Navigation adapter
    private CustomSpinnerAdapter adapter;
    //toolbar spinner
    private Spinner spinner_nav;
    //currency spinner
    public Spinner mCurrencySpinner;
    //activity parent layout
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*initialize arrays*/
        currencyNames = getResources().getStringArray(R.array.array_currency_options);
        currencySymbols = getResources().getStringArray(R.array.array_symbol_options);
        countryFlags = getResources().obtainTypedArray(R.array.array_flag_options);
        coinImages = getResources().obtainTypedArray(R.array.array_coin_img);
        coinNames = getResources().getStringArray(R.array.array_coins);
        currencyUnicodes = getResources().getStringArray(R.array.array_currency_unicodes);

        //setup toolbar and parent layout for activity
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_activity_coordinator_layout);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.white_menu_stack);
        ab.setDisplayHomeAsUpEnabled(true);

        //navigation layout setup
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setUpDrawerToggle();
        mDrawerLayout.addDrawerListener(drawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        addItemsToSpinner();

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


        //FAB leads to editor dialogue for currencies pick
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.layout_add, null);
                mBuilder.setTitle("Please select a Currency");
                mBuilder.setIcon(R.drawable.alert_bell_notification);
                mCurrencySpinner = (Spinner) mView.findViewById(R.id.fullname_text);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<String> currencyArrayAdapter = new ArrayAdapter<>(MainActivity.this,
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
        // set up recycler view
        recyclerView = (RecyclerView) findViewById(R.id.list_view);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecorator(this, LinearLayoutManager.VERTICAL));
    }


    /*method to handle network calls*/
    private void loadCurrencyRates() {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Checking for Crypto Exchange Rates...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Service apiService =
                    Client.getClient().create(Service.class);
            Call<BitcoinResponse> call = apiService.getBtcCurrencyRate();
            call.enqueue(new Callback<BitcoinResponse>() {
                @Override
                public void onResponse(Call<BitcoinResponse> call, Response<BitcoinResponse> response) {
                    if (response != null) {
                        bitcoinResponse = response.body();
                        exchanges = bitcoinResponse.getBtcRates();
                        progressDialog.hide();
                        if (exchanges != null) {

                            //creating a string array for the currency rates
                            rates = new String[25];
                            rates[0] = exchanges.getAED().toString();
                            rates[1] = exchanges.getARS().toString();
                            rates[2] = exchanges.getAUD().toString();
                            rates[3] = exchanges.getBTC().toString();
                            rates[4] = exchanges.getBRL().toString();
                            rates[5] = exchanges.getGBP().toString();
                            rates[6] = exchanges.getXAF().toString();
                            rates[7] = exchanges.getCAD().toString();
                            rates[8] = exchanges.getDKK().toString();
                            rates[9] = exchanges.getETH().toString();
                            rates[10] = exchanges.getEUR().toString();
                            rates[11] = exchanges.getINR().toString();
                            rates[12] = exchanges.getILS().toString();
                            rates[13] = exchanges.getJPY().toString();
                            rates[14] = exchanges.getKES().toString();
                            rates[15] = exchanges.getKRW().toString();
                            rates[16] = exchanges.getNGN().toString();
                            rates[17] = exchanges.getPLN().toString();
                            rates[18] = exchanges.getZAR().toString();
                            rates[19] = exchanges.getCNY().toString();
                            rates[20] = exchanges.getRUB().toString();
                            rates[21] = exchanges.getSAR().toString();
                            rates[22] = exchanges.getCHF().toString();
                            rates[23] = exchanges.getTRY().toString();
                            rates[24] = exchanges.getUSD().toString();

                        }

                    }
                }

                @Override
                public void onFailure(Call<BitcoinResponse> call, Throwable t) {
                    Log.e("Error", t.getMessage());
                    progressDialog.hide();
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

    /*
    * check if the app has network connection*/
    public boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo connectedNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isActive = (connectedNetwork != null && connectedNetwork.isConnectedOrConnecting());
        return isActive;
    }

    /*method to handle dialogue spinner selection*/
    public class onSpinnerItemClicked implements AdapterView.OnItemSelectedListener {
        // This method is supposed to call the on item selected listener on the spinner class
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //this method gets called automatically when the user selects an item so we need to
            // retrieve what the user has clicked
                spinnerPosition = position;
                parent.getItemAtPosition(position);
            countryFlags.getResourceId(parent.getSelectedItemPosition(), -1);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //Do nothing
        }
    }

    /*method to handle user card creation*/
    public void addCard() {
        int coinIndex = 0;
        currencies.add(new Currency(currencyNames[spinnerPosition],
                currencySymbols[spinnerPosition], rates[spinnerPosition], coinNames[0], exchanges.getBTC().toString(),
                currencyUnicodes[spinnerPosition], currencyUnicodes[3], countryFlags.getDrawable(spinnerPosition),
                coinImages.getDrawable(coinIndex)));
        mAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    // method handles toolbar spinner and add items into spinner dynamically
    public void addItemsToSpinner() {

        ArrayList<String> list = new ArrayList<>();
        list.add("Bitcoin");
        list.add("Ethereum");

        // Custom ArrayAdapter with spinner item layout to set popup background
        CustomSpinnerAdapter spinAdapter = new CustomSpinnerAdapter(
                getApplicationContext(), list);

        spinner_nav.setAdapter(spinAdapter);

        spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {

                switch (position) {
                    case (0):
                        break;
                    case (1):
                        Intent intent = new Intent(MainActivity.this, EtheriumActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // Do nothing
            }
        });

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

    //navigation drawer toogle state
    private ActionBarDrawerToggle setUpDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.app_name);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * On selecting action bar icons
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.refresh:
                loadCurrencyRates();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //setting up drawer content items and click functionalities
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        //return true;
                        switch (menuItem.getItemId()) {
                            case R.id.cryptocompare_intent:
                                Intent cryptoIntent = new Intent(Intent.ACTION_VIEW);
                                String url = "https://www.cryptocompare.com";
                                cryptoIntent.setData(Uri.parse(url));
                                startActivity(Intent.createChooser(cryptoIntent, "Visit Website via"));
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.andela_intent:
                                Intent andelaIntent = new Intent(Intent.ACTION_VIEW);
                                String url1 = "https://andela.com/alcwithgoogle/";
                                andelaIntent.setData(Uri.parse(url1));
                                startActivity(Intent.createChooser(andelaIntent, "Visit Website via"));
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.feedback:
                                Intent feedbackEmail = new Intent(Intent.ACTION_SENDTO);
                                feedbackEmail.setData(Uri.parse("mailto:omawumieyekpimi@gmail.com")); // only email apps should handle this
                                feedbackEmail.putExtra(Intent.EXTRA_EMAIL, "");
                                feedbackEmail.putExtra(Intent.EXTRA_SUBJECT, "Crypto Balance Feedback");
                                if (feedbackEmail.resolveActivity(getPackageManager()) != null) {
                                    startActivity(Intent.createChooser(feedbackEmail, "Send Crypto Balance Feedback via"));
                                }
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.about:
                                final Dialog d = new Dialog(MainActivity.this);
                                d.setContentView(R.layout.about_crypto_balance);
                                d.setTitle("About");
                                Button connect = (Button) d.findViewById(R.id.dialogButtonOK);
                                connect.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        d.dismiss();
                                    }
                                });
                                d.show();
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.help:
                                final Dialog dialogHelp = new Dialog(MainActivity.this);
                                dialogHelp.setContentView(R.layout.help_crypto_balance);
                                dialogHelp.setTitle("Help");
                                Button helpButton = (Button) dialogHelp.findViewById(R.id.dialogButtonOK);
                                helpButton.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        dialogHelp.dismiss();
                                    }
                                });
                                dialogHelp.show();
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.license:
                                final Dialog dialogLicense = new Dialog(MainActivity.this);
                                dialogLicense.setContentView(R.layout.license_crypto_balance);
                                dialogLicense.setTitle("License");
                                Button helpLicense = (Button) dialogLicense.findViewById(R.id.dialogButtonOK);
                                helpLicense.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        dialogLicense.dismiss();
                                    }
                                });
                                dialogLicense.show();
                                mDrawerLayout.closeDrawers();
                                break;
                            case android.R.id.home:
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                break;
                        }
                        return true;
                    }
                });
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

    //recycler view search filter for users
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