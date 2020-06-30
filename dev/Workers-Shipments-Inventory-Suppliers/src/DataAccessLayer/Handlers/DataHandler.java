package DataAccessLayer.Handlers;

import DataAccessLayer.DALDTO.DALDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class DataHandler<T extends DALDTO> {
    Map<String, T> stringMapper;
    Map<Integer, T> intMapper;

    public DataHandler() {
        stringMapper = new LinkedHashMap<>();
        intMapper = new LinkedHashMap<>();
    }

    public Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:../dev/Workers-Shipments-Inventory-Suppliers/data/SuperLee8.db");
            //return DriverManager.getConnection("jdbc:sqlite:dev/Workers-Shipments-Inventory-Suppliers/data/SuperLee8.db");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void save(T dalObject);

    public abstract void update(T dalObject);

    public abstract void delete(T dalObject);

    public abstract T loadByString(String id);

    public abstract T loadByInt(int id);

    public abstract int loadAutoID();

    public abstract List<T> loadAll();

}
