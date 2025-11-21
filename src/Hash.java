import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.Arrays;

public class Hash {
    private final byte[] hash;

    public Hash(byte[] hash) {
        if (hash == null) {
            throw new RuntimeException();
        }
        this.hash = hash;
    }

    public Hash() {
        this.hash = new byte[]{0};
    }

    public static Hash generateWalletAddress(PublicKey key) {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-1");
            digester.update(key.getEncoded());

            return new Hash(digester.digest());
        } catch (Exception e) { // Impossible to reach, but must be handled
            return new Hash();
        }
    }

    public static Hash generateHash(Block block) {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-256");

            digester.update(block.getPrevHash().toString().getBytes(StandardCharsets.UTF_8));
            digester.update(Long.toString(block.getTimeStamp()).getBytes(StandardCharsets.UTF_8));
            digester.update(Integer.toString(block.getNonce()).getBytes(StandardCharsets.UTF_8));

            digester.update(block.getTransaction().getSenderPublicKeyEncoding());
            digester.update(block.getTransaction().getSender());
            digester.update(block.getTransaction().getReceiver());
            digester.update(Double.toString(block.getTransaction().getAmount()).getBytes(StandardCharsets.UTF_8));
            digester.update(block.getTransaction().getDigitalSignature());

            return new Hash(digester.digest());
        } catch (Exception e) { // Impossible to reach, but must be handled
            return new Hash();
        }
    }

    public boolean isPOW(int difficulty) {
        for (int i = 0; i < difficulty; ++i) {
            byte currByte = hash[i / 2];

            if (i % 2 == 0) {
                if ((currByte & (byte)0xf0) != 0) {
                    return false;
                }
            } else {
                if ((currByte & (byte)0x0f) != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public byte[] getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return byteArrToString(hash);
    }

    public static String byteArrToString(byte[] data) {
        StringBuilder builder = new StringBuilder();

        for (byte b: data) {
            String bits = Integer.toHexString(b & 0xff);
            for (int i = 0; i < 2 - bits.length(); ++i)
                builder.append('0');
            builder.append(bits);
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Hash)) {
            return false;
        }

        Hash other = (Hash)obj;

        return Arrays.equals(this.hash, other.hash);
    }
}
