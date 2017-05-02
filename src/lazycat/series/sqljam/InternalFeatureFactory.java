package lazycat.series.sqljam;

import java.util.Map;

import lazycat.series.collection.CaseInsensitiveMap;
import lazycat.series.sqljam.feature.Feature;
import lazycat.series.sqljam.feature.MySqlFeature;
import lazycat.series.sqljam.feature.PostgreSqlFeature;
import lazycat.series.sqljam.feature.SqlServerFeature;

/**
 * InternalFeatureFactory
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class InternalFeatureFactory implements FeatureFactory {

	private final Map<String, Feature> featureRegistry = new CaseInsensitiveMap<Feature>();

	public InternalFeatureFactory() {
		registerFeatures();
	}

	protected void registerFeatures() {
		featureRegistry.put("mysql", new MySqlFeature());
		featureRegistry.put("postgresql", new PostgreSqlFeature());
		featureRegistry.put("sqlserver", new SqlServerFeature());
	}

	public Feature buildFeature(ProductMetadata productMetadata) {
		String productName = productMetadata.getDatabaseProductName();
		return featureRegistry.get(productName);
	}

}
