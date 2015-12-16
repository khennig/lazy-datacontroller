package com.tri.ui.model;

import com.tri.ui.model.utility.Validate;

public enum SortPropertyOrder {
	/** Ascending order */
	ASCENDING("ASC"),

	/** Descending order */
	DESCENDING("DESC");

	private String sqlKeyword;

	private SortPropertyOrder(String sqlKeyword) {
		Validate.notEmpty(sqlKeyword, "SQL Keyword required");
		this.sqlKeyword = sqlKeyword;
	}

	public String getSqlKeyword() {
		return sqlKeyword;
	}
}