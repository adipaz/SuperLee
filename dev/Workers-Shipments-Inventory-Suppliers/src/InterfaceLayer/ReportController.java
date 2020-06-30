package InterfaceLayer;

import BusinessLayer.SuppliersAndInventory.Report;
import BusinessLayer.SuppliersAndInventory.Store;
import BusinessLayer.SuppliersAndInventory.TYPE;
import InterfaceLayer.DTO.ReportDTO;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportController {

    private List<Report> Reports;

    public ReportController() {
        Reports = new LinkedList<>();
    }

    public ReportDTO createSupplyReport(List<String> categories, int branch) throws SQLException {

        Report rep = Report.createSupplyReport(categories,branch);
        Reports.add(rep);
        ReportDTO repDTO= createDTO(rep);
        Store.getInstance().saveReport(repDTO);
        return repDTO;

    }


    public ReportDTO createFlawReport( int branch) throws SQLException {
        Report rep = Report.createFlawReport(branch);
        Reports.add(rep);
        ReportDTO repDTO= createDTO(rep);
        Store.getInstance().saveReport(repDTO);

        return repDTO;

    }

    public ReportDTO createOrderReport(int branch) throws SQLException {

        Report rep = Report.createOrderReport(branch);
        Reports.add(rep);
        ReportDTO repDTO= createDTO(rep);
        Store.getInstance().saveReport(repDTO);
        return repDTO;


    }

    private ReportDTO createDTO(Report rep) {
        String date=rep.getCreationTime().toString();
        String type;
        if(rep.getType()== TYPE.FLAW_REPORT)
            type="FLAW REPORT";
        else if(rep.getType()==TYPE.ORDER_REPORT)
            type="ORDER REPORT";
        else
            type="SUPPLY REPORT";
        return new ReportDTO(date,type,rep.getDescription(),rep.getAddress());
    }


    private String addSpaces(int total, String str, int strLength, int branch) {
        int spaces=total+12-strLength;
        String ans = str;
        for(int i=0;i<spaces%4 ; i++){
            ans+=" ";
        }
        for(int i=0;i<spaces/4 ; i++){
            ans+="\t";
        }

        return ans;
    }

    public String getAllReports() throws SQLException {
        return Store.getInstance().getAllReports();
    }
}
