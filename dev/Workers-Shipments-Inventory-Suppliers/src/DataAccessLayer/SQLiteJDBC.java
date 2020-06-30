package DataAccessLayer;


import DataAccessLayer.DALDTO.*;
import DataAccessLayer.DALDTO.DALContact;
import DataAccessLayer.DALDTO.DALOrder;
import DataAccessLayer.DALDTO.DALProductS;
import DataAccessLayer.DALDTO.DALSupplier;
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.List;

public class SQLiteJDBC {


    public static void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:../dev/Workers-Shipments-Inventory-Suppliers/data/" + fileName;
        //String url = "jdbc:sqlite:dev/Workers-Shipments-Inventory-Suppliers/data/" + fileName;

        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            Connection conn = DriverManager.getConnection(url, config.toProperties());


            //Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();

                createNewTables();
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTables() {
        // SQLite connection string
        String url = "jdbc:sqlite:../dev/Workers-Shipments-Inventory-Suppliers/data/SuperLee8.db";

        //String url = "jdbc:sqlite:dev/Workers-Shipments-Inventory-Suppliers/data/SuperLee8.db";

        // SQL statement for creating a new table

        String sql = "CREATE TABLE IF NOT EXISTS \"product\" (\n" +
                "\t\"catalogID\"\tTEXT NOT NULL UNIQUE,\n" +
                "\t\"name\"\tTEXT NOT NULL,\n" +
                "\t\"weight\"\tINTEGER NOT NULL CHECK(Weight>0),\n" +
                "\tPRIMARY KEY(\"catalogID\")\n" +
                ")";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"orderCounter\" (\n" +
                "\t\"counter\"\tINTEGER NOT NULL DEFAULT 1\n" +
                ");";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

         sql = "CREATE TABLE IF NOT EXISTS productExternalInfo (\n"
                + " branch integer REFERENCES productInternalInfo(\"branch\"),\n"
                + " catalogID text NOT NULL REFERENCES productInternalInfo(\"catalogID\"),\n"
                + " quantity integer NOT NULL,\n"
                + " minQuantity integer NOT NULL,\n"
                + " counter integer NOT NULL,\n"
                + " PRIMARY KEY('catalogID','branch')\n"
                + ");";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        sql = "CREATE TABLE IF NOT EXISTS categoryToCatalogID  (\n"
                + " branch integer,\n"
                + " category text NOT NULL,\n"
                + " catalogID text NOT NULL REFERENCES productInternalInfo(\"catalogID\"),\n"
                + " PRIMARY KEY('category','branch','catalogID')\n"
                + ");";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        sql = "CREATE TABLE IF NOT EXISTS productInternalInfo (\n"
                + " branch integer,\n"
                + " catalogID text NOT NULL,\n"
                + " name text NOT NULL,\n"
                + " sellPrice text NOT NULL,\n"
                + " buyPrice text NOT NULL,\n"
                + " manufacturer text NOT NULL,\n"
                + " category text NOT NULL,\n"
                + " subCategory text NOT NULL,\n"
                + " subSubCategory text NOT NULL,\n"
                + " weight text NOT NULL,\n"
                + " id text NOT NULL,\n"
                + " location text NOT NULL,\n"
                + " isFlaw text NOT NULL,\n"
                + " year text NOT NULL,\n"
                + " month text NOT NULL,\n"
                + " day text NOT NULL,\n"
                + " discount text NOT NULL,\n"
                + " PRIMARY KEY('catalogID','branch','id')\n"
                + ");";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        sql = "CREATE TABLE IF NOT EXISTS \"Supplier\" (\n" +
                "\t\"bnNumber\"\tTEXT NOT NULL,\n" +
                "\t\"name\"\tTEXT NOT NULL,\n" +
                "\t\"bankAccountNumber\"\tTEXT NOT NULL,\n" +
                "\t\"paymentMethod\"\tTEXT NOT NULL,\n" +
                "\t\"address\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"bnNumber\")\n" +
                ");";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        sql = "CREATE TABLE IF NOT EXISTS \"Contact\" (\n" +
                "\t\"name\"\tTEXT NOT NULL,\n" +
                "\t\"email\"\tTEXT NOT NULL,\n" +
                "\t\"phone\"\tTEXT NOT NULL,\n" +
                "\t\"bnNumber\"\tTEXT NOT NULL,\n" +
                "\tFOREIGN KEY(\"bnNumber\") REFERENCES \"Supplier\"(\"bnNumber\") ON UPDATE CASCADE ON DELETE CASCADE\n" +
                ");";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Orders\" (\n" +
                "\t\"orderID\"\tTEXT NOT NULL,\n" +
                "\t\"bnNumber\"\tTEXT NOT NULL,\n" +
                "\t\"name\"\tTEXT NOT NULL,\n" +
                "\t\"address\"\tTEXT NOT NULL,\n" +
                "\t\"orderDate\"\tTEXT NOT NULL,\n" +
                "\t\"phone\"\tTEXT NOT NULL,\n" +
                "\tFOREIGN KEY(\"bnNumber\") REFERENCES \"Supplier\"(\"bnNumber\") ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"orderID\")\n" +
                ");";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Contract\" (\n" +
                "\t\"bnNumber\"\tTEXT NOT NULL,\n" +
                "\t\"xDiscount\"\tTEXT NOT NULL,\n" +
                "\t\"sunday\"\tTEXT DEFAULT '0',\n" +
                "\t\"monday\"\tTEXT DEFAULT '0',\n" +
                "\t\"tuesday\"\tTEXT DEFAULT '0',\n" +
                "\t\"wensday\"\tTEXT DEFAULT '0',\n" +
                "\t\"thursday\"\tTEXT DEFAULT '0',\n" +
                "\t\"friday\"\tTEXT DEFAULT '0',\n" +
                "\t\"saturday\"\tTEXT DEFAULT '0',\n" +
                "\tFOREIGN KEY(\"bnNumber\") REFERENCES \"Supplier\"(\"bnNumber\") ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"bnNumber\")\n" +
                ");";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"productInContract\" (\n" +
                "\t\"catalogID\"\ttext NOT NULL,\n" +
                "\t\"name\"\ttext NOT NULL,\n" +
                "\t\"price\"\ttext NOT NULL,\n" +
                "\t\"weight\"\tINTEGER NOT NULL CHECK(Weight>0),\n"+
                "\t\"bnNumber\"\ttext NOT NULL,\n" +
                "\tPRIMARY KEY(\"catalogID\",\"bnNumber\"),\n" +
                "\tFOREIGN KEY(\"bnNumber\") REFERENCES \"Supplier\"(\"bnNumber\") ON UPDATE CASCADE ON DELETE CASCADE\n" +
                ");";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        sql = "CREATE TABLE IF NOT EXISTS \"productInOrder\" (\n" +
                "\t\"orderID\"\tTEXT NOT NULL,\n" +
                "\t\"catalogID\"\tTEXT NOT NULL,\n" +
                "\t\"quantity\"\tTEXT NOT NULL,\n" +
                "\t\"bnNumber\"\tTEXT NOT NULL,\n" +
                "\tFOREIGN KEY(\"catalogID\",\"bnNumber\") REFERENCES \"productInContract\"(\"catalogID\",\"bnNumber\") ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"orderID\") REFERENCES \"Order\"(\"orderID\") ON UPDATE CASCADE ON DELETE CASCADE\n" +
                ");";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }



        sql = "CREATE TABLE IF NOT EXISTS \"InventoryReports\" (\n" +
                "\t\"type\"\ttext NOT NULL,\n" +
                "\t\"creationTime\"\ttext NOT NULL,\n" +
                "\t\"address\"\ttext NOT NULL,\n" +
                "\t\"description\"\ttext NOT NULL,\n" +
                "\tPRIMARY KEY(\"creationTime\",\"type\",\"address\")\n" +
                ");";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"AuthoritiesForJobs\" (\n" +
                "\t\"AuthorityID\"\tINTEGER NOT NULL,\n" +
                "\t\"JobID\"\tINTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(\"AuthorityID\") REFERENCES \"Authority\"(\"ID\") ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"AuthorityID\",\"JobID\"),\n" +
                "\tFOREIGN KEY(\"JobID\") REFERENCES \"Job\"(\"ID\") ON DELETE CASCADE\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Authority\" (\n" +
                "\t\"ID\"\tINTEGER NOT NULL UNIQUE,\n" +
                "\t\"Title\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"ID\")\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Branch\" (\n" +
                "\t\"Address\"\tTEXT NOT NULL UNIQUE,\n" +
                "\tPRIMARY KEY(\"Address\")\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Certificate\" (\n" +
                "\t\"ID\"\tINTEGER NOT NULL UNIQUE,\n" +
                "\t\"ShipmentID\"\tINTEGER NOT NULL,\n" +
                "\t\"BranchAddress\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"ID\"),\n" +
                "\tFOREIGN KEY(\"ShipmentID\") REFERENCES \"Shipment\"(\"ID\") ON DELETE NO ACTION,\n" +
                "\tFOREIGN KEY(\"BranchAddress\") REFERENCES \"Branch\"(\"Address\") ON DELETE NO ACTION\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Job\" (\n" +
                "\t\"ID\"\tINTEGER NOT NULL UNIQUE,\n" +
                "\t\"Title\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"ID\")\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"JobAllocationsInShift\" (\n" +
                "\t\"ShiftID\"\tINTEGER NOT NULL,\n" +
                "\t\"JobID\"\tINTEGER NOT NULL,\n" +
                "\t\"AllocationsNum\"\tINTEGER NOT NULL DEFAULT 0,\n" +
                "\tPRIMARY KEY(\"ShiftID\",\"JobID\"),\n" +
                "\tFOREIGN KEY(\"ShiftID\") REFERENCES \"Shift\"(\"ID\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"JobID\") REFERENCES \"Job\"(\"ID\") ON DELETE CASCADE\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"ProductsOfCertificate\" (\n" +
                "\t\"CertificateID\"\tINTEGER NOT NULL,\n" +
                "\t\"CatalogNumber\"\tTEXT NOT NULL,\n" +
                "\t\"Quantity\"\tINTEGER NOT NULL CHECK(Quantity>0),\n" +
                "\tFOREIGN KEY(\"CertificateID\") REFERENCES \"Certificate\"(\"ID\") ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"CertificateID\",\"CatalogNumber\")\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Shift\" (\n" +
                "\t\"ID\"\tINTEGER NOT NULL UNIQUE,\n" +
                "\t\"Date\"\tDATE NOT NULL,\n" +
                "\t\"isDay\"\tBOOLEAN NOT NULL,\n" +
                "\t\"Ready\"\tBOOLEAN NOT NULL,\n" +
                "\t\"BranchAddress\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"ID\"),\n" +
                "\tFOREIGN KEY(\"BranchAddress\") REFERENCES \"Branch\"(\"Address\") ON DELETE NO ACTION\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Shipment\" (\n" +
                "\t\"ID\"\tINTEGER NOT NULL UNIQUE,\n" +
                "\t\"Date\"\tDATE NOT NULL,\n" +
                "\t\"DepartureTime\"\tTIME NOT NULL,\n" +
                "\t\"OverallWeight\"\tREAL NOT NULL,\n" +
                "\t\"TruckLicenseNumber\"\tTEXT NOT NULL,\n" +
                "\t\"SourceAddress\"\tTEXT NOT NULL,\n" +
                "\t\"ShiftID\"\tINTEGER NOT NULL,\n" +
                "\t\"DriverID\"\tTEXT NOT NULL,\n" +
                "\t\"Delivered\"\tBOOLEAN NOT NULL,\n" +
                "\tFOREIGN KEY(\"DriverID\") REFERENCES \"Worker\"(\"ID\") ON UPDATE CASCADE ON DELETE NO ACTION,\n" +
                "\tFOREIGN KEY(\"TruckLicenseNumber\") REFERENCES \"Truck\"(\"LicenseNumber\") ON DELETE NO ACTION,\n" +
                "\tPRIMARY KEY(\"ID\"),\n" +
                "\tFOREIGN KEY(\"ShiftID\") REFERENCES \"Shift\"(\"ID\") ON DELETE NO ACTION\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"ShipmentComments\" (\n" +
                "\t\"ID\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t\"Comment\"\tTEXT NOT NULL,\n" +
                "\t\"ShipmentID\"\tINTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(\"ShipmentID\") REFERENCES \"Shipment\"(\"ID\") ON DELETE CASCADE\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        sql = "CREATE TABLE IF NOT EXISTS \"Term\" (\n" +
                "\t\"Description\"\tTEXT NOT NULL,\n" +
                "\t\"WorkerID\"\tTEXT NOT NULL,\n" +
                "\tFOREIGN KEY(\"WorkerID\") REFERENCES \"Worker\"(\"ID\") ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"Description\",\"WorkerID\")\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Truck\" (\n" +
                "\t\"LicenseNumber\"\tTEXT NOT NULL UNIQUE,\n" +
                "\t\"Model\"\tTEXT NOT NULL,\n" +
                "\t\"TruckWeight\"\tREAL NOT NULL,\n" +
                "\t\"MaxCarryWeight\"\tREAL NOT NULL,\n" +
                "\tPRIMARY KEY(\"LicenseNumber\")\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Worker\" (\n" +
                "\t\"ID\"\tTEXT(9) NOT NULL UNIQUE,\n" +
                "\t\"FirstName\"\tTEXT NOT NULL,\n" +
                "\t\"LastName\"\tTEXT NOT NULL,\n" +
                "\t\"StartWorkingDate\"\tDATE NOT NULL,\n" +
                "\t\"Salary\"\tREAL NOT NULL,\n" +
                "\t\"BankAccount\"\tINTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"ID\")\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"WorkerAuthority\" (\n" +
                "\t\"WorkerID\"\tTEXT NOT NULL,\n" +
                "\t\"AuthorityID\"\tINTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"WorkerID\",\"AuthorityID\"),\n" +
                "\tFOREIGN KEY(\"AuthorityID\") REFERENCES \"Authority\"(\"ID\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"WorkerID\") REFERENCES \"Worker\"(\"ID\") ON DELETE CASCADE\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"WorkerJobInShift\" (\n" +
                "\t\"WorkerID\"\tTEXT,\n" +
                "\t\"JobID\"\tINTEGER NOT NULL,\n" +
                "\t\"ShiftID\"\tINTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(\"WorkerID\") REFERENCES \"Worker\"(\"ID\") ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"WorkerID\",\"JobID\",\"ShiftID\"),\n" +
                "\tFOREIGN KEY(\"JobID\") REFERENCES \"Job\"(\"ID\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"ShiftID\") REFERENCES \"Shift\"(\"ID\") ON DELETE CASCADE\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"WorkingTime\" (\n" +
                "\t\"Day\"\tBOOLEAN NOT NULL,\n" +
                "\t\"Night\"\tBOOLEAN NOT NULL,\n" +
                "\t\"DayInWeek\"\tINTEGER NOT NULL CHECK(DayInWeek >= 1 AND DayInWeek <= 7),\n" +
                "\t\"WorkerID\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"DayInWeek\",\"WorkerID\"),\n" +
                "\tFOREIGN KEY(\"WorkerID\") REFERENCES \"Worker\"(\"ID\") ON DELETE CASCADE\n" +
                ")";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    public void addNewProduct(int branch, DALProduct dal, int minQuan) {

        try {
            String sql = "INSERT INTO productExternalInfo(branch, catalogID,quantity,minQuantity,counter) VALUES(?,?,?,?,?)";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(3, 0);
            pstmt.setString(2, dal.getCatalogID());
            pstmt.setInt(1, branch);
            pstmt.setInt(4, minQuan);
            pstmt.setInt(5, 1);
            pstmt.executeUpdate();

            sql = "INSERT INTO categoryToCatalogID(branch, category,catalogID) VALUES(?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(3, dal.getCatalogID());
            pstmt.setString(2, dal.getCategory());
            pstmt.setInt(1, branch);
            pstmt.executeUpdate();

            sql = "INSERT INTO categoryToCatalogID(branch, category,catalogID) VALUES(?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(3, dal.getCatalogID());
            pstmt.setString(2, dal.getSubCategory());
            pstmt.setInt(1, branch);
            pstmt.executeUpdate();

            sql = "INSERT INTO categoryToCatalogID(branch, category,catalogID) VALUES(?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(3, dal.getCatalogID());
            pstmt.setString(2, dal.getSubSubCategory());
            pstmt.setInt(1, branch);
            pstmt.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:../dev/Workers-Shipments-Inventory-Suppliers/data/SuperLee8.db";

        //String url = "jdbc:sqlite:dev/Workers-Shipments-Inventory-Suppliers/data/SuperLee8.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public ResultSet selectAll(String tableName) {
        //createNewDatabase("nituz2.db");
        String sql = "SELECT * FROM " + tableName;

        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //conn.close();
            return rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ResultSet selectExternalInfoCol(String col) {
       // createNewDatabase("nituz2.db");
        String sql = "SELECT branch,catalogID," + col + " FROM productExternalInfo";

        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //conn.close();
            return rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //createNewDatabase("nituz2.db");
        //createNewTables();
        //SQLiteJDBC s = new SQLiteJDBC();
        //s.insert(1,"123b",3);
        //s.insert(2,"123a",2);
        //s.insert(2,"123b",6);
    }

    public void addProduct(DALProduct dal, int branch) {
        try {
            String sql = "INSERT INTO productInternalInfo(branch, catalogID,name,sellPrice,buyPrice,manufacturer,category,subCategory,subSubCategory" +
                    ",weight,id,location,isFlaw,year,month,day,discount) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, branch);
            pstmt.setString(2, dal.getCatalogID());
            pstmt.setString(3, dal.getName());
            pstmt.setString(4, dal.getSellPrice());
            pstmt.setString(5, dal.getBuyPrice());
            pstmt.setString(6, dal.getManufacturer());
            pstmt.setString(7, dal.getCategory());
            pstmt.setString(8, dal.getSubCategory());
            pstmt.setString(9, dal.getSubSubCategory());
            pstmt.setString(10, dal.getWeight());
            pstmt.setString(11, dal.getId());
            pstmt.setString(12, dal.getLocation());
            pstmt.setString(13, dal.getIsFlaw());
            pstmt.setString(14, dal.getYear());
            pstmt.setString(15, dal.getMonth());
            pstmt.setString(16, dal.getDay());
            pstmt.setString(17, dal.getDiscount());

            pstmt.executeUpdate();

            //conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateQuantity(int quantity, String catalogID, int branch) {
        try {
            String sql = "UPDATE productExternalInfo SET quantity = ? WHERE branch = ? AND catalogID = ?";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(2, branch);
            pstmt.setString(3, catalogID);
            pstmt.setInt(1, quantity);

            pstmt.executeUpdate();

            //conn.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void updateCounter(int startingId, String catalogId, int branch) {
        try {
            String sql = "UPDATE productExternalInfo SET counter = ? WHERE branch = ? AND catalogID = ?";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(2, branch);
            pstmt.setString(3, catalogId);
            pstmt.setInt(1, startingId);

            pstmt.executeUpdate();
            //conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeFromTables(boolean removeAll, List<Integer> idToDelete, int branch, String catalogId) {
        try {
            if (removeAll) {
                String sql = "DELETE FROM productInternalInfo WHERE branch = ? AND catalogID = ?";
                Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, branch);
                pstmt.setString(2, catalogId);
                pstmt.executeUpdate();
                sql = "DELETE FROM productExternalInfo WHERE branch = ? AND catalogID = ?";
                conn = this.connect();
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, branch);
                pstmt.setString(2, catalogId);
                pstmt.executeUpdate();
                sql = "DELETE FROM categoryToCatalogID WHERE branch = ? AND catalogID = ?";
                conn = this.connect();
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, branch);
                pstmt.setString(2, catalogId);
                pstmt.executeUpdate();
               // conn.close();
            } else {
                Connection conn = this.connect();
                for (Integer i : idToDelete) {
                    String sql = "DELETE FROM productInternalInfo WHERE branch = ? AND catalogID = ? AND id = ?";

                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, branch);
                    pstmt.setInt(3, (i));
                    pstmt.setString(2, catalogId);

                    pstmt.executeUpdate();

                }
                //conn.close();

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void ruinProducts(boolean ruinAll, List<Integer> idToRuin, int branch, String catalogId) {
        try {
            if (ruinAll) {
                String sql = "UPDATE productInternalInfo SET isFlaw = ? WHERE branch = ? AND catalogID = ?";
                Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "true");
                pstmt.setInt(2, branch);
                pstmt.setString(3, catalogId);
                pstmt.executeUpdate();
                //conn.close();

            } else {
                Connection conn = this.connect();
                for (Integer i : idToRuin) {
                    String sql = "UPDATE productInternalInfo SET isFlaw = ? WHERE branch = ? AND catalogID = ? AND id = ?";

                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, "true");
                    pstmt.setInt(2, branch);
                    pstmt.setString(3, catalogId);
                    pstmt.setInt(4, i);
                    pstmt.executeUpdate();

                }
                //conn.close();

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void moveProductsToShelf(boolean moveAll, List<Integer> idToMove, int branch, String catalogId) {
        try {
            if (moveAll) {
                String sql = "UPDATE productInternalInfo SET location = ? WHERE branch = ? AND catalogID = ?";
                Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "StoreShelf");
                pstmt.setInt(2, branch);
                pstmt.setString(3, catalogId);
                pstmt.executeUpdate();
                //conn.close();

            } else {
                Connection conn = this.connect();
                for (Integer i : idToMove) {
                    String sql = "UPDATE productInternalInfo SET location = ? WHERE branch = ? AND catalogID = ? AND id = ?";

                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, "StoreShelf");
                    pstmt.setInt(2, branch);
                    pstmt.setString(3, catalogId);
                    pstmt.setInt(4, i);
                    pstmt.executeUpdate();

                }
                //conn.close();

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isProductSupplied (String catalogId){
        String sql = "SELECT * FROM product WHERE catalogID = "+catalogId;

        try {
            Connection conn = this.connect();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            return rs.next()!=false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void discountProducts(int branch, String catalogId, int discount) {
        try {
            String sql = "UPDATE productInternalInfo SET discount = ? WHERE branch = ? AND catalogID = ?";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, discount);
            pstmt.setInt(2, branch);
            pstmt.setString(3, catalogId);
            pstmt.executeUpdate();

            //conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    //**************suppliers**************

    public void addNewSupplier(DALSupplier dalSupplier) {
        try {
            String sql = "INSERT INTO supplier(name,bnNumber,bankAccountNumber,paymentMethod,address) VALUES(?,?,?,?,?)";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dalSupplier.getName());
            pstmt.setString(2, dalSupplier.getBnNumber());
            pstmt.setString(3, dalSupplier.getBankAccountNumber());
            pstmt.setString(4, dalSupplier.getPaymentMethod());
            pstmt.setString(5 , dalSupplier.getAddress());
            pstmt.executeUpdate();

            sql = "INSERT INTO contract(bnNumber,xDiscount) VALUES(?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dalSupplier.getBnNumber());
            pstmt.setString(2, dalSupplier.getContract().getxDiscout());
            pstmt.executeUpdate();


            for (DALContact c:dalSupplier.getContacts()) {
                sql = "INSERT INTO contact(name,email,phone,bnNumber) VALUES(?,?,?,?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, c.getName());
                pstmt.setString(2, c.getEmail());
                pstmt.setString(3, c.getPhone());
                pstmt.setString(4, dalSupplier.getBnNumber());
                pstmt.executeUpdate();
            }


            conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addNewProduct(DataAccessLayer.DALDTO.DALProductS dalProduct, String bnNumber)//new product
    {
        try {
            String sql = "INSERT INTO productInContract(catalogID,name,price,weight,bnNumber) VALUES(?,?,?,?,?)";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dalProduct.getCatalogID());
            pstmt.setString(2,dalProduct.getName());
            pstmt.setString(3, dalProduct.getPrice());
            pstmt.setDouble(4,dalProduct.getWeight());
            pstmt.setString(5, bnNumber);
            pstmt.executeUpdate();

            sql = "INSERT INTO product(catalogID,name,weight) VALUES(?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dalProduct.getCatalogID());
            pstmt.setString(2, dalProduct.getName());
            pstmt.setDouble(3, dalProduct.getWeight());
            pstmt.executeUpdate();




            conn.close();
        }

        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void addPerOrder(DataAccessLayer.DALDTO.DALOrder dalOrder)
    {
        try {
            String sql = "INSERT INTO Orders(orderID,bnNumber,name,address,orderDate,phone) VALUES(?,?,?,?,?,?)";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dalOrder.getOrderID());
            pstmt.setString(2,dalOrder.getBnNumber());
            pstmt.setString(3, dalOrder.getSupplierName());
            pstmt.setString(4, dalOrder.getAdress());
            pstmt.setString(5,dalOrder.getDate());
            pstmt.setString(6,dalOrder.getPhone());
            pstmt.executeUpdate();

            for (String p:dalOrder.getProducts().keySet()) {
                sql = "INSERT INTO productInOrder(orderID,catalogID,quantity,bnNumber) VALUES(?,?,?,?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1,dalOrder.getOrderID());
                pstmt.setString(2,  dalOrder.getProducts().get(p).getFirst());
                pstmt.setString(3,  dalOrder.getProducts().get(p).getSecond());
                pstmt.setString(4, dalOrder.getBnNumber());
                pstmt.executeUpdate();
            }

            switch(Integer.parseInt(dalOrder.getDate())) {
                case 1:
                {
                    sql = "UPDATE Contract SET sunday = ? WHERE bnNumber = ? ";
                    break;
                }
                case 2:
                {
                    sql = "UPDATE Contract SET monday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 3:
                {
                    sql = "UPDATE Contract SET tuesday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 4:
                {
                    sql = "UPDATE Contract SET wensday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 5:
                {
                    sql = "UPDATE Contract SET thursday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 6:
                {
                    sql = "UPDATE Contract SET friday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 7:
                {
                    sql = "UPDATE Contract SET saturday = ? WHERE bnNumber = ? ";
                    break;
                }

            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,dalOrder.getOrderID());
            pstmt.setString(2,  dalOrder.getBnNumber());
            pstmt.executeUpdate();

            conn.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addExistProduct(DALProductS dalProductS, String bnNumber) {
        try {
            String sql = "INSERT INTO productInContract(catalogID,name,price,weight,bnNumber) VALUES(?,?,?,?,?)";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dalProductS.getCatalogID());
            pstmt.setString(2,dalProductS.getName());
            pstmt.setString(3, dalProductS.getPrice());
            pstmt.setDouble(4,dalProductS.getWeight());
            pstmt.setString(5, bnNumber);
            pstmt.executeUpdate();

            conn.close();
        }

        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateOrderCounter(int newval)
    {
        try {
            String sql = "UPDATE orderCounter SET counter = ? WHERE counter = ? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(newval));
            pstmt.setString(2, String.valueOf(newval-1));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSupplierName(String bnNumber, String newName) {
        try {
            String sql = "UPDATE Supplier SET name = ? WHERE bnNumber = ? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newName);
            pstmt.setString(2,bnNumber);
            pstmt.executeUpdate();

            sql = "UPDATE Orders SET name = ? WHERE bnNumber = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newName);
            pstmt.setString(2,bnNumber);
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateSupplierBnNumber(String oldBnNumber, String newBnNumber) {
        try {
            String sql = "UPDATE Supplier SET bnNumber = ? WHERE bnNumber = ? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newBnNumber);
            pstmt.setString(2,oldBnNumber);
            pstmt.executeUpdate();

            sql = "UPDATE Contact SET bnNumber = ? WHERE bnNumber = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newBnNumber);
            pstmt.setString(2,oldBnNumber);
            pstmt.executeUpdate();

            sql = "UPDATE Contract SET bnNumber = ? WHERE bnNumber = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newBnNumber);
            pstmt.setString(2,oldBnNumber);
            pstmt.executeUpdate();

            sql = "UPDATE Orders SET bnNumber = ? WHERE bnNumber = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newBnNumber);
            pstmt.setString(2,oldBnNumber);
            pstmt.executeUpdate();

            sql = "UPDATE productInContract SET bnNumber = ? WHERE bnNumber = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newBnNumber);
            pstmt.setString(2,oldBnNumber);
            pstmt.executeUpdate();

            sql = "UPDATE productInOrder SET bnNumber = ? WHERE bnNumber = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newBnNumber);
            pstmt.setString(2,oldBnNumber);
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateSupplierBankAccountNumber(String bnNumber, String newBankAccountNumber) {
        try {
            String sql = "UPDATE Supplier SET bankAccountNumber = ? WHERE bnNumber = ? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newBankAccountNumber);
            pstmt.setString(2,bnNumber);
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePaymentMethod(String bnNumber, String newPaymentMethod) {
        try {
            String sql = "UPDATE Supplier SET paymentMethod = ? WHERE bnNumber = ? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newPaymentMethod);
            pstmt.setString(2,bnNumber);
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addNewContact(DataAccessLayer.DALDTO.DALContact dalContact, String bnNumber) {
        try {
            String sql = "INSERT INTO Contact(name,email,phone,bnNumber) VALUES(?,?,?,?)";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dalContact.getName());
            pstmt.setString(2,dalContact.getEmail());
            pstmt.setString(3, dalContact.getPhone());
            pstmt.setString(4, bnNumber);
            pstmt.executeUpdate();

            conn.close();
        }

        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateSupplierContact(DataAccessLayer.DALDTO.DALContact oldContact, DataAccessLayer.DALDTO.DALContact newContact, String bnNumber) {
        try {
            String sql = "UPDATE Contact SET name = ?,email=?,phone=? WHERE bnNumber = ? AND name=? AND email=? AND phone=? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newContact.getName());
            pstmt.setString(2,newContact.getEmail());
            pstmt.setString(3,newContact.getPhone());
            pstmt.setString(4,bnNumber);
            pstmt.setString(5,oldContact.getName());
            pstmt.setString(6,oldContact.getEmail());
            pstmt.setString(7,oldContact.getPhone());
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateMinDiscount(String bnNumber, String newDiscount) {
        try {
            String sql = "UPDATE Contract SET xDiscount = ? WHERE bnNumber = ? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newDiscount);
            pstmt.setString(2,bnNumber);
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePerOrderAddress(DataAccessLayer.DALDTO.DALOrder dalOrder, String newAddress)
    {
        try {
            String sql = "UPDATE Orders SET address = ? WHERE orderID = ? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newAddress);
            pstmt.setString(2,dalOrder.getOrderID());
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePerOrderDate(DataAccessLayer.DALDTO.DALOrder dalOrder, String newday, String oldday)
    {
        try {
            String sql = "UPDATE Orders SET orderDate = ? WHERE orderID = ? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newday);
            pstmt.setString(2,dalOrder.getOrderID());
            pstmt.executeUpdate();

            switch(Integer.parseInt(newday)) {
                case 1:
                {
                    sql = "UPDATE Contract SET sunday = ? WHERE bnNumber = ? ";
                    break;
                }
                case 2:
                {
                    sql = "UPDATE Contract SET monday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 3:
                {
                    sql = "UPDATE Contract SET tuesday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 4:
                {
                    sql = "UPDATE Contract SET wensday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 5:
                {
                    sql = "UPDATE Contract SET thursday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 6:
                {
                    sql = "UPDATE Contract SET friday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 7:
                {
                    sql = "UPDATE Contract SET saturday = ? WHERE bnNumber = ? ";
                    break;
                }

            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,dalOrder.getOrderID());
            pstmt.setString(2,dalOrder.getBnNumber());
            pstmt.executeUpdate();

            switch(Integer.parseInt(oldday)) {
                case 1:
                {
                    sql = "UPDATE Contract SET sunday = ? WHERE bnNumber = ? ";
                    break;
                }
                case 2:
                {
                    sql = "UPDATE Contract SET monday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 3:
                {
                    sql = "UPDATE Contract SET tuesday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 4:
                {
                    sql = "UPDATE Contract SET wensday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 5:
                {
                    sql = "UPDATE Contract SET thursday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 6:
                {
                    sql = "UPDATE Contract SET friday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 7:
                {
                    sql = "UPDATE Contract SET saturday = ? WHERE bnNumber = ? ";
                    break;
                }

            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,dalOrder.getOrderID());
            pstmt.setString(2,"0");
            pstmt.executeUpdate();


            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePerOrderPhoneNumber(DataAccessLayer.DALDTO.DALOrder dalOrder, String phone)
        {
        try {
            String sql = "UPDATE Orders SET address = ? WHERE orderID = ? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,phone);
            pstmt.setString(2,dalOrder.getOrderID());
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void deleteProductFromPerOrder(String orderID , String catalogID)
    {
        try {
            String sql = "DELETE FROM productInOrder WHERE orderID = ? AND catalogID = ?";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,orderID);
            pstmt.setString(2,catalogID);
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void updateProductPrice(String bnNumber, String catalogID, String newPrice) {
        try {
            String sql = "UPDATE productInContract SET price = ? WHERE bnNumber = ? AND catalogID=? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,newPrice);
            pstmt.setString(2,bnNumber);
            pstmt.setString(3,catalogID);
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void deleteSupplier(String bnNumber) {
        try {

            String sql = "DELETE FROM Contact WHERE bnNumber = ? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,bnNumber);
            pstmt.executeUpdate();

            sql = "DELETE FROM Contract WHERE bnNumber = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,bnNumber);
            pstmt.executeUpdate();

            sql = "DELETE FROM productInContract WHERE bnNumber = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,bnNumber);
            pstmt.executeUpdate();

            sql = "DELETE FROM productInOrder WHERE bnNumber = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,bnNumber);
            pstmt.executeUpdate();

            sql = "DELETE FROM Orders WHERE bnNumber = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,bnNumber);
            pstmt.executeUpdate();

            sql = "DELETE FROM Supplier WHERE bnNumber = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,bnNumber);
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteProductFromSystem(String catalogID) {
        try {
            String sql = "DELETE FROM Product WHERE catalogID = ? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,catalogID);
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addProductToPerOrder(String orderID, String catalogID,String quantity,String bnNumber)
    {
        try {
           // String sql = "INSERT INTO productInOrder(orderID,catalogID,quantity,bnNumber) VALUES(?,?,?,?)";
            String sql = "DELETE FROM productInorder WHERE catalogID = ? AND orderID = ?;";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, catalogID);
            pstmt.setString(2, orderID);
            pstmt.executeUpdate();

            String sql2 = "INSERT INTO productInOrder(orderID,catalogID,quantity,bnNumber) VALUES(?,?,?,?); ";
            pstmt = conn.prepareStatement(sql2);
            pstmt.setString(1, orderID);
            pstmt.setString(2, catalogID);
            pstmt.setString(3, quantity);
            pstmt.setString(4, bnNumber);
            pstmt.executeUpdate();

            conn.close();
        }

        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void deleteSupplierContact(String bnNumber, DataAccessLayer.DALDTO.DALContact dalContact) {
        try {
            String sql = "DELETE FROM Contact WHERE bnNumber = ? AND name= ? AND email=? AND phone= ? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,bnNumber);
            pstmt.setString(2,dalContact.getName());
            pstmt.setString(3,dalContact.getEmail());
            pstmt.setString(4,dalContact.getPhone());
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteProductFromContract(String bnNumber, String catalogID) {
        try {
            String sql = "DELETE FROM productInContract WHERE bnNumber = ? AND catalogID= ? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,bnNumber);
            pstmt.setString(2,catalogID);
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deletePermanentOrder(DALOrder order) {
        try {
            String sql = "DELETE FROM productInOrder WHERE orderID = ? ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,order.getOrderID());
            pstmt.executeUpdate();

            sql = "DELETE FROM Orders WHERE orderID = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,order.getOrderID());
            pstmt.executeUpdate();

            String sql3 = "UPDATE Contract SET sunday = ? WHERE bnNumber = ? ";
            switch(Integer.parseInt(order.getDate())) {
                case 1:
                {
                    break;
                }
                case 2:
                {
                    sql3 = "UPDATE Contract SET monday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 3:
                {
                    sql3 = "UPDATE Contract SET tuesday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 4:
                {
                    sql3 = "UPDATE Contract SET wensday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 5:
                {
                    sql3 = "UPDATE Contract SET thursday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 6:
                {
                    sql3 = "UPDATE Contract SET friday = ? WHERE bnNumber = ? ";
                    break;

                }
                case 7:
                {
                    sql3 = "UPDATE Contract SET saturday = ? WHERE bnNumber = ? ";
                    break;
                }

            }
            pstmt = conn.prepareStatement(sql3);
            pstmt.setString(1,order.getOrderID());
            pstmt.setString(2,  order.getBnNumber());
            pstmt.executeUpdate();


            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    public void insertCounter() {
        try {
            String sql = "INSERT INTO orderCounter(counter) VALUES(?)";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 1);
            pstmt.executeUpdate();

            conn.close();
        }

        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addNewReport(DALReport DALr) {
        try {
            String sql = "INSERT INTO InventoryReports(type, creationTime,address,description) VALUES(?,?,?,?)";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, DALr.getType());
            pstmt.setString(2, DALr.getCreationDate());
            pstmt.setString(3, DALr.getAddress());
            pstmt.setString(4, DALr.getDescription());

            pstmt.executeUpdate();

            //conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void updateShippmentProductInfo(int branch, String catalogIdToUpdate, String newCategory, String newSubCategory, String newSubSubCategory, String newManufacturer, int newSellPriceI, int newBuyPriceI) {
        try {
            String sql = "DELETE FROM categoryToCatalogID where catalogID = ?";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, catalogIdToUpdate);

            pstmt.executeUpdate();

            //conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            String sql = "INSERT INTO categoryToCatalogID(branch, category,catalogID) VALUES(?,?,?)";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, branch);
            pstmt.setString(2, newCategory);
            pstmt.setString(3, catalogIdToUpdate);

            pstmt.executeUpdate();

            //conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            String sql = "INSERT INTO categoryToCatalogID(branch, category,catalogID) VALUES(?,?,?)";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, branch);
            pstmt.setString(2, newSubCategory);
            pstmt.setString(3, catalogIdToUpdate);

            pstmt.executeUpdate();

            //conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            String sql = "INSERT INTO categoryToCatalogID(branch, category,catalogID) VALUES(?,?,?)";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, branch);
            pstmt.setString(2, newSubSubCategory);
            pstmt.setString(3, catalogIdToUpdate);

            pstmt.executeUpdate();

            //conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        try {
            String sql = "UPDATE productInternalInfo SET sellPrice = ? , buyPrice = ? ,manufacturer = ? , category = ? , subCategory = ? , subSubCategory = ?  " +
                    "WHERE catalogID= ?  ";
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,newSellPriceI);
            pstmt.setInt(2,newBuyPriceI);
            pstmt.setString(3,newManufacturer);
            pstmt.setString(4,newCategory);
            pstmt.setString(5,newSubCategory);
            pstmt.setString(6,newSubSubCategory);
            pstmt.setString(7,catalogIdToUpdate);

            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }



    }
}