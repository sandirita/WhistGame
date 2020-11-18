package com.game;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import sun.management.NotificationEmitterSupport;

@Controller
public class WhistController {

	@Autowired
	GameService gameService;
	ExecutorService sseExecutor = Executors.newSingleThreadExecutor();
	
	CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<SseEmitter>();
	public int eventID;
	
	
	public void sendEvents(String messageName, Object o ) {
		sseExecutor.execute(()-> {
			for (SseEmitter emitter : emitters) {
				SseEventBuilder eb = SseEmitter.event()
		               .id(Integer.toString(eventID++))
		               .name(messageName)
		               .data(o);
			
				try {
					emitter.send(eb);
				} catch (IOException e) {
					System.out.println("exception");
				}
			}
		});
	}
	
	@GetMapping("/")
	public String index(
			@CookieValue(name="name", required= false) 
			String name,
			ModelMap model) {
		
		model.addAttribute("name", name);
		model.addAttribute("games", gameService.getGames());
		
		return "index";
	}
	
	@PostMapping ("/newGame")
	public ModelAndView newGame(@RequestParam (name= "name") String name, 
			ModelMap model, 
			HttpServletResponse response){
		
		Player player = new Player(name);
		
		Cookie cookie = new Cookie("name",name);
		response.addCookie(cookie);
		
		Game actualGame = gameService.newGame(player);
		Integer id = actualGame.getId();
		
		sendEvents("gameCreated", gameService.getGames());
		model.addAttribute("id", id);
		
		return new ModelAndView("redirect:/game", model);
	}	
	
	@PostMapping ("/joinGame")
	public ModelAndView joinGame(@RequestParam (name= "name") String name, 
			ModelMap model, @RequestParam (name= "id") Integer id, 
			HttpServletResponse response) {
		
		Game actualGame = gameService.getGame(id);
    
		Vector<Player> players = actualGame.getPlayers();
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getName().equals(name)) {
				model.addAttribute("error", "same name");
				
				return new ModelAndView("redirect:/", model);
			}
		}
		
		Player p1 = new Player(name);
		actualGame.join(p1);
		
		sendEvents("playerJoined", actualGame.getPlayers());
		
		model.addAttribute("name", name);
		
		Cookie cookie = new Cookie("name",name);
		response.addCookie(cookie);
		
		model.addAttribute("id", id);
	
		return new ModelAndView("redirect:/game", model);
		
	}
	
	@GetMapping("/game")
	public String game(ModelMap model, 
			@CookieValue(name="name", required= true) 
			String name, @RequestParam (name= "id") Integer id) {
		
		model.addAttribute("game", gameService.getGame(id));
		model.addAttribute("name", name);
		
		return "game";
		
	}
	
	@DeleteMapping("/game")
	public ResponseEntity<String> endGame(@RequestParam (name= "id") Integer id) {
		gameService.endGame(id);
		
		return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
	}
	
	
	
	@GetMapping(path= "/sse")
	SseEmitter sse() {
		SseEmitter emitter = new SseEmitter();
		emitters.add(emitter);
	
		emitter.onCompletion(() -> {
			emitters.remove(emitter);
		});
		
	    return emitter;
	}	
}
