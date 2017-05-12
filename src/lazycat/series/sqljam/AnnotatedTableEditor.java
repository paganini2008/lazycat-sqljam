package lazycat.series.sqljam;

import java.lang.reflect.Field;
import java.util.Iterator;

import lazycat.series.lang.StringUtils;
import lazycat.series.reflect.FieldUtils;
import lazycat.series.sqljam.annotation.Column;
import lazycat.series.sqljam.annotation.Default;
import lazycat.series.sqljam.annotation.ForeignKey;
import lazycat.series.sqljam.annotation.Generator;
import lazycat.series.sqljam.annotation.PrimaryKey;
import lazycat.series.sqljam.annotation.Table;
import lazycat.series.sqljam.annotation.UniqueKey;
import lazycat.series.sqljam.relational.ColumnEditor;
import lazycat.series.sqljam.relational.TableEditorImpl;

/**
 * AnnotatedTableEditor
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class AnnotatedTableEditor extends TableEditorImpl {

	public AnnotatedTableEditor(Class<?> mappedClass, JdbcAdmin jdbcAdmin) {
		super(mappedClass, jdbcAdmin);
		mapClass(mappedClass);
	}

	private void mapClass(Class<?> mappedClass) {
		Table annotation = mappedClass.getAnnotation(Table.class);
		if (annotation == null) {
			throw new MappingException("Unrecognized mappedClass: " + mappedClass);
		}
		setName(annotation.schema(), annotation.name());
		setAutoDdl(annotation.autoDdl());
		setComment(annotation.comment());
		setDefineConstraintOnCreate(annotation.defineConstraintOnCreate());
		for (Iterator<Field> iter = FieldUtils.fieldIterator(mappedClass); iter.hasNext();) {
			Field field = iter.next();
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				ColumnEditor columnEditor = addColumn(field.getName(), field.getType(),
						StringUtils.isNotBlank(column.name()) ? column.name() : StringUtils.toSegmentCase(field.getName(), "_"),
						column.jdbcType());
				mapColumn(columnEditor, column);
				mapOther(columnEditor, field);
			}
		}
	}

	private void mapColumn(ColumnEditor columnEditor, Column column) {
		columnEditor.setLength(column.length());
		columnEditor.setPrecision(column.precision());
		columnEditor.setScale(column.scale());
		columnEditor.setNullable(column.nullable());
		columnEditor.setAutoIncrement(column.autoIncrement());
		columnEditor.setUnsigned(column.unsigned());
		columnEditor.setComment(column.comment());
		columnEditor.setDefaultValue(column.defaultValue());
	}

	protected void mapOther(ColumnEditor columnEditor, Field field) {
		if (field.isAnnotationPresent(PrimaryKey.class)) {
			PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
			columnEditor.setNullable(false);
			columnEditor.asPrimaryKey().setConstraintName(primaryKey.name()).setPosition(primaryKey.position());
		}
		if (field.isAnnotationPresent(UniqueKey.class)) {
			if (getTableDefinition().isPrimaryKey(columnEditor.getColumnDefinition().getMappedProperty())) {
				throw new MappingException(
						"Duplicated unique key. MappedProperty: " + columnEditor.getColumnDefinition().getMappedProperty()
								+ ", ColumnName: " + columnEditor.getColumnDefinition().getColumnName());
			}
			UniqueKey uniqueKey = field.getAnnotation(UniqueKey.class);
			columnEditor.asUniqueKey().setConstraintName(uniqueKey.name()).setPosition(uniqueKey.position());
		}
		if (field.isAnnotationPresent(ForeignKey.class)) {
			ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
			columnEditor.asForeignKey(foreignKey.refMappedClass(), foreignKey.refMappedProperty()).setConstraintName(foreignKey.name())
					.setPosition(foreignKey.position()).setRequired(foreignKey.required()).setOnUpdate(foreignKey.onUpdate())
					.setOnDelete(foreignKey.onDelete());
		}
		if (field.isAnnotationPresent(Default.class)) {
			Default def = field.getAnnotation(Default.class);
			addDefault(field.getName()).setValue(def.value()).setDataType(def.dataType());
		}
		if (field.isAnnotationPresent(Generator.class)) {
			Generator generator = field.getAnnotation(Generator.class);
			columnEditor.useGenerator(generator.value(), generator.name());
		}
	}

}
