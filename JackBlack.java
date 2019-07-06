package Dag1;

import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class JackBlack {

	public static void main(String[] args) {
		new BlackJack();
	}

}

class BlackJack {
	static String[] names = { "Joel", "Grant", "Normand", "Curt", "Markus", "Adalberto", "Rusty", "Thaddeus", "Bud",
			"Brendon", "Alfred", "Lauren", "Darwin", "Victor", "Gino", "Nicolas", "Lynn", "Fletcher", "Carlton",
			"Gordon" };
	static Scanner scanner = new Scanner(System.in);
	static Random rnd = new Random();
	static ArrayList<Cards> card = new ArrayList<Cards>();
	static ArrayList<Players> player = new ArrayList<Players>();
	static boolean gameIsOn = true;
	static int currentPlayersTurn = 0;
	static LocalTime[] nu = new LocalTime[2];
	static int timeATurn = 30;

	BlackJack() {
		setupCards();
		player.add(new Players("Dealer"));
		allCardsToDealer();
		shuffleCards(0);
		System.out.println(player.get(0).naam + " displays his cards:");
		for (int i = 0; i < player.get(0).card.size(); i++) {
			System.out.print("[" + player.get(0).card.get(i).naam + player.get(0).card.get(i).type + "] ");
			if (player.get(0).card.get(i).naam.length() == 1) {
				System.out.print(" ");
			}
			if ((i + 1) % 13 == 0) {
				System.out.println();
			}
		}
		System.out.print("The dealer will shuffle his deck again after you press enter...");
		scanner.nextLine();
		System.out.println("The dealer is shuffling his deck once more...");
		shuffleCards(0);
		System.out.println(
				"The game will begin after the first player joins.\nPlayers will have "+timeATurn+" seconds to decide their actions, if they take longer they are disqualified!\nType 'j' to let a player join.\nType 'k' to get a card.\nType 'q' for a player to quit.\nType 'p' for a player to pass.");
		System.out.println("Let the games begin...");
		while (gameIsOn) {
			nu[0] = LocalTime.now();
			System.out.print("Command: ");
			String command = scanner.nextLine().trim().toLowerCase();
			if (player.size() < 2) {
				if (command.equals("j")) {
					String nameJoined = names[rnd.nextInt(names.length)];
					player.add(new Players(nameJoined));
					System.out.println(nameJoined + " has joined the game...");
					System.out.println("The game will start...");
				}
			} else {
				if (currentPlayersTurn == 0) {
					currentPlayersTurn = 1;
				}
				nu[1] = LocalTime.now();
				if (ChronoUnit.SECONDS.between(nu[0], nu[1]) > timeATurn) {
					timeIsUp((int) ChronoUnit.SECONDS.between(nu[0], nu[1]));
				} else {
					if (command.equals("j")) {
						String nameJoined = names[rnd.nextInt(names.length)];
						player.add(new Players(nameJoined));
						System.out.println(nameJoined + " has joined the game...");
					}
					if (command.equals("q")) {
						playerQuit();
					}
					if (command.equals("k")) {
						System.out.println(player.get(currentPlayersTurn).naam + " pulls a card");
						if (player.get(0).card.size() > 0) {
							giveCardFromTo(0, 0, currentPlayersTurn);
							System.out.println("... and he pulls a ["
									+ player.get(currentPlayersTurn).card
											.get(player.get(currentPlayersTurn).card.size() - 1).naam
									+ player.get(currentPlayersTurn).card
											.get(player.get(currentPlayersTurn).card.size() - 1).type
									+ "]");
							if (player.get(currentPlayersTurn).card
									.get(player.get(currentPlayersTurn).card.size() - 1).naam.equals("A")) {
								System.out.print(player.get(currentPlayersTurn).naam
										+ " pulls a Ace, would you like this to count as 1 points or 11 points?: ");
								int changeAceValue = scanner.nextInt();
								while (!(changeAceValue == 1 || changeAceValue == 11)) {
									System.out.print(changeAceValue + " is not 1 or 11, please try again: ");
									changeAceValue = scanner.nextInt();
								}
								player.get(currentPlayersTurn).card
										.get(player.get(currentPlayersTurn).card.size() - 1).waarde = changeAceValue;
							}
							System.out.println(player.get(currentPlayersTurn).naam + " number of cards are "
									+ player.get(currentPlayersTurn).card.size());
						} else {
							System.out.println("Awkard! The dealer has no cards left");
						}
					}
					if (command.equals("p")) {
						System.out.println(player.get(currentPlayersTurn).naam + " passes this turn...");
					}
				}

				nextTurn();

			}
		}
	}

	private void timeIsUp(int n) {
		System.out.println(player.get(currentPlayersTurn).naam + " took to long to decide his move. " + n
				+ " seconds instead of "+timeATurn+". He must leave the game.");
		playerQuit();
	}

	private void nextTurn() {

		if (player.size() >= 2) {
			int calculateTotalPoints = 0;
			for (Cards spelerKaarten : player.get(currentPlayersTurn).card) {
				calculateTotalPoints += spelerKaarten.waarde;
			}

			if (calculateTotalPoints > 21) {
				System.out.println(player.get(currentPlayersTurn).naam + " has a total points of "
						+ calculateTotalPoints + " which exceeds the limit of 21.");
				System.out.println(player.get(currentPlayersTurn).naam + " lost the game.");
				playerQuit();
			} else if (calculateTotalPoints == 21) {
				System.out.println(player.get(currentPlayersTurn).naam
						+ " has the maximum score! He can run away with the prize!");
				playerQuit();
			} else {
				System.out
						.println(player.get(currentPlayersTurn).naam + " has a total score of " + calculateTotalPoints);
			}
		}

		if (player.size() >= 2) {
			currentPlayersTurn++;
			if (currentPlayersTurn > player.size() - 1) {
				currentPlayersTurn = 1;
			}
			System.out.println("Next player is " + player.get(currentPlayersTurn).naam + " he displays his cards:");
			for (int i = 0; i < player.get(currentPlayersTurn).card.size(); i++) {
				System.out.print("[" + player.get(currentPlayersTurn).card.get(i).naam
						+ player.get(currentPlayersTurn).card.get(i).type + "] ");
				if (player.get(currentPlayersTurn).card.get(i).naam.length() == 1) {
					System.out.print(" ");
				}
				if ((i + 1) % 13 == 0) {
					System.out.println();
				}
			}
		} else {
			System.out.println("Waiting for players to join the game... (new players can join by enterint 'j')");
		}
	}

	private void playerQuit() {
		int calculateTotalPoints = 0;
		for (Cards spelerKaarten : player.get(currentPlayersTurn).card) {
			calculateTotalPoints += spelerKaarten.waarde;
		}
		System.out.println(
				player.get(currentPlayersTurn).naam + " has left the game with " + calculateTotalPoints + " points...");
		for (int i = 0; i < player.get(currentPlayersTurn).card.size(); i++) {
			giveCardFromTo(0, currentPlayersTurn, 0);
		}
		System.out.println("Returning the players cards back into the deck...");
		shuffleCards(0);
		System.out.println("Shuffling the dealers deck...");
		player.remove(currentPlayersTurn);
		if (player.size() < 2) {
			System.out.println("There are no players currently in the game...");
		}
	}

	private void giveCardFromTo(int cardnr, int n1, int n2) {
		player.get(n2).getCard(player.get(n1).card.get(cardnr));
		player.get(n1).card.remove(cardnr);
	}

	private void allCardsToDealer() {
		for (int i = 0; i < card.size(); i++) {
			player.get(0).getCard(card.get(i));
		}
		card.removeAll(card);
	}

	private void shuffleCards(int playerDeck) {
		Collections.shuffle(player.get(playerDeck).card);
	}

	// Maakt alle benodigde kaarten aan voor het spel
	private void setupCards() {
		String type = null;
		for (int i = 0; i < 4; i++) {
			if (i == 0) {
				type = "H";
			} else if (i == 1) {
				type = "S";
			} else if (i == 2) {
				type = "K";
			} else if (i == 3) {
				type = "R";
			}
			for (int j = 2; j < 11; j++) {
				card.add(new Cards(String.valueOf(j), type, j));
			}
			card.add(new Cards("B", type, 10));
			card.add(new Cards("V", type, 10));
			card.add(new Cards("H", type, 10));
			card.add(new Cards("A", type, 11));
		}
	}
}

class Cards {
	String place = "deck";
	String naam;
	String type;
	int waarde;

	Cards(String naam, String type, int waarde) {
		this.naam = naam;
		this.type = type;
		this.waarde = waarde;
	}
}

class Players {
	String naam;
	ArrayList<Cards> card = new ArrayList<Cards>();

	Players(String naam) {
		this.naam = naam;
	}

	public void getCard(Cards cards) {
		card.add(cards);
	}
}
