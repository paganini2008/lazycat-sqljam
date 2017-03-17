package lazycat.series.sqljam;

import java.util.Map;

import lazycat.series.collection.CaseInsensitiveMap;

public class FunctionMapper { 

	private final Map<String, String> functionTemplates = new CaseInsensitiveMap<String>();

	public void put(String name, String template) {
		functionTemplates.put(name, template);
	}
	
	public String get(String name) {
		return functionTemplates.get(name);
	}
}
