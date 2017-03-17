package lazycat.series.sqljam.example.model;

import lazycat.series.sqljam.AutoDdl;
import lazycat.series.sqljam.annotation.Column;
import lazycat.series.sqljam.annotation.PrimaryKey;
import lazycat.series.sqljam.annotation.Table;

@Table(name = "tb_album", autoDdl = AutoDdl.UPDATE, comment = "This is a album table.")
public class Album {

	@PrimaryKey
	@Column(autoIncrement = true)
	private int id;
	@Column(nullable = false,length=1000)
	private String name;
	@Column(length = 4000)
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
