package lazycat.series.sqljam;

import lazycat.series.sqljam.query.Cacheable;

public interface Cached {

	Cacheable cacheAs(String name);
	
}
