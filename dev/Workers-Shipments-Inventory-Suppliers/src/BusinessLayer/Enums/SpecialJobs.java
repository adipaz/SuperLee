package BusinessLayer.Enums;

public enum SpecialJobs {
    Manager(0),Cashier(1),StoreKeeper(2), Driver(3);

    private int numVal;

    SpecialJobs(int numVal) {
        this.numVal = numVal;
    }
    public int getNumVal() {
        return numVal;
    }
}
