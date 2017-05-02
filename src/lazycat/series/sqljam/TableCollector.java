package lazycat.series.sqljam;

import java.util.Observable;

public class TableCollector extends Observable {
	
	public void collect(){
		
	}

	public void reflect() {
		this.setChanged();
		this.notifyObservers();
	}

}
