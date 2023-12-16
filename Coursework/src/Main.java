import java.time.LocalDate;
import java.util.List;

public class Main {
    private static final SpendingHistory spendingHistory = new SpendingHistory();
    private static final List<String> categories = List.of(
            "Одежда",
            "Транспорт и автомобиль",
            "Дом и ремонт",
            "Здоровье и красота",
            "Питание",
            "Подарки",
            "Техника",
            "Услуги",
            "Развлечения",
            "Личные расходы");

    public static void main(String[] args) {
        mainMenu();
    }

    public static void mainMenu() {
        switch (SelectionList.view("Главное меню",
                List.of("Новая запись",
                        "Статистика",
                        "Статистика в Excel"),
                false)) {
            case 1 -> dateMenu();
            case 2 -> statisticsMenu();
            case 3 -> spendingHistory.excelOutput();
            default -> System.out.println("Ошибка в switch-case");
        }
    }

    public static String addCategory() {
        int choice = SelectionList.view("Выбор категории", categories, false);
        return categories.get(choice - 1);
    }

    public static int addValue() {
        System.out.println("----- Ввод суммы -----");
        return SelectionList.intInput(1);
    }

    public static LocalDate addDate() {
        System.out.println("----- Ввод даты -----");
        int year, month, day;
        System.out.println("Год");
        year = SelectionList.intInput(1919, LocalDate.now().getYear());
        System.out.println("Месяц");
        if (year == LocalDate.now().getYear()) {
            month = SelectionList.intInput(1, LocalDate.now().getMonthValue());
        } else {
            month = SelectionList.intInput(1, 12);
        }
        System.out.println("День");
        LocalDate date = LocalDate.of(year, month, 1);
        if (year == LocalDate.now().getYear() && month == LocalDate.now().getMonthValue()) {
            day = SelectionList.intInput(1, LocalDate.now().getDayOfMonth());
        } else {
            day = SelectionList.intInput(1, date.lengthOfMonth());
        }
        date = LocalDate.of(year, month, day);
        return date;
    }

    public static void dateMenu() {
        String category = addCategory();
        int value = addValue();
        switch (SelectionList.view("Выбор даты",
                List.of("Сегодня",
                        "Другая дата"),
                false)) {
            case 1 -> {
                spendingHistory.add(category, value);
                System.out.println("[Запись добавлена]");
                mainMenu();
            }
            case 2 -> {
                LocalDate date = addDate();
                spendingHistory.add(category, value, date);
                System.out.println("[Запись добавлена]");
                mainMenu();
            }
            default -> System.out.println("Ошибка в switch-case");
        }
    }

    public static void statisticsMenu() {
        switch (SelectionList.view("Статистика",
                List.of("Общая статистика",
                        "Статистика категории"),
                true)) {
            case 1 -> {
                generalStatistics();
                mainMenu();
            }
            case 2 -> categoryStatistic();
            case 0 -> mainMenu();
            default -> System.out.println("Ошибка в switch-case");
        }
    }

    public static void generalStatistics() {
        System.out.println("----- Общая статистика -----");
        System.out.println("Всего потрачено: " + spendingHistory.getValue() + " рублей");
        for (String category : categories) {
            int value = spendingHistory.getValue(category);
            if (value != 0) {
                System.out.println(category + ": " + value + " (" + value * 100 / spendingHistory.getValue() + "%)");
            }
        }
        mainMenu();
    }

    public static void categoryStatistic() {
        String category = addCategory();
        System.out.println("----- Статистика категории \"" + category + "\" -----");
        System.out.println("Всего потрачено: " + spendingHistory.getValue(category));
        List<List<String>> history = spendingHistory.getHistory(category);
        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i).get(0) + ": " + history.get(i).get(1));
        }
        mainMenu();
    }
}
