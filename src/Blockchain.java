import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    public static final int DIFFICULTY = 5;
    private final ArrayList<Block> chain = new ArrayList<>();

    public void addBlock(final Transaction transaction) {
        if (chain.isEmpty()) {
            chain.add(new Block(transaction));
        } else {
            chain.add(new Block(transaction, chain.getLast().getHash()));
        }

        chain.getLast().mine(DIFFICULTY);

    }

    public boolean isValid() {
        for (int i = 0; i < chain.size(); ++i) {
            if (!chain.get(i).getHash().equals(Hash.generateHash(chain.get(i)))) {
                System.out.println(i + ": invalid generation");
                return false;
            }

            if (i > 0 && !chain.get(i).getPrevHash().equals(chain.get(i - 1).getHash())) {
                System.out.println(i + ": prevHash doesnt match prev hash");
                return false;
            }

            if (!chain.get(i).getTransaction().verify()) {
                System.out.println(i + ": invalid digital signature");
                return false;
            }
        }

        return true;
    }

    public List<Block> getBlocks() {
        return chain;
    }
}
