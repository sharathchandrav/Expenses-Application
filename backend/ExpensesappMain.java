package com.scv.expensesapplication.backend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import com.scv.bankaccount.backend.Account;
import com.scv.bankaccount.backend.AccountType;

public class ExpensesappMain {
	static List<Expense> expenseList = new ArrayList<>();
	static int id = 1;

	public static void main(String[] args) throws ParseException {
		programInitiation();
	}

	private static void programInitiation() throws ParseException {
		Scanner s = new Scanner(System.in);

		while (true) {
			System.out.println(
					"---------Choices----------\n1.Add an Expense\n2.Delete an Expense\n3.Upadate an Expense\n4.Summary\n5.Exit");
			System.out.println("Enter the Choice");
			int choice2 = s.nextInt();

			switch (choice2) {
			case 1:
				addAnExpense(s);
				break;
			case 2:
				deleteAnExpense(s);
				break;
			case 3:
				updateAnExpense(s);
				break;
			case 4:
				printYearlySummary();
				printMonthlySummary();
				printDailySummary();
				printCategorySummary();
				break;
			case 5:
				System.out.println("EXITING................");
				System.exit(0);
				s.close();
			default:
				System.out.println("Invalid Input");
			}
		}
	}

	private static void addAnExpense(Scanner s) throws ParseException {
		System.out.println("How many Entries do you want to enter?");
		int times = s.nextInt();
		s.nextLine();

		System.out.print("Enter your expense as Comma separated string (Eg: Eg:YYYY/MM/DD,100, C01, For Dinner): \n");

		for (int i = 0; i < times; i++) {
			String userInput = s.nextLine();

			// String Processing
			String[] tokens = userInput.split(",");
			if (tokens.length != 4) {
				System.out.println("Invalid input entered. Requested operation cannot be performed!");
				return;
			}
			
			Categories category = stringToCategory(tokens[2]);
			if (category == Categories.UNKNOWN) {
				System.out.println("Invalid category entered. Requested operation cannot be performed!");
				return;
			}
			
			Date finalDate = stringToDate(tokens[0].trim());
			int amount = Integer.parseInt(tokens[1].trim());
			String notes = tokens[3].trim();
			
			Expense e1 = new Expense(finalDate, amount, category, notes, id);
			expenseList.add(e1);
			id++;
		}
	}

	private static void updateAnExpense(Scanner s) throws ParseException {
		printAllEntriesWithId();
	
		int particularExpenseId = 0;
		int index = 0;		
		
		System.out.println("Enter the S.No. of the Entry to be Updated :");
		int choice = s.nextInt();
		s.nextLine();
		
		Expense expenseToBeUpdated = getExpenseFromId(expenseList, choice);
		index = expenseList.indexOf(expenseToBeUpdated);
		particularExpenseId = expenseToBeUpdated.getId();
		
		if (expenseToBeUpdated == null) {
			System.out.println("Element with id '" + choice + "' is not found. Requested operation cannot be done.");
		} else {
			System.out
					.print("Enter your expense as Comma separated string (Eg: Eg:YYYY/MM/DD,100, C01, For Dinner): \n");
			String userInput = s.nextLine();
	
			// String Processing
			String[] tokens = userInput.split(",");
			if (tokens.length != 4) {
				System.out.println("Invalid input entered. Requested operation cannot be performed!");
				return;
			}
			
			Categories category = stringToCategory(tokens[2]);
			if (category == Categories.UNKNOWN) {
				System.out.println("Invalid category entered. Requested operation cannot be performed!");
				return;
			}
			
			Date finalDate = stringToDate(tokens[0].trim());
			int amount = Integer.parseInt(tokens[1].trim());
			String notes = tokens[3].trim();
			
			Expense e1 = new Expense(finalDate, amount, category, notes, particularExpenseId);
			expenseList.set(index, e1);
			System.out.println("Successfully Updated");
		}
	}

	private static void deleteAnExpense(Scanner s) {
		// 8. Prepare map of Delete, Map<Expenses>
		printAllEntriesWithId();
		
		System.out.println("Enter the S.No. of the Entry to be Deleted :");
		int choice = s.nextInt();
		
		Expense expenseToBeDeleted = getExpenseFromId(expenseList, choice);
		if (expenseToBeDeleted == null) {
			System.out.println("Element with id '" + choice + "' is not found");
		} else {
			expenseList.remove(expenseToBeDeleted);
			System.out.println("Successfully Deleted");
		}
	}

	private static void printYearlySummary() {
		// 2. Prepare map of year, List<Expense>
		Map<Integer, List<Expense>> mapForYear = new HashMap<Integer, List<Expense>>();
		for (Expense thisExpense : expenseList) {
			int year = thisExpense.getOnlyYear();
			if (mapForYear.containsKey(year) == false) {
				List<Expense> list = new ArrayList<Expense>();
				mapForYear.put(year, list);
			}
			List<Expense> list = mapForYear.get(year);
			list.add(thisExpense);
		}

		// 3. Print the mapOfYear so that we know that we have written code
		// correctly
		System.out.println("--------The Summary in Years---------");
		for (Map.Entry<Integer, List<Expense>> entry : mapForYear.entrySet()) {
			System.out.println("");
			System.out.print("Key: " + entry.getKey());
			System.out.print(", Value: " + entry.getValue() + "\n");
			List<Expense> expensesInThisYear = entry.getValue();
			int totalYearCount = 0;
			for (Expense e : expensesInThisYear) {
				totalYearCount = totalYearCount + e.getAmount();
			}
			System.out.println("The Total amount in the year " + "is " + totalYearCount);
		}
	}

	private static void printMonthlySummary() {
		// 4. Prepare map of month, List<Expense>
		Map<String, List<Expense>> mapForMonth = new HashMap<String, List<Expense>>();
		for (Expense thisExpense : expenseList) {
			String month = thisExpense.getOnlyYear() + " " + thisExpense.getYearMonth();
			if (mapForMonth.containsKey(month) == false) {
				List<Expense> list = new ArrayList<Expense>();
				mapForMonth.put(month, list);
			}
			List<Expense> list = mapForMonth.get(month);
			list.add(thisExpense);
		}

		// 5. Print the mapOfMonth so that we know that we have written code
		// correctly
		System.out.println("--------The Summary in Months---------");
		for (Map.Entry<String, List<Expense>> entry : mapForMonth.entrySet()) {
			System.out.println("");
			System.out.print("Key: " + entry.getKey());
			System.out.print(", Value: " + entry.getValue() + "\n");
			List<Expense> expensesInThisMonth = entry.getValue();
			int totalMonthCount = 0;
			for (Expense e : expensesInThisMonth) {
				totalMonthCount = totalMonthCount + e.getAmount();
			}
			System.out.println("The Total amount in the month " + "is " + totalMonthCount);
		}
	}

	private static void printDailySummary() {
		// 6. Prepare map of Daily, List<Expense>
		Map<String, List<Expense>> mapForDaily = new HashMap<String, List<Expense>>();
		for (Expense thisExpense : expenseList) {
			String date = thisExpense.getOnlyYear() + " " + thisExpense.getYearMonth() + " " + thisExpense.getDate();
			if (mapForDaily.containsKey(date) == false) {
				List<Expense> list = new ArrayList<Expense>();
				mapForDaily.put(date, list);
			}
			List<Expense> list = mapForDaily.get(date);
			list.add(thisExpense);
		}
		// 7. Print the mapOfDate so that we know that we have written code
		// correctly
		System.out.println("--------The Summary in Dates---------");
		for (Map.Entry<String, List<Expense>> entry : mapForDaily.entrySet()) {
			System.out.println("");
			System.out.print("Key: " + entry.getKey());
			System.out.print(", Value: " + entry.getValue() + "\n");
			List<Expense> expensesInThisDate = entry.getValue();
			int totalDailyCount = 0;
			for (Expense e : expensesInThisDate) {
				totalDailyCount = totalDailyCount + e.getAmount();
			}
			System.out.println("The Total amount in the year " + "is " + totalDailyCount);
		}
	}

	private static void printCategorySummary() {
		// 8. Prepare map of Category, List<Expense>
		Map<Categories, List<Expense>> mapForCategory = new HashMap<Categories, List<Expense>>();
		for (Expense thisExpense : expenseList) {
			Categories containCategory = thisExpense.getCategory();
			if (mapForCategory.containsKey(containCategory) == false) {
				List<Expense> list = new ArrayList<Expense>();
				mapForCategory.put(containCategory, list);
			}
			List<Expense> list = mapForCategory.get(containCategory);
			list.add(thisExpense);
		}
		// 7. Print the mapOfDate so that we know that we have written code
		// correctly
		System.out.println("--------The Summary in Category---------");
		for (Map.Entry<Categories, List<Expense>> entry : mapForCategory.entrySet()) {
			System.out.println("");
			System.out.print("Key: " + entry.getKey());
			System.out.print(", Value: " + entry.getValue() + "\n");
			List<Expense> expensesInThisCategory = entry.getValue();
			int totalCategoryCount = 0;
			for (Expense e : expensesInThisCategory) {
				totalCategoryCount = totalCategoryCount + e.getAmount();
			}
			System.out.println("The Total amount in the year " + "is " + totalCategoryCount);
		}
	}

	private static void printAllEntriesWithId() {
		System.out.println("-------- Displaying all the entries ---------");
		for (Expense thisExpense : expenseList) {
			int id = thisExpense.getId();
			System.out.print(id + "." + " " + thisExpense + "\n");
		}
	}

	private static Expense getExpenseFromId(List<Expense> expenseList, int choice) {
		Expense expenseToBeDeleted = null;
		
		for (Expense thisExpense : expenseList) {
			int id = thisExpense.getId();
			if (choice == id) {
				expenseToBeDeleted = thisExpense;
			}
		}
		
		return expenseToBeDeleted;
	}

	private static Date stringToDate(String dateAsString) throws ParseException {
		SimpleDateFormat readingFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date finalDate = readingFormat.parse(dateAsString);
		return finalDate;
	}

	private static Categories stringToCategory(String categoryAsString) throws ParseException {
		 return Categories.from(categoryAsString.trim().toUpperCase());
	}
}