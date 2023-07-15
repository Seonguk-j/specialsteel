package com.seah.specialsteel.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

// "C:\\jeongTest\\ResponseData_N29790.json"
// "C:\\jeongTest\\ResponseData_N29791.json"

@JsonSerialize
public class CompareDTO {
    ResultDTO ori;
    ResultDTO rev;
    ArrayList<Ingredient> diffAlloyInputList;
    ArrayList<Ingredient> diffMaterialList;

    public CompareDTO(ResultDTO ori, ResultDTO rev) {
        this.ori = ori;
        this.rev = rev;
        compareAlloyInputs();
        compareMaterials();
    }
    
    ArrayList<Ingredient> compareAlloyInputs() {
        diffAlloyInputList = new ArrayList<>();

        for(String key : ori.getAlloyInputs().keySet()) {
            if(rev.getAlloyInputs().get(key) != null) {
                if (!ori.getAlloyInputs().get(key).equals(rev.getAlloyInputs().get(key))) {
                    diffAlloyInputList.add(new Ingredient(key, Double.parseDouble(ori.getAlloyInputs().get(key)) - Double.parseDouble(rev.getAlloyInputs().get(key))));
                }
            }
        }

        return  diffAlloyInputList;
    }

    ArrayList<Ingredient> compareMaterials() {
        diffMaterialList = new ArrayList<>();

        for(String key : ori.getExpectMaterials().keySet()) {
            if(rev.getExpectMaterials().get(key) != null) {
                if (!ori.getExpectMaterials().get(key).equals(rev.getExpectMaterials().get(key))) {
                    diffMaterialList.add(new Ingredient(key, Double.parseDouble(ori.getExpectMaterials().get(key)) - Double.parseDouble(rev.getExpectMaterials().get(key))));
                }
            }
        }

        return diffMaterialList;
    }

    public String diffAlloyInputListToString() {
        String str = "";

        if(diffAlloyInputList != null) {
            for (Ingredient s : diffAlloyInputList)
                str += s.name + " : " + s.diff + "\n";
        }

        return str;
    }

    public String diffMaterialListToString() {
        String str = "";

        if(diffMaterialList != null) {
            for (Ingredient s : diffMaterialList)
                str += ",\n['" + s.name + "', '" + s.diff + "']";
        }

        return str;
    }


    /// @@@@@@@
    public void setModifiedFiles(List<String> modifiedFileNames) {
    }

    static class Ingredient {
        private String name;
        private Double diff;

        Ingredient(String name, Double diff) {
            this.name = name;
            this.diff = diff;
        }

    }

}
