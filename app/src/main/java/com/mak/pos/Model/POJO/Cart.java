package com.mak.pos.Model.POJO;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable
{
    private String prefix;

    private String doctime;

    private String noofPrints;

    private String tableCode;

    private String store;

    private String captain;

    private String srl
            ;

    private String type;

    private String enteredBy;

    private int totalbillAmount;

    public int getTotalbillAmountWithDsic() {
        return totalbillAmountWithDsic;
    }

    public void setTotalbillAmountWithDsic(int totalbillAmountWithDsic) {
        this.totalbillAmountWithDsic = totalbillAmountWithDsic;
    }

    private int totalbillAmountWithDsic;

    private String branch;

    private String partyContact;

    private String paxNo;

    private String partyEmail;

    private String partyAddr;

    private String partyName;

    private String totalDiscAmt;

    private String roundoff;

    private String docdate;

    private ArrayList<Items> items;

    private String storeCode;

    private String Hwserial;

    public String getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    private String tableStatus;
    private boolean isBillGenrate;
    private String specialDiscount;

    public boolean isBillGenrate() {
        return isBillGenrate;
    }

    public void setBillGenrate(boolean billGenrate) {
        isBillGenrate = billGenrate;
    }

    public String getSpecialDiscount() {
        return specialDiscount;
    }

    public void setSpecialDiscount(String specialDiscount) {
        this.specialDiscount = specialDiscount;
    }

    public String getPrefix ()
    {
        return prefix;
    }

    public void setPrefix (String prefix)
    {
        this.prefix = prefix;
    }

    public String getDoctime ()
    {
        return doctime;
    }

    public void setDoctime (String doctime)
    {
        this.doctime = doctime;
    }

    public String getNoofPrints ()
    {
        return noofPrints;
    }

    public void setNoofPrints (String noofPrints)
    {
        this.noofPrints = noofPrints;
    }

    public String getTableCode ()
    {
        return tableCode;
    }

    public void setTableCode (String tableCode)
    {
        this.tableCode = tableCode;
    }

    public String getStore ()
    {
        return store;
    }

    public void setStore (String store)
    {
        this.store = store;
    }

    public String getCaptain ()
    {
        return captain;
    }

    public void setCaptain (String captain)
    {
        this.captain = captain;
    }

    public String getSrl ()
    {
        return srl;
    }

    public void setSrl (String srl)
    {
        this.srl = srl;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getEnteredBy ()
    {
        return enteredBy;
    }

    public void setEnteredBy (String enteredBy)
    {
        this.enteredBy = enteredBy;
    }

    public int getTotalbillAmount ()
    {
        return totalbillAmount;
    }

    public void setTotalbillAmount (int totalbillAmount)
    {
        this.totalbillAmount = totalbillAmount;
    }

    public String getBranch ()
    {
        return branch;
    }

    public void setBranch (String branch)
    {
        this.branch = branch;
    }

    public String getPartyContact ()
    {
        return partyContact;
    }

    public void setPartyContact (String partyContact)
    {
        this.partyContact = partyContact;
    }

    public String getPaxNo ()
    {
        return paxNo;
    }

    public void setPaxNo (String paxNo)
    {
        this.paxNo = paxNo;
    }

    public String getPartyEmail ()
    {
        return partyEmail;
    }

    public void setPartyEmail (String partyEmail)
    {
        this.partyEmail = partyEmail;
    }

    public String getPartyAddr ()
    {
        return partyAddr;
    }

    public void setPartyAddr (String partyAddr)
    {
        this.partyAddr = partyAddr;
    }

    public String getPartyName ()
    {
        return partyName;
    }

    public void setPartyName (String partyName)
    {
        this.partyName = partyName;
    }

    public String getTotalDiscAmt ()
    {
        return totalDiscAmt;
    }

    public void setTotalDiscAmt (String totalDiscAmt)
    {
        this.totalDiscAmt = totalDiscAmt;
    }

    public String getRoundoff ()
    {
        return roundoff;
    }

    public void setRoundoff (String roundoff)
    {
        this.roundoff = roundoff;
    }

    public String getDocdate ()
    {
        return docdate;
    }

    public void setDocdate (String docdate)
    {
        this.docdate = docdate;
    }

    public ArrayList<Items> getItems ()
    {
        return items;
    }

    public void setItems (ArrayList<Items> items)
    {
        this.items = items;
    }

    public String getStoreCode()
    {
        return storeCode;
    }

    public void setStoreCode (String storeCode)
    {
        this.storeCode = storeCode;
    }

    public String getHwserial ()
    {
        return Hwserial;
    }

    public void setHwserial (String Hwserial)
    {
        this.Hwserial = Hwserial;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [prefix = "+prefix+", doctime = "+doctime+", noofPrints = "+noofPrints+", tableCode = "+tableCode+", store = "+store+", captain = "+captain+", srl = "+srl+", type = "+type+", enteredBy = "+enteredBy+", totalbillAmount = "+totalbillAmount+", branch = "+branch+", partyContact = "+partyContact+", paxNo = "+paxNo+", partyEmail = "+partyEmail+", partyAddr = "+partyAddr+", partyName = "+partyName+", totalDiscAmt = "+totalDiscAmt+", roundoff = "+roundoff+", docdate = "+docdate+", items = "+items+", storeCode = "+storeCode+", Hwserial = "+Hwserial+"]";
    }
}

			