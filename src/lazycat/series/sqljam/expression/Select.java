package lazycat.series.sqljam.expression;

import java.util.ArrayList;
import java.util.List;

import lazycat.series.collection.CollectionUtils;
import lazycat.series.lang.ArrayUtils;
import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.relational.ColumnDefinition;

/**
 * SelectAll
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Select extends AbstractField {

	public static final Select ALL = new Select(null);

	private final Table table;
	private final String[] includedProperties;
	private boolean asProperty;

	public Select(String tableAlias) {
		this(tableAlias, true);
	}

	public Select(String tableAlias, boolean asProperty) {
		this(tableAlias, asProperty, null);
	}

	public Select(String tableAlias, boolean asProperty, String[] includedProperties) {
		this.table = new Table(tableAlias);
		this.includedProperties = includedProperties;
		this.asProperty = asProperty;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		final String tableAlias = table.getText(session, translator, configuration);
		String prefix = StringUtils.isNotBlank(tableAlias) ? tableAlias + "." : "";
		List<String> list = new ArrayList<String>();
		ColumnDefinition[] columnDefinitions = translator.getColumns(tableAlias, configuration);
		for (ColumnDefinition columnDefinition : columnDefinitions) {
			if (includedProperties == null || ArrayUtils.contains(includedProperties, columnDefinition.getMappedProperty())) {
				String columnName = prefix + columnDefinition.getColumnName();
				list.add(asProperty ? configuration.getJdbcAdmin().getFeature().columnAs(columnName, columnDefinition.getMappedProperty())
						: columnName);
			}
		}
		return CollectionUtils.join(list, ", ");
	}

}
