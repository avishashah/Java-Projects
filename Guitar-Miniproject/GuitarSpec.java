package com.aurionpro.model;

public class GuitarSpec {
	private Builder builder;
	private Type type;
	private Wood backWood;
	private Wood topWood;
	private String model;

	public GuitarSpec(Builder builder, Type type, Wood backWood, Wood topWood, String model) {
		this.builder = builder;
		this.type = type;
		this.backWood = backWood;
		this.topWood = topWood;
		this.model = model;
	}

	public Builder getBuilder() {
		return builder;
	}

	public Type getType() {
		return type;
	}

	public Wood getBackWood() {
		return backWood;
	}

	public Wood getTopWood() {
		return topWood;
	}

	public String getModel() {
		return model;
	}

	public boolean matches(GuitarSpec other) {
		if (other.builder != null && builder != other.builder)
			return false;
		if (other.model != null && !other.model.equalsIgnoreCase("") && !model.equalsIgnoreCase(other.model))
			return false;
		if (other.type != null && type != other.type)
			return false;
		if (other.backWood != null && backWood != other.backWood)
			return false;
		if (other.topWood != null && topWood != other.topWood)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "\nModel: " + model + "\nBuilder: " + builder + "\nType: " + type + "\nBackWood: " + backWood + "\nTopWood: "
				+ topWood +"\n------------------";
	}

}
