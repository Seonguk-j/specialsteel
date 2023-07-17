package com.seah.specialsteel.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize
public class CompareDTO {
    ResultDTO ori;
    ResultDTO rev;
    public ArrayList<Ingredient> diffAlloyInputList;
    public ArrayList<Ingredient> diffMaterialList;

    public CompareDTO(ResultDTO ori, ResultDTO rev) {
        this.ori = ori;
        this.rev = rev;
        compareAlloyInputs();
        compareMaterials();
    }
    
    ArrayList<Ingredient> compareAlloyInputs() {
        diffAlloyInputList = new ArrayList<>();

        for(String key : ori.getAlloyInputs().keySet()) {
            if(rev.getAlloyInputs().containsKey(key)) {
                if (!ori.getAlloyInputs().get(key).equals(rev.getAlloyInputs().get(key))) {
                    diffAlloyInputList.add(new Ingredient(key, Double.parseDouble(ori.getAlloyInputs().get(key)) - Double.parseDouble(rev.getAlloyInputs().get(key))));
                }
            }
            else {
                diffAlloyInputList.add(new Ingredient(key, Double.parseDouble(ori.getAlloyInputs().get(key))));
            }
        }
        for(String key : rev.getAlloyInputs().keySet()) {
            if(!ori.getAlloyInputs().containsKey(key)) {
                diffAlloyInputList.add(new Ingredient(key, Double.parseDouble(rev.getAlloyInputs().get(key))));
            }
        }

        return  diffAlloyInputList;
    }

    ArrayList<Ingredient> compareMaterials() {
        diffMaterialList = new ArrayList<>();

        for(String key : ori.getExpectMaterials().keySet()) {
            if(rev.getExpectMaterials().containsKey(key)) {
                if (!ori.getExpectMaterials().get(key).equals(rev.getExpectMaterials().get(key))) {
                    diffMaterialList.add(new Ingredient(key, Double.parseDouble(ori.getExpectMaterials().get(key)) - Double.parseDouble(rev.getExpectMaterials().get(key))));
                }
            }
            else {
                diffMaterialList.add(new Ingredient(key, Double.parseDouble(ori.getExpectMaterials().get(key))));
            }
        }
        for(String key : rev.getExpectMaterials().keySet()) {
            if(!ori.getExpectMaterials().containsKey(key)) {
                diffMaterialList.add(new Ingredient(key, Double.parseDouble(rev.getExpectMaterials().get(key))));
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

    public static class Ingredient {
        private String name;
        private Double diff;

        public String getName() {
            return name;
        }

        public Double getDiff() {
            return diff;
        }

        Ingredient(String name, Double diff) {
            this.name = name;
            this.diff = diff;
        }

    }

}
