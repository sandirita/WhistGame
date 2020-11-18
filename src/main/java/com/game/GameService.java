package com.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class GameService {
	Map<Integer, Game> games = new HashMap<Integer, Game>();
	int i;
	
	public Game getGame(Integer id) {
		return games.get(id);
	}
	public Game newGame(Player player) {
		Game game = new Game(player, i);
		games.put(i++, game);
		return game;
	}
	
	public void endGame(Integer id) {
		games.remove(id);
	}
	
	public ArrayList<Game> getGames(){
		return new ArrayList<Game>(games.values());
	}
}
