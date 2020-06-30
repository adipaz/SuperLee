package BusinessLayer.SuppliersAndInventory;

import InterfaceLayer.DTO.ProductDTO;

import java.util.Date;

public class SpecificProduct extends Product {
    private int id;
    private LOCATION location;
    private boolean isFlaw;
    private Date expirationDate;

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public SpecificProduct(String catalogID, String name, double sellPrice, double buyPrice, String manufacturer, String category, String subCategory, String subSubCategory, double weight,
                           int id, LOCATION location, boolean isFlaw, Date expirationDate, int discount) {
        super(catalogID, name, sellPrice, buyPrice, manufacturer, category, subCategory, subSubCategory, weight, discount);
        this.id = id;
        this.location = location;
        this.isFlaw = isFlaw;
        this.expirationDate = expirationDate;
    }

    public static SpecificProduct my_clone(SpecificProduct sp) {
        SpecificProduct sp1 = new SpecificProduct(sp.getCatalogID(), sp.getName(), sp.getSellPrice(), sp.getBuyPrice(), sp.getManufacturer(), sp.getCategory(), sp.getSubCategory(),
                sp.getSubSubCategory(), sp.getWeight(), sp.id, sp.getLocation(), sp.isFlaw, sp.getExpirationDate(), sp.getDiscount());
        return sp1;
    }

    public static SpecificProduct createProduct(ProductDTO sp) {
        LOCATION l;
        if (sp.getLocation().equals("STORAGE")) {
            l = LOCATION.STORAGE;
        } else {
            l = LOCATION.STORESHELF;
        }
        if (!validateDate(Integer.parseInt(sp.getYear()), Integer.parseInt((sp.getMonth())), Integer.parseInt(sp.getDay()))) {
            return null;
        }
        Date date = new Date(Integer.parseInt(sp.getYear()) - 1900, Integer.parseInt(sp.getMonth()) - 1, Integer.parseInt(sp.getDay()));
        if (!(validatePositive(Double.parseDouble(sp.getWeight())) && validatePositive(Double.parseDouble(sp.getSellPrice()))
                && validatePositive(Double.parseDouble(sp.getBuyPrice())) && validateCategoryLength(sp.getCategory())
                && validateCategoryLength(sp.getSubCategory()) && validateCategoryLength(sp.getSubSubCategory())))
            return null;

        SpecificProduct sp1 = new SpecificProduct(sp.getCatalogID(), sp.getName(), Double.parseDouble(sp.getSellPrice()), Double.parseDouble(sp.getBuyPrice()), sp.getManufacturer(), sp.getCategory(), sp.getSubCategory(),
                sp.getSubSubCategory(), Double.parseDouble(sp.getWeight()), 0, l, Boolean.parseBoolean(sp.getIsFlaw()), date, Integer.parseInt(sp.getDiscount()));

        return sp1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LOCATION getLocation() {
        return location;
    }

    public void setLocation(LOCATION location) {
        this.location = location;
    }

    public boolean isFlaw() {
        return isFlaw;
    }

    public void setFlaw(boolean flaw) {
        isFlaw = flaw;
    }


    @Override
    public String toString() {
        return "SpecificProduct{" +
                "id=" + id +
                ", location=" + location +
                ", isFlaw=" + isFlaw +
                ", expirationDate=" + expirationDate +
                '}';
    }

    protected static boolean validatePositive(double number) {
        return number > 0;
    }

    protected static boolean validatePositive(int number) {
        return number > 0;
    }

    private static boolean validateDate(int year, int month, int day) {
        if (year > 1900 && year <= 2100) {
            if (month > 0 && month <= 12) {
                if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                    return (day > 0 && day <= 31);
                } else if (month == 2 && year % 4 != 0) {
                    return (day > 0 && day <= 28);
                } else if (month == 2 && year % 4 == 0) {

                    return (day > 0 && day <= 29);
                } else {
                    return (day > 0 && day <= 30);
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean validateCategoryLength(String category) {
        return category.length() > 0 && category.length() <= 9;
    }


}
