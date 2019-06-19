package com.lebelle.cryptobalance.api;

import com.lebelle.cryptobalance.model.BitcoinResponse;
import com.lebelle.cryptobalance.model.EtheriumResponse;
import com.lebelle.cryptobalance.query.Url;

import retrofit2.Call;
import retrofit2.http.GET;

import static com.lebelle.cryptobalance.query.Url.BTC;
import static com.lebelle.cryptobalance.query.Url.END;
import static com.lebelle.cryptobalance.query.Url.ETH;

/**
 * Created by Omawumi Eyekpimi on 12-Oct-17.
 */

public interface Service {
    @GET(Url.SEARCH_URL + BTC + END)
    Call<BitcoinResponse> getBtcCurrencyRate();

    @GET(Url.SEARCH_URL + ETH + END)
    Call<EtheriumResponse> getEthCurrencyRate();
}