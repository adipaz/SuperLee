package DataAccessLayer.Handlers;

import DataAccessLayer.DALDTO.AuthorityDALDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorityHandler extends DataHandler<AuthorityDALDTO> {
    private static AuthorityHandler instance = null;

    private AuthorityHandler() {
        super();
    }

    public static AuthorityHandler getInstance() {
        if (instance == null)
            instance = new AuthorityHandler();
        return instance;
    }

    @Override
    public void save(AuthorityDALDTO dalObject) {
        PreparedStatement pstmt = null;
        try (Connection conn = connect()) {
            pstmt = conn.prepareStatement("INSERT INTO Authority (ID, Title) VALUES (?, ?)");
            pstmt.setInt(1, dalObject.getID());
            pstmt.setString(2, dalObject.getName());
            pstmt.executeUpdate();

            intMapper.put(dalObject.getID(), dalObject);
            stringMapper.put(dalObject.getName(), dalObject);
            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(AuthorityDALDTO dalObject) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("UPDATE Authority SET Title = ? WHERE ID = ?");
            pstmt.setString(1, dalObject.getName());
            pstmt.setInt(2, dalObject.getID());
            pstmt.executeUpdate();

            intMapper.replace(dalObject.getID(), dalObject);
            stringMapper.replace(dalObject.getName(), dalObject);
            pstmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(AuthorityDALDTO dalObject) {
        try (Connection conn = connect()) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("DELETE FROM Authority WHERE ID = ?");
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
    public AuthorityDALDTO loadByString(String id) {
        if (stringMapper.containsKey(id))
            return stringMapper.get(id);
        AuthorityDALDTO dalAuthority = null;
        try (Connection conn = connect()) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("SELECT ID FROM Authority WHERE Title = ?");
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dalAuthority = new AuthorityDALDTO(rs.getInt("ID"), id);
                intMapper.put(dalAuthority.getID(), dalAuthority);
                stringMapper.put(id, dalAuthority);
            }

            pstmt.close();
            return dalAuthority;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public AuthorityDALDTO loadByInt(int id) {
        if (stringMapper.containsKey(id))
            return stringMapper.get(id);
        AuthorityDALDTO dalAuthority = null;
        try (Connection conn = connect()) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("SELECT Title FROM Authority WHERE ID = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dalAuthority = new AuthorityDALDTO(id, rs.getString("Title"));
                intMapper.put(id, dalAuthority);
                stringMapper.put(dalAuthority.getName(), dalAuthority);
            }

            pstmt.close();
            return dalAuthority;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<AuthorityDALDTO> loadJobsPerAuthorities(int jobID) {
        List<AuthorityDALDTO> dalAuthorities = new ArrayList<>();
        PreparedStatement pstmt = null;
        try (Connection conn = connect()) {
            pstmt = conn.prepareStatement("SELECT AuthorityID, Title FROM AuthoritiesForJobs JOIN Authority ON Authority.ID = AuthoritiesForJobs.AuthorityID WHERE JobID = ?");
            pstmt.setInt(1, jobID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int authorityID = rs.getInt("AuthorityID");
                String title = rs.getString("Title");
                if (intMapper.containsKey(authorityID))
                    dalAuthorities.add(intMapper.get(authorityID));
                else {
                    AuthorityDALDTO dalAuthority = new AuthorityDALDTO(authorityID, title);
                    dalAuthorities.add(dalAuthority);
                    intMapper.put(authorityID, dalAuthority);
                    stringMapper.put(title, dalAuthority);
                }
            }
            pstmt.close();
            return dalAuthorities;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return dalAuthorities;
    }

    public int loadAutoID() {
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count (ID) AS MaxID FROM Authority");
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
    public List<AuthorityDALDTO> loadAll() {
        List<AuthorityDALDTO> dalAuthorities = new ArrayList<>();
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID FROM Authority");
            while (rs.next()) {
                int ID = rs.getInt("ID");
                AuthorityDALDTO dalAuthority = loadByInt(ID);
                if (!intMapper.containsKey(ID)) {
                    intMapper.put(ID, dalAuthority);
                    stringMapper.put(dalAuthority.getName(), dalAuthority);
                }
                dalAuthorities.add(dalAuthority);
            }
            stmt.close();
            return dalAuthorities;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dalAuthorities;
    }
}
