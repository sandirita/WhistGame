package com.game;

public class Card {
	private Color color;
	private Name name;

	public Color getColor() {
	    return color;
	}

    public Name getName() {
        return name;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Card (Color color, Name name) {
        this.color = color;
        this.name = name;
    }
}

	enum Color {CLUB, DIAMOND, HEARTS, SPADE}

