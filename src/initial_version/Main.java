package initial_version;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Guest {

    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;

    public Guest(String lastName, String firstName, String email, String phoneNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Guest(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = "";
        this.phoneNumber = "";
    }

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
        int prime = 31;
        int result = 1;
        result = prime * result + ((lastName == null) ? 0 : lastName.toUpperCase().hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.toUpperCase().hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Guest other = (Guest) obj;
        return this.lastName.equalsIgnoreCase(other.lastName) && this.firstName.equalsIgnoreCase(other.firstName);
    }

    @Override
    public String toString() {
        return "Name: " + lastName + " " + firstName + ", Email: " + email + ", Phone: " + phoneNumber;
    }

    public String fullName() {
        return "Name: " + lastName + " " + firstName;
    }
}

class GuestsList {

    private int numberOfAvailableSpots;
    private ArrayList<Guest> guestsList;
    private ArrayList<Guest> waitingList;

    public GuestsList(int guestsCapacity) {
        this.numberOfAvailableSpots = guestsCapacity;
        this.guestsList = new ArrayList<>(guestsCapacity);
        this.waitingList = new ArrayList<>();
    }

    /**
     * Check if someone is already registered ( as a guest, or on the waiting
     * list).
     *
     * @param g the guest we are searching for
     * @return true if present, false if not
     */
    private boolean isOnTheListAlready(Guest g) {
        if (guestsList.contains(g) || waitingList.contains(g)) {
            return true;
        }
        return false;
    }

    /**
     * Add a new, unique guest to the list.
     *
     * @param g the guest to be added
     * @return '-1' if the guest is already present,
     * '0' if is a guest,
     * or the number on the waiting list
     */
    public int add(Guest g) {
        if (isOnTheListAlready(g)) {
            System.out.println("Guest already on the list.");
            return -1; // Guest already exists
        } else if (numberOfAvailableSpots > 0) {
            guestsList.add(g);
            numberOfAvailableSpots--;
            System.out.println("[" + g.getLastName() + " " + g.getFirstName() + "] Congratulations! Your place at the event is confirmed. We are waiting for you!");

            return 0; // Guest added to guestsList

        } else {
            waitingList.add(g);
            System.out.println("[" + g.getLastName() + " " + g.getFirstName() + "] You have successfully joined the waiting list and received the order number " + waitingList.size() + ". We will notify you if a spot becomes available.");

            return waitingList.size(); // Guest added to waitingList
        }
    }

    /**
     * Search for a guest based on first and last name. Return the first result.
     *
     * @param lastName  last name of the guest
     * @param firstName first name of the guest
     * @return the guest if found, null if not
     */

    public Guest search(String lastName, String firstName) {
        Guest searchGuest = new Guest(lastName, firstName);
        for (Guest guest : guestsList) {
            if (guest.equals(searchGuest)) {
                return guest;
            }
        }
        for (Guest guest : waitingList) {
            if (guest.equals(searchGuest)) {
                return guest;
            }
        }
        return null; // Guest not found
    }

    /**
     * Search for a guest based on email or phone number. Return the first result.
     *
     * @param opt   option to use for searching: 2 for email, 3 for phone number
     * @param match what is searched for
     * @return the guest if found, null if not
     */

    public Guest search(int opt, String match) {
        for (Guest guest : guestsList) {
            if (matchesCriteria(guest, opt, match)) {
                return guest;
            }
        }

        for (Guest guest : waitingList) {
            if (matchesCriteria(guest, opt, match)) {
                return guest;
            }
        }

        return null;
    }

    private boolean matchesCriteria(Guest guest, int opt, String match) {
        switch (opt) {
            case 2: // Search by email
                return guest.getEmail().equalsIgnoreCase(match);
            case 3: // Search by phone number
                return guest.getPhoneNumber().equalsIgnoreCase(match);
            default:
                // Handle invalid option
                System.out.println("Invalid option for searching.");
                return false;
        }
    }


    /**
     * Remove a guest based on first and last name. Remove the first result.
     *
     * @param lastName  last name of the guest
     * @param firstName first name of the guest
     * @return true if removed, false if not
     */
    public boolean remove(String lastName, String firstName) {
        Guest toRemove = new Guest(lastName, firstName);
        boolean removed = false;

        for (int i = 0; i < guestsList.size(); i++) {
            Guest guest = guestsList.get(i);

            if (guest.equals(toRemove)) {

                guestsList.remove(i);
                numberOfAvailableSpots++;

                if (!waitingList.isEmpty()) {
                    Guest firstInWaitingList = waitingList.get(0);
                    waitingList.remove(0);
                    guestsList.add(firstInWaitingList);

                    System.out.println("[" + firstInWaitingList.getLastName() + " " + firstInWaitingList.getFirstName() + "] Congratulations! Your spot at the event is confirmed. We are waiting for you!");
                }

                removed = true;
                break;
            }
        }

        if (!removed) {
            for (int i = 0; i < waitingList.size(); i++) {
                Guest guest = waitingList.get(i);
                if (guest.equals(toRemove)) {
                    waitingList.remove(i);
                    removed = true;
                }
            }
        }

        return removed;
    }

    /**
     * Remove a guest based on email or phone number. Remove the first result.
     *
     * @param opt   option to use for searching: 2 for email, 3 for phone number
     * @param match the match we are searching for
     * @return true if removed, false if not
     */

    public boolean remove(int opt, String match) {
        boolean removed = false;

        for (int i = 0; i < guestsList.size(); i++) {
            Guest guest = guestsList.get(i);
            switch (opt) {
                case 2: // Search by email
                    if (guest.getEmail().equalsIgnoreCase(match)) {
                        guestsList.remove(i);
                        numberOfAvailableSpots++;

                        if (!waitingList.isEmpty()) {
                            Guest firstInWaitingList = waitingList.get(0);
                            waitingList.remove(0);
                            guestsList.add(firstInWaitingList);

                            System.out.println("[" + firstInWaitingList.getLastName() + " " + firstInWaitingList.getFirstName() + "] Congratulations! Your spot at the event is confirmed. We are waiting for you!");
                        }
                        removed = true;
                    }
                    break;
                case 3: // Search by phone number
                    if (guest.getPhoneNumber().equalsIgnoreCase(match)) {
                        guestsList.remove(i);
                        numberOfAvailableSpots++;

                        if (!waitingList.isEmpty()) {
                            Guest firstInWaitingList = waitingList.get(0);
                            waitingList.remove(0);
                            guestsList.add(firstInWaitingList);

                            System.out.println("[" + firstInWaitingList.getLastName() + " " + firstInWaitingList.getFirstName() + "] Congratulations! Your spot at the event is confirmed. We are waiting for you!");
                        }

                        removed = true;
                    }
                    break;
            }
            if (removed) {
                break;
            }
        }

        if (!removed) {
            for (int i = 0; i < waitingList.size(); i++) {
                Guest guest = waitingList.get(i);
                switch (opt) {
                    case 2: // Search by email
                        if (guest.getEmail().equalsIgnoreCase(match)) {
                            waitingList.remove(i);
                            removed = true;
                        }
                        break;
                    case 3: // Search by phone number
                        if (guest.getPhoneNumber().equalsIgnoreCase(match)) {
                            waitingList.remove(i);
                            removed = true;
                        }
                        break;
                }
                if (removed) {
                    break;
                }
            }
        }
        return removed;

    }

    // Show the list of guests.
    public void showGuestsList() {
        if (guestsList.isEmpty()) {
            System.out.println("The guest list is empty...");
        } else {
            for (int i = 0; i < guestsList.size(); i++) {
                System.out.println((i + 1) + ". " + guestsList.get(i));
            }
        }
    }

    // Show the people on the waiting list.
    public void showWaitingList() {
        if (waitingList.isEmpty()) {
            System.out.println("The waiting list is empty...");
        } else {
            for (int i = 0; i < waitingList.size(); i++) {
                System.out.println((i + 1) + ". " + waitingList.get(i));
            }
        }
    }

    /**
     * Show how many free spots are left.
     *
     * @return the number of spots left for guests
     */
    public int numberOfAvailableSpots() {
        return this.numberOfAvailableSpots;
    }

    /**
     * Show how many guests there are.
     *
     * @return the number of guests
     */
    public int numberOfGuests() {
        return guestsList.size();
    }

    /**
     * Show how many people are on the waiting list.
     *
     * @return number of people on the waiting list
     */
    public int numberOfPeopleWaiting() {
        return waitingList.size();
    }

    /**
     * Show how many people there are in total, including guests.
     *
     * @return how many people there are in total
     */
    public int numberOfPeopleTotal() {
        return guestsList.size() + waitingList.size();
    }

    /**
     * Find all people based on a partial value search.
     *
     * @param match the match we are looking for
     * @return a list of people matching the criteria
     */
    public List<Guest> partialSearch(String match) {
        List<Guest> matchingGuests = new ArrayList<>();

        for (Guest guest : guestsList) {
            if (guest.getLastName().toLowerCase().contains(match.toLowerCase()) || guest.getFirstName().toLowerCase().contains(match.toLowerCase()) || guest.getEmail().toLowerCase().contains(match.toLowerCase()) || guest.getPhoneNumber().toLowerCase().contains(match.toLowerCase())) {
                matchingGuests.add(guest);
            }
        }

        for (Guest guest : waitingList) {
            if (guest.getLastName().toLowerCase().contains(match.toLowerCase()) || guest.getFirstName().toLowerCase().contains(match.toLowerCase()) || guest.getEmail().toLowerCase().contains(match.toLowerCase()) || guest.getPhoneNumber().toLowerCase().contains(match.toLowerCase())) {
                matchingGuests.add(guest);
            }
        }

        return matchingGuests;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Number of spots left: " + numberOfAvailableSpots()).append("\n");

        for (int i = 0; i < guestsList.size(); i++) {
            sb.append((i + 1) + ". " + guestsList.get(i)).append("\n");
        }

        sb.append("Size of waiting list: " + numberOfPeopleWaiting()).append("\n");

        for (int i = 0; i < waitingList.size(); i++) {
            sb.append((i + 1) + ". " + guestsList.get(i)).append("\n");
        }
        return sb.toString();
    }


    public boolean updateGuestAndCheckForDuplicates(Guest guestFound, String newValue, int optionUpdate) {
        boolean isDuplicate = false;

        switch (optionUpdate) {
            case 1: // Update last name
                for (Guest guest : guestsList) {
                    if (guest.getLastName().equalsIgnoreCase(newValue)) {
                        isDuplicate = true;
                    }
                }
                for (Guest guest : waitingList) {
                    if (guest.getLastName().equalsIgnoreCase(newValue)) {
                        isDuplicate = true;
                    }
                }
                if (isDuplicate == false) {
                    guestFound.setLastName(newValue);
                }
                break;

            case 2: // Update first name
                for (Guest guest : guestsList) {
                    if (guest.getFirstName().equalsIgnoreCase(newValue)) {
                        isDuplicate = true;
                    }
                }
                for (Guest guest : waitingList) {
                    if (guest.getFirstName().equalsIgnoreCase(newValue)) {
                        isDuplicate = true;
                    }
                }
                if (isDuplicate == false) {
                    guestFound.setFirstName(newValue);
                }
                break;

            case 3: // Update email
                for (Guest guest : guestsList) {
                    if (guest.getEmail().equalsIgnoreCase(newValue)) {
                        isDuplicate = true;
                    }
                }
                for (Guest guest : waitingList) {
                    if (guest.getEmail().equalsIgnoreCase(newValue)) {
                        isDuplicate = true;
                    }
                }
                if (isDuplicate == false) {
                    guestFound.setEmail(newValue);
                }
                break;

            case 4: // Update phone number
                for (Guest guest : guestsList) {
                    if (guest.getPhoneNumber().equalsIgnoreCase(newValue)) {
                        isDuplicate = true;
                    }
                }
                for (Guest guest : waitingList) {
                    if (guest.getPhoneNumber().equalsIgnoreCase(newValue)) {
                        isDuplicate = true;
                    }
                }
                if (isDuplicate == false) {
                    guestFound.setPhoneNumber(newValue);
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid option for update.");
        }

        return isDuplicate;
    }
}

public class Main {

    public static boolean isValidFullName(String fullName) {
        if (fullName == null || fullName.isEmpty() || !Character.isUpperCase(fullName.charAt(0))) {
            return false;
        }
        String regex = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fullName);

        return matcher.find();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^(\\+?)(\\d{10})$";
        return phoneNumber.matches(regex);
    }

    public static boolean isValidEmail(String email) {
        return email.matches("^[a-z0-9_+&*-]+(?:\\.[a-z0-9_+&*-]+)*@(?:[a-z0-9-]+\\.)+[a-z]{2,7}$");
    }

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
        String lastName = sc.nextLine();
        String firstName = sc.nextLine();
        String email = sc.nextLine();
        String phoneNumber = sc.nextLine();

        if (!isValidFullName(lastName) || !isValidFullName(firstName) || !isValidEmail(email) || !isValidPhoneNumber(phoneNumber)) {
            System.out.println("Invalid input type.");
            return;
        }

        Guest newGuest = new Guest(lastName, firstName, email, phoneNumber);

        int result = list.add(newGuest);
    }

    private static void checkGuest(Scanner sc, GuestsList list) {
        int option = sc.nextInt();
        sc.nextLine();

        if (option < 1 || option > 3) {
            System.out.println("Invalid option! Please select a number between 1 and 3.");
            return;
        }

        switch (option) {
            case 1:
                String lastName = sc.nextLine().trim();
                String firstName = sc.nextLine().trim();

                Guest foundGuestByName = list.search(lastName, firstName);
                if (foundGuestByName != null) {
                    System.out.println(foundGuestByName.toString());
                } else {
                    System.out.println("Guest not found by last and first name.");
                }
                break;

            case 2:
                String email = sc.nextLine().trim();

                Guest foundGuestByEmail = list.search(2, email);
                if (foundGuestByEmail != null) {
                    System.out.println(foundGuestByEmail.toString());
                } else {
                    System.out.println("Guest not found by email.");
                }
                break;

            case 3:
                String phoneNumber = sc.nextLine().trim();
                Guest foundGuestByPhone = list.search(3, phoneNumber);
                if (foundGuestByPhone != null) {
                    System.out.println(foundGuestByPhone.toString());
                } else {
                    System.out.println("Guest not found by phone number.");
                }
                break;
        }
    }

    private static void removeGuest(Scanner sc, GuestsList list) {
        int option = sc.nextInt();
        sc.nextLine();

        if (option < 1 || option > 3) {
            System.out.println("Invalid option! Please select a number between 1 and 3.");
            return;
        }

        boolean result;

        switch (option) {
            case 1:
                String lastName = sc.nextLine().trim();
                String firstName = sc.nextLine().trim();

                result = list.remove(lastName, firstName);
                if (result == false) {
                    System.out.println("Guest not found by name.");
                }
                break;

            case 2:
                String email = sc.nextLine().trim();

                result = list.remove(2, email);
                if (result == false) {
                    System.out.println("Guest not found by email.");
                }
                break;

            case 3:
                String phoneNumber = sc.nextLine().trim();
                result = list.remove(3, phoneNumber);
                if (result == false) {
                    System.out.println("Guest not found by phone number.");
                }
                break;
        }
    }

    private static void updateGuest(Scanner sc, GuestsList list) {
        int optionSearch = sc.nextInt();
        sc.nextLine();

        if (optionSearch < 1 || optionSearch > 3) {
            System.out.println("Invalid option! Please select a number between 1 and 3.");
            return;
        }

        Guest guestFound = null;
        String searchLastName = "", searchFirstName = "", searchEmail = "", searchPhoneNumber = "";

        switch (optionSearch) {
            case 1:
                searchLastName = sc.nextLine().trim();
                searchFirstName = sc.nextLine().trim();

                guestFound = list.search(searchLastName, searchFirstName);
                break;

            case 2:
                searchEmail = sc.nextLine().trim();

                guestFound = list.search(2, searchEmail);
                break;

            case 3:
                searchPhoneNumber = sc.nextLine().trim();

                guestFound = list.search(3, searchPhoneNumber);
                break;
        }

        if (guestFound == null) {
            System.out.println("Guest not found.");
            return;
        }

        int optionUpdate = sc.nextInt();
        sc.nextLine();

        if (optionUpdate < 1 || optionUpdate > 4) {
            System.out.println("Invalid option! Please select a number between 1 and 4.");
            return;
        }

        String newValue = sc.nextLine();
        boolean ok = true;
        switch (optionUpdate) {
            case 1:
                if (!isValidFullName(newValue)) {
                    System.out.println("Invalid input type!");
                    ok = isValidFullName(newValue);
                }
                break;
            case 2:
                if (!isValidFullName(newValue)) {
                    System.out.println("Invalid input type!");
                    ok = isValidFullName(newValue);
                }
                break;
            case 3:
                if (!isValidEmail(newValue)) {
                    System.out.println("Invalid input type!");
                    ok = isValidEmail(newValue);
                }
                break;
            case 4:
                if (!isValidPhoneNumber(newValue)) {
                    System.out.println("Invalid input type!");
                    ok = isValidPhoneNumber(newValue);
                }
                break;
        }

        if (ok == true) {
            boolean isDuplicate = list.updateGuestAndCheckForDuplicates(guestFound, newValue, optionUpdate);
            if (isDuplicate == true) {
                System.out.println("Person already registered...");
            }
        }
    }

    private static void searchList(Scanner sc, GuestsList list) {
        String searchQuery = sc.nextLine();
        List<Guest> searchResults = list.partialSearch(searchQuery);

        if (searchResults.isEmpty()) {
            System.out.println("Nothing found");
        } else {
            for (int i = 0; i < searchResults.size(); i++) {
                System.out.println(searchResults.get(i));
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        try {
            File file = new File("src/resources/test.txt");
            Scanner fileScanner = new Scanner(file);
            int size = fileScanner.nextInt();
            fileScanner.nextLine();

            GuestsList list = new GuestsList(size);

            boolean running = true;
            while (running) {
                String command = fileScanner.nextLine();

                switch (command) {
                    case "help":
                        showCommands();
                        break;
                    case "add":
                        addNewGuest(fileScanner, list);
                        break;
                    case "check":
                        checkGuest(fileScanner, list);
                        break;
                    case "remove":
                        removeGuest(fileScanner, list);
                        break;
                    case "update":
                        updateGuest(fileScanner, list);
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
                        System.out.println("Total number of participants: " + list.numberOfPeopleTotal());
                        break;
                    case "search":
                        searchList(fileScanner, list);
                        break;
                    case "quit":
                        System.out.println("The app closes...");
                        fileScanner.close();
                        running = false;
                        break;
                    default:
                        System.out.println("Command not valid.");
                        System.out.println("Try again.");

                }
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }
}
