package com.lebelle.cryptobalance.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Omawumi Eyekpimi on 05-Oct-17.
 */

public class Currency {
    /**
     * parameter for fullname
     */
    private String mFullname;

    /**
     * parameter for symbol
     */
    private String mSymbol;

    /**
     * parameter for rate
     */
    private String mRate;

    /**
     * parameter for coin
     */
    private String mCoin;

    /**
     * parameter for coin number
     */
    private String mNumber;

    /**
     * parameter for currency unicode
     */
    private String mUnicode;

    /**
     * parameter for flag image
     */
    private Drawable mAvatar;

    /**
     * parameter for coin unicode
     */
    private String mCoinUnicode;

    /**
     * parameter for coin image
     */
    private Drawable mCoinImage;

    /**
     * create a new currency object.
     *
     * @param fullname  is the fullname of the currency
     * @param symbol    is the symbol of the currency
     * @param rate      is the conversion rate of the currency
     * @param avatar    is the country flag of the currency
     * @param number    is the initial rate of the currency
     * @param coinImage is the image of the coin
     * @param unicode   is the unicode of the currency
     *                  @param coinUnicode   is the unicode of the coin
     * @@param coin is the coin in the exchange
     */


    public Currency(String fullname, String symbol, String rate, String coin, String number, String unicode, String coinUnicode, Drawable avatar, Drawable coinImage) {
        this.mFullname = fullname;
        this.mSymbol = symbol;
        this.mRate = rate;
        this.mCoin = coin;
        this.mNumber = number;
        this.mUnicode = unicode;
        this.mCoinUnicode = coinUnicode;
        this.mAvatar = avatar;
        this.mCoinImage = coinImage;
    }

    //getters and setters

    /**
     * get the fullname of the currency
     */
    public String getFullname() {
        return mFullname;
    }

    /**
     * Sets the main string to be displayed in the row
     *
     * @param fullname String which is displayed as main message
     */
    public void setFullname(String fullname) {
        this.mFullname = fullname;
    }

    /**
     * get the symbol of the currency
     */
    public String getSymbol() {
        return mSymbol;
    }

    /**
     * Sets the main string to be displayed in the row
     *
     * @param symbol String which is displayed as main message
     */
    public void setSymbol(String symbol) {
        this.mSymbol = symbol;
    }

    /**
     * get the symbol of the currency
     */
    public String getRate() {
        return mRate;
    }

    /**
     * Sets the main string to be displayed in the row
     *
     * @param rate string which is displayed as main message
     */
    public void setRate(String rate) {
        this.mRate = rate;
    }

    public String getCoin() {
        return mCoin;
    }


    /**
     * Sets the main string to be displayed in the row
     *
     * @param coin string which is displayed as main message
     */
    public void setCoin(String coin) {
        this.mCoin = coin;
    }

    public String getNumber() {
        return mNumber;
    }

    /**
     * Sets the main string to be displayed in the row
     *
     * @param number string which is displayed as main message
     */
    public void setNumber(String number) {
        this.mNumber = number;
    }

    public Drawable getAvatar() {
        return mAvatar;
    }

    /**
     * Sets the main image to be displayed in the row
     *
     * @param avatar image which is displayed as main message
     */
    public void setAvatar(Drawable avatar) {
        this.mAvatar = avatar;
    }

    public Drawable getCoinImage() {
        return mCoinImage;
    }

    /**
     * Sets the main string to be displayed in the row
     *
     * @param coinImage image which is displayed as main message
     */
    public void setCoinImage(Drawable coinImage) {
        this.mCoinImage = coinImage;
    }

    /**
     * Gets and Sets the main string to be displayed in the row
     *
     * unicode of the currency which is displayed as main message
     */
    public String getUnicode() {
        return mUnicode;
    }

    public void setUnicode(String mUnicode) {
        this.mUnicode = mUnicode;
    }

    public String getCoinUnicode() {
        return mCoinUnicode;
    }

    public void setCoinUnicode(String mCoinUnicode) {
        this.mCoinUnicode = mCoinUnicode;
    }
}
