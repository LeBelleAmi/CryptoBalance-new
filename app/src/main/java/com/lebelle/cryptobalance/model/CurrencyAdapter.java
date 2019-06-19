package com.lebelle.cryptobalance.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lebelle.cryptobalance.R;
import com.lebelle.cryptobalance.controllers.ConvertCurrencyActivity;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Omawumi Eyekpimi on 05-Oct-17.
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> implements Filterable{

    private Context context;
    private List<Currency> currencies;
    private List<Currency> mCurrencyFiltered;
    private List<Currency> searchArrayList;
    ValueFilter valueFilter;


    /**
     * Contructor to initialize context and list items.
     *
     * @param applicationContext  Context of the Activity on which RecyclerView is initialised
     * @param currenciesArrayList List of POJO object that contains the data to update the rows
     */

    public CurrencyAdapter(Context applicationContext, List<Currency> currenciesArrayList) {
        this.context = applicationContext;
        this.currencies = currenciesArrayList;
        this.mCurrencyFiltered = currenciesArrayList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        CurrencyViewHolder currencyViewHolder = new CurrencyViewHolder(view);
        return currencyViewHolder;
    }

    @Override
    public void onBindViewHolder(final CurrencyViewHolder currencyViewHolder, int i) {
        currencyViewHolder.fullname.setText(currencies.get(i).getFullname());
        currencyViewHolder.symbol.setText(currencies.get(i).getSymbol());
        currencyViewHolder.rate.setText(currencies.get(i).getRate());
        currencyViewHolder.coin.setText(currencies.get(i).getCoin());
        currencyViewHolder.one.setText(currencies.get(i).getNumber());
        currencyViewHolder.unicode.setText(currencies.get(i).getUnicode());
        currencyViewHolder.flag.setImageDrawable(currencies.get(i).getAvatar());
        currencyViewHolder.coinImg.setImageDrawable(currencies.get(i).getCoinImage());


        currencyViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = currencyViewHolder.getAdapterPosition();
                Currency items = currencies.get(pos);
                Bundle args = new Bundle();
                args.putString("fullname", currencies.get(pos).getFullname());
                args.putString("symbol", currencies.get(pos).getSymbol());
                args.putString("rate", currencies.get(pos).getRate());
                args.putString("coin", currencies.get(pos).getCoin());
                args.putString("one", currencies.get(pos).getNumber());
                args.putString("coinUnicode", currencies.get(pos).getCoinUnicode());
                args.putString("currencyUnicode", currencies.get(pos).getUnicode());

                Drawable image = currencies.get(pos).getAvatar();
                Bitmap bs = ((BitmapDrawable) image).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bs.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                Drawable img = currencies.get(pos).getCoinImage();
                Bitmap btc = ((BitmapDrawable) img).getBitmap();
                ByteArrayOutputStream btcStream = new ByteArrayOutputStream();
                btc.compress(Bitmap.CompressFormat.PNG, 50, btcStream);
                byte[] btcArray = btcStream.toByteArray();

                Intent intent = new Intent(context, ConvertCurrencyActivity.class);
                intent.putExtra("items", args);
                intent.putExtra("byteArray", byteArray);
                intent.putExtra("btcArray", btcArray);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                //Toast.makeText(context, "you've clicked on " +  items.getAvatar(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public int getItemCount() {

        if (currencies == null) {
            return 0;
        } else {
            return currencies.size();
        }
    }

    // Remove a RecyclerView item containing a specified Data object
    public void removeItem(int position) {
        currencies.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        notifyItemRemoved(position);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void restoreItem(Currency data, int position) {
        currencies.add(position, data);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class CurrencyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout lv;
        CircularImageView flag;
        CircularImageView coinImg;
        TextView fullname;
        TextView symbol;
        TextView rate;
        TextView coin;
        TextView one;
        TextView unicode;
        TextView coinUnicode;
        CardView viewForeground;
        View background;


        //initialize views of the viewholder
        public CurrencyViewHolder(View itemView) {
            super(itemView);
            lv = (RelativeLayout) itemView.findViewById(R.id.card_linear_holder);
            flag = (CircularImageView) itemView.findViewById(R.id.avatar_holder);
            fullname = (TextView) itemView.findViewById(R.id.full_name);
            symbol = (TextView) itemView.findViewById(R.id.symbol_1);
            rate = (TextView) itemView.findViewById(R.id.rate_1);
            coin = (TextView) itemView.findViewById(R.id.coin_name);
            one = (TextView) itemView.findViewById(R.id.coin_one);
            unicode = (TextView) itemView.findViewById(R.id.unicode);
            coinUnicode = (TextView) itemView.findViewById(R.id.coin_unicode_text);
            coinImg = (CircularImageView) itemView.findViewById(R.id.coin_holder);
            background = itemView.findViewById(R.id.view_background);
            viewForeground = (CardView) itemView.findViewById(R.id.view_foreground);
        }
    }

    public void addAll(List<Currency> newCurrency) {
        currencies = newCurrency;
        searchArrayList = currencies;
        notifyDataSetChanged();
    }


    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String charString = charSequence.toString();
            if (charString.isEmpty()) {
                addAll(mCurrencyFiltered);
                mCurrencyFiltered = searchArrayList;
            } else {
                List<Currency> filteredList = new ArrayList<>();
                for (Currency currency : searchArrayList) {
                    if (currency.getFullname().contains(charString) || currency.getSymbol().contains(charString)
                            || currency.getCoin().contains(charString) ||
                            currency.getCoinUnicode().contains(charString) || currency.getNumber().contains(charString)
                            || currency.getRate().contains(charString) || currency.getUnicode().contains(charString)) {
                        filteredList.add(currency);

                    }
                }
                mCurrencyFiltered = filteredList;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = mCurrencyFiltered;
            filterResults.count = mCurrencyFiltered.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mCurrencyFiltered = (List<Currency>) filterResults.values;
            currencies = mCurrencyFiltered;
            notifyDataSetChanged();
        }
    }
}





