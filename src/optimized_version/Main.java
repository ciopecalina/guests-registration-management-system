package optimized_version;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Guest {

    // TO DO: define fields
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;

    public Guest(String lastName, String firstName, String email, String phoneNumber) {
        // TO DO:
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // TO DO: Implement getters and setters for the fields
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int hashCode() {
        // TO DO:
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.lastName == null) ? 0 : this.lastName.hashCode());
        result = prime * result + ((this.firstName == null) ? 0 : this.firstName.hashCode());
        result = prime * result + ((this.email == null) ? 0 : this.email.hashCode());
        result = prime * result + ((this.phoneNumber == null) ? 0 : this.phoneNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        // TO DO:
        if (this == obj) { // for performance reasons
            return true;
        }

        if (obj == null) {
            return false;
        }

        // both objects have the same type
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        Guest guestObj = (Guest) obj;
        if (this.email.equals(guestObj.email) &&
                this.phoneNumber.equals(guestObj.phoneNumber) &&
                this.lastName.equals(guestObj.lastName) &&
                this.firstName.equals(guestObj.firstName)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Name: " + this.lastName + " " + this.firstName + ", Email: "
                + this.email + ", Phone: " + this.phoneNumber;
    }

    public String fullName() {
        // TO DO:
        return this.lastName + " " + this.firstName;
    }

    //negative orderNo means that guest is on Participation list
    public void notifyGuest(int orderNo) {
        if (orderNo < 0) {
            System.out.println("[" + this.lastName + " " + this.firstName +
                    "] Congratulations! Your place at the event is confirmed. We are waiting for you!");
        } else {
            System.out.println("[" + this.lastName + " " + this.firstName
                    + "] You have successfully entered the waiting list "
                    + "and you received the order number " + orderNo
                    + ". We will notify you if a place becomes available.");
        }
    }
}

class GuestsList {

    private int guestsCapacity;
    private ArrayList<Guest> participants;

    public GuestsList(int guestsCapacity) {
        // TO DO:
        this.guestsCapacity = guestsCapacity;
        this.participants = new ArrayList<Guest>(this.guestsCapacity);
    }

    /**
     * Add a new, unique guest to the list.
     *
     * @param g the guest to be added
     * @return '-1' if the guest is already present, '0' if he is a guest, or the
     * number on the waiting list
     */
    public int add(Guest g) {
        if (isOnTheListAlready(g)) {
            return -1;
        }

        this.participants.add(g);
        if (this.participants.size() <= this.guestsCapacity) {
            g.notifyGuest(-1);
            return 0;
        }

        int waitlistPosition = this.participants.size() - this.guestsCapacity;
        g.notifyGuest(waitlistPosition);
        return waitlistPosition;
    }

    /**
     * Check if someone is already registered (either as a guest, or on the waiting
     * list).
     *
     * @param g the guest we are searching for
     * @return true if present, false if not
     */
    private boolean isOnTheListAlready(Guest g) {
        for (Guest guest : this.participants) {
            if (guest.equals(g)) {
                System.out.println("Person already registered.");
                return true;
            }

            if (guest.getPhoneNumber().equals(g.getPhoneNumber())) {
                System.out.println("Phone number already registered.");
                return true;
            }

            if (guest.getEmail().equals(g.getEmail())) {
                System.out.println("Email address already registered.");
                return true;
            }
        }
        return false;
    }

    /**
     * Search for a guest based on first and last name. Return the first result.
     *
     * @param firstName first name of the guest
     * @param lastName  last name of the guest
     * @return the guest if found, null if not
     */
    public Guest search(String firstName, String lastName) {
        // TO DO:
        String fullNameToSearch = lastName + " " + firstName;
        for (Guest g : this.participants) {
            if (g.fullName().equalsIgnoreCase(fullNameToSearch)) {
                return g;
            }
        }
        return null;
    }

    private Guest searchByEmail(String email) {
        for (Guest g : this.participants) {
            if (g.getEmail().equalsIgnoreCase(email)) {
                return g;
            }
        }
        return null;
    }

    private Guest searchByPhoneNumber(String phoneNumber) {
        for (Guest g : this.participants) {
            if (g.getPhoneNumber().equalsIgnoreCase(phoneNumber)) {
                return g;
            }
        }
        return null;
    }

    /**
     * Search for a guest based on email or phone number. Return the first result.
     *
     * @param opt   option to use for searching: 2 for email, 3 for phone number
     * @param match the match we are searching for
     * @return the guest if found, null if not
     */
    public Guest search(int opt, String match) {

        Guest searchResult = null;

        switch (opt) {
            case 2:
                searchResult = searchByEmail(match);
                break;
            case 3:
                searchResult = searchByPhoneNumber(match);
                break;
            default:
                break;
        }

        return searchResult;
    }

    /**
     * Remove a guest based on first and last name. Remove the first result.
     *
     * @param firstName first name of the guest
     * @param lastName  last name of the guest
     * @return true if removed, false if not
     */
    public boolean remove(String firstName, String lastName) {

        Guest g = search(firstName, lastName);

        if (g != null) {
            int gIndex = this.participants.indexOf(g);
            this.participants.remove(g);
            if (gIndex < this.guestsCapacity && this.guestsCapacity <= this.participants.size()) {
                Guest g1 = this.participants.get(this.guestsCapacity - 1);
                System.out.println("[" + g1.fullName() + "]" + " Congratulations! Your place at the event is confirmed. We are waiting for you!");
            }
        }

        return false;
    }

    /**
     * Remove a guest based on email or phone number. Remove the first result.
     *
     * @param opt   option to use for searching: 2 for email, 3 for phone number
     * @param match the match we are searching for
     * @return true if removed, false if not
     */
    public boolean remove(int opt, String match) {

        Guest g = search(opt, match);

        if (g != null) {
            int gIndex = this.participants.indexOf(g);
            this.participants.remove(g);
            if (gIndex < this.guestsCapacity && this.guestsCapacity <= this.participants.size()) {
                Guest g1 = this.participants.get(this.guestsCapacity - 1);
                System.out.println("[" + g1.fullName() + "]" + " Congratulations! Your place at the event is confirmed. We are waiting for you!");
            }
        }


        return false;
    }

    // Show the list of guests.
    public void showGuestsList() {
        if (this.participants.isEmpty()) {
            System.out.println("No participants registered...");
            return;
        }

        int limit = this.numberOfGuests();

        for (int i = 0; i < limit; i++) {
            System.out.println((i + 1) + ". " + this.participants.get(i));
        }
    }

    // Show the people on the waiting list.
    public void showWaitingList() {
        if (this.numberOfPeopleWaiting() == 0) {
            System.out.println("The waiting list is empty...");
            return;
        }

        for (int i = this.guestsCapacity, j = 1; i < this.participants.size(); i++, j++)
            System.out.println(j + ". " + this.participants.get(i));
    }

    /**
     * Show how many free spots are left.
     *
     * @return the number of spots left for guests
     */
    public int numberOfAvailableSpots() {

        int available = this.guestsCapacity - this.participants.size();

        return Math.max(available, 0);
    }

    /**
     * Show how many guests there are.
     *
     * @return the number of guests
     */
    public int numberOfGuests() {

        return Math.min(this.participants.size(), this.guestsCapacity);
    }

    /**
     * Show how many people are on the waiting list.
     *
     * @return number of people on the waiting list
     */
    public int numberOfPeopleWaiting() {

        int waitlistSize = this.participants.size() - this.guestsCapacity;
        return Math.max(waitlistSize, 0);
    }

    /**
     * Show how many people there are in total, including guests.
     *
     * @return how many people there are in total
     */
    public int numberOfPeopleTotal() {

        return this.participants.size();
    }

    /**
     * Find all people based on a partial value search.
     *
     * @param match the match we are looking for
     * @return a list of people matching the criteria
     */
    public List<Guest> partialSearch(String match) {

        List<Guest> searchResults = new ArrayList<Guest>();

        for (Guest guest : this.participants) {
            if (guest.getFirstName().toLowerCase().contains(match.toLowerCase())
                    || guest.getLastName().toLowerCase().contains(match.toLowerCase())
                    || guest.getEmail().toLowerCase().contains(match.toLowerCase())
                    || guest.getPhoneNumber().contains(match))
                searchResults.add(guest);
        }

        return searchResults;
    }

    @Override
    public String toString() {
        return this.participants.toString();
    }
}

public class Main {
    private static void showCommands() {
        System.out.println("help         - Display list of commands");
        System.out.println("add          - Add a new person (registration)");
        System.out.println("check        - Check if a person is registered for the event");
        System.out.println("remove       - Remove a registered person");
        System.out.println("update       - Updates a person's details");
        System.out.println("guests       - List of people participating at the event");
        System.out.println("waitlist     - People on the waiting list");
        System.out.println("available    - Number of available spots");
        System.out.println("guests_no    - The number of people participating at the event");
        System.out.println("waitlist_no  - The number of people on the waiting list");
        System.out.println("subscribe_no - The total number of people registered");
        System.out.println("search       - Search according to the entered character string");
        System.out.println("quit         - Close the app");
    }

    private static void addNewGuest(Scanner sc, GuestsList list) {
        // TO DO:
        String lastName = sc.nextLine();
        String firstName = sc.nextLine();
        String email = sc.nextLine();
        String phoneNumber = sc.nextLine();

        Guest g = new Guest(lastName, firstName, email, phoneNumber);
        list.add(g);
    }

    private static void checkGuest(Scanner sc, GuestsList list) {

        int opt;
        while (true) {
            opt = sc.nextInt();
            sc.nextLine();

            if (opt < 1 || opt > 3) System.out.println("Please enter an integer between 1 and 3.");
            else break;
        }

        Guest foundGuest = null;

        switch (opt) {
            case 1:
                String lastName = sc.nextLine();
                String firstName = sc.nextLine();
                foundGuest = list.search(firstName, lastName);
                break;
            case 2:
            case 3:
                String match = sc.nextLine();
                foundGuest = list.search(opt, match);
                break;
        }
        if (foundGuest == null) System.out.println("Not found");
        else System.out.println(foundGuest);
    }

    private static void removeGuest(Scanner sc, GuestsList list) {

        int opt;
        while (true) {
            opt = sc.nextInt();
            sc.nextLine();

            if (opt < 1 || opt > 3) System.out.println("Please enter an integer between 1 and 3.");
            else break;
        }

        switch (opt) {
            case 1:
                String lastName = sc.nextLine();
                String firstName = sc.nextLine();
                list.remove(firstName, lastName);
                break;
            case 2:
            case 3:
                String match = sc.nextLine();
                list.remove(opt, match);
                break;
        }
    }

    private static void updateGuest(Scanner sc, GuestsList list) {

        int opt;
        while (true) {
            opt = sc.nextInt();
            sc.nextLine();

            if (opt < 1 || opt > 3) System.out.println("Please enter an integer between 1 and 3.");
            else break;
        }

        Guest foundGuest = null;

        switch (opt) {
            case 1:
                String lastName = sc.nextLine();
                String firstName = sc.nextLine();
                foundGuest = list.search(firstName, lastName);
                break;
            case 2:
            case 3:
                String match = sc.nextLine();
                foundGuest = list.search(opt, match);
                break;
        }
        // If we have no results, exit
        if (foundGuest == null) {
            System.out.println("Error: The person is not registered for the event.");
            return;
        }
        while (true) {
            opt = sc.nextInt();
            sc.nextLine();

            if (opt < 1 || opt > 4) System.out.println("Please enter an integer between 1 and 4.");
            else break;
        }

        switch (opt) {
            case 1:
                foundGuest.setLastName(sc.nextLine());
                break;
            case 2:
                foundGuest.setFirstName(sc.nextLine());
                break;
            case 3:
                foundGuest.setEmail(sc.nextLine());
                break;
            case 4:
                foundGuest.setPhoneNumber(sc.nextLine());
                break;
        }
    }

    private static void searchList(Scanner sc, GuestsList list) {

        String match = sc.nextLine();
        List<Guest> results = list.partialSearch(match);
        for (Guest g : results)
            System.out.println(g.toString());
        if (results.size() == 0)
            System.out.println("Nothing found");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();
        scanner.nextLine();

        GuestsList list = new GuestsList(size);

        boolean running = true;
        while (running) {
            String command = scanner.nextLine();

            switch (command) {
                case "help":
                    showCommands();
                    break;
                case "add":
                    addNewGuest(scanner, list);
                    break;
                case "check":
                    checkGuest(scanner, list);
                    break;
                case "remove":
                    removeGuest(scanner, list);
                    break;
                case "update":
                    updateGuest(scanner, list);
                    break;
                case "guests":
                    list.showGuestsList();
                    break;
                case "waitlist":
                    list.showWaitingList();
                    break;
                case "available":
                    System.out.println("Number of available spots: " + list.numberOfAvailableSpots());
                    break;
                case "guests_no":
                    System.out.println("Number of participants: " + list.numberOfGuests());
                    break;
                case "waitlist_no":
                    System.out.println("Size of the waiting list: " + list.numberOfPeopleWaiting());
                    break;
                case "subscribe_no":
                    System.out.println("Numarul total de persoane: " + list.numberOfPeopleTotal());
                    break;
                case "search":
                    searchList(scanner, list);
                    break;
                case "quit":
                    System.out.println("The app closes...");
                    scanner.close();
                    running = false;
                    break;
                default:
                    System.out.println("Command not valid.");
                    System.out.println("Try again.");

            }
        }
    }
}
