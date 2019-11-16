package com.mak.pos.Model;

import java.io.Serializable;

/**
 * Created by MAKsuD on 9/1/2019.
 */

public class PartyInfoModel implements Serializable {
    public String getParty_name() {
        return party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public String getParty_address() {
        return party_address;
    }

    public void setParty_address(String party_address) {
        this.party_address = party_address;
    }

    public String getParty_number() {
        return party_number;
    }

    public void setParty_number(String party_number) {
        this.party_number = party_number;
    }

    public String getParty_person() {
        return party_person;
    }

    public void setParty_person(String party_person) {
        this.party_person = party_person;
    }

    private String party_name;
    private String party_address;
    private String party_number;
    private String party_person;

    public String getSalesmen_id() {
        return salesmen_id;
    }

    public void setSalesmen_id(String salesmen_id) {
        this.salesmen_id = salesmen_id;
    }

    public String getSalesmen_name() {
        return salesmen_name;
    }

    public void setSalesmen_name(String salesmen_name) {
        this.salesmen_name = salesmen_name;
    }

    private String salesmen_id;
    private String salesmen_name;
}
