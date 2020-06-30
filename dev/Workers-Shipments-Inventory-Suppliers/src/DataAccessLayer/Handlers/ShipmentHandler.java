package DataAccessLayer.Handlers;

import BusinessLayer.SuppliersAndInventory.SharedClass;
import DataAccessLayer.DALDTO.ShipmentDALDTO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ShipmentHandler extends DataHandler<ShipmentDALDTO> {
    private static ShipmentHandler instance = null;

    private ShipmentHandler() {
        super();
    }

    public static ShipmentHandler getInstance() {
        if (instance == null)
            instance = new ShipmentHandler();
        return instance;
    }

    @Override
    public void save(ShipmentDALDTO dalObject) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Shipment (ID, Date, DepartureTime, OverallWeight, TruckLicenseNumber, SourceAddress, ShiftID, DriverID, Delivered) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)");
            pstmt.setInt(1, dalObject.getID());
            pstmt.setDate(2, Date.valueOf(dalObject.getDate()));
            pstmt.setTime(3, Time.valueOf(dalObject.getDepartureTime()));
            pstmt.setDouble(4, dalObject.getOverallWeight());
            pstmt.setString(5, dalObject.getTruckNumber());
            pstmt.setString(6, dalObject.getSource());
            pstmt.setInt(7, dalObject.getShiftID());
            pstmt.setString(8, dalObject.getDriverID());
            pstmt.executeUpdate();

            for (String comment : dalObject.getComments()) {
                pstmt = conn.prepareStatement("INSERT INTO ShipmentComments (Comment, ShipmentID) VALUES (?, ?)");
                pstmt.setString(1, comment);
                pstmt.setInt(2, dalObject.getID());
                pstmt.executeUpdate();
            }

            intMapper.put(dalObject.getID(), dalObject);
            pstmt.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(ShipmentDALDTO dalObject) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("UPDATE Shipment SET Delivered = 1 WHERE ID = ?");
            pstmt.setInt(1, dalObject.getID());

            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(ShipmentDALDTO dalObject) {

    }

    @Override
    public ShipmentDALDTO loadByString(String id) {
        //not in use in this class
        return null;
    }

    @Override
    public ShipmentDALDTO loadByInt(int id) {
        LocalDate date;
        LocalTime departureTime;
        String truckNumber;
        String source;
        List<Integer> certificateIDs = new ArrayList<>();
        int overallWeight;
        List<String> comments = new ArrayList<>();
        int shiftID;
        String driverID;
        String driverName;

        ShipmentDALDTO dalShipment = null;

        if (intMapper.containsKey(id))
            return intMapper.get(id);
        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT Date, DepartureTime, OverallWeight, TruckLicenseNumber, SourceAddress, DriverID, FirstName, LastName, ShiftID " +
                    "FROM Shipment JOIN Worker ON DriverID = Worker.ID " +
                    "WHERE Shipment.ID = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                date = rs.getDate("Date").toLocalDate();
                departureTime = rs.getTime("DepartureTime").toLocalTime();
                overallWeight = rs.getInt("OverallWeight");
                truckNumber = rs.getString("TruckLiceseNumber");
                source = rs.getString("Source");
                driverID = rs.getString("DriverID");
                driverName = rs.getString("FirstName") + " " + rs.getString("LastName");
                shiftID = rs.getInt("ShiftID");

                pstmt = conn.prepareStatement("SELECT ID FROM Certificate WHERE ShipmentID = ?");
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    certificateIDs.add(rs.getInt("ID"));
                }

                pstmt = conn.prepareStatement("SELECT Comment FROM ShipmentComments WHERE ShipmentID = ?");
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    comments.add(rs.getString("Comment"));
                }
                dalShipment = new ShipmentDALDTO(id, date, departureTime, truckNumber, driverName,
                        driverID, source, certificateIDs, overallWeight, comments, shiftID);
                intMapper.put(id, dalShipment);
            }
            pstmt.close();
            return dalShipment;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public int loadAutoID() {
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count (ID) AS MaxID FROM Shipment");
            rs.next();

            int maxID = rs.getInt("MaxID");
            stmt.close();
            return maxID;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<ShipmentDALDTO> loadAll() {
        List<ShipmentDALDTO> dalShipments = new Vector<>();
        int ID;
        LocalDate date;
        LocalTime departureTime;
        String truckNumber;
        String source;
        List<Integer> certificateIDs;
        int overallWeight;
        List<String> comments;
        int shiftID;
        String driverID;
        String driverName;

        ShipmentDALDTO dalShipment = null;
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Shipment.ID AS ID, Date, DepartureTime, OverallWeight, TruckLicenseNumber, SourceAddress, DriverID, FirstName, LastName, ShiftID, Delivered " +
                    "FROM Shipment JOIN Worker ON DriverID = Worker.ID" );
            PreparedStatement pstmt = null;
            while (rs.next()) {
                certificateIDs = new ArrayList<>();
                comments = new ArrayList<>();
                ID = rs.getInt("ID");
                date = rs.getDate("Date").toLocalDate();
                departureTime = rs.getTime("DepartureTime").toLocalTime();
                overallWeight = rs.getInt("OverallWeight");
                truckNumber = rs.getString("TruckLicenseNumber");
                source = rs.getString("SourceAddress");
                driverID = rs.getString("DriverID");
                driverName = rs.getString("FirstName") + " " + rs.getString("LastName");
                shiftID = rs.getInt("ShiftID");

                pstmt = conn.prepareStatement("SELECT ID FROM Certificate WHERE ShipmentID = ?");
                pstmt.setInt(1, ID);
                ResultSet newRS = pstmt.executeQuery();
                while (newRS.next()) {
                    certificateIDs.add(newRS.getInt("ID"));
                }

                pstmt = conn.prepareStatement("SELECT Comment FROM ShipmentComments WHERE ShipmentID = ?");
                pstmt.setInt(1, ID);
                newRS = pstmt.executeQuery();
                while (newRS.next()) {
                    comments.add(newRS.getString("Comment"));
                }
                if (!rs.getBoolean("Delivered")){
                    dalShipment = new ShipmentDALDTO(ID, date, departureTime, truckNumber, driverName,
                            driverID, source, certificateIDs, overallWeight, comments, shiftID);
                    intMapper.put(ID, dalShipment);
                    dalShipments.add(dalShipment);
                }

                pstmt.close();
            }
            stmt.close();
            return dalShipments;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dalShipments;
    }

    public List<ShipmentDALDTO> loadToReport() {
        List<ShipmentDALDTO> dalShipments = new Vector<>();
        int ID;
        LocalDate date;
        LocalTime departureTime;
        String truckNumber;
        String source;
        List<Integer> certificateIDs ;
        int overallWeight;
        List<String> comments ;
        int shiftID;
        String driverID;
        String driverName;

        ShipmentDALDTO dalShipment = null;
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Shipment.ID AS ID, Date, DepartureTime, OverallWeight, TruckLicenseNumber, SourceAddress, DriverID, FirstName, LastName, ShiftID, Delivered " +
                    "FROM Shipment JOIN Worker ON DriverID = Worker.ID");
            PreparedStatement pstmt = null;
            while (rs.next()) {
                certificateIDs = new ArrayList<>();
                comments = new ArrayList<>();
                ID = rs.getInt("ID");
                date = rs.getDate("Date").toLocalDate();
                departureTime = rs.getTime("DepartureTime").toLocalTime();
                overallWeight = rs.getInt("OverallWeight");
                truckNumber = rs.getString("TruckLicenseNumber");
                source = rs.getString("SourceAddress");
                driverID = rs.getString("DriverID");
                driverName = rs.getString("FirstName") + " " + rs.getString("LastName");
                shiftID = rs.getInt("ShiftID");

                pstmt = conn.prepareStatement("SELECT ID FROM Certificate WHERE ShipmentID = ?");
                pstmt.setInt(1, ID);
                ResultSet newRS = pstmt.executeQuery();
                while (newRS.next()) {
                    certificateIDs.add(newRS.getInt("ID"));
                }

                pstmt = conn.prepareStatement("SELECT Comment FROM ShipmentComments WHERE ShipmentID = ?");
                pstmt.setInt(1, ID);
                newRS = pstmt.executeQuery();
                while (newRS.next()) {
                    comments.add(newRS.getString("Comment"));
                }
                if (!rs.getBoolean("Delivered") || (rs.getBoolean("Delivered") && SharedClass.getSharedClass().getDATE().isAfter(date))) {
                    dalShipment = new ShipmentDALDTO(ID, date, departureTime, truckNumber, driverName,
                            driverID, source, certificateIDs, overallWeight, comments, shiftID);
                    intMapper.put(ID, dalShipment);
                    dalShipments.add(dalShipment);
                }

                pstmt.close();
            }
            stmt.close();
            return dalShipments;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dalShipments;
    }
}
