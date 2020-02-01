package com.mak.pos.Utility;

import java.util.HashMap;
import java.util.Map;

public class ApplicationObjectSharing {

    private static Map<Integer, Boolean> tableBillStatusMap = new HashMap<>();

    public static void billGenerateForTable(Integer tableId) {
        tableBillStatusMap.put(tableId, true);
    }

    public static void resetFlagForTable(Integer tableId) {
        tableBillStatusMap.put(tableId, false);
    }

    public static boolean getBillGenerateForTable(Integer tableId) {
        if (tableBillStatusMap.containsKey(tableId)) {
            return tableBillStatusMap.get(tableId);
        } else {
            return false;
        }

    }
}
