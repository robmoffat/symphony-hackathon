package com.db.symphonyp.tabs;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TableConverter {
	
	@Autowired
	ObjectMapper om;

	public String getMessageML(Table t) {
		List<String> columns = t.getColumns();
		return "<messageML><table><thead><tr>"+getColumnML(columns)+"</tr></thead><tbody>"+
				getRowsML(t.getRows(),columns)+"</tbody></table></messageML>";
	}
	
	private String getRowsML(List<Map<String, Object>> rows, List<String> columns) {
		return "<tr>"+rows.stream().map(r -> getRowML(r, columns)).reduce("", (a,b) -> a+b)+"</tr>";
	}

	private String getRowML(Map<String, Object> r, List<String> columns) {
		return columns.stream()
			.map(c -> r.getOrDefault(c, ""))
			.map(v -> "<td>"+v+"</td>")
			.reduce("", (a,b) -> a+b);
	}

	private String getColumnML(List<String> columns) {
		return columns.stream().map(c -> "<td>"+c+"</td>").reduce("", (a, b) -> a+b);
	}

	public String getJson(Table t) {
		return null;
	}
}
