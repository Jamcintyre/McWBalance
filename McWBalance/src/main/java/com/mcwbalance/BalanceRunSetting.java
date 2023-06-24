package com.mcwbalance;

public class BalanceRunSetting {
    
String name;
int duration;
DataClimate climate;
DataRunoffCoeff rc;

// Not Sure i need these, probably not for the solve since i can juts pass the ObjELMList and ObjTRNLists directly. 
String[] transferList = new String[ProjSetting.MAX_TRNS];
String[] runoffList = new String[ProjSetting.MAX_ELMS]; 
String[] directprecipList = new String[ProjSetting.MAX_ELMS];
String[] evaporationList = new String[ProjSetting.MAX_ELMS];
String[] lossList = new String[ProjSetting.MAX_ELMS];
String[] storageList = new String[ProjSetting.MAX_ELMS];

int ntransfer = 0;
int nrunoff = 0;
int ndirectprecip = 0;
int nevaporation = 0;
int nloss = 0;
int nstorage = 0;

int nwaterVol = 0;
int nsolidsVol = 0;
int ntotalVol = 0;


String[] runSolveOrdType; // can be TRN or ELM
int[] runSolveOrdIndex; //index for whichever TRN or ELM is being solved

BalanceRunSetting(String runName, int runduration){
    name = runName;
    duration = runduration;
}

public void BuildResultList(ObjELMList eLMList, ObjTRNList tRNList){
    transferList = new String[tRNList.count]; // resest back to no elements
    for (int i = 0; i < tRNList.count; i++){
        transferList[i] = tRNList.tRNs[i].objname;
    }
    
}



    
}
