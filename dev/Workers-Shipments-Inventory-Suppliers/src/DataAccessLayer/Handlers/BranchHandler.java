package DataAccessLayer.Handlers;

import DataAccessLayer.DALDTO.BranchDALDTO;

import java.sql.*;
import java.util.List;
import java.util.Vector;

public class BranchHandler extends DataHandler<BranchDALDTO> {

    private static BranchHandler instance = null;

    private BranchHandler() {
        super();
    }

    public static BranchHandler getInstance() {
        if (instance == null)
            instance = new BranchHandler();
        return instance;
    }

    @Override
    public void save(BranchDALDTO dalObject) {

    }

    @Override
    public void update(BranchDALDTO dalObject) {

    }

    @Override
    public void delete(BranchDALDTO dalObject) {

    }

    @Override
    public BranchDALDTO loadByString(String id) {
        if (stringMapper.containsKey(id))
            return stringMapper.get(id);
        try (Connection conn = connect()) {
            BranchDALDTO dalBranch = null;
            String address;
            char area;
            PreparedStatement pstmt = conn.prepareStatement("SELECT  Area FROM Branch WHERE Address = ?");
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                area = rs.getString("Area").charAt(0);
                dalBranch = new BranchDALDTO(id, area);
            }

            pstmt.close();
            return dalBranch;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public BranchDALDTO loadByInt(int id) {
        //not in use in this class
        return null;
    }

    @Override
    public int loadAutoID() {
        //not in use in this class
        return 0;
    }

    @Override
    public List<BranchDALDTO> loadAll() {
        try (Connection conn = connect()) {
            List<BranchDALDTO> dalBranches = new Vector<>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Branch");
            while (rs.next()) {
                BranchDALDTO dalBranch = new BranchDALDTO(rs.getString("Address"), rs.getString("Area").charAt(0));
                dalBranches.add(dalBranch);
            }
            stmt.close();
            return dalBranches;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return new Vector<>();
    }

    public BranchDALDTO loadBranchByShift(int shiftID) {
        BranchDALDTO dalBranch = null;
        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT Address FROM Branch JOIN Shift ON Address = BranchAddress WHERE ID = ?");
            pstmt.setInt(1, shiftID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dalBranch = loadByString(rs.getString("Address"));
            }
            pstmt.close();
            return dalBranch;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
