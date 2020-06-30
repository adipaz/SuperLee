package DataAccessLayer.Handlers;

import DataAccessLayer.DALDTO.CertificateDALDTO;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CertificateHandler extends DataHandler<CertificateDALDTO> {
    private static CertificateHandler instance = null;

    private CertificateHandler() {
        super();
    }

    public static CertificateHandler getInstance() {
        if (instance == null)
            instance = new CertificateHandler();
        return instance;
    }

    @Override
    public void save(CertificateDALDTO dalObject) {
        PreparedStatement pstmt = null;
        try (Connection conn = connect()) {
            pstmt = conn.prepareStatement("INSERT INTO Certificate (ID, ShipmentID, BranchAddress) VALUES (?, ?, ?)");
            pstmt.setInt(1, dalObject.getID());
            pstmt.setInt(2, dalObject.getShipmentID());
            pstmt.setString(3, dalObject.getAddress());
            pstmt.executeUpdate();

            for (Map.Entry<String, Integer> entry: dalObject.getProductsQuantities().entrySet()) {
                pstmt = conn.prepareStatement("INSERT INTO ProductsOfCertificate (CertificateID, CatalogNumber, Quantity) VALUES (?, ?, ?)");
                pstmt.setInt(1, dalObject.getID());
                pstmt.setString(2, entry.getKey());
                pstmt.setInt(3, entry.getValue());
                pstmt.executeUpdate();
            }
            intMapper.put(dalObject.getID(), dalObject);
            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(CertificateDALDTO dalObject) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = null;
            for (Map.Entry<String, Integer> entry : dalObject.getProductsQuantities().entrySet()) {
                pstmt = conn.prepareStatement("UPDATE ProductsOfCertificate SET Quantity = ? WHERE CertificateID = ? AND CatalogNumber = ?");
                pstmt.setInt(1, entry.getValue());
                pstmt.setInt(2, dalObject.getID());
                pstmt.setString(3, entry.getKey());
                pstmt.executeUpdate();
            }
            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(CertificateDALDTO dalObject) {
        PreparedStatement pstmt = null;
        try (Connection conn = connect()) {
            pstmt = conn.prepareStatement("DELETE FROM Certificate WHERE ID = ?");
            pstmt.setInt(1, dalObject.getID());
            pstmt.executeUpdate();
            intMapper.remove(dalObject.getID());

            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public CertificateDALDTO loadByString(String id) {
        // not in use in this class
        return null;
    }

    @Override
    public CertificateDALDTO loadByInt(int id) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT BranchAddress FROM Certificate WHERE ID = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Map<String, Integer> productsQuantities = new HashMap<>();
                String address = rs.getString("BranchAddress");
                pstmt = conn.prepareStatement("SELECT CatalogNumber, Quantity FROM ProductsOfCertificate WHERE CertificateID = ?");
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    productsQuantities.put(rs.getString("CatalogNumber"), rs.getInt("Quantity"));
                }
                CertificateDALDTO dalCertificate = new CertificateDALDTO(id, productsQuantities, address, 0);
                intMapper.put(id, dalCertificate);
                pstmt.close();
                return dalCertificate;
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public int loadAutoID() {
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT max (ID) AS MaxID FROM Certificate");
            rs.next();

            int maxID = rs.getInt("MaxID");
            stmt.close();
            return maxID + 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<CertificateDALDTO> loadAll() {
        return null;
    }
}
