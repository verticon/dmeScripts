package io.verticon.dmescripts.controllers;

public class Product {

	public enum Category {
		IndwellingCatheter("Indwelling (foley) Catheter"),
		CondomCatheter("Condom Catheter"),
		IntermittentCatheter("Intermittent Catheter");

	    private final String description;       

	    private Category(String description) { this.description = description; }

	    @Override
	    public String toString() { return this.description; }
	}

	private String name;
	private String hcpc;
	private Category category;

	public Product(Category category, String name, String hcpc) {
		this.category = category;
		this.name = name;
		this.hcpc = hcpc;
	}

	public String getName() { return name; }

	public String getHcpc() { return hcpc; }

	public Category getCategory() { return category; }

	@Override
	public String toString() { return String.format("%s -> %s (%s)", category, name, hcpc); }

}
