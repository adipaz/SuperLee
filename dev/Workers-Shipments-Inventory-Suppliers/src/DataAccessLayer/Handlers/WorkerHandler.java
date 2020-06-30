package DataAccessLayer.Handlers;

import BusinessLayer.Enums.WorkingTimes;
import DataAccessLayer.DALDTO.WorkerDALDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class WorkerHandler extends DataHandler<WorkerDALDTO> {
    private static WorkerHandler instance = null;

    private WorkerHandler() {
        super();
    }

    public static WorkerHandler getInstance() {
        if (instance == null)
            instance = new WorkerHandler();
        return instance;
    }

    @Override
    public void save(WorkerDALDTO dalObject) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Worker (ID, FirstName, LastName, StartWorkingDate, Salary, BankAccount) VALUES (?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, dalObject.getID());
            pstmt.setString(2, dalObject.getFirstName());
            pstmt.setString(3, dalObject.getLastName());
            pstmt.setDate(4, Date.valueOf(dalObject.getStartWorkingDate()));
            pstmt.setDouble(5, dalObject.getSalary());
            pstmt.setInt(6, dalObject.getBankAccount());
            pstmt.executeUpdate();

            pstmt.close();
            stringMapper.put(dalObject.getID(), dalObject);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(WorkerDALDTO dalObject) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("UPDATE Worker SET FirstName = ?, LastName = ?, StartWorkingDate = ?, Salary = ?, BankAccount = ? WHERE ID = ?");
            pstmt.setString(1, dalObject.getFirstName());
            pstmt.setString(2, dalObject.getLastName());
            pstmt.setDate(3, Date.valueOf(dalObject.getStartWorkingDate()));
            pstmt.setDouble(4, dalObject.getSalary());
            pstmt.setInt(5, dalObject.getBankAccount());
            pstmt.setString(6, dalObject.getID());
            pstmt.executeUpdate();

            pstmt.close();
            stringMapper.replace(dalObject.getID(), dalObject);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(WorkerDALDTO dalObject) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("DELETE FROM Worker WHERE ID = ?");
            pstmt.setString(1, dalObject.getID());
            pstmt.executeUpdate();

            pstmt.close();
            stringMapper.remove(dalObject.getID());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public WorkerDALDTO loadByString(String id) {
        if (stringMapper.containsKey(id))
            return stringMapper.get(id);
        try (Connection conn = connect()) {
            WorkingTimes[] constrains;
            String firstName, lastName;
            LocalDate startWorkingDate;
            double salary;
            int bankAccount;
            List<Integer> authoritiesID = new ArrayList<>();
            List<String> terms = new ArrayList<>();

            PreparedStatement pstmt = conn.prepareStatement("SELECT FirstName, LastName, StartWorkingDate, Salary, BankAccount FROM Worker WHERE ID = ?");
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                firstName = rs.getString("FirstName");
                constrains = new WorkingTimes[7];
                lastName = rs.getString("LastName");
                startWorkingDate = rs.getDate("StartWorkingDate").toLocalDate();
                salary = rs.getDouble("Salary");
                bankAccount = rs.getInt("BankAccount");

                pstmt = conn.prepareStatement("SELECT AuthorityID FROM WorkerAuthority WHERE WorkerID = ?");
                pstmt.setString(1, id);
                rs = pstmt.executeQuery();
                while (rs.next())
                    authoritiesID.add(rs.getInt("AuthorityID"));

                pstmt = conn.prepareStatement("SELECT Day, Night, DayInWeek FROM WorkingTime WHERE WorkerID = ?");
                pstmt.setString(1, id);
                rs = pstmt.executeQuery();
                while (rs.next())
                    constrains[rs.getInt("DayInWeek") - 1] = getWorkingTimes(rs);

                pstmt = conn.prepareStatement("SELECT Description FROM TERM WHERE WorkerID = ?");
                pstmt.setString(1, id);
                rs = pstmt.executeQuery();
                while (rs.next())
                    terms.add(rs.getString("Description"));
            }
            else {
                pstmt.close();
                return null;
            }

            pstmt.close();
            return new WorkerDALDTO(id, firstName, lastName, startWorkingDate, salary, bankAccount, authoritiesID, constrains, terms);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    private WorkingTimes getWorkingTimes(ResultSet rs) {
        WorkingTimes times;
        try {
            if (rs.getBoolean("Day")) {
                if (rs.getBoolean("Night"))
                    times = WorkingTimes.both;
                else
                    times = WorkingTimes.day;
            }
            else {
                if (rs.getBoolean("Night"))
                    times = WorkingTimes.night;
                else
                    times = WorkingTimes.none;
            }
            return times;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public WorkerDALDTO loadByInt(int id) {
        //not in use in this class
        return null;
    }

    @Override
    public int loadAutoID() {
        //not in use in this class
        return 0;
    }

    @Override
    public List<WorkerDALDTO> loadAll() {
        List<WorkerDALDTO> dalWorkers = new ArrayList<>();
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID FROM Worker");
            while (rs.next()) {
                String ID = rs.getString("ID");
                if (stringMapper.containsKey(ID))
                    dalWorkers.add(stringMapper.get(ID));
                else {
                    WorkerDALDTO dalWorker = loadByString(rs.getString("ID"));
                    stringMapper.put(ID, dalWorker);
                    dalWorkers.add(dalWorker);
                }
            }
            stmt.close();
            return dalWorkers;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dalWorkers;
    }

    public void saveNewAuthority(String workerID, int authorityID) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO WorkerAuthority (WorkerID, AuthorityID) VALUES (?, ?)");
            pstmt.setString(1, workerID);
            pstmt.setInt(2, authorityID);
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveTerm(String description, String workerID) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Term (Description, WorkerID) VALUES (?, ?)");
            pstmt.setString(1, description);
            pstmt.setString(2, workerID);
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveWorkingTime(boolean day, boolean night, int dayInWeek, String workerID) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO WorkingTime (Day, Night, DayInWeek, WorkerID) VALUES (?, ?, ?, ?)");
            pstmt.setBoolean(1, day);
            pstmt.setBoolean(2, night);
            pstmt.setInt(3, dayInWeek);
            pstmt.setString(4, workerID);
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<WorkerDALDTO> loadDriversByLicense(int licenseID) {
        List<WorkerDALDTO> dalWorkers = new Vector<>();
        try (Connection conn = connect()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT WorkerID FROM WorkerAuthority WHERE AuthorityID >= ? AND AuthorityID <= 6");
            pstmt.setInt(1, licenseID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String workerID = rs.getString("WorkerID");
                dalWorkers.add(loadByString(workerID));
            }
            pstmt.close();
            return dalWorkers;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dalWorkers;
    }


}
