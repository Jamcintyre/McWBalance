package com.mcwbalance;

public class BalanceRunSetting {

    String name;
    int duration;
    DataClimate climate;

    int[] runTRNSolveOrdIndex; //index for whichever TRN or ELM is being solved
    String[] runTRNSolveOrdType;
    BalanceRunSetting(String runName, int runduration) {
        name = runName;
        duration = runduration;
    }
    
    
    public void autoSetOrder(ObjTRNList tRNList, ObjELMList eLMList){
        int initSolveIndex[] = new int[ProjSetting.MAX_TRNS];
        String initSolveType[] = new String[ProjSetting.MAX_TRNS]; 
        int c = 0;
        int tr, el;
        
        boolean tRNCounted[] = new boolean[tRNList.count];
        for (int i = 0; i < tRNCounted.length; i++){
            tRNCounted[i] = false;
        }
        boolean eLMCounted[] = new boolean[eLMList.count];
        for (int i = 0; i < eLMCounted.length; i++){
            eLMCounted[i] = false;
        }
        // first pass sets any fixed flow rates
        for (int i = 0; i < eLMList.count; i ++){
            if(eLMList.eLMs[i].tailsTRN != -1){ // sets any tailings deposition to first solve order as it will be fixed
                tr = eLMList.eLMs[i].tailsTRN;
                initSolveIndex[c] = tr;
                initSolveType[c] = "SOLIDS";
                tRNCounted[tr] = true;
                c ++;
            }
            for(int j = 0; j < eLMList.eLMs[i].inflowFixedTRN.count; j++){ //loops through the fixed inflows
                // need to check if it has no output or is not a demand then can solve
                tr = eLMList.eLMs[i].inflowFixedTRN.getObjIndex(j);
                if (!tRNCounted[tr]) {
                    el = tRNList.tRNs[tr].outObjNumber;
                    if (el == -1 || eLMList.eLMs[el].outflowFixedTRN.getListIndex(tr) != -1) {
                        initSolveIndex[c] = tr;
                        initSolveType[c] = "FIXED";
                        tRNCounted[tr] = true;
                        c++;
                    }
                }
            }
            for(int j = 0; j < eLMList.eLMs[i].outflowFixedTRN.count; j++){ //loops through the fixed outflows
                // need to check if it has no output or is not a demand then can solve
                tr = eLMList.eLMs[i].outflowFixedTRN.getObjIndex(j);
                if (!tRNCounted[tr]) {
                    el = tRNList.tRNs[tr].outObjNumber;
                    if (el == -1 || eLMList.eLMs[el].inflowFixedTRN.getListIndex(tr) != -1) {
                        initSolveIndex[c] = tr;
                        initSolveType[c] = "FIXED";
                        tRNCounted[tr] = true;
                        c++;
                    }
                }
            }
        }
        // Second Pass for the on demand rates
        for (int i = 0; i < eLMList.count; i++) {
            for (int j = 0; j < eLMList.eLMs[i].inflowOnDemandTRN.count; j++) { //loops through the fixed inflows
                // need to check if it has no output or is not a demand then can solve
                tr = eLMList.eLMs[i].inflowOnDemandTRN.getObjIndex(j);
                if (!tRNCounted[tr]) {
                    initSolveIndex[c] = tr;
                    initSolveType[c] = "ON_DEMAND";
                    tRNCounted[tr] = true;
                    c++;
                }
            }
            for (int j = 0; j < eLMList.eLMs[i].outflowOnDemandTRN.count; j++) { //loops through the fixed outflows
                // need to check if it has no output or is not a demand then can solve
                tr = eLMList.eLMs[i].outflowOnDemandTRN.getObjIndex(j);
                if (!tRNCounted[tr]) {
                    initSolveIndex[c] = tr;
                    initSolveType[c] = "ON_DEMAND";
                    tRNCounted[tr] = true;
                    c++;

                }
            }
            tr = eLMList.eLMs[i].overflowTRN;
            if(tr != -1 && !tRNCounted[tr]){
                initSolveIndex[c] = tr;
                initSolveType[c] = "ON_DEMAND";
                tRNCounted[tr] = true;
                c++;
            }
            
        }
        
        runTRNSolveOrdIndex = new int[c];
        runTRNSolveOrdType = new String[c];
        for (int i = 0; i < c; i++){
            runTRNSolveOrdIndex[c] = initSolveIndex[c];
            runTRNSolveOrdType[c] = initSolveType[c];
        }
    }

}
