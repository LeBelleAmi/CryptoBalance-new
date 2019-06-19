package com.lebelle.cryptobalance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Omawumi Eyekpimi on 12-Oct-17.
 */

public class BitcoinResponse {
    @SerializedName("BTC")
    @Expose
    private BtcExchange exchanges;

    public BitcoinResponse(BtcExchange exchanges) {
        this.exchanges = exchanges;
    }

    public BtcExchange getBtcRates() {
        return exchanges;
    }

    public void setBtcRates(BtcExchange exchanges) {
        this.exchanges = exchanges;
    }
}
