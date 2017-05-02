package lazycat.series.sqljam.admin;

public interface TableOptions {

	boolean exists();

	void modify();

	void create();

	void drop();

	void validate();

}
