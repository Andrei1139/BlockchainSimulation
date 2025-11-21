import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;

public class Node {
    private KeyPair keys;
    private Hash address;
    private Blockchain blockchain;

    public Node() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024, SecureRandom.getInstanceStrong());
            keys = generator.generateKeyPair();

            address = Hash.generateWalletAddress(keys.getPublic());
        } catch (Exception ignore) {

        }
    }

    public void setBlockchain(Blockchain bc) {
        this.blockchain = bc;
    }

    public Transaction signTransaction(Hash receiver, double amount) {
        return new Transaction(keys.getPublic(), address, receiver, amount, keys.getPrivate());
    }

    public double getAmount() {
        double amount = 0;

        for (Block block: blockchain.getBlocks()) {
            if (block.getTransaction().isSender(address)) {
                amount -= block.getTransaction().getAmount();
            }

            if (block.getTransaction().isReceiver(address)) {
                amount += block.getTransaction().getAmount();
            }
        }

        return amount;
    }

    public Hash getAddress() {
        return address;
    }

    public static void main(String[] args) {
        Node node1 = new Node();
        Node node2 = new Node();

        Transaction transaction = node1.signTransaction(node2.getAddress(), 10);
        Transaction transaction2 = node2.signTransaction(node1.getAddress(), 5);
        Blockchain blockchain = new Blockchain();
        blockchain.addBlock(transaction);
        blockchain.addBlock(transaction2);
        node1.setBlockchain(blockchain);
        node2.setBlockchain(blockchain);

        System.out.println(node1.getAmount());
    }
}
