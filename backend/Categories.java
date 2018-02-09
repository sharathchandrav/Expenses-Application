package com.scv.expensesapplication.backend;

public enum Categories {

    C01("Home"), C02("Medical"), UNKNOWN("Unknown Expense Type");
    String data;

    private Categories(String description) {
        data = description;
    }

    public static Categories from(String name) {
        for (Categories category : values()) {
            if (category.toString().equals(name)) {
                return category;
            }
        }

        return Categories.UNKNOWN;
    }
}
