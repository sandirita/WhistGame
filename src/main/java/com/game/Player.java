package com.game;

import java.util.ArrayList;

public class Player {
	private String name;
    private ArrayList<Card> cards;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Player(String name) {
        this.name = name;
    }

    public String toString() {
        return getName();
    }

    public void addCard(Card c) {
        cards.add(c);
    }

}
