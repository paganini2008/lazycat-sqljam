package lazycat.series.sqljam;

import lazycat.series.sqljam.feature.Feature;

/**
 * FeatureFactory
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface FeatureFactory {

	Feature buildFeature(ProductMetadata productMetadata);

}
