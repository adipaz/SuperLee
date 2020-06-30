package DataAccessLayer.Handlers;

import DataAccessLayer.DALDTO.ShiftDALDTO;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ShiftHandler extends DataHandler<ShiftDALDTO> {

    private static ShiftHandler instance = null;

    private ShiftHandler() {
        super();
    }

    public static ShiftHandler getInstance() {
        if (instance == null)
            instance = new ShiftHandler();
        return instance;
    }

    @Override
    public void save(ShiftDALDTO dalObject) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Shift (ID, Date, isDay, Ready, BranchAddress) VALUES (?, ?, ?, ?, ?)");
            pstmt.setInt(1, dalObject.getID());
            pstmt.setDate(2, Date.valueOf(dalObject.getDate()));
            pstmt.setBoolean(3, dalObject.isDayShift());
            pstmt.setBoolean(4, dalObject.isReady());
            pstmt.setString(5, dalObject.getBranchAddress());
            pstmt.executeUpdate();

            pstmt.close();
            intMapper.put(dalObject.getID(), dalObject);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(ShiftDALDTO dalObject) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("UPDATE Shift SET Ready = ? WHERE ID = ?");
            pstmt.setBoolean(1, dalObject.isReady());
            pstmt.setInt(2, dalObject.getID());
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(ShiftDALDTO dalObject) {

    }

    @Override
    public ShiftDALDTO loadByString(String id) {
        //not in use in this class
        return null;
    }

    @Override
    public ShiftDALDTO loadByInt(int id) {
        ShiftDALDTO dalShift = null;

        LocalDate date;
        boolean isDay;
        Map<Integer, Integer> jobNumbers = new LinkedHashMap<>();
        Map<String, Integer> workersJobs = new LinkedHashMap<>();
        boolean ready;
        String branchAddress;
        List<String> managersID = new ArrayList<>();
        try (Connection conn = connect()){
            PreparedStatement pstmt = conn.prepareStatement("SELECT Date, isDay, Ready, BranchAddress FROM Shift WHERE ID = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                date = rs.getDate("Date").toLocalDate();
                isDay = rs.getBoolean("isDay");
                ready = rs.getBoolean("Ready");
                branchAddress = rs.getString("BranchAddress");

                pstmt = conn.prepareStatement("SELECT WorkerID, JobID FROM WorkerJobInShift WHERE ShiftID = ?");
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    int jobID = rs.getInt("JobID");
                    String workerID = rs.getString("WorkerID");
                    workersJobs.put(workerID, jobID);
                    if (jobID == 0)
                        managersID.add(workerID);
                }

                pstmt = conn.prepareStatement("SELECT JobID, AllocationsNum FROM JobAllocationsInShift WHERE ShiftID = ?");
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    jobNumbers.put(rs.getInt("JobID"), rs.getInt("AllocationsNum"));
                }
                dalShift = new ShiftDALDTO(id, date, isDay, jobNumbers, workersJobs, ready, managersID, branchAddress);
            }
            pstmt.close();
            return dalShift;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void saveNewWorker(int shiftID, int jobID, String workerID) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO WorkerJobInShift (WorkerID, jobID, shiftID) VALUES (?, ?, ?)");
            pstmt.setString(1, workerID);
            pstmt.setInt(2, jobID);
            pstmt.setInt(3, shiftID);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("UPDATE JobAllocationsInShift SET AllocationsNum = AllocationsNum - 1 WHERE ShiftID = ? AND JobID = ?");
            pstmt.setInt(1, shiftID);
            pstmt.setInt(2, jobID);
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveNewJob(int shiftID, int jobID) {
        PreparedStatement pstmt = null;
        try (Connection conn = connect()) {
            pstmt = conn.prepareStatement("UPDATE JobAllocationsInShift SET AllocationsNum = AllocationsNum + 1 WHERE ShiftID = ? AND JobID = ?");
            pstmt.setInt(1, shiftID);
            pstmt.setInt(2, jobID);
            int numChanged = pstmt.executeUpdate();
            if (numChanged == 0) {
                pstmt = conn.prepareStatement("INSERT INTO JobAllocationsInShift (AllocationsNum, ShiftID, JobID) VALUES (1, ?, ?)");
                pstmt.setInt(1, shiftID);
                pstmt.setInt(2, jobID);
                pstmt.executeUpdate();
            }
            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<ShiftDALDTO> loadBranchShifts(String branchAddress) {
        List<ShiftDALDTO> dalShifts = new ArrayList<>();

        int shiftID;
        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT ID, Date, isDay, Ready FROM Shift WHERE BranchAddress = ?");
            pstmt.setString(1, branchAddress);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                shiftID = rs.getInt("ID");
                dalShifts.add(loadByInt(shiftID));
            }
            pstmt.close();
            return dalShifts;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<ShiftDALDTO> loadShiftHistory(String workerID) {
        List<ShiftDALDTO> dalShifts = new ArrayList<>();
        PreparedStatement pstmt = null;
        try (Connection conn = connect()) {
            pstmt = conn.prepareStatement("SELECT ShiftID FROM WorkerJobInShift WHERE WorkerID = ?");
            pstmt.setString(1, workerID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                dalShifts.add(loadByInt(rs.getInt("ShiftID")));
            }
            pstmt.close();
            return dalShifts;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dalShifts;
    }

    @Override
    public int loadAutoID() {
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count (ID) AS MaxID FROM Shift");
            rs.next();

            int maxID = rs.getInt("MaxID");
            stmt.close();
            return maxID+1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<ShiftDALDTO> loadAll() {
        List<ShiftDALDTO> dalShifts = new ArrayList<>();
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID FROM Shift");
            while (rs.next()) {
                int ID = rs.getInt("ID");
                if (intMapper.containsKey(ID))
                    dalShifts.add(intMapper.get(ID));
                else {
                    ShiftDALDTO dalShift = loadByInt(ID);
                    intMapper.put(ID, dalShift);
                    dalShifts.add(dalShift);
                }
            }
            stmt.close();
            return dalShifts;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dalShifts;
    }

    public List<ShiftDALDTO> loadShiftsByDate(LocalDate date) {
        List<ShiftDALDTO> dalShifts = new ArrayList<>();
        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT ID FROM Shift WHERE Date = ?");
            pstmt.setDate(1, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int shiftID = rs.getInt("ID");
                dalShifts.add(loadByInt(shiftID));
            }
            pstmt.close();
            return dalShifts;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dalShifts;
    }

    public void releaseWorker(String workerID, int shiftID, int jobID){
        try (Connection conn = connect()) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("DELETE FROM WorkerJobInShift WHERE WorkerID = ? AND ShiftID = ?");
            pstmt.setString(1, workerID);
            pstmt.setInt(2, shiftID);
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        PreparedStatement pstmt = null;
        try (Connection conn = connect()) {
            pstmt = conn.prepareStatement("SELECT AllocationsNum FROM JobAllocationsInShift WHERE ShiftID = ? AND JobID = ?");
            pstmt.setInt(1, shiftID);
            pstmt.setInt(2, jobID);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if (rs.getInt("AllocationsNum") <= 1) {
                pstmt = conn.prepareStatement("DELETE FROM JobAllocationsInShift WHERE ShiftID = ? AND JobID = ?");
            }
            else {
                pstmt = conn.prepareStatement("UPDATE JobAllocationsInShift SET AllocationsNum = AllocationsNum - 1 WHERE ShiftID = ? AND JobID = ?");
            }
            pstmt.setInt(1, shiftID);
            pstmt.setInt(2, jobID);
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
