package com.mak.pos.Model.POJO;

import java.io.Serializable;

public class Items implements Serializable
{
    private String code;

    private float addtaxAmt2;

    private float rate;

    private String sno;

    private int Qty;

    private float disc;
    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    private String item_name;

    public float getDisc() {
        return disc;
    }

    public void setDisc(float disc) {
        this.disc = disc;
    }

    private String taxCode;

    private float taxamt;

    private float discAmt;

    private float addtaxAmt;

    private String remarks;

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public float getAddtaxAmt2 ()
    {
        return addtaxAmt2;
    }

    public void setAddtaxAmt2 (float addtaxAmt2)
    {
        this.addtaxAmt2 = addtaxAmt2;
    }

    public float getRate ()
    {
        return rate;
    }

    public void setRate (float rate)
    {
        this.rate = rate;
    }

    public String getSno ()
    {
        return sno;
    }

    public void setSno (String sno)
    {
        this.sno = sno;
    }

    public int getQty ()
    {
        return Qty;
    }

    public void setQty (int Qty)
    {
        this.Qty = Qty;
    }


    public String getTaxCode ()
    {
        return taxCode;
    }

    public void setTaxCode (String taxCode)
    {
        this.taxCode = taxCode;
    }

    public float getTaxamt ()
    {
        return taxamt;
    }

    public void setTaxamt (float taxamt)
    {
        this.taxamt = taxamt;
    }

    public float getDiscAmt ()
    {
        return discAmt;
    }

    public void setDiscAmt (float discAmt)
    {
        this.discAmt = discAmt;
    }

    public float getAddtaxAmt ()
    {
        return addtaxAmt;
    }

    public void setAddtaxAmt (float addtaxAmt)
    {
        this.addtaxAmt = addtaxAmt;
    }

    public String getRemarks ()
    {
        return remarks;
    }

    public void setRemarks (String remarks)
    {
        this.remarks = remarks;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [code = "+code+", addtaxAmt2 = "+addtaxAmt2+", rate = "+rate+", sno = "+sno+", Qty = "+Qty+", disc = "+disc+", taxCode = "+taxCode+", taxamt = "+taxamt+", discAmt = "+discAmt+", addtaxAmt = "+addtaxAmt+", remarks = "+remarks+"]";
    }
}

			