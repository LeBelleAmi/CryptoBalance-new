package com.lebelle.cryptobalance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Omawumi Eyekpimi on 12-Oct-17.
 */

public class EtheriumResponse {
    @SerializedName("ETH")
    @Expose
    private EthExchange ethExchanges;

    public EtheriumResponse(EthExchange ethExchanges) {
        this.ethExchanges = ethExchanges;
    }

    public EthExchange getEthRates() {
        return ethExchanges;
    }

    public void setEthRates(EthExchange ethExchanges) {
        this.ethExchanges = ethExchanges;
    }

    @Override
    public String toString() {
        return "EtheriumResponse{" +
                "ethExchanges=" + ethExchanges +
                '}';
    }
}
