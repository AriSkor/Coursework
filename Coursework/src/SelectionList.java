import java.util.List;
import java.util.Scanner;

public class SelectionList {
    public static int view(String title, List<String> list, boolean returnButton) {
        System.out.println("----- " + title + " -----");
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ") " + list.get(i));
        }
        int minimum = 1;
        if (returnButton) {
            System.out.println("0) Назад");
            minimum = 0;
        }
        return intInput(minimum, list.size());
    }

    public static int intInput(int minValue, int maxValue) {
        System.out.print("Ввод: ");
        int choice;
        try {
            Scanner in = new Scanner(System.in);
            choice = in.nextInt();
        } catch (Exception e) {
            System.out.println("Неверный ввод!!!");
            choice = intInput(minValue, maxValue);
        }
        if (choice < minValue || choice > maxValue) {
            System.out.println("Неверный ввод!!!");
            choice = intInput(minValue, maxValue);
        }
        return choice;
    }

    public static int intInput(int minValue) {
        System.out.print("Ввод: ");
        int choice;
        try {
            Scanner in = new Scanner(System.in);
            choice = in.nextInt();
        } catch (Exception e) {
            System.out.println("Неверный ввод!!!");
            choice = intInput(minValue);
        }
        if (choice < minValue) {
            System.out.println("Неверный ввод!!!");
            choice = intInput(minValue);
        }
        return choice;
    }
}
