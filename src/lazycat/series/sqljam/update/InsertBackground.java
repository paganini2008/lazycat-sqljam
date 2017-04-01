package lazycat.series.sqljam.update;

import lazycat.series.concurrent.FutureCallback;
import lazycat.series.sqljam.KeyStore;

public interface InsertBackground extends Background {

	void execute(KeyStore keyStore);

	void execute(KeyStore keyStore, FutureCallback<Integer> callback);

}
