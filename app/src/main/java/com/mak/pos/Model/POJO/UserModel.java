package com.mak.pos.Model.POJO;

import java.io.Serializable;

public class UserModel implements Serializable
{
    private String hwserial;

    private String salesman;

    private String id;

    private String branch;

    public String getHwserial ()
    {
        return hwserial;
    }

    public void setHwserial (String hwserial)
    {
        this.hwserial = hwserial;
    }

    public String getSalesman ()
    {
        return salesman;
    }

    public void setSalesman (String salesman)
    {
        this.salesman = salesman;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getBranch ()
    {
        return branch;
    }

    public void setBranch (String branch)
    {
        this.branch = branch;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [hwserial = "+hwserial+", salesman = "+salesman+", id = "+id+", branch = "+branch+"]";
    }
}