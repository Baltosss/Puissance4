package com.puissance4.model;

public class Player {
	private String name;
	private int slotColor;
	
	public Player(String name, int slotColor)
	{
		this.name = name;
		this.slotColor = slotColor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSlotColor() {
		return slotColor;
	}

	public void setSlotColor(int slotColor) {
		this.slotColor = slotColor;
	}
}
