package InterfaceLayer.DTO;


public class ProductDTO {
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




    public ProductDTO(String catalogID, String name, String sellPrice, String buyPrice, String manufacturer,
                      String category, String subCategory, String subSubCategory, String weight, String id,
                      String location, String isFlaw, String year, String month, String day, String discount) {
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
        this.discount=discount;

    }


    public String getCatalogID() {
        return catalogID;
    }

    public String getName() {
        return name;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getSubSubCategory() {
        return subSubCategory;
    }

    public String getWeight() {
        return weight;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getIsFlaw() {
        return isFlaw;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }
    public String getDiscount() {
        return discount;
    }

    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setSubSubCategory(String subSubCategory) {
        this.subSubCategory = subSubCategory;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setIsFlaw(String isFlaw) {
        this.isFlaw = isFlaw;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
