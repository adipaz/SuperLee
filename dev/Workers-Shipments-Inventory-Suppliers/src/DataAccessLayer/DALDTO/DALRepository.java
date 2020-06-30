package DataAccessLayer.DALDTO;


import java.util.List;
import java.util.Map;

public class DALRepository {
    private List<DALSupplier> dalSupplierList;
    private Map<String,DALOrder> dalOrderMap;

    public DALRepository(List<DALSupplier> dalSupplierList, Map<String, DALOrder> dalOrderMap) {
        this.dalSupplierList = dalSupplierList;
        this.dalOrderMap = dalOrderMap;
    }

    public List<DALSupplier> getDalSupplierList() {
        return dalSupplierList;
    }

    public void setDalSupplierList(List<DALSupplier> dalSupplierList) {
        this.dalSupplierList = dalSupplierList;
    }

    public Map<String, DALOrder> getDalOrderMap() {
        return dalOrderMap;
    }

    public void setDalOrderMap(Map<String, DALOrder> dalOrderMap) {
        this.dalOrderMap = dalOrderMap;
    }
}
