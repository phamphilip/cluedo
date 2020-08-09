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
    private Set<Player> out;
    private Board board;
    private int numPlayers = 0;
    private boolean gameOver;
    private Player currentPlayer;
    private CharacterCard murderer;
    private WeaponCard murderWeapon;
    private RoomCard murderRoom;
    private Map<String, CharacterCard> characterCards;
    private Map<String, RoomCard> roomCards;
    private Map<String, WeaponCard> weaponCards;


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
            for (String c : players.get(i).getCards().keySet()) {
                System.out.println(c);
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
            this.currentPlayer = players.get(player);
            this.currentPlayer.setSteps(rollDice());

            while (this.currentPlayer.getSteps() > 0) {
                boolean hasTurn = true;
                while (hasTurn) {
                    printNewLine();
                    System.out.println("You have " + this.currentPlayer.getSteps() + " steps remaining. Type a direction to move (e.g. \"down\") or \"cards\" to see your cards");
                    Scanner sc = new Scanner(System.in);
                    String input = sc.next().toLowerCase();
                    Location previousLoc = board.getLocations()[currentPlayer.getLocation().y][currentPlayer.getLocation().x];

                    switch (input) {
                        case "up":
                            this.currentPlayer.move(board, 0, -1);
                            break;
                        case "down":
                            this.currentPlayer.move(board, 0, 1);
                            break;
                        case "left":
                            this.currentPlayer.move(board, -1, 0);
                            break;
                        case "right":
                            this.currentPlayer.move(board, 1, 0);
                            break;
                        case "cards":
                            for (String c : this.currentPlayer.getCards().keySet()) {
                                System.out.println(c);
                            }
                            break;
                        default:
                            System.out.println("Invalid Direction");
                    }
                    if (currentPlayer.getSteps() <= 0) hasTurn = false;
                    Location currentLoc = board.getLocations()[currentPlayer.getLocation().y][currentPlayer.getLocation().x];
                    if (previousLoc instanceof Floor && currentLoc instanceof Room) {
                        printNewLine();
                        System.out.println("You have entered the " + ((Room)board.getLocation(currentPlayer.getLocation().x, currentPlayer.getLocation().y)).getName());
                        System.out.println("If you would like to make a suggestion enter \"suggestion\", press \"continue\" to continue your turn and \"end\" to end your turn");
                        boolean validInput = false;
                        while (!validInput) {
                            sc = new Scanner(System.in);
                            input = sc.next().toLowerCase();
                            if (!input.equals("continue")) {
                                if (input.equals("suggestion")) {
                                    makeSuggestion(player);
                                    this.currentPlayer.setSteps(0);
                                    hasTurn = false;
                                    if (!gameOver) System.out.print(board.toString());
                                    validInput = true;
                                } else if (input.equals("end")) {
                                    this.currentPlayer.setSteps(0);
                                    hasTurn = false;
                                    validInput = true;
                                } else {
                                    System.out.println("Invalid Input");
                                    System.out.print("try again: ");
                                }
                            } else {
                                validInput = true;
                            }
                        }
                    }
                }
            }

            if (out.size() == players.size()) {
                gameOver = true;
                continue;
            }

            player++;
            if (player >= players.size()) player = 0;

            while (out.contains(players.get(player))) {
                player++;
                if (player >= players.size()) player = 0;
            }
        }
    }

    private void makeSuggestion(int playerNum) {
        listAllCards();

        RoomCard suggestedRoom = null;
        int xPos = this.currentPlayer.getLocation().x;
        int yPos = this.currentPlayer.getLocation().y;
        if (roomCards.containsKey(((Room) this.board.getLocations()[yPos][xPos]).getName())) suggestedRoom = roomCards.get(((Room) this.board.getLocations()[yPos][xPos]).getName());
        Scanner sc;
        String input;

        CharacterCard suggestedCharacter = null;
        System.out.print("Select a Character to suggest: ");
        while (suggestedCharacter == null) {
            sc = new Scanner(System.in);
            input = sc.nextLine().toLowerCase();
            if (characterCards.containsKey(input)) suggestedCharacter = characterCards.get(input);
            if (suggestedCharacter == null) System.out.print("Invalid input, try again: ");
        }

        WeaponCard suggestedWeapon = null;
        System.out.print("Select a Weapon to suggest: ");
        while (suggestedWeapon == null) {
            sc = new Scanner(System.in);
            input = sc.nextLine().toLowerCase();
            if (weaponCards.containsKey(input)) suggestedWeapon = weaponCards.get(input);
            if (suggestedWeapon == null) System.out.print("Invalid input, try again: ");
        }

        if (!refuteSuggestion(playerNum, suggestedRoom, suggestedCharacter, suggestedWeapon)) {
            System.out.println("No one refuted your suggestion do you want to make an accusation? (yes/no)");
            boolean validInput = false;
            while (!validInput) {
                sc = new Scanner(System.in);
                input = sc.nextLine().toLowerCase();
                switch (input) {
                    case "yes":
                        makeAccusation();
                        validInput = true;
                        break;
                    case "no":
                        validInput = true;
                        break;
                    default:
                        System.out.println("Invalid input\ntry again: ");
                }
            }
        }

    }

    private void makeAccusation() {
        listAllCards();
        Scanner sc;
        String input;

        CharacterCard suggestedCharacter = null;
        System.out.print("Select a Character to suggest: ");
        while (suggestedCharacter == null) {
            sc = new Scanner(System.in);
            input = sc.nextLine().toLowerCase();
            if (characterCards.containsKey(input)) suggestedCharacter = characterCards.get(input);
            if (suggestedCharacter == null) System.out.print("Invalid input, try again: ");
        }

        WeaponCard suggestedWeapon = null;
        System.out.print("Select a Weapon to suggest: ");
        while (suggestedWeapon == null) {
            sc = new Scanner(System.in);
            input = sc.nextLine().toLowerCase();
            if (weaponCards.containsKey(input)) suggestedWeapon = weaponCards.get(input);
            if (suggestedWeapon == null) System.out.print("Invalid input, try again: ");
        }

        RoomCard suggestedRoom = null;
        System.out.print("Select a Room to suggest: ");
        while (suggestedRoom == null) {
            sc = new Scanner(System.in);
            input = sc.nextLine().toLowerCase();
            if (roomCards.containsKey(input)) suggestedRoom = roomCards.get(input);
            if (suggestedRoom == null) System.out.print("Invalid input, try again: ");
        }

        System.out.printf("Your accusation:\n\t%s\n\t%s\t\n\t%s\n", suggestedCharacter.getName(), suggestedWeapon.getName(), suggestedRoom.getName());
        System.out.printf("solution:\n\t%s\n\t%s\n\t%s\n", murderer.getName(), murderWeapon.getName(), murderRoom.getName());
        if (suggestedCharacter.getName().equals((murderer.getName()))
                && suggestedWeapon.getName().equals((murderWeapon.getName()))
                && suggestedRoom.getName().equals((murderRoom.getName()))) {
            System.out.println("You Won!");
            gameOver = true;
        } else {
            System.out.println("You Lost!");
            out.add(currentPlayer);
            board.getLocations()[currentPlayer.getLocation().y][currentPlayer.getLocation().x].setItem(null);
        }
    }

    private boolean refuteSuggestion(int playerNum, RoomCard suggestedRoom, CharacterCard suggestedCharacter, WeaponCard suggestedWeapon) {
        boolean refuted = false;
        printNewLine();
        System.out.printf("Suggested scenario: %s/%s/%s\n", suggestedRoom.getName(), suggestedCharacter.getName(), suggestedWeapon.getName());
        printNewLine();
        int finish = playerNum;
        playerNum++;
        while (playerNum != finish) {
            if (playerNum >= players.size()) {
                playerNum = 0;
                if (playerNum == finish) break;
            }
            boolean canRefute = false;
            for (String c : players.get(playerNum).getCards().keySet()) {
                if (c.equals(suggestedRoom.getName()) || c.equals(suggestedCharacter.getName()) || c.equals(suggestedWeapon.getName())) {
                    canRefute = true;
                    break;
                }
            }
            if (canRefute) {
                System.out.println("Player " + (playerNum+1) + " enter a card that matches one of the suggested cards to refute it (enters \"cards\" to see your cards)");
                boolean validInput = false;
                while (!validInput) {
                    System.out.print("Card: ");
                    Scanner sc = new Scanner(System.in);
                    String input = sc.nextLine().toLowerCase();
                    if (input.equals("cards")) {
                        for (String c : players.get(playerNum).getCards().keySet()) {
                            System.out.println(c);
                        }
                        printNewLine();
                    }
                    else if (!characterCards.containsKey(input) && !weaponCards.containsKey(input) && !roomCards.containsKey(input)) {
                        System.out.println("Not a Card");
                    }
                    else if (!players.get(playerNum).getCards().containsKey(input)) {
                        System.out.println("You do not have this card");
                    }
                    else if (input.equals(suggestedRoom.getName().toLowerCase())) {
                        System.out.println("Player " + (playerNum+1) + " refuted " + suggestedRoom.getName());
                        validInput = true;
                    } else if (input.equals(suggestedCharacter.getName().toLowerCase())) {
                        System.out.println("Player " + (playerNum+1) + " refuted " + suggestedCharacter.getName());
                        validInput = true;
                    } else if (input.equals(suggestedWeapon.getName().toLowerCase())) {
                        System.out.println("Player " + (playerNum+1) + " refuted " + suggestedWeapon.getName());
                        validInput = true;
                    } else {
                        System.out.println("This does not match any of the suggested cards");
                    }
                }
                refuted = true;
            } else System.out.println("Player " + (playerNum+1) + " is unable to refute this suggestion");
            printNewLine();
            playerNum++;
        }
        return refuted;
    }

    private void listAllCards() {
        List<CharacterCard> cardsTemp = new ArrayList<>(characterCards.values());
        List<WeaponCard> weaponsTemp = new ArrayList<>(weaponCards.values());
        List<RoomCard> roomsTemp = new ArrayList<>(roomCards.values());
        printNewLine();
        System.out.printf("%-20s%-20s%s\n", "Characters", "Weapons", "Rooms");
        printNewLine();
        int max = Math.max(characterCards.size(), weaponCards.size());
        for (int i = 0; i < max; i++) {
            if (i < characterCards.size()) System.out.printf("%-20s", cardsTemp.get(i));
            else System.out.printf("%-20s", "");
            if (i < weaponCards.size()) System.out.printf("%-20s", weaponsTemp.get(i));
            else System.out.printf("%-20s", "");
            if (i < roomCards.size()) System.out.printf("%-20s", roomsTemp.get(i));
            System.out.println();
        }
        printNewLine();
    }

    private void printNewLine() {
        for (int i = 0; i < 70; i++) {
            System.out.print("-");
        }
        System.out.print("\n");
    }

    private int rollDice() {
        int min = 1;
        int max = 6;
        int diceOne = (int) (Math.random() * (max - min + 1) + min);
        int diceTwo = (int) (Math.random() * (max - min + 1) + min);
        System.out.println("*Rolls Dice*\n\nDice One: " + diceOne + "\nDice Two: " + diceTwo);
        return diceOne+diceTwo;
    }

    private void createCards() {
        List<CharacterCard> cc = new ArrayList<>(characterCards.values());
        List<RoomCard> rc = new ArrayList<>(roomCards.values());
        List<WeaponCard> wc = new ArrayList<>(weaponCards.values());
        Collections.shuffle(cc);
        Collections.shuffle(wc);
        Collections.shuffle(rc);
        murderer = cc.get(0);
        cc.remove(0);
        murderWeapon = wc.get(0);
        wc.remove(0);
        murderRoom = rc.get(0);
        rc.remove(0);
        List<Card> cards = new ArrayList<>();
        cards.addAll(cc);
        cards.addAll(rc);
        cards.addAll(wc);
        cards.remove(murderer);
        cards.remove(murderRoom);
        cards.remove(murderWeapon);
        Collections.shuffle(cards);
        int i = 0;
        while (!cards.isEmpty()) {
            players.get(i).getCards().put(cards.get(0).getName(), cards.get(0));
            cards.remove(0);
            i++;
            if (i >= players.size()) i = 0;
        }
    }

    private Map<String, WeaponCard> createWeaponCards() {
        Map<String, WeaponCard> weaponCards = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        weaponCards.put("Candlestick", new WeaponCard("Candlestick"));
        weaponCards.put("Dagger", new WeaponCard("Dagger"));
        weaponCards.put("Lead Pipe", new WeaponCard("Lead Pipe"));
        weaponCards.put("Revolver", new WeaponCard("Revolver"));
        weaponCards.put("Rope", new WeaponCard("Rope"));
        weaponCards.put("Spanner", new WeaponCard("Spanner"));
        return weaponCards;
    }

    private Map<String, RoomCard> createRoomCards() {
        Map<String, RoomCard> roomCards = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        roomCards.put("Ball Room", new RoomCard("Ball Room"));
        roomCards.put("Conservatory", new RoomCard("Conservatory"));
        roomCards.put("Billiard Room", new RoomCard("Billiard Room"));
        roomCards.put("Library", new RoomCard("Library"));
        roomCards.put("Study", new RoomCard("Study"));
        roomCards.put("Hall", new RoomCard("Hall"));
        roomCards.put("Lounge", new RoomCard("Lounge"));
        roomCards.put("Dining Room", new RoomCard("Dining Room"));
        roomCards.put("Kitchen", new RoomCard("Kitchen"));
        return roomCards;
    }

    private Map<String, CharacterCard> createCharacterCards() {
        Map<String, CharacterCard> characterCards = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        characterCards.put("Miss Scarlett", new CharacterCard("Miss Scarlett"));
        characterCards.put("Colonel Mustard", new CharacterCard("Colonel Mustard"));
        characterCards.put("Mrs. White", new CharacterCard("Mrs. White"));
        characterCards.put("Mr. Green", new CharacterCard("Mr. Green"));
        characterCards.put("Mrs. Peacock", new CharacterCard("Mrs. Peacock"));
        characterCards.put("Professor Plum", new CharacterCard("Professor Plum"));
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
            Point point = new Point(x, y);
            switch (tile) {
                case "_":
                    tiles[y][x] = new Floor(null, point);
                    break;
                case "#":
                    tiles[y][x] = new Wall(point);
                    break;
                case "k":
                    tiles[y][x] = new Room("Kitchen", "k", null, point, isDoor);
                    break;
                case "b":
                    tiles[y][x] = new Room("Ball Room", "b", null, point, isDoor);
                    break;
                case "c":
                    tiles[y][x] = new Room("Conservatory", "c", null, point, isDoor);
                    break;
                case "d":
                    tiles[y][x] = new Room("Dining Room", "d", null, point, isDoor);
                    break;
                case "i":
                    tiles[y][x] = new Room("Billiard Room", "i", null, point, isDoor);
                    break;
                case "l":
                    tiles[y][x] = new Room("Library", "l", null, point, isDoor);
                    break;
                case "o":
                    tiles[y][x] = new Room("Lounge", "o", null, point, isDoor);
                    break;
                case "h":
                    tiles[y][x] = new Room("Hall", "h", null, point, isDoor);
                    break;
                case "y":
                    tiles[y][x] = new Room("Study", "y", null, point, isDoor);
                    break;
                default:
                    int playerNum = Integer.parseInt(tile);
                    if (playerNum > numPlayers) {
                        tiles[y][x] = new Floor(null, point);
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
                    tiles[y][x] = new Floor(player, point);
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
        weaponCards = createWeaponCards();
        characterCards = createCharacterCards();
        roomCards = createRoomCards();
        out = new HashSet<>();
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