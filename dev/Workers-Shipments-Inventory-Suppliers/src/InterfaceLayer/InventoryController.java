package InterfaceLayer;


import InterfaceLayer.DTO.ProductDTO;
import InterfaceLayer.DTO.ReportDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class InventoryController {
    private static ReportController reportController = new ReportController();
    private static ProductController productController=new ProductController();


    public static void loadProducts(String arg, int branch) throws IOException {
        productController.loadProducts(arg, branch);
    }

    public static String delegateAddProduct(String catalogId,String name,String sellPrice,String buyPrice,String manufacturer,String category,
                                   String subCategory,String subSubCategory,String weight,
                                   String id, String Location,String isFlaw,String year,String month,String day,String quantity,String minQuantity, boolean isNewProduct, int branch) throws SQLException {
        ProductDTO pDTO = new ProductDTO(catalogId,name,sellPrice,buyPrice,manufacturer,category,
                subCategory,subSubCategory,weight,id,Location,isFlaw,year,month,day,"0");
        return productController.addProduct(pDTO,quantity,minQuantity, isNewProduct, branch);
    }


    public static String delegateRemoveProducts(String catalogId,int quantity, int branch) throws SQLException {

        return productController.removeProducts(catalogId,quantity, branch);
    }

    public static ReportDTO delegateOrderReport(int branch) throws SQLException {
        return reportController.createOrderReport( branch);
    }

    public static String delegateMakeProductsFlaw(String catalogId,int quantity, int branch) throws SQLException {

        return productController.ruinItems(catalogId,quantity, branch);
    }

    public static ReportDTO delegateInventoryReport(List<String> categories, int branch) throws SQLException {


       ReportDTO report = reportController.createSupplyReport(categories, branch);
        return report;
    }

    public static ReportDTO delegateFlawsReport( int branch) throws SQLException {
        return reportController.createFlawReport( branch);
    }

    public static String MoveProductsToShelf(String catalogId, int quantity, int branch) throws SQLException {
        return productController.moveProductsToShelf(catalogId,quantity, branch);
    }

    public static String addDiscountProduct(String catalogId, int discount, int branch) throws SQLException {
        return productController.addDiscountProduct(catalogId,discount, branch);
    }

    public static String addDiscountCategory(String category, int discount, int branch) throws SQLException {
        return productController.addDiscountCategory(category,discount, branch);
    }


    public static String updateShippmentProductInfo(int branch,String catalogIdToUpdate, String newCategory, String newSubCategory, String newSubSubCategory, String newManufacturer, int newSellPriceI, int newBuyPriceI) throws SQLException {
        return productController.updateShippmentProductInfo(branch,catalogIdToUpdate,newCategory,newSubCategory,newSubSubCategory,newManufacturer,newSellPriceI,newBuyPriceI);
    }

    public static String getAllReports() throws SQLException {
        return reportController.getAllReports();
    }
}
