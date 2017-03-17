package lazycat.series.sqljam.example.model;

import java.util.Date;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.AutoDdl;
import lazycat.series.sqljam.annotation.Column;
import lazycat.series.sqljam.annotation.ForeignKey;
import lazycat.series.sqljam.annotation.PrimaryKey;
import lazycat.series.sqljam.annotation.Table;
import lazycat.series.sqljam.annotation.UniqueKey;

@Table(name = "tb_image", autoDdl = AutoDdl.UPDATE, comment = "This is a image table.")
public class Image {

	@PrimaryKey
	@Column(autoIncrement = true)
	private int id;

	@Column(jdbcType = JdbcType.SMALLINT, nullable = false)
	private int flag;

	@UniqueKey
	@Column
	private String url;

	@Column
	private byte[] content;

	@Column(length = 4000)
	private String description;

	@Column(jdbcType = JdbcType.CLOB, nullable = false)
	private String info;

	@Column(jdbcType = JdbcType.REAL)
	private float score;

	@Column(defaultValue = "current_timestamp")
	private Date lastModified;

	@Column
	private long follow;

	@Column
	private boolean visible;

	@ForeignKey(refMappedProperty = "id", refMappedClass = Album.class)
	@Column
	private int albumId;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public long getFollow() {
		return follow;
	}

	public void setFollow(long follow) {
		this.follow = follow;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

}
