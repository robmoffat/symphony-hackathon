package com.db.symphonyp.tabs.common;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Table {

	private List<Map<String, Object>> rows;

	private Map<String, String> approvals;
	
	private String tableId;

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String id) {
		this.tableId = id;
	}

	public Table(List<Map<String, Object>> rows, Map<String, String> approvals) {
		super();
		this.rows = rows;
		this.approvals = approvals;
	}

	public Table() {
	}

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}

	public Map<String, String> getApprovals() {
		return approvals;
	}

	public void setApprovals(Map<String, String> approvals) {
		this.approvals = approvals;
	}

	public List<String> getColumns() {
		Set<String> out = new LinkedHashSet<>();
		for (Map<String, Object> row : rows) {
			out.addAll(row.keySet());
		}
		
		return new ArrayList<>(out);
	}
}
