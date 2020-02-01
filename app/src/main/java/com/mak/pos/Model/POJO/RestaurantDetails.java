package com.mak.pos.Model.POJO;

import java.io.Serializable;

public class RestaurantDetails implements Serializable
{
    private String rGSTNo;

    private String rAddress;

    private String rName;

    private String rTelephone;

    public String getRGSTNo ()
    {
        return rGSTNo;
    }

    public void setRGSTNo (String rGSTNo)
    {
        this.rGSTNo = rGSTNo;
    }

    public String getRAddress ()
    {
        return rAddress;
    }

    public void setRAddress (String rAddress)
    {
        this.rAddress = rAddress;
    }

    public String getRName ()
    {
        return rName;
    }

    public void setRName (String rName)
    {
        this.rName = rName;
    }

    public String getRTelephone ()
    {
        return rTelephone;
    }

    public void setRTelephone (String rTelephone)
    {
        this.rTelephone = rTelephone;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [rGSTNo = "+rGSTNo+", rAddress = "+rAddress+", rName = "+rName+", rTelephone = "+rTelephone+"]";
    }
}
		