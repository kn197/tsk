package ua.gss.tsk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import ua.gss.tsk.model.Node;
import ua.gss.tsk.model.Symbol;

public class Run {

	private static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	private static final long timeout = 100L;

	public static void main(String[] args) throws IOException {
		System.out
				.println("Выберете операцию\n\t[0] Подсчитать количество комбинаций\n\t[1] Вывести комбинации\n\t[По завершении нажмите Enter]");
		String action = stdin.readLine();
		if (action.equals("0")) {
			calcCombinations();
		}
		if (action.equals("1")) {
			writeCombinations();
		}
	}

	public static void calcCombinations() throws NumberFormatException, IOException {
		System.out.println("Введите начальное значение количества интервалов Т0");
		int intervalBegin = Integer.valueOf(stdin.readLine());
		System.out.println("Введите конечное значение количества интервалов Т0");
		int intervalEnd = Integer.valueOf(stdin.readLine());
		System.out.println("Введите начальное значение delta");
		int deltaBegin = Integer.valueOf(stdin.readLine());
		System.out.println("Введите конечное значение delta");
		int deltaEnd = Integer.valueOf(stdin.readLine());
		System.out.println("Введите кодовые позиции в формате f0A0,f0A1,f0A2,f1A0,f1A1,f1A2");
		String[] codePositions = stdin.readLine().split("\\s*,\\s*");
		System.out
				.println("Введите название файла csv для сохранения информации. ВНИМАНИЕ! Существующий файл будет перезаписан");
		String fileName = stdin.readLine();

		List<Symbol> alphabet = new LinkedList<Symbol>();
		for (String codePosition : codePositions) {
			alphabet.add(new Symbol(codePosition));
		}

		File outputFile = new File(fileName);
		CSVFormat format = CSVFormat.EXCEL.withHeader("Количество интервалов T0", "Delta", "Количество позиций кода",
				"Количество кодовых комбинаций", "Время работы алгоритма");
		CSVPrinter printer = format.print(new PrintStream(outputFile));

		int progressTotal = (intervalEnd - intervalBegin + 1) * (deltaEnd - deltaBegin + 1);
		int progressCurrent = 0;
		long prevTime = 0;
		for (int interval = intervalBegin; interval <= intervalEnd; interval++) {
			for (int delta = deltaBegin; delta <= deltaEnd; delta++) {
				progressCurrent++;

				BigDecimal combinations;
				long startTime = System.currentTimeMillis();
				Node rootNode = new Node();
				rootNode.setSymbol(Symbol.startSymbol());
				rootNode.setLength(-1);

				CombinationsCalculator calc = new CombinationsCalculator(alphabet, delta);
				combinations = calc.calculateNk(delta * interval, rootNode);
				long endTime = System.currentTimeMillis();
				printer.printRecord(interval, delta, alphabet.size(), combinations.toString(), endTime - startTime);
				if ((System.currentTimeMillis() - prevTime) > timeout) {
					prevTime = System.currentTimeMillis();
					int progress = (int) (100 * progressCurrent / progressTotal);
					System.out.print("\b\b\b");
					System.out.print(String.format("%02d", progress) + "%");
				}
			}
		}
		System.out.print("\b\b\b");
		System.out.println("100%");
	}

	public static void writeCombinations() throws NumberFormatException, IOException {
		System.out.println("Введите значение количества интервалов Т0");
		int interval = Integer.valueOf(stdin.readLine());
		System.out.println("Введите значение delta");
		int delta = Integer.valueOf(stdin.readLine());
		System.out.println("Введите кодовые позиции в формате f0A0,f0A1,f0A2,f1A0,f1A1,f1A2");
		String[] codePositions = stdin.readLine().split("\\s*,\\s*");
		System.out
				.println("Введите название файла csv для сохранения информации. ВНИМАНИЕ! Существующий файл будет перезаписан");
		String fileName = stdin.readLine();

		List<Symbol> alphabet = new LinkedList<Symbol>();
		for (String codePosition : codePositions) {
			alphabet.add(new Symbol(codePosition));
		}

		File outputFile = new File(fileName);
		CSVFormat format = CSVFormat.EXCEL;
		CSVPrinter printer = format.print(new PrintStream(outputFile));

		Node rootNode = new Node();
		rootNode.setSymbol(Symbol.startSymbol());
		rootNode.setLength(-1);

		CombinationsCalculator calc = new CombinationsCalculator(alphabet, delta);
		calc.calculateNk(delta * interval, rootNode);

		List<List<Symbol>> combinations = CombinationsCalculator.parseNodeTree(rootNode);
		int progressTotal = combinations.size();
		int progressCurrent = 0;
		long prevTime = 0;
		for (List<Symbol> combination : combinations) {
			progressCurrent++;
			printer.printRecord(combination);
			if ((System.currentTimeMillis() - prevTime) > timeout) {
				prevTime = System.currentTimeMillis();
				int progress = (int) (100 * progressCurrent / progressTotal);
				System.out.print("\b\b\b");
				System.out.print(String.format("%02d", progress) + "%");
			}
		}
		System.out.print("\b\b\b");
		System.out.println("100%");

	}

}
