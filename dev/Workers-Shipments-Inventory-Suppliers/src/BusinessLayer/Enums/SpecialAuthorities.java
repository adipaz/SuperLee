package BusinessLayer.Enums;

public enum SpecialAuthorities {
    ManageTeam(0), Cancel_card_holder(1),Register_operator(2),
    LicenseA(3),LicenseB(4),LicenseC(5),LicenseD(6),
    License(7),StoreKeeping(8),CashRegisterOperator(9);

    private int numVal;

    SpecialAuthorities(int numVal) {
        this.numVal = numVal;
    }
    public int getNumVal() {
        return numVal;
    }
}

//TODO : use the special authorities