package com.seah.specialsteel.tools;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// "C:\\jeongTest\\ResponseData_N29790.json"
// "C:\\jeongTest\\ResponseData_N29791.json"

public class CompareResult {
    ExtractJson ori;
    ExtractJson rev;
    ArrayList<Something> diffAlloyInputList;
    ArrayList<Something> diffMaterialList;

    public CompareResult() throws IOException, ParseException {
//        ori = new ExtractJson(oriFileName);
//        rev = new ExtractJson(revFileName);
    }
    
//    ArrayList<Something> compareAlloyInputs() {
//        diffAlloyInputList = new ArrayList<>();
//
//        for(String key : ori.getAlloyInputs().keySet()) {
//            if(rev.getAlloyInputs().get(key) != null) {
//                if (!ori.getAlloyInputs().get(key).equals(rev.getAlloyInputs().get(key))) {
//                    diffAlloyInputList.add(new Something(key, Double.parseDouble(ori.getAlloyInputs().get(key)) - Double.parseDouble(rev.getAlloyInputs().get(key))));
//                }
//            }
//        }
//
//        return  diffAlloyInputList;
//    }
//
//    ArrayList<Something> compareMaterials() {
//        diffMaterialList = new ArrayList<>();
//
//        for(String key : ori.getExpectMaterials().keySet()) {
//            if(rev.getExpectMaterials().get(key) != null) {
//                if (!ori.getExpectMaterials().get(key).equals(rev.getExpectMaterials().get(key))) {
//                    diffMaterialList.add(new Something(key, Double.parseDouble(ori.getExpectMaterials().get(key)) - Double.parseDouble(rev.getExpectMaterials().get(key))));
//                }
//            }
//        }
//
//        return diffMaterialList;
//    }

    String diffAlloyInputListToString() {
        String str = "";

        if(diffMaterialList != null) {
            for (Something s : diffAlloyInputList)
                str += s.name + " : " + s.diff + "\n";
        }

        return str;
    }

    String diffMaterialListToString() {
        String str = "";

        if(diffMaterialList != null) {
            for (Something s : diffMaterialList)
                str += s.name + " : " + s.diff + "\n";
        }

        return str;
    }


    /// @@@@@@@
    public void setModifiedFiles(List<String> modifiedFileNames) {
    }

    static class Something {
        private String name;
        private Double diff;

        Something(String name, Double diff) {
            this.name = name;
            this.diff = diff;
        }

    }

}
