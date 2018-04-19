/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author stuar
 */
public class Resources {

    private String root;
    private Heating heating;
    private int historyMaxLen = 5;

    private Map<String, String> users = new HashMap<>();
    private Map<String, String> locations = new HashMap<>();

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public Heating getHeating() {
        return heating;
    }

    public void setHeating(Heating heating) {
        this.heating = heating;
    }

    public int getHistoryMaxLen() {
        return historyMaxLen;
    }

    public void setHistoryMaxLen(int historyMaxLen) {
        this.historyMaxLen = historyMaxLen;
    }

    public Map<String, String> getUsers() {
        return users;
    }

    public void setUsers(Map<String, String> users) {
        this.users = users;
    }

    public Map<String, String> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, String> locations) {
        this.locations = locations;
    }

}
