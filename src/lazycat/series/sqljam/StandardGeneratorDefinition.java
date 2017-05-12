package lazycat.series.sqljam;

import lazycat.series.sqljam.generator.Generator;

public class StandardGeneratorDefinition implements GeneratorDefinition {

	private final String generatorType;
	private final String name;
	private final Generator generator;

	public String getGeneratorType() {
		return generatorType;
	}

	public String getName() {
		return name;
	}

	public Generator getGenerator() {
		return generator;
	}

	public StandardGeneratorDefinition(String generatorType, String name, Generator generator) {
		this.generatorType = generatorType;
		this.name = name;
		this.generator = generator;
	}

}
