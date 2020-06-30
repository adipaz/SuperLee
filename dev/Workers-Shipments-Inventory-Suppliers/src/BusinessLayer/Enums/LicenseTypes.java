package BusinessLayer.Enums;

public enum LicenseTypes {
    A(3000),B(4000),C(5000),D(6000);

    private int numVal;

    LicenseTypes(int numVal) {
        this.numVal = numVal;
    }
    public int getNumVal() {
        return numVal;
    }
}
