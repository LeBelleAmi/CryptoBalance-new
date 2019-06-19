package com.lebelle.cryptobalance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Omawumi Eyekpimi on 22-Oct-17.
 */

public class EthExchange {
    @SerializedName("AED")
    @Expose
    private Double AED;

    @SerializedName("BRL")
    @Expose
    private Double BRL;

    @SerializedName("ARS")
    @Expose
    private Double ARS;

    @SerializedName("XAF")
    @Expose
    private Double XAF;

    @SerializedName("TRY")
    @Expose
    private Double TRY;

    @SerializedName("KES")
    @Expose
    private Double KES;

    @SerializedName("SAR")
    @Expose
    private Double SAR;

    @SerializedName("RUB")
    @Expose
    private Double RUB;

    @SerializedName("ZAR")
    @Expose
    private Double ZAR;

    @SerializedName("CHF")
    @Expose
    private Double CHF;

    @SerializedName("KRW")
    @Expose
    private Double KRW;

    @SerializedName("INR")
    @Expose
    private Double INR;

    @SerializedName("DKK")
    @Expose
    private Double DKK;

    @SerializedName("CNY")
    @Expose
    private Double CNY;

    @SerializedName("JPY")
    @Expose
    private Double JPY;

    @SerializedName("GBP")
    @Expose
    private Double GBP;

    @SerializedName("CAD")
    @Expose
    private Double CAD;

    @SerializedName("AUD")
    @Expose
    private Double AUD;

    @SerializedName("NGN")
    @Expose
    private Double NGN;

    @SerializedName("EUR")
    @Expose
    private Double EUR;

    @SerializedName("USD")
    @Expose
    private Double USD;

    @SerializedName("ILS")
    @Expose
    private Double ILS;

    @SerializedName("PLN")
    @Expose
    private Double PLN;

    @SerializedName("ETH")
    @Expose
    private Double ETH;

    @SerializedName("BTC")
    @Expose
    private Double BTC;

    public EthExchange(Double AED, Double BRL, Double ARS, Double XAF, Double TRY, Double KES, Double SAR, Double RUB, Double ZAR, Double CHF, Double KRW, Double INR, Double DKK, Double CNY, Double JPY, Double GBP, Double CAD, Double AUD, Double NGN, Double EUR, Double USD, Double ILS, Double PLN, Double ETH, Double BTC) {
        this.AED = AED;
        this.BRL = BRL;
        this.ARS = ARS;
        this.XAF = XAF;
        this.TRY = TRY;
        this.KES = KES;
        this.SAR = SAR;
        this.RUB = RUB;
        this.ZAR = ZAR;
        this.CHF = CHF;
        this.KRW = KRW;
        this.INR = INR;
        this.DKK = DKK;
        this.CNY = CNY;
        this.JPY = JPY;
        this.GBP = GBP;
        this.CAD = CAD;
        this.AUD = AUD;
        this.NGN = NGN;
        this.EUR = EUR;
        this.USD = USD;
        this.ILS = ILS;
        this.PLN = PLN;
        this.ETH = ETH;
        this.BTC = BTC;
    }

    public Double getAED() {
        return AED;
    }

    public void setAED(Double AED) {
        this.AED = AED;
    }

    public Double getBRL() {
        return BRL;
    }

    public void setBRL(Double BRL) {
        this.BRL = BRL;
    }

    public Double getARS() {
        return ARS;
    }

    public void setARS(Double ARS) {
        this.ARS = ARS;
    }

    public Double getXAF() {
        return XAF;
    }

    public void setXAF(Double XAF) {
        this.XAF = XAF;
    }

    public Double getTRY() {
        return TRY;
    }

    public void setTRY(Double TRY) {
        this.TRY = TRY;
    }

    public Double getKES() {
        return KES;
    }

    public void setKES(Double KES) {
        this.KES = KES;
    }

    public Double getSAR() {
        return SAR;
    }

    public void setSAR(Double SAR) {
        this.SAR = SAR;
    }

    public Double getRUB() {
        return RUB;
    }

    public void setRUB(Double RUB) {
        this.RUB = RUB;
    }

    public Double getZAR() {
        return ZAR;
    }

    public void setZAR(Double ZAR) {
        this.ZAR = ZAR;
    }

    public Double getCHF() {
        return CHF;
    }

    public void setCHF(Double CHF) {
        this.CHF = CHF;
    }

    public Double getKRW() {
        return KRW;
    }

    public void setKRW(Double KRW) {
        this.KRW = KRW;
    }

    public Double getINR() {
        return INR;
    }

    public void setINR(Double INR) {
        this.INR = INR;
    }

    public Double getDKK() {
        return DKK;
    }

    public void setDKK(Double DKK) {
        this.DKK = DKK;
    }

    public Double getCNY() {
        return CNY;
    }

    public void setCNY(Double CNY) {
        this.CNY = CNY;
    }

    public Double getJPY() {
        return JPY;
    }

    public void setJPY(Double JPY) {
        this.JPY = JPY;
    }

    public Double getGBP() {
        return GBP;
    }

    public void setGBP(Double GBP) {
        this.GBP = GBP;
    }

    public Double getCAD() {
        return CAD;
    }

    public void setCAD(Double CAD) {
        this.CAD = CAD;
    }

    public Double getAUD() {
        return AUD;
    }

    public void setAUD(Double AUD) {
        this.AUD = AUD;
    }

    public Double getNGN() {
        return NGN;
    }

    public void setNGN(Double NGN) {
        this.NGN = NGN;
    }

    public Double getEUR() {
        return EUR;
    }

    public void setEUR(Double EUR) {
        this.EUR = EUR;
    }

    public Double getUSD() {
        return USD;
    }

    public void setUSD(Double USD) {
        this.USD = USD;
    }

    public Double getILS() {
        return ILS;
    }

    public void setILS(Double ILS) {
        this.ILS = ILS;
    }

    public Double getPLN() {
        return PLN;
    }

    public void setPLN(Double PLN) {
        this.PLN = PLN;
    }

    public Double getETH() {
        return ETH;
    }

    public void setETH(Double ETH) {
        this.ETH = ETH;
    }

    public Double getBTC() {
        return BTC;
    }

    public void setBTC(Double BTC) {
        this.BTC = BTC;
    }
}
