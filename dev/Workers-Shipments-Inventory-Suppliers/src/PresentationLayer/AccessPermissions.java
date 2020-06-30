package PresentationLayer;

public enum AccessPermissions {
    WORKERS_MANAGER("human"),
    SHIPMENTS_MANAGER("logistic"),
    PRODUCTS_MANAGER("inventory"),
    SUPPLIERS_MANAGER("store"),
    DEFAULT("default");

    private final String accessPermission;

    AccessPermissions(String accessPermission) {
        this.accessPermission = accessPermission;
    }

    public static AccessPermissions permissionFromStr(String permissionStr){
       switch (permissionStr.toLowerCase()){
           case("human"):
               return WORKERS_MANAGER;
           case("logistic"):
               return SHIPMENTS_MANAGER;
           case("inventory"):
               return PRODUCTS_MANAGER;
           case("store"):
               return SUPPLIERS_MANAGER;
           default:
               return DEFAULT;
       }
    }
    public String toString() {
        return accessPermission;
    }
}
