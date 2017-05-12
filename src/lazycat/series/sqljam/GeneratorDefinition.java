package lazycat.series.sqljam;

import lazycat.series.sqljam.generator.Generator;

public interface GeneratorDefinition {

	String getGeneratorType();

	String getName();

	Generator getGenerator();

}