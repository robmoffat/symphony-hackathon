package com.db.symphonyp.tabs.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public String getJson(Table t) throws JsonProcessingException {
		Map<String, Object> out = new HashMap<String, Object>();
		Map<String, Object> ob1 = new HashMap<String, Object>();
		ob1.put("type", "com.db.symphonyp.tabs");
		ob1.put("version", "1.0");

		ob1.put("id", Collections.singletonList(t));

		out.put("object001", ob1);
		String outString = om.writeValueAsString(out);
		return outString;
	}

//	{
//	    "object001":
//	    {
//	        "type":     "org.symphonyoss.fin.security",
//	        "version":  "1.0",
//	        "id":
//	        [
//	            {
//	                "type":     "org.symphonyoss.fin.security.id.ticker",
//	                "value":    "IBM"
//	            },
//	            {
//	                "type":     "org.symphonyoss.fin.security.id.isin",
//	                "value":    "US0378331005"
//	            },
//	            {
//	                "type":     "org.symphonyoss.fin.security.id.cusip",
//	                "value":    "037833100"
//	            }
//	        ]
//	    }
//	}
}
