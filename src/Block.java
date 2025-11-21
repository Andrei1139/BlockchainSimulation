public class Block {
    private Hash hash, prevHash;
    private Transaction transaction;
    private final long timeStamp;

    private int nonce;

    public int getNonce() {
        return nonce;
    }

    public Hash getHash() {
        return hash;
    }

    public void setHash(Hash hash) {
        this.hash = hash;
    }

    public Hash getPrevHash() {
        return prevHash;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Block(Transaction transaction) {
        this(transaction, new Hash());
    }

    public Block(Transaction transaction, Hash prevHash) {
        this.transaction = transaction;
        this.prevHash = prevHash;
        this.timeStamp = System.currentTimeMillis();

        this.hash = Hash.generateHash(this);
    }

    // Determine the smallest nonce such that the first [difficulty] digits of
    // the hash are all 0, thus proving work has been employed for its generation
    public void mine(int difficulty) {
        while (!hash.isPOW(difficulty)) {
            ++nonce;
            hash = Hash.generateHash(this);
        }

        System.out.println("Mined block: " + hash);
    }
}
