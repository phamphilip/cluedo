/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.0.5092.1e2e91fc6 modeling language!*/


import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

// line 2 "model.ump"
// line 78 "model.ump"
public class Game {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Game Associations
    private List<Player> players;
    private Board board;
    private int numPlayers = 0;
    private boolean gameOver;
    private CharacterCard murderer;
    private WeaponCard murderWeapon;
    private RoomCard murderRoom;
    private List<CharacterCard> characterCards = createCharacterCards();
    private List<RoomCard> roomCards = createRoomCards();
    private List<WeaponCard> weaponCards = createWeaponCards();


    public static void main(String... args) {
        Game game = new Game();
        game.run();
    }

    private void run() {
        System.out.println("Welcome to Cluedo!\n\nHow many players are playing? (3-6)");
        boolean validPlayers = false;
        while (!validPlayers) {
            Scanner input = new Scanner(System.in);
            if (input.hasNextInt()) {
                numPlayers = input.nextInt();
                if (numPlayers < 3 || numPlayers > 6) System.out.println("Not a valid Number, try again.");
                else validPlayers = true;
            }
            else System.out.println("Not a number, try again.");
        }

        createBoard();
        createCards();

        System.out.println("The murder circumstance is:");
        System.out.println(murderer);
        System.out.println(murderWeapon);
        System.out.println(murderRoom);
        for (int i = 0; i < players.size(); i++) {
            System.out.println("\n" + players.get(i).getName() + ":");
            for (Card c : players.get(i).getCards()) {
                System.out.println(c.getName());
            }
        }
        System.out.println("\nPress ENTER to continue (Might have to press it twice)");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(board.toString());

        gameOver = false;
        int player = 0;
        while (!gameOver) {
            printNewLine();
            System.out.println("It is Player " + (player+1) + "'s turn");
            Player currentPlayer = players.get(player);
            currentPlayer.setSteps(rollDice());

            while (currentPlayer.getSteps() > 0) {
                boolean hasTurn = true;
                while (hasTurn) {
                    printNewLine();
                    System.out.println("You have " + currentPlayer.getSteps() + " steps remaining. Type a direction to move (e.g. \"down\") or \"cards\" to see your cards");
                    Scanner sc = new Scanner(System.in);
                    String input = sc.next().toLowerCase();

                    switch (input) {
                        case "up":
                            currentPlayer.move(board, 0, -1);
                            break;
                        case "down":
                            currentPlayer.move(board, 0, 1);
                            break;
                        case "left":
                            currentPlayer.move(board, -1, 0);
                            break;
                        case "right":
                            currentPlayer.move(board, 1, 0);
                            break;
                        case "cards":
                            for (Card c : currentPlayer.getCards()) {
                                System.out.println(c.getName());
                            }
                            break;
                        default:
                            System.out.println("Invalid Direction");
                    }
                    if (currentPlayer.getSteps() <= 0) hasTurn = false;
                    if (board.getLocations()[currentPlayer.getLocation().y][currentPlayer.getLocation().x] instanceof Room) {
                        printNewLine();
                        System.out.println("You have entered the " + ((Room)board.getLocation(currentPlayer.getLocation().x, currentPlayer.getLocation().y)).getName());
                        System.out.println("If you would like to make a suggestion enter \"suggestion\" otherwise press ENTER");
                        sc = new Scanner(System.in);
                        input = sc.next().toLowerCase();
                        if (input.equals("suggestion")) makeSuggestion();
                    }
                }
            }

            player++;
            if (player >= players.size()) player = 0;
        }
    }

    private void makeSuggestion() {
        listAllCards();
    }

    private void listAllCards() {
        printNewLine();
        System.out.printf("%-20s%-20s%s\n", "Characters", "Weapons", "Rooms");
        printNewLine();
        int maxTemp = Math.max(characterCards.size(), weaponCards.size());
        int maxFinal = Math.max(maxTemp, roomCards.size());
        for (int i = 0; i < maxFinal; i++) {
            if (i < characterCards.size()) System.out.printf("%-20s", characterCards.get(i));
            else System.out.printf("%-20s", "");
            if (i < weaponCards.size()) System.out.printf("%-20s", weaponCards.get(i));
            else System.out.printf("%-20s", "");
            if (i < roomCards.size()) System.out.printf("%-20s", roomCards.get(i));
            System.out.println();
        }
    }

    private void printNewLine() {
        for (int i = 0; i < 49; i++) {
            System.out.print("-");
        }
        System.out.print("\n");
    }

    private int rollDice() {
        int min = 1;
        int max = 6;
        int diceOne = (int) (Math.random() * (max - min + 1) + min);
        int diceTwo = (int) (Math.random() * (max - min + 1) + min);
        System.out.println("*Rolls Dice*\n\nDice One: " + diceOne + "\nDice Two: " + diceTwo + "\n");
        return diceOne+diceTwo;
    }

    private void createCards() {
        Collections.shuffle(characterCards);
        Collections.shuffle(roomCards);
        Collections.shuffle(weaponCards);
        murderer = characterCards.get(0);
        murderWeapon = weaponCards.get(0);
        murderRoom = roomCards.get(0);
        List<Card> cards = new ArrayList<>();
        cards.addAll(characterCards);
        cards.addAll(roomCards);
        cards.addAll(weaponCards);
        cards.remove(murderer);
        cards.remove(murderRoom);
        cards.remove(murderWeapon);
        Collections.shuffle(cards);
        int i = 0;
        while (!cards.isEmpty()) {
            players.get(i).addCard(cards.get(0));
            cards.remove(0);
            i++;
            if (i >= players.size()) i = 0;
        }
    }

    private List<WeaponCard> createWeaponCards() {
        List<WeaponCard> weaponCards = new ArrayList<>();
        weaponCards.add(new WeaponCard("Candlestick"));
        weaponCards.add(new WeaponCard("Dagger"));
        weaponCards.add(new WeaponCard("Lead Pipe"));
        weaponCards.add(new WeaponCard("Revolver"));
        weaponCards.add(new WeaponCard("Rope"));
        weaponCards.add(new WeaponCard("Spanner"));
        return weaponCards;
    }

    private List<RoomCard> createRoomCards() {
        List<RoomCard> roomCards = new ArrayList<>();
        roomCards.add(new RoomCard("Ball Room"));
        roomCards.add(new RoomCard("Conservatory"));
        roomCards.add(new RoomCard("Billiard Room"));
        roomCards.add(new RoomCard("Library"));
        roomCards.add(new RoomCard("Study"));
        roomCards.add(new RoomCard("Hall"));
        roomCards.add(new RoomCard("Lounge"));
        roomCards.add(new RoomCard("Dining Room"));
        roomCards.add(new RoomCard("Kitchen"));
        return roomCards;
    }

    private List<CharacterCard> createCharacterCards() {
        List<CharacterCard> characterCards = new ArrayList<>();
        characterCards.add(new CharacterCard("Miss Scarlett"));
        characterCards.add(new CharacterCard("Colonel Mustard"));
        characterCards.add(new CharacterCard("Mrs. White"));
        characterCards.add(new CharacterCard("Mr. Green"));
        characterCards.add(new CharacterCard("Mrs. Peacock"));
        characterCards.add(new CharacterCard("Professor Plum"));
        return characterCards;
    }

    private void createBoard() {
        String baseBoard =
                "|#|#|#|#|#|#|#|#|#|1|#|#|#|#|2|#|#|#|#|#|#|#|#|#|" +
                "|k|k|k|k|k|k|#|_|_|_|b|b|b|b|_|_|_|#|c|c|c|c|c|c|" +
                "|k|k|k|k|k|k|_|_|b|b|b|b|b|b|b|b|_|_|c|c|c|c|c|c|" +
                "|k|k|k|k|k|k|_|_|b|b|b|b|b|b|b|b|_|_|c|c|c|c|c|c|" +
                "|k|k|k|k|k|k|_|_|b|b|b|b|b|b|b|b|_|_|C|c|c|c|c|c|" +
                "|k|k|k|k|k|k|_|_|B|b|b|b|b|b|b|B|_|_|_|c|c|c|c|#|" +
                "|#|k|k|k|K|k|_|_|b|b|b|b|b|b|b|b|_|_|_|_|_|_|_|3|" +
                "|_|_|_|_|_|_|_|_|b|B|b|b|b|b|B|b|_|_|_|_|_|_|_|#|" +
                "|#|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|i|i|i|i|i|i|" +
                "|d|d|d|d|d|_|_|_|_|_|_|_|_|_|_|_|_|_|I|i|i|i|i|i|" +
                "|d|d|d|d|d|d|d|d|_|_|#|#|#|#|#|_|_|_|i|i|i|i|i|i|" +
                "|d|d|d|d|d|d|d|d|_|_|#|#|#|#|#|_|_|_|i|i|i|i|i|i|" +
                "|d|d|d|d|d|d|d|D|_|_|#|#|#|#|#|_|_|_|i|i|i|i|I|i|" +
                "|d|d|d|d|d|d|d|d|_|_|#|#|#|#|#|_|_|_|_|_|_|_|_|#|" +
                "|d|d|d|d|d|d|d|d|_|_|#|#|#|#|#|_|_|_|l|l|L|l|l|#|" +
                "|d|d|d|d|d|d|D|d|_|_|#|#|#|#|#|_|_|l|l|l|l|l|l|l|" +
                "|#|_|_|_|_|_|_|_|_|_|#|#|#|#|#|_|_|L|l|l|l|l|l|l|" +
                "|6|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|l|l|l|l|l|l|l|" +
                "|#|_|_|_|_|_|_|_|_|h|h|H|H|h|h|_|_|_|l|l|l|l|l|#|" +
                "|o|o|o|o|o|o|O|_|_|h|h|h|h|h|h|_|_|_|_|_|_|_|_|4|" +
                "|o|o|o|o|o|o|o|_|_|h|h|h|h|h|H|_|_|_|_|_|_|_|_|#|" +
                "|o|o|o|o|o|o|o|_|_|h|h|h|h|h|h|_|_|Y|y|y|y|y|y|y|" +
                "|o|o|o|o|o|o|o|_|_|h|h|h|h|h|h|_|_|y|y|y|y|y|y|y|" +
                "|o|o|o|o|o|o|o|_|_|h|h|h|h|h|h|_|_|y|y|y|y|y|y|y|" +
                "|o|o|o|o|o|o|#|5|#|h|h|h|h|h|h|#|_|#|y|y|y|y|y|y|";

        Scanner sc = new Scanner(baseBoard).useDelimiter("\\|");
        Location[][] tiles = new Location[25][24];
        int x = 0;
        int y = 0;
        while (sc.hasNext()) {
            String tile = sc.next();
            if (tile.equals("")) continue;
            Boolean isDoor = false;
            if (!Character.isLowerCase(tile.toCharArray()[0])) {
                isDoor = true;
                tile = tile.toLowerCase();
            }
            switch (tile) {
                case "_":
                    tiles[y][x] = new Floor(null);
                    break;
                case "#":
                    tiles[y][x] = new Wall();
                    break;
                case "k":
                    tiles[y][x] = new Room("Kitchen", "k", null, isDoor);
                    break;
                case "b":
                    tiles[y][x] = new Room("Ball Room", "b", null, isDoor);
                    break;
                case "c":
                    tiles[y][x] = new Room("Conservatory", "c", null, isDoor);
                    break;
                case "d":
                    tiles[y][x] = new Room("Dining Room", "d", null, isDoor);
                    break;
                case "i":
                    tiles[y][x] = new Room("Billiard Room", "i", null, isDoor);
                    break;
                case "l":
                    tiles[y][x] = new Room("Library", "l", null, isDoor);
                    break;
                case "o":
                    tiles[y][x] = new Room("Lounge", "o", null, isDoor);
                    break;
                case "h":
                    tiles[y][x] = new Room("Hall", "h", null, isDoor);
                    break;
                case "y":
                    tiles[y][x] = new Room("Study", "y", null, isDoor);
                    break;
                default:
                    int playerNum = Integer.parseInt(tile);
                    if (playerNum > numPlayers) {
                        tiles[y][x] = new Floor(null);
                        break;
                    }
                    Player player;
                    switch (playerNum) {
                        case 1:
                            player = new Player("1", new Point(x, y));
                            break;
                        case 2:
                            player = new Player("2", new Point(x, y));
                            break;
                        case 3:
                            player = new Player("3", new Point(x, y));
                            break;
                        case 4:
                            player = new Player("4", new Point(x, y));
                            break;
                        case 5:
                            player = new Player("5", new Point(x, y));
                            break;
                        default:
                            player = new Player("6", new Point(x, y));
                            break;
                    }
                    tiles[y][x] = new Floor(player);
                    players.add(player);
                    break;
            }
            x++;
            if (x == 24) {
                x = 0;
                y++;
            }
        }
        board = new Board(tiles);
    }

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Game() {
        players = new ArrayList<Player>();
        board = null;
        murderer = null;
        murderRoom = null;
        murderWeapon = null;
    }

    //------------------------
    // INTERFACE
    //------------------------
    /* Code from template association_GetMany */
    public Player getPlayer(int index) {
       return players.get(index);
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public int numberOfPlayers() {
        return players.size();
    }

    public boolean hasPlayers() {
      return players.size() > 0;
    }

    public int indexOfPlayer(Player aPlayer) {
      return players.indexOf(aPlayer);
    }

    /* Code from template association_GetOne */
    public Board getBoard() {
        return board;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfPlayers() {
        return 0;
    }

    /* Code from template association_AddUnidirectionalMany */
    public boolean addPlayer(Player player) {
        boolean wasAdded = false;
        if (players.contains(player)) {
            return false;
        }
        players.add(player);
        wasAdded = true;
        return wasAdded;
    }

    public boolean removePlayer(Player aPlayer) {
        boolean wasRemoved = false;
        if (players.contains(aPlayer)) {
            players.remove(aPlayer);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addPlayerAt(Player aPlayer, int index) {
        boolean wasAdded = false;
        if (addPlayer(aPlayer)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfPlayers()) {
                index = numberOfPlayers() - 1;
            }
            players.remove(aPlayer);
            players.add(index, aPlayer);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMovePlayerAt(Player aPlayer, int index) {
        boolean wasAdded = false;
        if (players.contains(aPlayer)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfPlayers()) {
                index = numberOfPlayers() - 1;
            }
            players.remove(aPlayer);
            players.add(index, aPlayer);
            wasAdded = true;
        } else {
            wasAdded = addPlayerAt(aPlayer, index);
        }
        return wasAdded;
    }

    /* Code from template association_SetUnidirectionalOne */
    public boolean setBoard(Board aNewBoard) {
        boolean wasSet = false;
        if (aNewBoard != null) {
            board = aNewBoard;
            wasSet = true;
        }
        return wasSet;
    }

    public void delete() {
        players.clear();
        board = null;
    }

}