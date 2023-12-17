import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Abiturient implements Comparable<Abiturient> {
    private int id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String address;
    private String phoneNumber;
    private List<Integer> grades;

    public Abiturient(int id, String lastName, String firstName, String patronymic, String address, String phoneNumber, List<Integer> grades) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.grades = grades;
    }

    public int calculateTotalGrades() {
        return grades.stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public int compareTo(Abiturient other) {
        return Integer.compare(this.calculateTotalGrades(), other.calculateTotalGrades());
    }

    @Override
    public String toString() {
        return String.format("%d,%s, %s, %s, %s, " +
                "%s, %s", id, lastName, firstName, patronymic, address, phoneNumber, grades);
    }

    public static List<Abiturient> readAndSort(String inputFile, String outputFile) {
        List<Abiturient> abiturients = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String lastName = parts[1];
                String firstName = parts[2];
                String patronymic = parts[3];
                String address = parts[4];
                String phoneNumber = parts[5];
                List<Integer> grades = Arrays.stream(parts[6].split(" ")).map(Integer::parseInt).collect(Collectors.toList());

                Abiturient abiturient = new Abiturient(id, lastName, firstName, patronymic, address, phoneNumber, grades);
                abiturients.add(abiturient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        abiturients.sort(Collections.reverseOrder());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (Abiturient abiturient : abiturients) {
                writer.write(abiturient.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return abiturients;
    }

    public static List<Abiturient> findAbiturientsWithUnsatisfactoryGrades(List<Abiturient> abiturients) {
        return abiturients.stream()
                .filter(abiturient -> abiturient.grades.stream().anyMatch(grade -> grade < 60))
                .collect(Collectors.toList());
    }

    public static List<Abiturient> findAbiturientsWithTotalGradesAbove(List<Abiturient> abiturients, int threshold) {
        return abiturients.stream()
                .filter(abiturient -> abiturient.calculateTotalGrades() > threshold)
                .collect(Collectors.toList());
    }

    public static List<Abiturient> selectTopNByTotalGrades(List<Abiturient> abiturients, int n) {
        return abiturients.stream()
                .sorted(Collections.reverseOrder())
                .limit(n)
                .collect(Collectors.toList());
    }

    public static void writeToFile(List<Abiturient> abiturients, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (Abiturient abiturient : abiturients) {
                writer.write(abiturient.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<Abiturient> abiturients = readAndSort("input.txt", "output.txt");

        List<Abiturient> unsatisfactoryGrades = findAbiturientsWithUnsatisfactoryGrades(abiturients);
        System.out.println("Абитуриенты с неудовлетворительными оценками:");
        for (Abiturient abiturient : unsatisfactoryGrades) {
            System.out.println(abiturient);
        }
        writeToFile(unsatisfactoryGrades, "Абитуриенты_с_неудовлетворительными_оценками.txt");

        List<Abiturient> highTotalGrades = findAbiturientsWithTotalGradesAbove(abiturients, 200);
        System.out.println("\nАбитуриенты с суммой баллов выше 200:");
        for (Abiturient abiturient : highTotalGrades) {
            System.out.println(abiturient);
        }
        writeToFile(highTotalGrades, "Абитуриенты_с_суммой_баллов_200.txt");

        List<Abiturient> topNByTotalGrades = selectTopNByTotalGrades(abiturients, 3);
        System.out.println("\nТоп 3 абитуриента с самой высокой суммой баллов:");
        for (Abiturient abiturient : topNByTotalGrades) {
            System.out.println(abiturient);
        }
        writeToFile(topNByTotalGrades, "Топ_3_абитуриента_с_самой_высокой_суммой_баллов.txt");
    }
}

