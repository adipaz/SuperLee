package InterfaceLayer;
import BusinessLayer.*;
import BusinessLayer.SuppliersAndInventory.Store;
import InterfaceLayer.DTO.ProductDTO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class ProductController {

    public String addProduct(ProductDTO pDTO, String quantity, String minQuantity, boolean isNewProduct, int branch) throws SQLException {
        String retValAdd = Store.getInstance().addProduct(pDTO.getCatalogID(), pDTO, Integer.parseInt(quantity), Integer.parseInt(minQuantity), isNewProduct,branch);
        //Store.getInstance().printListOfProducts();
        return  retValAdd;
    }


    public String ruinItems(String catalogId, int quantity, int branch) throws SQLException {
        return Store.getInstance().ruinItems(catalogId,quantity,branch);
    }

    public String removeProducts(String catalogId, int quantity, int branch) throws SQLException {
        return Store.getInstance().removeProducts(catalogId,quantity,branch);
    }

    public void loadProducts(String arg, int branch) throws IOException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(arg));
            String line = reader.readLine();
            while (line != null) {
                handleline(line,branch);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleline(String line, int branch) throws SQLException {
        String[] splitLine;
        splitLine = line.split(", ");
        int quantity, minQuantity;



        quantity = Integer.parseInt(splitLine[2]);
        minQuantity = Integer.parseInt(splitLine[3]);


        ProductDTO p = new ProductDTO(splitLine[0],splitLine[1],splitLine[4],splitLine[5],splitLine[6],splitLine[7],splitLine[8],splitLine[9],splitLine[10],
                splitLine[11],splitLine[12],splitLine[13],splitLine[16],splitLine[15],splitLine[14],"0");


        Store.getInstance().addProduct(splitLine[0], p, quantity, minQuantity, true,branch);
        //Store.getInstance().printListOfProducts();
    }

    public String moveProductsToShelf(String catalogId, int quantity, int branch) throws SQLException {
        return Store.getInstance().moveProductsToShelf(catalogId,quantity,branch);
    }

    public String addDiscountProduct(String catalogId, int discount, int branch) throws SQLException {
        return Store.getInstance().addDiscountProduct(catalogId,discount,branch);
    }

    public String addDiscountCategory(String category, int discount, int branch) throws SQLException {
        return Store.getInstance().addDiscountCategory(category,discount,branch);
    }


    public String updateShippmentProductInfo(int branch,String catalogIdToUpdate, String newCategory, String newSubCategory, String newSubSubCategory, String newManufacturer, int newSellPriceI, int newBuyPriceI) throws SQLException {
        return Store.getInstance().updateShippmentProductInfo(branch,catalogIdToUpdate,newCategory,newSubCategory,newSubSubCategory,newManufacturer,newSellPriceI,newBuyPriceI);

    }
}
