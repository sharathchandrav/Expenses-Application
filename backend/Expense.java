package com.scv.expensesapplication.backend;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Date;

public class Expense {

	private int amount;
	private String notes;
	private Categories category;
	private Date finalDate;
	private String datePrint;
	private int id;

	public Expense(Date finalDate, int amount, Categories category, String notes, int id) {
		this.amount = amount;
		this.category = category;
		this.notes = notes;
		SimpleDateFormat readingFormat = new SimpleDateFormat("yyyy/MM/dd");
		this.datePrint = readingFormat.format(finalDate);
		this.id = id;
		this.finalDate = finalDate;
	}

	public String toString() {
		// TODO: Print data like 2017/12/24
		String show = this.datePrint + " >>> Rs " + this.amount + " - " + this.notes + " (" + this.category + ")";
		return show;
	}

	public int getAmount() {
		return amount;
	}

	public int getOnlyYear() {
		return finalDate.getYear() + 1900;
	}

	public String getYearMonth() {
		int monthNumber;
		monthNumber = finalDate.getMonth() + 1;
		return Month.of(monthNumber).name();
	}

	public int getDate() {
		return finalDate.getDate();
	}

	public Categories getCategory() {
		return category;
	}

	public int getId() {
		return id;
	}
}