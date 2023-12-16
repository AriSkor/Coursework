import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class SpendingHistory {
    private final LocalDate date = LocalDate.now();

    private final List<Day> daysList = new LinkedList<>();

    public void add(String category, int value) {
        if (!daysList.isEmpty()) {
            boolean flag = false;
            for (Day day : daysList) {
                if (day.getDate() == date) {
                    day.add(category, value);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                for (int i = 0; i < daysList.size(); i++) {
                    if (date.isBefore(daysList.get(i).getDate())) {
                        daysList.add(i, new Day(date, category, value));
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    daysList.add(new Day(date, category, value));
                }
            }
        } else {
            daysList.add(new Day(date, category, value));
        }
    }

    public void add(String category, int value, LocalDate date) {
        if (!daysList.isEmpty()) {
            boolean flag = false;
            for (Day day : daysList) {
                if (Objects.equals(day.getDate(), date)) {
                    day.add(category, value);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                for (int i = 0; i < daysList.size(); i++) {
                    if (date.isBefore(daysList.get(i).getDate())) {
                        daysList.add(i, new Day(date, category, value));
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    daysList.add(new Day(date, category, value));
                }
            }
        } else {
            daysList.add(new Day(date, category, value));
        }
    }

    public int getValue() {
        int sum = 0;
        if (!daysList.isEmpty()) {
            for (Day day : daysList) {
                sum += day.getValue();
            }
        }
        return sum;
    }

    public int getValue(String category) {
        int sum = 0;
        if (!daysList.isEmpty()) {
            for (Day day : daysList) {
                sum += day.getValue(category);
            }
        }
        return sum;
    }

    public List<List<String>> getHistory(String category) {
        List<List<String>> history = new ArrayList<>();
        if (!daysList.isEmpty()) {
            for (Day day : daysList) {
                if (day.getValue(category) != 0) {
                    history.add(List.of(day.getDate().toString(), Integer.toString(day.getValue(category))));
                }
            }
        }
        return history;
    }

    public void excelOutput() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet 1");
        int rownum = 0;
        for (Day day : daysList) {
            String date = String.valueOf(day.getDate());
            List<List<String>> info = day.getInfo();
            Row mainRow = sheet.createRow(rownum++);
            Cell dateCell = mainRow.createCell(0);
            dateCell.setCellValue(date);
            for (List<String> strings : info) {
                Row subRow = sheet.createRow(rownum++);
                Cell categoryCell = subRow.createCell(0);
                categoryCell.setCellValue(strings.get(0));
                Cell valueCell = subRow.createCell(1);
                valueCell.setCellValue(Integer.parseInt(strings.get(1)));
            }
        }
        try {
            FileOutputStream out = new FileOutputStream("Статистика.xlsx");
            workbook.write(out);
            out.close();
            System.out.println("Файл успешно создан");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении файла!!! (возможно, файл с таким именем уже существует)");
        }
    }
}

class Day {
    private final LocalDate date;
    private final Map<String, Integer> dailyExpenses = new HashMap<>();

    public Day(LocalDate date, String category, int value) {
        this.date = date;
        dailyExpenses.put(category, value);
    }

    public void add(String category, int value) {
        if (dailyExpenses.containsKey(category)) {
            dailyExpenses.put(category, dailyExpenses.get(category) + value);
        } else {
            dailyExpenses.put(category, value);
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public int getValue() {
        int sum = 0;
        for (Map.Entry<String, Integer> pair : dailyExpenses.entrySet()) {
            sum += pair.getValue();
        }
        return sum;
    }

    public int getValue(String category) {
        int sum = 0;
        for (Map.Entry<String, Integer> pair : dailyExpenses.entrySet()) {
            if (Objects.equals(pair.getKey(), category)) {
                sum += pair.getValue();
            }
        }
        return sum;
    }

    public List<List<String>> getInfo() {
        List<List<String>> dailyExpensesList = new ArrayList<>();
        for (Map.Entry<String, Integer> pair : dailyExpenses.entrySet()) {
            dailyExpensesList.add(List.of(pair.getKey(), Integer.toString(pair.getValue())));
        }
        return dailyExpensesList;
    }
}
