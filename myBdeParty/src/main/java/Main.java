import myParty.Party;

public class Main {
    public static void main(String[] args) {
        myParty.Party party = new Party(100, 10);

        System.out.println("Accepting 10");
        for (int i = 0; i < 10; i++) {
            party.accept();
        }

        System.out.println("Selling 40");
        for (int i = 0; i < 40; i++) {
            party.sell();
        }

        System.out.println("Launching error handling");
        try {
            for (int i = 0; i < 141; i++) {
                party.sell();
            }
            assert(false);
        } catch (RuntimeException e) {
            System.out.println("Exception successfully handled");
        }

        assert(!party.accept());
    }
}
