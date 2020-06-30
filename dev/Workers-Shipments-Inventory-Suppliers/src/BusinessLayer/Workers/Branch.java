package BusinessLayer.Workers;

import DataAccessLayer.DALDTO.BranchDALDTO;

public class Branch {
    private String branchAddress;
    char area;

    public Branch(BranchDALDTO branch) {
        this.branchAddress=branch.getAddress();
        this.area = branch.getArea();
    }

    public char getArea() {
        return area;
    }

    public Branch(String address, char area){
        this.branchAddress=address;
        this.area = area;
    }

    public String getAddress() {
        return this.branchAddress;
    }
}
