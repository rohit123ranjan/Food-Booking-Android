package com.example.rohitranjan.foodonbook;

public class pojoOrder {

    private String oName,oPhone,oTotal,dataTable,rating;

    public pojoOrder(String oName, String oPhone, String oTotal, String dataTable, String rating) {
        this.oName = oName;
        this.oPhone = oPhone;
        this.oTotal = oTotal;
        this.dataTable = dataTable;
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public pojoOrder() {
    }

    public String getDataTable() {
        return dataTable;
    }

    public void setDataTable(String dataTable) {
        this.dataTable = dataTable;
    }

    public String getoName() {
        return oName;
    }

    public void setoName(String oName) {
        this.oName = oName;
    }

    public String getoPhone() {
        return oPhone;
    }

    public void setoPhone(String oPhone) {
        this.oPhone = oPhone;
    }

    public String getoTotal() {
        return oTotal;
    }

    public void setoTotal(String oTotal) {
        this.oTotal = oTotal;
    }
}
