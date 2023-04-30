package org.digdes.school;

import javax.lang.model.type.NullType;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaSchoolStarter {
    private static class Cell {
        private final String name;

        private final Object value;

        public Cell(String name, String value) {
            this.name = name;
            this.value = value;
        }
        public Cell(String name, Long value) {
            this.name = name;
            this.value = value;
        }
        public Cell(String name, Boolean value) {
            this.name = name;
            this.value = value;
        }
        public Cell(String name, Double value) {
            this.name = name;
            this.value = value;
        }

        public Cell(String name, NullType value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    private static class Requirement extends Cell{
        private final String requirement;

        public Requirement(String name, String value, String requirement) {
            super(name, value);
            this.requirement = requirement;
        }

        public Requirement(String name, Long value, String requirement) {
            super(name, value);
            this.requirement = requirement;
        }

        public Requirement(String name, Boolean value, String requirement) {
            super(name, value);
            this.requirement = requirement;
        }

        public Requirement(String name, NullType value, String requirement) {
            super(name, value);
            this.requirement = requirement;
        }
        public Requirement(String name, Double value, String requirement) {
            super(name, value);
            this.requirement = requirement;
        }
    }
    private List<Map<String, Object>> table;
    private Pattern pattern;
    public JavaSchoolStarter() {
        this.table = new ArrayList<>();
    }
    public List<Map<String,Object>> execute(String request) throws Exception {
        request = request.trim();
        String command = request.split(" ")[0];
        switch (command.toUpperCase()) {
            case "INSERT" -> {
                if (!request.split(" ")[1].equalsIgnoreCase("VALUES"))
                    throw new Exception("Ошибка выполнения, введена не верная команда");
                return INSERT(request);
            }
            case "UPDATE" -> {
                if (!request.split(" ")[1].equalsIgnoreCase("VALUES"))
                    throw new Exception("Ошибка выполнения, введена не верная команда");
                return UPDATE(request);
            }
            case "DELETE" -> {
                return DELETE(request);
            }
            case "SELECT" -> {
                return SELECT(request);
            }
            default -> throw new Exception("Ошибка выполнения, введена не верная команда");
        }
    }

    private List<Map<String,Object>> INSERT(String values) throws Exception {
        Map<String,Object> map = new HashMap<>();
        values = values.substring(13).trim();

        boolean lastNameExist = false;
        boolean idExist = false;
        boolean ageExist = false;
        boolean costExist = false;
        boolean activeExist = false;

        for (Cell temp : getRow(values)) {
            switch (temp.getName().toLowerCase()) {
                case "lastname" -> {
                    lastNameExist = true;
                }
                case "id" -> {
                    if (temp.getValue() != null) try {Long.parseLong(temp.getValue().toString());} catch (Exception LongParseException) {throw LongParseException;}
                    idExist = true;
                }
                case "age" -> {
                    if (temp.getValue() != null) try {Long.parseLong(temp.getValue().toString());} catch (Exception LongParseException) {throw LongParseException;}
                    ageExist = true;
                }
                case "cost" -> {
                    if (temp.getValue() != null) try {Double.parseDouble(temp.getValue().toString());} catch (Exception DoubleParseException) {throw DoubleParseException;}
                    costExist = true;
                }
                case "active" -> {
                    if (temp.getValue() != null) try {Boolean.parseBoolean(temp.getValue().toString());} catch (Exception BooleanParseException) {throw BooleanParseException;}
                    activeExist = true;
                }
                default -> throw new Exception();
            }
            map.put(temp.getName(), temp.getValue());
        }
        if (ageExist && lastNameExist && activeExist && costExist && idExist) {
            this.table.add(map);
        }
        else throw new Exception();

        return this.table;
    }

    private List<Map<String,Object>> UPDATE(String request) {
        request = request.substring(13);
        Cell[] newRow = getRow(request);
        List<List<Requirement>> listOfRequirements = getRequirement(request);

        this.table = this.table.stream().peek(currentRow -> {
            thisRow: {
                for (List<Requirement> requirements : listOfRequirements) {
                    int tmp = 0;
                    for (int i = 0; i < requirements.size(); i++) {
                        String name = requirements.get(i).getName();
                        for (String keys : currentRow.keySet()) {
                            if (keys.equalsIgnoreCase(name)) {
                                try {
                                    if (compareTo(currentRow.get(keys), name, requirements.get(i).requirement, requirements.get(i))) {
                                        tmp++;
                                        break;
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    if (tmp == requirements.size()) {
                        Arrays.stream(newRow).forEach(row -> {
                            for (String name : currentRow.keySet()) {
                                if (name.equalsIgnoreCase(row.getName())) {
                                    currentRow.remove(name);
                                    currentRow.put(name, row.getValue());
                                    break;
                                }
                            }
                        });
                        break thisRow;
                    }
                }
            }
        }).toList();
        return this.table;
    }

    private List<Map<String,Object>> DELETE(String values) {
        var listOfRequirements = getRequirement(values);
        List<Map<String, Object>> mapList = new ArrayList<>();

        this.table.forEach(currentRow -> {
            int countEquals = 0;
            for (List<Requirement> requirements : listOfRequirements) {
                int tmp = 0;
                thisRow: {
                    for (int i = 0; i < requirements.size(); i++) {
                        String name = requirements.get(i).getName();
                        for (String keys : currentRow.keySet()) {
                            if (keys.equalsIgnoreCase(name)) {
                                try {
                                    if (!compareTo(currentRow.get(keys), name, requirements.get(i).requirement, requirements.get(i))) {
                                        tmp = 0;
                                        break thisRow;
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        tmp++;
                    }
                }
                countEquals += tmp;
            }
            if (countEquals == 0) {
                mapList.add(currentRow);
            }
        });
        this.table = mapList;
        return this.table;
    }

    private List<Map<String,Object>> SELECT(String values) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        var listOfRequirements = getRequirement(values);

        this.table.forEach(currentRow -> {
            thisRow: {
                for (List<Requirement> requirements : listOfRequirements) {
                    int tmp = 0;
                    for (int i = 0; i < requirements.size(); i++) {
                        String name = requirements.get(i).getName();
                        for (String keys : currentRow.keySet()) {
                            if (keys.equalsIgnoreCase(name)) {
                                try {
                                    if (compareTo(currentRow.get(keys), name, requirements.get(i).requirement, requirements.get(i))) {
                                        tmp++;
                                    }
                                    break;
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    if (tmp == requirements.size()) {
                        mapList.add(currentRow);
                        break thisRow;
                    }
                }
            }
        });
        return mapList;
    }

    private Cell[] getRow(String request) {
        pattern = Pattern.compile("[\\p{Digit}\\p{Alpha}А-Яа-яёЁ=,.]");
        StringBuilder values = new StringBuilder();
        for (int i = 0; i < request.length(); i++) {
            String splitedRequest = request.split("")[i];
            if (splitedRequest.equals("‘")) {
                int lenght = 1;
                StringBuilder current = new StringBuilder();
                while (!splitedRequest.equals("’")) {
                    splitedRequest = request.split("")[i + lenght];
                    current.append(splitedRequest);
                    lenght++;
                }
                values = new StringBuilder(values.append(current).substring(0, values.length() - 1));
                i += lenght - 1;
                continue;
            }
            if (pattern.matcher(splitedRequest).matches()) values.append(splitedRequest);
        }
        int indexOfWhere = indexOfWord("where", values.toString());
        if (indexOfWhere > 0) values = new StringBuilder(values.toString().substring(0, indexOfWhere));
        Cell[] row = new Cell[values.toString().split(",").length];
        int i = 0;
        for (String cell : values.toString().split(",")) {
            String[] nameWithData = cell.split("=");
            row[i] = new Cell(nameWithData[0], nameWithData.length == 2 ? nameWithData[1] : null);
            i++;
        }
        return row;
    }

    private List<List<Requirement>> getRequirement(String request) {
        int indexOfWhere = request.toLowerCase().indexOf("where");
        request = request.substring(indexOfWhere + 5).trim();
        List<List<Requirement>> listOfRequirements = new ArrayList<>();
        for (String dataSeporetedByOr : request.trim().split("[oO][rR]")) {
            List<Requirement> requirements = new ArrayList<>();
            for (String dataSeporetedByAnd : dataSeporetedByOr.trim().split("[aA][nN][dD]")) {
                String NAME = "";
                String REQUIREMENT = "";
                String tempValue = "";
                pattern = Pattern.compile("[\\p{Alpha}\\p{Digit}=%><А-Яа-яёЁ]");
                for (int i = 0; i < dataSeporetedByAnd.length(); i++) {
                    String splitedRequest = dataSeporetedByAnd.split("")[i];
                    if (splitedRequest.equals("‘")) {
                        int lenght = 1;
                        StringBuilder current = new StringBuilder();
                        while (!splitedRequest.equals("’")) {
                            splitedRequest = dataSeporetedByAnd.split("")[i + lenght];
                            current.append(splitedRequest);
                            lenght++;
                        }
                        current = new StringBuilder(current.substring(0, current.length() - 1));
                        if (NAME.length() == 0) NAME = current.toString();
                        else tempValue = current.toString();
                        i += lenght - 1;
                    } else {
                        Pattern tempPattern = Pattern.compile("[ilke<>=%]");
                        Matcher matcher = tempPattern.matcher(splitedRequest.toLowerCase());
                        StringBuilder current = new StringBuilder();
                        int lenght = 0;
                        if (matcher.matches()) {
                            while (matcher.matches()) {
                                current.append(splitedRequest);
                                lenght++;
                                if (i + lenght < dataSeporetedByAnd.length()){
                                    splitedRequest = dataSeporetedByAnd.split("")[i + lenght];
                                    matcher = tempPattern.matcher(splitedRequest);
                                }
                                else break;
                            }
                            i += lenght - 1;
                            tempPattern = Pattern.compile("like|ilike|<|>|=|<=|>=|!=");
                            matcher = tempPattern.matcher(current.toString().trim().toLowerCase());
                            if (matcher.matches()) {
                                REQUIREMENT = current.toString().trim();
                                continue;
                            }
                        }
                        tempValue += splitedRequest.trim();
                    }
                }
                Double doubleVelue;
                Long longVelue;
                Boolean boolVelue;
                try {
                    longVelue = Long.parseLong(tempValue);
                    requirements.add(new Requirement(NAME, longVelue, REQUIREMENT));
                } catch (Exception ignoredLong) {
                    try {
                        doubleVelue = Double.parseDouble(tempValue);
                        requirements.add(new Requirement(NAME, doubleVelue, REQUIREMENT));
                    } catch (Exception ignoredDouble) {
                        try {
                            if (tempValue.equalsIgnoreCase("false") || tempValue.equalsIgnoreCase("true")) {
                                boolVelue = Boolean.parseBoolean(tempValue);
                                requirements.add(new Requirement(NAME, boolVelue, REQUIREMENT));
                            }
                            else throw new Exception();
                        } catch (Exception ignoredBoolean) {
                            requirements.add(new Requirement(NAME, tempValue, REQUIREMENT));
                        }
                    }
                }
            }
            listOfRequirements.add(requirements);
        }
        return listOfRequirements;
    }

    private boolean compareTo(Object first, String name, String comparetor, Requirement second ) throws Exception {
        Long firstLong;
        Boolean firstBool;
        Double firstDouble;
        String firstString;

        if (second.getValue() == null) throw new Exception();
        else {
            switch (name.toLowerCase()) {
                case "lastname" -> {
                    if (first != null) {
                        firstString = first.toString();
                        if (comparetor.equals("=")) return firstString.equals(second.getValue().toString());

                        String[] parts = second.getValue().toString().split("%");
                        String regex = "";
                        if (parts.length > 1) {
                            for (int i = 0; i < parts.length - 1; i++) {
                                regex += parts[i] + "\\p{all}";
                            }
                            regex += parts[parts.length - 1];
                        } else if (second.getValue().toString().contains("%")) {
                            if (second.getValue().toString().indexOf("%") == 0) {
                                regex = "\\p{all}" + second.toString().substring(1);
                            } else
                                regex = second.getValue().toString().substring(0, second.getValue().toString().length() - 1) + "\\p{all}";
                        }
                        switch (comparetor) {
                            case "like" -> {
                                pattern = Pattern.compile(regex.length() > 1 ? regex : second.getValue().toString());
                                Matcher matcher = pattern.matcher(first.toString());
                                return matcher.find();
                            }
                            case "ilike" -> {
                                pattern = Pattern.compile(regex.length() > 1 ? regex.toLowerCase() : second.getValue().toString().toLowerCase());
                                Matcher matcher = pattern.matcher(first.toString());
                                return matcher.find();
                            }
                        }
                    }
                    return false;
                }
                case "id", "age" -> {
                    if (first != null) {
                        try {
                            firstLong = Long.parseLong(first.toString());
                        } catch (Exception LongParseException) {
                            throw LongParseException;
                        }
                        switch (comparetor) {
                            case "=" -> {
                                return firstLong.equals(second.getValue());
                            }
                            case "<=" -> {
                                return Double.parseDouble(firstLong.toString()) <= Double.parseDouble(second.getValue().toString());
                            }
                            case ">=" -> {
                                return Double.parseDouble(firstLong.toString()) >= Double.parseDouble(second.getValue().toString());
                            }
                            case "<" -> {
                                return Double.parseDouble(firstLong.toString()) < Double.parseDouble(second.getValue().toString());
                            }
                            case ">" -> {
                                return Double.parseDouble(firstLong.toString()) > Double.parseDouble(second.getValue().toString());
                            }
                        }
                    }
                    return false;
                }
                case "cost" -> {
                    if (first != null) {
                        try {
                            firstDouble = Double.parseDouble(first.toString());
                        } catch (Exception DoubleParseExeption) {
                            throw DoubleParseExeption;
                        }
                        ;
                        switch (comparetor) {
                            case "=" -> {
                                return firstDouble.equals(second.getValue());
                            }
                            case "<=" -> {
                                return Double.parseDouble(firstDouble.toString()) <= Double.parseDouble(second.getValue().toString());
                            }
                            case ">=" -> {
                                return Double.parseDouble(firstDouble.toString()) >= Double.parseDouble(second.getValue().toString());
                            }
                            case "<" -> {
                                return Double.parseDouble(firstDouble.toString()) < Double.parseDouble(second.getValue().toString());
                            }
                            case ">" -> {
                                return Double.parseDouble(firstDouble.toString()) > Double.parseDouble(second.getValue().toString());
                            }
                        }
                    }
                    return false;
                }
                case "active" -> {
                    if (first != null) {
                        try {
                            firstBool = Boolean.parseBoolean(first.toString());
                        } catch (Exception BooleanParseExceprion) {
                            throw BooleanParseExceprion;
                        }
                        return firstBool.equals(second.getValue());
                    }
                    return false;
                }
                default -> throw new Exception();
            }
        }
    }

    private int indexOfWord(String word, String src) {
        src = src.toLowerCase();
        StringBuilder mutableSrc = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            String current = src.split("")[i];
            if (current.equals("‘")) {
                int lenght = 0;
                current = src.split("")[i + 1];
                while (!current.equals("’")) {
                    mutableSrc.append(" ");
                    lenght = mutableSrc.length();
                    current = src.split("")[i + lenght];
                }
                i += lenght - 1;
            }
            else mutableSrc.append(current);
        }
        return src.indexOf(word);
    }
}