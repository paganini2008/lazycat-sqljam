package lazycat.series.sqljam.feature;

import java.sql.DatabaseMetaData;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.relational.ColumnDefinition;

/**
 * SqlServerFeature
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SqlServerFeature extends BasicFeature {

	public SqlServerFeature() {
		registerColumnType(JdbcType.BINARY, "binary($l)");
		registerColumnType(JdbcType.BIT, "bit");
		registerColumnType(JdbcType.BIGINT, "bigint");
		registerColumnType(JdbcType.SMALLINT, "smallint");
		registerColumnType(JdbcType.TINYINT, "smallint");
		registerColumnType(JdbcType.INTEGER, "int");
		registerColumnType(JdbcType.CHAR, "char($l)");
		registerColumnType(JdbcType.NCHAR, "nchar($l)");
		registerColumnType(JdbcType.REAL, "real");
		registerColumnType(JdbcType.FLOAT, "float");
		registerColumnType(JdbcType.DOUBLE, "double precision");
		registerColumnType(JdbcType.DATE, "date");
		registerColumnType(JdbcType.TIME, "time");
		registerColumnType(JdbcType.TIMESTAMP, "datetime2");
		registerColumnType(JdbcType.VARBINARY, "varbinary($l)");
		registerColumnType(JdbcType.NUMERIC, "numeric($p,$s)");
		registerColumnType(JdbcType.BOOLEAN, "bit");
		registerColumnType(JdbcType.BLOB, "varbinary(MAX)");
		registerColumnType(JdbcType.CLOB, "varchar(MAX)");
		registerColumnType(JdbcType.VARBINARY, "varbinary(MAX)");
		registerColumnType(JdbcType.VARBINARY, 8000, "varbinary($l)");
		registerColumnType(JdbcType.LONGVARBINARY, "varbinary(MAX)");
		registerColumnType(JdbcType.LONGVARCHAR, "varchar(MAX)");
		registerColumnType(JdbcType.VARCHAR, "varchar(MAX)");
		registerColumnType(JdbcType.VARCHAR, 8000, "varchar($l)");
		registerColumnType(JdbcType.NVARCHAR, "nvarchar(MAX)");
		registerColumnType(JdbcType.NVARCHAR, 4000, "nvarchar($l)");
	}

	protected String getAddColumnSqlString() {
		return "alter table ? add ?";
	}

	protected String getDropColumnSqlString() {
		return "alter table ? drop ?";
	}

	protected String getModifyColumnSqlString() {
		return "alter table ? alter ?";
	}

	protected String getAddPrimaryKeySqlString() {
		return "alter table ? add constraint ? primary key (?)";
	}

	protected String getSetPrimaryKeySqlString() {
		return "constraint ? primary key (?)";
	}

	protected String getDropPrimaryKeySqlString() {
		return "alter table ? drop constraint ?";
	}

	protected String getAddForeignKeySqlString() {
		return "alter table ? add constraint ? foreign key (?) references ?(?)";
	}

	protected String getDropForeignKeySqlString() {
		return "alter table ? drop constraint ?";
	}

	protected String getSetForeignKeySqlString() {
		return "constraint ? foreign key (?)";
	}

	protected String getAddUniqueKeySqlString() {
		return null;
	}

	protected String getDropUniqueKeySqlString() {
		return null;
	}

	protected String getSetUniqueKeySqlString() {
		return null;
	}

	protected String getIdentitySqlString() {
		return "identity(?,?)";
	}

	public String selectUUID() {
		return "select newid()";
	}

	protected String getAddTableCommentSqlString() {
		return null;
	}

	@Override
	protected int[] getColumnModified(ColumnDefinition columnDefinition, DatabaseMetaData dbmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String defineColumn(ColumnDefinition columnDefinition) {
		// TODO Auto-generated method stub
		return null;
	}

}
