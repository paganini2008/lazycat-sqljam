package lazycat.series.sqljam;

import lazycat.series.lang.Hook;
import lazycat.series.lang.Hooks;

/**
 * CreateDropDdlResolver
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class CreateDropDdlResolver extends CreateDdlResolver {

	public void resolve(final JdbcAdmin jdbcAdmin, final Class<?> mappedClass) {
		super.resolve(jdbcAdmin, mappedClass);
		Hooks.addHook(new Hook() {
			public void run() {
				jdbcAdmin.dropTable(mappedClass);
			}

			public int getPriority() {
				return 0;
			}

			public void handleThrown(Throwable e) {
				throw new DdlResolverException(e);
			}
		});
	}

}
