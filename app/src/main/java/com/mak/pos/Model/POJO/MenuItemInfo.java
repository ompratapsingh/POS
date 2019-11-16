
package com.mak.pos.Model.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MenuItemInfo implements Serializable {

    @SerializedName("item_master_code")
    @Expose
    private String itemMasterCode;
    @SerializedName("item_master_name")
    @Expose
    private String itemMasterName;
    @SerializedName("article")
    @Expose
    private String article;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("rate")
    @Expose
    private float rate;
    @SerializedName("discPercent")
    @Expose
    private  float discPercent;
    @SerializedName("taxCode")
    @Expose
    private String taxCode;

    @SerializedName("appGroup")
    @Expose
    private String appGroup;
    @SerializedName("kotPrinter")
    @Expose
    private String kotPrinter;
    @SerializedName("tax_name")
    @Expose
    private String taxName;
    @SerializedName("tax_scope")
    @Expose
    private String taxScope;

    public String getStore_code() {
        return store_code;
    }

    public void setStore_code(String store_code) {
        this.store_code = store_code;
    }

    @SerializedName("store_code")
    @Expose
    private String store_code;

    @SerializedName("tax_percentage")
    @Expose
    private float taxPercentage;

    @SerializedName("add_tax")
    @Expose
    private float addTax;

    @SerializedName("surcharge")
    @Expose
    private float surcharge;

    public int getItemQty() {
        return ItemQty;
    }

    public void setItemQty(int itemQty) {
        ItemQty = itemQty;
    }

    @Expose
    private int ItemQty;

    public String getItemMasterCode() {
        return itemMasterCode;
    }

    public void setItemMasterCode(String itemMasterCode) {
        this.itemMasterCode = itemMasterCode;
    }

    public String getItemMasterName() {
        return itemMasterName;
    }

    public void setItemMasterName(String itemMasterName) {
        this.itemMasterName = itemMasterName;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getDiscPercent() {
        return discPercent;
    }

    public void setDiscPercent(float discPercent) {
        this.discPercent = discPercent;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getAppGroup() {
        return appGroup;
    }

    public void setAppGroup(String appGroup) {
        this.appGroup = appGroup;
    }

    public String getKotPrinter() {
        return kotPrinter;
    }

    public void setKotPrinter(String kotPrinter) {
        this.kotPrinter = kotPrinter;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getTaxScope() {
        return taxScope;
    }

    public void setTaxScope(String taxScope) {
        this.taxScope = taxScope;
    }

    public float getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(float taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public float getAddTax() {
        return addTax;
    }

    public void setAddTax(float addTax) {
        this.addTax = addTax;
    }

    public float getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(float surcharge) {
        this.surcharge = surcharge;
    }

}
