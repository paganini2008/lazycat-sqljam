package lazycat.series.sqljam.example.model;

import java.math.BigInteger;
import java.util.Date;

import lazycat.series.beans.ToStringBuilder;
import lazycat.series.sqljam.AutoDdl;
import lazycat.series.sqljam.annotation.Column;
import lazycat.series.sqljam.annotation.PrimaryKey;
import lazycat.series.sqljam.annotation.Table;

@Table(name = "tb_article_clone", autoDdl = AutoDdl.UPDATE, comment = "This is a article clone table.")
public class ArticleCopy {
	
	@PrimaryKey
	@Column(autoIncrement = true)
	private int id;
	@PrimaryKey
	@Column(length=255)
	private String author;
	@Column(length = 255, nullable = false)
	private String title;
	@Column(length = 4000, nullable = false)
	private String text;
	@Column(length = 250, nullable = false)
	private String content;
	@Column(length = 250, nullable = false, comment = "网址链接")
	private String url;
	@Column
	private float score;
	@Column(comment = "最后更新时间", defaultValue = "current_timestamp")
	private Date lastModified;

	@Column
	private BigInteger followCount;

	public BigInteger getFollowCount() {
		return followCount;
	}

	public void setFollowCount(BigInteger followCount) {
		this.followCount = followCount;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	public String toString(){
		return ToStringBuilder.reflectInvokeToString(this);
	}
}
