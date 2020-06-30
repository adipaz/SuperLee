package DataAccessLayer.Handlers;

import BusinessLayer.SuppliersAndInventory.SpecificProduct;
import DataAccessLayer.DALDTO.DALProduct;
import DataAccessLayer.DALDTO.DALReport;
import DataAccessLayer.SQLiteJDBC;
import InterfaceLayer.DTO.ProductDTO;
import InterfaceLayer.DTO.ReportDTO;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DatabaseHandler {

    private DALProduct dp;
    private SQLiteJDBC s;
    private DALReport dr;

    public DatabaseHandler() {
        this.dp = new DALProduct();
        this.dr = new DALReport();
        s = new SQLiteJDBC();
    }


    public void addNewProductToTables(int branch, ProductDTO pDTO, int minQuantity) {
        s.addNewProduct(branch,dp.convertDTOProductToDALProduct(pDTO),minQuantity);
    }
    public void addNewProductToTables(int branch, SpecificProduct p, int minQuantity) {
        s.addNewProduct(branch,dp.convertProductToDALProduct(p),minQuantity);
    }

    public void addProduct(SpecificProduct sp1, int branch) {
        s.addProduct(dp.convertProductToDALProduct(sp1), branch);
    }

    public void updateQuantity(int quantity,String catalogID, int branch) {
        s.updateQuantity(quantity,catalogID,branch);
    }

    public void updateCounter(int startingId, String catalogId, int branch) {
        s.updateCounter(startingId,catalogId,branch);
    }

    public void removeFromTables(boolean removeAll, List<Integer> idToDelete, int branch, String catalogId) {
        s.removeFromTables(removeAll,idToDelete,branch,catalogId);
    }

    public void ruinProducts(boolean ruinAll, List<Integer> idToRuin, int branch, String catalogId) {
        s.ruinProducts(ruinAll,idToRuin,branch,catalogId);
    }
    public void moveProductsToShelf(boolean moveAll, List<Integer> idToMove, int branch, String catalogId) {
        s.moveProductsToShelf(moveAll,idToMove,branch,catalogId);
    }

    public void diacountProducts(int branch, String catalogId, int discount) {
        s.discountProducts(branch,catalogId,discount);
    }

    public Map<Integer, Map<String, Integer>> getQuanTable() throws SQLException {
        ResultSet rs = s.selectExternalInfoCol("quantity");
        return getIntegerMapMap(rs);
    }

    public Map<Integer, Map<String, Integer>> getMinQuanTable() throws SQLException {
        ResultSet rs = s.selectExternalInfoCol("minQuantity");
        return getIntegerMapMap(rs);
    }

    public Map<Integer, Map<String, Integer>> getCatalogIdToCounterTable() throws SQLException {
        ResultSet rs = s.selectExternalInfoCol("counter");
        return getIntegerMapMap(rs);
    }

    private Map<Integer, Map<String, Integer>> getIntegerMapMap(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        Map<Integer, Map<String, Integer>> ret = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            ret.put(i, new HashMap<>());
        }
        while (rs.next()) {
            Map<String, Integer> row = new HashMap();
            Integer branch = (Integer) rs.getObject(1);
            String cid = (String) rs.getObject(2);
            Integer i = (Integer) rs.getObject(3);
            row.put(cid, i);
            ret.get(branch).put(cid, i);
        }
        return ret;
    }



    public Map<Integer, Map<String, List<String>>> getCategoryToCatalogIDTable() throws SQLException {
        ResultSet rs = s.selectAll("categoryToCatalogID");
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        Map<Integer, Map<String, List<String>>> ret = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            ret.put(i, new HashMap<>());
        }
        while (rs.next()) {
            Integer branch = (Integer) rs.getObject(1);
            String category = (String) rs.getObject(2);
            String cid = (String) rs.getObject(3);
            if (ret.get(branch).containsKey(category)) {
                ret.get(branch).get(category).add(cid);
            } else {
                List<String> catalogIds = new LinkedList<>();
                catalogIds.add(cid);
                ret.get(branch).put(category, catalogIds);
            }


        }
        return ret;

    }

    public Map<Integer, Map<String, List<SpecificProduct>>> getCatalogIdToProductsTable() throws SQLException {
        ResultSet rs = s.selectAll("productInternalInfo");
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        Map<Integer, Map<String, List<SpecificProduct>>> ret = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            ret.put(i, new HashMap<>());
        }
        while (rs.next()) {
            Integer branch = (Integer) rs.getObject(1);


            SpecificProduct sp = dp.convertDALProductToProduct(new DALProduct((String) rs.getObject(2), (String) rs.getObject(3), (String) rs.getObject(4),
                    (String) rs.getObject(5), (String) rs.getObject(6), (String) rs.getObject(7), (String) rs.getObject(8), (String) rs.getObject(9),
                    (String) rs.getObject(10), (String) rs.getObject(11), (String) rs.getObject(12), (String) rs.getObject(13),
                    (String) rs.getObject(14), (String) rs.getObject(15),
                    (String) rs.getObject(16), (String) rs.getObject(17)));

            String cid = sp.getCatalogID();

            if (ret.get(branch).containsKey(cid)) {
                ret.get(branch).get(cid).add(sp);
            } else {
                List<SpecificProduct> sps = new LinkedList<>();
                sps.add(sp);
                ret.get(branch).put(cid, sps);
            }

        }
        return ret;
    }


    public boolean isProductSupplied(String catalogId) {
        return s.isProductSupplied(catalogId);
    }

    public void saveReport(ReportDTO rDTO) {
        s.addNewReport(dr.convertDTOReportToDALReport(rDTO));
    }

    public void updateShippmentProductInfo(int branch, String catalogIdToUpdate, String newCategory, String newSubCategory, String newSubSubCategory, String newManufacturer, int newSellPriceI, int newBuyPriceI) {
        s.updateShippmentProductInfo(branch,catalogIdToUpdate,newCategory,newSubCategory,newSubSubCategory,newManufacturer,newSellPriceI,newBuyPriceI);
    }

    public String getAllReports() throws SQLException {
        ResultSet allReports = s.selectAll("InventoryReports");
        ResultSetMetaData md = allReports.getMetaData();
        String allReports_str = "";
        while (allReports.next()) {
            String type = (String) allReports.getObject(1);
            String creationTime = (String) allReports.getObject(2);
            String address = (String) allReports.getObject(3);
            String description = (String) allReports.getObject(4);
            allReports_str += "Report\n" + "type: " + type + "\n";
            allReports_str += "creationTime: " + creationTime + "\n";
            allReports_str += "address: " + address + "\n";
            allReports_str += "description:\n" + description + "\n\n";

        }
        return allReports_str;
    }

}
