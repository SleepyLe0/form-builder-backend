package project.formbuilderbackend.utils;

import java.util.*;

public class Converter {
    private static final String SEPARATOR = "$GYATT$";
    private static final String NESTED_SEPARATOR = "$SIGMAOHIO$";

    public static Map<String, Object> convertArrayToObject(List<Object> arr) {
        Map<String, Object> result = new HashMap<>();

        for (int i = 0; i < arr.size(); i++) {
            result.put(Integer.toString(i + 1), arr.get(i));
        }

        return result;
    }

    public static List<Object> convertObjectToArray(Map<String, Object> obj) {
        List<Object> result = new ArrayList<>(Collections.nCopies(obj.size(), null));

        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            int index = Integer.parseInt(entry.getKey()) - 1;
            result.set(index, entry.getValue());
        }

        return result;
    }

    public static List<Object> revertStringToArray(String str) {
        List<Object> result = new ArrayList<>();

        String[] mainParts = str.split(SEPARATOR);
        for (String part : mainParts) {
            if (part.contains(NESTED_SEPARATOR)) {
                result.add(Arrays.asList(part.split(NESTED_SEPARATOR)));
            } else {
                result.add(part);
            }
        }

        return result;
    }

    public static String convertArrayToString(List<Object> arr) {
        List<String> result = new ArrayList<>();

        for (Object element : arr) {
            if (element instanceof List) {
                result.add(String.join(NESTED_SEPARATOR, (List<String>) element));
            } else {
                result.add((String) element);
            }
        }

        return String.join(SEPARATOR, result);
    }
}

