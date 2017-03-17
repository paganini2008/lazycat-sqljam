package lazycat.series.sqljam;

import lazycat.series.sqljam.transcation.TransactionFactory;

/**
 * SessionFactory
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface SessionFactory {

	void setTransactionFactory(TransactionFactory transactionFactory);

	SessionExecutor getSessionExecutor();

	Session openSession();
	
	Session openSession(boolean autoRollback);

}