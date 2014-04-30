package com.test.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.test.repo.WordRepository;

@ServerEndpoint(value = "/test")
public class WSServerEndpoint {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@OnOpen
	public void onOpen(Session session) {
		logger.info("Connected ... " + session.getId());
	}

	@OnMessage
	public String onMessage(String unscrambledWord, Session session) {
		switch (unscrambledWord) {
		case "start":
			logger.info("Starting the game by sending first word");
			try {
				String scrambledWord = WordRepository.getInstance().getScrambledRandomWord();
				session.getUserProperties().put("scrambledWord", scrambledWord);
				return scrambledWord;
			} catch (IOException e) {
				logger.log(Level.SEVERE, "", e);
				return e.getMessage();
			}

		case "quit":
			try {
				session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Game ended"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			break;

		}
		String scrambledWord = (String) session.getUserProperties().get("scrambledWord");
		return checkLastWordAndSendANewWord(scrambledWord, unscrambledWord, session);
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
	}

	private String checkLastWordAndSendANewWord(String scrambledWord, String unscrambledWord, Session session) {
		try {
			WordRepository repository = WordRepository.getInstance();
			if (repository.isUnscrambledWord(scrambledWord, unscrambledWord)) {
				String nextScrambledWord = repository.getScrambledRandomWord();
				session.getUserProperties().put("scrambledWord", nextScrambledWord);
				return String.format("You guessed it right. Try the next word ...  %s", nextScrambledWord);
			} else {
				return String.format("You guessed it wrong. Try again!");
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "", e);
			return e.getMessage();
		}

	}

}
