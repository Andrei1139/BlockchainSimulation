import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class Transaction {
    private PublicKey sender;
    private double amount;
    private Hash senderAddr, recvAddr;
    private byte[] digitalSignature;

    public Transaction(PublicKey sender, Hash senderAddr, Hash recvAddr, double amount, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);

            signature.update(sender.toString().getBytes(StandardCharsets.UTF_8));
            signature.update(senderAddr.getHash());
            signature.update(recvAddr.getHash());
            signature.update(Double.toString(amount).getBytes(StandardCharsets.UTF_8));

            digitalSignature = signature.sign();

            this.sender = sender;
            this.senderAddr = senderAddr;
            this.recvAddr = recvAddr;
            this.amount = amount;
        } catch (Exception e) {
            System.out.println("Exception reached for Transaction constructor");
            System.out.println(e.getMessage());
        }
    }

    public boolean verify() {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(sender);

            signature.update(sender.toString().getBytes(StandardCharsets.UTF_8));
            signature.update(senderAddr.getHash());
            signature.update(recvAddr.getHash());
            signature.update(Double.toString(amount).getBytes(StandardCharsets.UTF_8));

            return signature.verify(digitalSignature);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Exception reached for Transaction verify()");
            return false;
        }
    }

    public byte[] getDigitalSignature() {
        return digitalSignature;
    }

    public boolean isSender(Hash addr) {
        return senderAddr.equals(addr);
    }

    public boolean isReceiver(Hash addr) {
        return recvAddr.equals(addr);
    }

    public double getAmount() {
        return amount;
    }

    public byte[] getSender() {
        return senderAddr.getHash();
    }

    public byte[] getReceiver() {
        return recvAddr.getHash();
    }

    public byte[] getSenderPublicKeyEncoding() {
        return sender.getEncoded();
    }
}
