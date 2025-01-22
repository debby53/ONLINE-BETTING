package BETTING;



import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

// Main Class
class BettingManagementUI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Game> availableGames = new ArrayList<>();
        List<RegularUser> regularUsers = new ArrayList<>();
        AdminUser adminUser;

        try {
            // Initialize admin user
            adminUser = new AdminUser("Admin01", "Admin@123");

            // Add some sample games
            adminUser.addGame(new Game("G001", "Football Match", 1.5));
            adminUser.addGame(new Game("G002", "Tennis Match", 2.0));
            availableGames.addAll(adminUser.getGames());

            // Main menu loop
            while (true) {
                System.out.println("\n=== Online Betting Management System ===");
                System.out.println("1. Admin Login");
                System.out.println("2. Regular User Login");
                System.out.println("3. Register as a Regular User");
                System.out.println("4. Exit");
                System.out.print("Select an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        adminMenu(scanner, adminUser, availableGames);
                        break;

                    case 2:
                        regularUserMenu(scanner, regularUsers, availableGames);
                        break;

                    case 3:
                        registerRegularUser(scanner, regularUsers);
                        break;

                    case 4:
                        System.out.println("Thank you for using the system. Goodbye!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (InvalidUserException | InvalidGameException e) {
            System.out.println("Initialization Error: " + e.getMessage());
        }
    }

    // Admin Menu
    private static void adminMenu(Scanner scanner, AdminUser adminUser, List<Game> availableGames) {
        System.out.println("\n--- Admin Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (!adminUser.login(username, password)) {
            System.out.println("Invalid credentials!");
            return;
        }

        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Game");
            System.out.println("2. Remove Game");
            System.out.println("3. View Audit Report");
            System.out.println("4. Logout");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    try {
                        System.out.print("Enter Game ID: ");
                        String gameId = scanner.nextLine();
                        System.out.print("Enter Game Name: ");
                        String gameName = scanner.nextLine();
                        System.out.print("Enter Odds: ");
                        double odds = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline
                        Game newGame = new Game(gameId, gameName, odds);
                        adminUser.addGame(newGame);
                        availableGames.add(newGame);
                        System.out.println("Game added successfully!");
                    } catch (InvalidGameException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Enter Game ID to remove: ");
                    String gameId = scanner.nextLine();
                    try {
                        adminUser.removeGame(gameId);
                        availableGames.removeIf(game -> game.getGameId().equals(gameId));
                        System.out.println("Game removed successfully!");
                    } catch (InvalidGameException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("\nAudit Report:");
                    System.out.println(adminUser.generateReport());
                    break;

                case 4:
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Regular User Menu
    private static void regularUserMenu(Scanner scanner, List<RegularUser> regularUsers, List<Game> availableGames) {
        System.out.println("\n--- Regular User Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        RegularUser user = regularUsers.stream()
                .filter(u -> u.login(username, password))
                .findFirst()
                .orElse(null);

        if (user == null) {
            System.out.println("Invalid credentials!");
            return;
        }

        while (true) {
            System.out.println("\n--- Regular User Menu ---");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Place a Bet");
            System.out.println("4. View Bet History");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Your balance: $" + user.getBalance());
                    break;

                case 2:
                    System.out.print("Enter deposit amount: ");
                    double amount = scanner.nextDouble();
                    try {
                        user.deposit(amount);
                        System.out.println("Deposit successful! New balance: $" + user.getBalance());
                    } catch (InvalidTransactionException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("Available Games:");
                    for (Game game : availableGames) {
                        System.out.println(game.getGameDetails());
                    }

                    System.out.print("Enter Game ID to bet on: ");
                    String gameId = scanner.nextLine();
                    System.out.print("Enter bet amount: ");
                    double betAmount = scanner.nextDouble();

                    try {
                        Bet bet = new Bet(betAmount, gameId);
                        user.placeBet(bet);
                        System.out.println("Bet placed successfully!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.println("\nYour Bet History:");
                    for (Bet bet : user.viewBetHistory()) {
                        System.out.println(bet.getBetDetails());
                    }
                    break;

                case 5:
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Register Regular User
    private static void registerRegularUser(Scanner scanner, List<RegularUser> regularUsers) {
        System.out.println("\n--- Register Regular User ---");
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            System.out.print("Enter initial deposit: ");
            double deposit = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            RegularUser newUser = new RegularUser(username, password, deposit);
            regularUsers.add(newUser);
            System.out.println("User registered successfully!");
        } catch (InvalidUserException | InvalidTransactionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

// Supporting Classes and Interfaces

abstract class User {
    private String username;
    private String password;
    private double balance;

    public User(String username, String password) throws InvalidUserException {
        if (!isValidPassword(password)) {
            throw new InvalidUserException("Password must be at least 8 characters long and contain letters and numbers.");
        }
        this.username = username;
        this.password = password;
        this.balance = 0.0;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void deposit(double amount) throws InvalidTransactionException {
        if (amount <= 0) {
            throw new InvalidTransactionException("Deposit amount must be positive.");
        }
        this.balance += amount;
    }

    public double getBalance() {
        return balance;
    }

    public void deductBalance(double amount) {
        this.balance -= amount;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[a-zA-Z].*") && password.matches(".*\\d.*");
    }

    public abstract void displayDetails();
}

class RegularUser extends User implements Notifiable {
    private List<Bet> betHistory;

    public RegularUser(String username, String password, double initialDeposit) throws InvalidUserException, InvalidTransactionException {
        super(username, password);
        deposit(initialDeposit);
        this.betHistory = new ArrayList<>();
    }

    public void placeBet(Bet bet) throws InvalidBetException, InsufficientBalanceException {
        if (bet.getAmount() > getBalance()) {
            throw new InsufficientBalanceException("Insufficient balance to place this bet.");
        }
        deductBalance(bet.getAmount());
        betHistory.add(bet);
    }

    public List<Bet> viewBetHistory() {
        return betHistory;
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("Notification: " + message);
    }

    @Override
    public void displayDetails() {
        System.out.println("Regular User Details:");
    }
}

class AdminUser extends User {
    private List<Game> games;

    public AdminUser(String username, String password) throws InvalidUserException {
        super(username, password);
        this.games = new ArrayList<>();
    }

    public void addGame(Game game) throws InvalidGameException {
        if (games.stream().anyMatch(g -> g.getGameId().equals(game.getGameId()))) {
            throw new InvalidGameException("Game with the same ID already exists.");
        }
        games.add(game);
    }

    public void removeGame(String gameId) throws InvalidGameException {
        boolean removed = games.removeIf(game -> game.getGameId().equals(gameId));
        if (!removed) {
            throw new InvalidGameException("Game with the given ID does not exist.");
        }
    }

    public List<Game> getGames() {
        return games;
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder("Admin Audit Report:\n");
        for (Game game : games) {
            report.append(game.getGameDetails()).append("\n");
        }
        return report.toString();
    }

    @Override
    public void displayDetails() {
        System.out.println("Admin User Details:");
    }
}

class Game {
    private String gameId;
    private String gameName;
    private double odds;

    public Game(String gameId, String gameName, double odds) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.odds = odds;
    }

    public String getGameId() {
        return gameId;
    }

    public String getGameDetails() {
        return String.format("ID: %s | Name: %s | Odds: %.2f", gameId, gameName, odds);
    }
}

class Bet {
    private String betId;
    private double amount;
    private String gameId;

    public Bet(double amount, String gameId) {
        this.betId = UUID.randomUUID().toString();
        this.amount = amount;
        this.gameId = gameId;
    }

    public String getBetDetails() {
        return String.format("Bet ID: %s | Amount: %.2f | Game ID: %s", betId, amount, gameId);
    }

    public double getAmount() {
        return amount;
    }
}

// Exceptions
class InvalidUserException extends Exception {
    public InvalidUserException(String message) {
        super(message);
    }
}

class InvalidTransactionException extends Exception {
    public InvalidTransactionException(String message) {
        super(message);
    }
}

class InvalidGameException extends Exception {
    public InvalidGameException(String message) {
        super(message);
    }
}

class InvalidBetException extends Exception {
    public InvalidBetException(String message) {
        super(message);
    }
}

class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

// Notifiable Interface
interface Notifiable {
    void sendNotification(String message);
}

