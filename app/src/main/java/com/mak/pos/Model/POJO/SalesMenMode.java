package com.mak.pos.Model.POJO;

import java.io.Serializable;

/**
 * Created by MAKsuD on 10/3/2019.
 */

public class SalesMenMode implements Serializable {
    public String getSalesMenId() {
        return salesMenId;
    }

    public void setSalesMenId(String salesMenId) {
        this.salesMenId = salesMenId;
    }

    public String getSalesMenName() {
        return salesMenName;
    }

    public void setSalesMenName(String salesMenName) {
        this.salesMenName = salesMenName;
    }

    private String salesMenId;
    private String salesMenName;
}
