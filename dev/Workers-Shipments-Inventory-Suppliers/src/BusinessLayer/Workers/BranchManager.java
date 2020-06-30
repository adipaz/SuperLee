package BusinessLayer.Workers;

import DataAccessLayer.DALDTO.BranchDALDTO;
import DataAccessLayer.Handlers.BranchHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BranchManager {

    private Map<String, Branch> allBranches;
    private static BranchManager instance=null;
    private BranchHandler handler;

    public BranchManager() {
        this.handler = BranchHandler.getInstance();
        this.allBranches= new HashMap<>();
    }

    public static BranchManager getInstance(){
        if(instance==null)
            instance=new BranchManager();
        return instance;
    }

    public Map<String, Branch> getbranchMap()
    {
        handler.loadAll().forEach(branchDALDTO -> allBranches.put(branchDALDTO.getAddress(),new Branch(branchDALDTO)));

        return this.allBranches;
    }


    public List<Branch> getAllBranches() {
        List<BranchDALDTO> dalBranches = handler.loadAll();
        for (BranchDALDTO dalBranch : dalBranches) {
            if (!allBranches.containsKey(dalBranch.getAddress()))
                allBranches.put(dalBranch.getAddress(), new Branch(dalBranch));
        }
        return allBranches.values().stream().collect(Collectors.toList());
    }


    public char getAreaOfSite(String address) {
        return allBranches.get(address).getArea();
    }

    public BranchDALDTO getBranchByShift(int shiftId) {
        return handler.loadBranchByShift(shiftId);
    }

    public boolean isBranchExists(String address) {
        if(allBranches.containsKey(address))
            return true;
        BranchDALDTO dalBranch= handler.loadByString(address);
        if(dalBranch!=null) {
            allBranches.put(address, new Branch(dalBranch));
            return true;
        }
        return false;
    }

    public Branch getBranchByAddress(String address) {
        if(isBranchExists(address))
            return allBranches.get(address);
        return null;
    }

}
