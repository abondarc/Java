

import java.math.BigInteger;
import java.util.Scanner;

class CalculatorThread extends Thread {
    private int start;
    private int end;
    private BigInteger result;
    private String operation;

    public CalculatorThread(int start, int end, String operation) {
        this.start = start;
        this.end = end;
        this.operation = operation;
    }

    @Override
    public void run() {
        switch (operation) {
            case "addition":
                result = performAddition();
                break;
            case "subtraction":
                result = performSubtraction();
                break;
            case "multiplication":
                result = performMultiplication();
                break;
            default:
                System.out.println("Invalid operation");
                break;
        }
    }

    private BigInteger performAddition() {
        BigInteger sum = BigInteger.ZERO;
        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                BigInteger factorial = calculateFactorial(i);
                sum = sum.add(factorial);
            }
        }
        return sum;
    }

    private BigInteger performSubtraction() {
        BigInteger diff = BigInteger.ZERO;
        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                BigInteger factorial = calculateFactorial(i);
                diff = diff.subtract(factorial);
            }
        }
        return diff;
    }

    private BigInteger performMultiplication() {
        BigInteger product = BigInteger.ONE;
        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                BigInteger factorial = calculateFactorial(i);
                product = product.multiply(factorial);
            }
        }
        return product;
    }

    private BigInteger calculateFactorial(int number) {
        BigInteger factorial = BigInteger.ONE;
        for (int i = 2; i <= number; i++) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
        }
        return factorial;
    }

    public BigInteger getResult() {
        return result;
    }
}

class ThreadGenerator {
    private int numThreads;
    private String operation;
    private int start;
    private int end;

    public void execute() {
        System.out.println("Executing problem in " + numThreads + " threads...");

        CalculatorThread[] threads = new CalculatorThread[numThreads];
        int interval = (end - start + 1) / numThreads;
        int remaining = (end - start + 1) % numThreads;

        int currentStart = start;
        int currentEnd = start + interval - 1;

        for (int i = 0; i < numThreads; i++) {
            if (i == numThreads - 1) {
                currentEnd += remaining;
            }

            threads[i] = new CalculatorThread(currentStart, currentEnd, operation);
            threads[i].start();

            currentStart = currentEnd + 1;
            currentEnd = currentStart + interval - 1;
        }

        BigInteger finalResult = BigInteger.ZERO;

        for (CalculatorThread thread : threads) {
            try {
                thread.join();
                finalResult = finalResult.add(thread.getResult());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Final result: " + finalResult);
    }

    public void getResult() {
        execute();
    }

    public void showMenu() {
        System.out.println("-------- Menu --------");
        System.out.println("1. Addition");
        System.out.println("2. Subtraction");
        System.out.println("3. Multiplication");
        System.out.println("-----------------------");
    }

    public void setParameters() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of threads: ");
        numThreads = scanner.nextInt();

        showMenu();
        System.out.print("Enter the operation number: ");
        int operationNumber = scanner.nextInt();

        switch (operationNumber) {
            case 1:
                operation = "addition";
                break;
            case 2:
                operation = "subtraction";
                break;
            case 3:
                operation = "multiplication";
                break;
            default:
                System.out.println("Invalid operation number. Performing addition by default.");
                operation = "addition";
                break;
        }

        System.out.print("Enter the start of the interval: ");
        start = scanner.nextInt();

        System.out.print("Enter the end of the interval: ");
        end = scanner.nextInt();
    }
}

public class Main {
    public static void main(String[] args) {
        ThreadGenerator threadGenerator = new ThreadGenerator();
        threadGenerator.setParameters();
        threadGenerator.getResult();
    }
}
