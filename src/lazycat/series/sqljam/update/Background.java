package lazycat.series.sqljam.update;

import lazycat.series.concurrent.FutureCallback;
import lazycat.series.sqljam.Executable;

public interface Background extends Executable {

	void execute();

	void execute(FutureCallback<Integer> callback);

}
