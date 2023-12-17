import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

class CalculatorThread implements Callable<Integer> {
    private final int start;
    private final int end;

    public CalculatorThread(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) {
                sum += i;
            }
        }
        return sum;
    }

    private boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}

public class ThreadGenerator {
    private final int numThreads;
    private final String operation;
    private final int lowerBound;
    private final int upperBound;
    private List<Future<Integer>> results;

    public ThreadGenerator(int numThreads, String operation, int lowerBound, int upperBound) {
        this.numThreads = numThreads;
        this.operation = operation;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.results = new ArrayList<>();
    }

    public void execute() {
        int intervalSize = (upperBound - lowerBound + 1) / numThreads;

        // Подготовка задач для каждого потока
        List<Callable<Integer>> tasks = new ArrayList<>();
        int currentStart = lowerBound;
        int currentEnd = currentStart + intervalSize - 1;
        for (int i = 0; i < numThreads; i++) {
            if (i == numThreads - 1) {
                currentEnd = upperBound;
            }
            tasks.add(new CalculatorThread(currentStart, currentEnd));
            currentStart = currentEnd + 1;
            currentEnd = currentStart + intervalSize - 1;
        }

        // Создание пула потоков
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        // Запуск задач и получение результатов
        try {
            results = executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException("Ошибка при выполнении потоков", e);
        }

        // Остановка пула потоков
        executorService.shutdown();
    }

    public int getResult() {
        int finalResult = 0;
        for (Future<Integer> result : results) {
            try {
                finalResult += result.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return finalResult;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите количество потоков: ");
        int numThreads = scanner.nextInt();

        System.out.print("Введите операцию (сложение или вычитание или умножение): ");
        String operation = scanner.next();

        System.out.print("Введите число a: ");
        int a = scanner.nextInt();

        ThreadGenerator threadGenerator = new ThreadGenerator(numThreads, operation, 1, a);
        threadGenerator.execute();

        System.out.println("Итоговый результат: " + threadGenerator.getResult());
    }
}
