package DataAccessLayer.Handlers;

import DataAccessLayer.DALDTO.TruckDALDTO;

import java.sql.*;
import java.util.List;
import java.util.Vector;

public class TruckHandler extends DataHandler<TruckDALDTO> {

    private static TruckHandler instance = null;

    private TruckHandler() {
        super();
    }

    public static TruckHandler getInstance() {
        if (instance == null)
            instance = new TruckHandler();
        return instance;
    }

    @Override
    public void save(TruckDALDTO dalObject) {

    }

    @Override
    public void update(TruckDALDTO dalObject) {

    }

    @Override
    public void delete(TruckDALDTO dalObject) {

    }

    @Override
    public TruckDALDTO loadByString(String id) {
        TruckDALDTO dalTruck = null;
        if (stringMapper.containsKey(id))
            return stringMapper.get(id);
        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT Model, TruckWeight, MaxCarryWeight FROM Truck WHERE LicenseNumber = ?");
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dalTruck = new TruckDALDTO(id, rs.getString("Model"),
                        rs.getDouble("TruckWeight"), rs.getDouble("MaxCarryWeight"));
                stringMapper.put(id, dalTruck);
            }
            pstmt.close();
            return dalTruck;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public TruckDALDTO loadByInt(int id) {
        //not in use in this class
        return null;
    }

    @Override
    public int loadAutoID() {
        //not in use in this class
        return 0;
    }

    @Override
    public List<TruckDALDTO> loadAll() {
        List<TruckDALDTO> dalTrucks = new Vector<>();
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Truck");
            while (rs.next()) {
                TruckDALDTO dalTruck = new TruckDALDTO(rs.getString("LicenseNumber"), rs.getString("Model"), rs.getDouble("TruckWeight"), rs.getDouble("MaxCarryWeight"));
                stringMapper.put(dalTruck.getLicenseNumber(), dalTruck);
                dalTrucks.add(dalTruck);
            }
            stmt.close();
            return dalTrucks;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dalTrucks;
    }

}
