package DataAccessLayer.DALDTO;


import DataStructures.Pair;

import java.util.HashMap;

public class DALOrder {
    private String orderID;
    private String supplierName;
    private String bnNumber;
    private String adress;
    private String date;
    private String phone;
    private HashMap<String, Pair<String,String>> products;

    public DALOrder(String supplierName,String bnNumber, String adress,String date,String phone,HashMap<String, Pair<String,String>> products,String orderID)
    {
        this.supplierName=supplierName;
        this.adress=adress;
        this.bnNumber=bnNumber;
        this.date=date;
        this.phone=phone;
        this.products=products;
        this.orderID=orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getBnNumber() {
        return bnNumber;
    }

    public void setBnNumber(String bnNumber) {
        this.bnNumber = bnNumber;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public HashMap<String, Pair<String, String>> getProducts() {
        return products;
    }

    public void setProducts(HashMap<String, Pair<String, String>> products) {
        this.products = products;
    }
}
