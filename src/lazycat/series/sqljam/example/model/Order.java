package lazycat.series.sqljam.example.model;

import java.math.BigDecimal;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.AutoDdl;
import lazycat.series.sqljam.annotation.Column;
import lazycat.series.sqljam.annotation.ForeignKey;
import lazycat.series.sqljam.annotation.PrimaryKey;
import lazycat.series.sqljam.annotation.Table;

@Table(name = "tb_order", autoDdl = AutoDdl.UPDATE)
public class Order {

	@PrimaryKey
	@Column(autoIncrement = true)
	private int id;

	@Column(jdbcType = JdbcType.DECIMAL, scale = 2, precision = 10)
	private BigDecimal price;

	@ForeignKey(refMappedClass = User.class, refMappedProperty = "id")
	@Column
	private int uid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String toString() {
		return "Order [id=" + id + ", price=" + price + ", uid=" + uid + "]";
	}

}
