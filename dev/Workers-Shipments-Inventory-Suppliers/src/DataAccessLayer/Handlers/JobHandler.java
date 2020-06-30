package DataAccessLayer.Handlers;

import DataAccessLayer.DALDTO.JobDALDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobHandler extends DataHandler<JobDALDTO> {
    private static JobHandler instance = null;

    private JobHandler() {
        super();
    }

    public static JobHandler getInstance() {
        if (instance == null)
            instance = new JobHandler();
        return instance;
    }

    @Override
    public void save(JobDALDTO dalObject) {
        PreparedStatement pstmt = null;
        try (Connection conn = connect()) {
            pstmt = conn.prepareStatement("INSERT INTO Job (ID, Title) VALUES (?, ?)");
            pstmt.setInt(1, dalObject.getID());
            pstmt.setString(2, dalObject.getName());
            pstmt.executeUpdate();

            pstmt.close();
            intMapper.put(dalObject.getID(), dalObject);
            stringMapper.put(dalObject.getName(), dalObject);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(JobDALDTO dalObject) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("UPDATE Job SET Title = ? WHERE ID = ?");
            pstmt.setString(1, dalObject.getName());
            pstmt.setInt(2, dalObject.getID());
            pstmt.executeUpdate();

            pstmt.close();
            intMapper.replace(dalObject.getID(), dalObject);
            stringMapper.replace(dalObject.getName(), dalObject);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(JobDALDTO dalObject) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("DELETE FROM Job WHERE ID = ?");
            pstmt.setInt(1, dalObject.getID());
            pstmt.executeUpdate();

            pstmt.close();
            intMapper.remove(dalObject.getID());
            stringMapper.remove(dalObject.getName());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public JobDALDTO loadByString(String id) {
        if (stringMapper.containsKey(id))
            return stringMapper.get(id);
        JobDALDTO dalJob = null;
        try (Connection conn = connect()) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("SELECT ID FROM Job WHERE Title = ?");
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dalJob = new JobDALDTO(rs.getInt("ID"), id);
                intMapper.put(dalJob.getID(), dalJob);
                stringMapper.put(id, dalJob);
            }

            pstmt.close();
            return dalJob;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public JobDALDTO loadByInt(int id) {
        if (intMapper.containsKey(id))
            return intMapper.get(id);
        JobDALDTO dalJob = null;
        try (Connection conn = connect()) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("SELECT Title FROM Job WHERE ID = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dalJob = new JobDALDTO(id, rs.getString("Title"));
                intMapper.put(id, dalJob);
                stringMapper.put(dalJob.getName(), dalJob);
            }

            pstmt.close();
            return dalJob;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void saveNewAuthority(int jobID, int authorityID) {
        PreparedStatement pstmt = null;
        try (Connection conn = connect()) {
            pstmt = conn.prepareStatement("INSERT INTO AuthoritiesForJobs (AuthorityID, JobID) VALUES (?, ?)");
            pstmt.setInt(1, authorityID);
            pstmt.setInt(2, jobID);
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public int loadAutoID() {
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count (ID) AS MaxID FROM Job");
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
    public List<JobDALDTO> loadAll() {
        List<JobDALDTO> dalJobs = new ArrayList<>();
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID FROM Job");
            while (rs.next()) {
                int ID = rs.getInt("ID");
                JobDALDTO dalJob = loadByInt(ID);
                if (!intMapper.containsKey(ID)) {
                    intMapper.put(ID, dalJob);
                    stringMapper.put(dalJob.getName(), dalJob);
                }
                dalJobs.add(dalJob);
            }
            stmt.close();
            return dalJobs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dalJobs;
    }
}
