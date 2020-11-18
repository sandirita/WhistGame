package com.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class Game {
	private Player host;
    private Vector<Player> players = new Vector<Player>(8);
    public ArrayList<Card> deck;
    private Card trump;
    private final Integer id;

    public Game(Player host, Integer id) {
        this.host = host;
        players.add(host);
        this.id = id;
    }


    public Player getHost(){
        return host;
    }

    public Vector<Player> getPlayers() {
        return players;
    }

    public void join(Player p1) {
        players.add(p1);
    }

    public void dealCards() {
        add();
        shuffle();

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            Card c = deck.remove(deck.size() -1);
            p.addCard(c);
        }
        trump = deck.remove(deck.size() - 1);
    }

    public void add() {
        for (Color color : Color.values()) {
            for (Name name: Name.values()) {
                if ((players.size() == 4) && (name.ordinal() > Name.SIX.ordinal())) {
                    deck.add(new Card(color, name));
                }
                else if ((players.size() == 3) && (name.ordinal() > Name.EIGHT.ordinal())) {
                    deck.add(new Card(color, name));
                }
                else if ((players.size() == 2) && (name.ordinal() > Name.JUMBO.ordinal())) {
                    deck.add(new Card(color, name));
                }

            }
        }
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }
    
    public Integer getId() {
    	return id;
    }

}
