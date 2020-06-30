package DataAccessLayer.DALDTO;

import BusinessLayer.SuppliersAndInventory.LOCATION;
import BusinessLayer.SuppliersAndInventory.SpecificProduct;
import InterfaceLayer.DTO.ProductDTO;

import java.util.*;

public class DALProduct {
    private String catalogID;
    private String name;
    private String sellPrice;
    private String buyPrice;
    private String manufacturer;
    private String category;
    private String subCategory;
    private String subSubCategory;
    private String weight;

    private String id;
    private String location;
    private String isFlaw;
    private String year;
    private String month;
    private String day;

    private String discount;

    public DALProduct(String catalogID, String name, String sellPrice, String buyPrice, String manufacturer, String category, String subCategory, String subSubCategory, String weight, String id, String location, String isFlaw, String year, String month, String day, String discount) {
        this.catalogID = catalogID;
        this.name = name;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.manufacturer = manufacturer;
        this.category = category;
        this.subCategory = subCategory;
        this.subSubCategory = subSubCategory;
        this.weight = weight;
        this.id = id;
        this.location = location;
        this.isFlaw = isFlaw;
        this.year = year;
        this.month = month;
        this.day = day;
        this.discount = discount;
    }

    public DALProduct() {

    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getSubSubCategory() {
        return subSubCategory;
    }

    public void setSubSubCategory(String subSubCategory) {
        this.subSubCategory = subSubCategory;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIsFlaw() {
        return isFlaw;
    }

    public void setIsFlaw(String isFlaw) {
        this.isFlaw = isFlaw;
    }

    public DALProduct convertProductToDALProduct(SpecificProduct specificProduct) {
        LOCATION l = specificProduct.getLocation();
        String location = l.equals(LOCATION.STORAGE) ? "Storage" : "StoreShelf";
        Date d = specificProduct.getExpirationDate();
        String year = Integer.toString(d.getYear());
        String month = Integer.toString(d.getMonth());
        String day = Integer.toString(d.getDay());

        return new DALProduct(specificProduct.getCatalogID(), specificProduct.getName(), Double.toString(specificProduct.getSellPrice()),
                Double.toString(specificProduct.getBuyPrice()), specificProduct.getManufacturer(), specificProduct.getCategory(),
                specificProduct.getSubCategory(), specificProduct.getSubSubCategory(), Double.toString(specificProduct.getWeight()),
                Integer.toString(specificProduct.getId()), location, Boolean.toString(specificProduct.isFlaw()),
                year, month, day, Integer.toString(specificProduct.getDiscount()));
    }


    public SpecificProduct convertDALProductToProduct(DALProduct dalProduct) {
        String l = dalProduct.getLocation();
        LOCATION location = l.equals("Storage") ? LOCATION.STORAGE : LOCATION.STORESHELF;
        Date date = new Date(Integer.parseInt(dalProduct.getYear()) , Integer.parseInt(dalProduct.getMonth()) ,
                Integer.parseInt(dalProduct.getDay()));
        ;


        return new SpecificProduct(dalProduct.getCatalogID(), dalProduct.getName(), Double.parseDouble(dalProduct.getSellPrice()),
                Double.parseDouble(dalProduct.getBuyPrice()), dalProduct.getManufacturer(), dalProduct.getCategory(),
                dalProduct.getSubCategory(), dalProduct.getSubSubCategory(), Double.parseDouble(dalProduct.getWeight()),
                Integer.parseInt(dalProduct.getId()), location, Boolean.parseBoolean(dalProduct.getIsFlaw()),
                date, Integer.parseInt(dalProduct.getDiscount()));
    }

    public DALProduct convertDTOProductToDALProduct(ProductDTO pDTO) {
        return new DALProduct(pDTO.getCatalogID(), pDTO.getName(),pDTO.getSellPrice(),
                pDTO.getBuyPrice(), pDTO.getManufacturer(), pDTO.getCategory(),
                pDTO.getSubCategory(), pDTO.getSubSubCategory(), pDTO.getWeight(),
                pDTO.getId(), pDTO.getLocation(), pDTO.getIsFlaw(),
                pDTO.getYear(),  pDTO.getMonth(),  pDTO.getDay(), pDTO.getDiscount());
    }



}