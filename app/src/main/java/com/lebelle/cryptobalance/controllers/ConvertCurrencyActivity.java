package com.lebelle.cryptobalance.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lebelle.cryptobalance.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConvertCurrencyActivity extends AppCompatActivity {
    //textviews, button, edittext and circular imageviews initialization
    AppCompatTextView calenderView,coinName, coinRate, currencyName, currencyRate, currencyFullname,
            calculatedText, coin_Unicode, currencyUnicode;
    AppCompatButton convert;
    AppCompatEditText valueEntered;
    CircularImageView coinFlag, currencyFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        //setup toolbar
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Crypto Converter");

        //get items from the adapter intent and set in views
        Bundle data = getIntent().getBundleExtra("items");
        String fullname = data.getString("fullname");
        final String symbol = data.getString("symbol");
        final String rate = data.getString("rate");
        String coin = data.getString("coin");
        String number = data.getString("one");
        String coinUnicode = data.getString("coinUnicode");
        final String unicode = data.getString("currencyUnicode");
        byte[] byteArray = getIntent().getByteArrayExtra("byteArray");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        byte[] btcArray = getIntent().getByteArrayExtra("btcArray");
        Bitmap coinView = BitmapFactory.decodeByteArray(btcArray, 0, btcArray.length);

        //declare views by id
        calenderView = (AppCompatTextView) findViewById(R.id.calender);
        coinName = (AppCompatTextView) findViewById(R.id.coin_name);
        coin_Unicode = (AppCompatTextView) findViewById(R.id.coin_unicode);
        currencyUnicode = (AppCompatTextView) findViewById(R.id.currency_unicode);
        coinRate = (AppCompatTextView) findViewById(R.id.coin_rate);
        currencyName = (AppCompatTextView) findViewById(R.id.currency_name);
        currencyRate = (AppCompatTextView) findViewById(R.id.currency_rate);
        calculatedText = (AppCompatTextView) findViewById(R.id.calculated_value_text);
        currencyFullname = (AppCompatTextView) findViewById(R.id.currency_full_name);
        convert = (AppCompatButton) findViewById(R.id.convert_button);
        valueEntered = (AppCompatEditText) findViewById(R.id.value_text_view);
        coinFlag = (CircularImageView) findViewById(R.id.bitcoin_image);
        currencyFlag = (CircularImageView) findViewById(R.id.flag_image);


        //set up intent data gotten from adapter
        currencyName.setText(symbol);
        currencyFullname.setText(fullname);
        currencyFlag.setImageBitmap(bitmap);
        coinName.setText(coin);
        coinRate.setText(number);
        coin_Unicode.setText(coinUnicode);
        currencyUnicode.setText(unicode);
        coinFlag.setImageBitmap(coinView);
        currencyRate.setText(rate);

        //set up system calender and time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm:ss a");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        calenderView.setText(date);

        //set up currency converter
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set up number and strings exception so that app doesn't crash if wrong inputs are entered
                if (valueEntered.getText().toString().length() == 0) {
                    return;
                }

                double inputNumber;
                try {
                    inputNumber = Double.parseDouble(valueEntered.getText().toString());
                } catch (NumberFormatException e) {
                    valueEntered.setText("");
                    return;
                }

                double calcRate = inputNumber / Double.valueOf(rate);
                DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                String result = unicode + String.valueOf(decimalFormat.format(calcRate));
                 calculatedText.setText(result);
            }
        });
    }

    //setup cryptobalance share button
    private Intent shareCryptoIntent(){
        Bundle data = getIntent().getBundleExtra("items");
        String fullname = data.getString("fullname");
        String coin = data.getString("coin");
        String rate = data.getString("rate");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm:ss a");
        String date = dateFormat.format(Calendar.getInstance().getTime());;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String stringToShare = "Hello! the current exchange rate of " + fullname + " to " + coin + " is " + rate + " @ " + date + ".";
        sharingIntent.putExtra(Intent.EXTRA_TEXT, stringToShare);
        startActivity(Intent.createChooser(sharingIntent, "Share Crypto Information via"));
        return sharingIntent;
    }

    //setup activity menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.converter_menu, menu);
        return true;
    }

    //setup toolbar icons and actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.share:
                shareCryptoIntent();
                return true;
            }
        return super.onOptionsItemSelected(item);
    }

}