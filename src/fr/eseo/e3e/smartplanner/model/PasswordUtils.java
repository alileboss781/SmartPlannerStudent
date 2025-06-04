package fr.eseo.e3e.smartplanner.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * The PasswordUtils class is provided to show a mechanism for creating a hash of a password, using the SHA-256 algorithm.
 * The string containing the password, is hashed into a byte array, along with 128 bits of random data (called a salt).
 *
 * There are also methods, which allow the hashed password array to be converted into a Base64 encoded String, which in the real world could be used to store the password in a database.
 * There is also a method which allows the reverse conversion.
 */
public class PasswordUtils {

    /**
     * The default constructor has the visibility private, to stop other classes creating an instance of this class.
     * As the class contains only static methods, this class never needs to be instantiated.
     */
    private PasswordUtils() {}

    /**
     * Generates a random sequence of 128 bits, to be used as a salt (<a href="https://en.wikipedia.org/wiki/Salt_(cryptography)">see wikipedia article for more information</a>)
     * Uses {@link SecureRandom} to create cryptographically strong random data.
     *
     * @return a byte array containing the salt
     */
    public static byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Hashes a password using SHA-256 with the provided salt.
     *
     * Utilizes <a href="https://en.wikipedia.org/wiki/Secure_Hash_Algorithms">SHA-256</a> for secure one-way password hashing.
     *
     * @param password the password to hash
     * @param salt the salt to use
     * @return the hashed password as a byte array
     * @throws NoSuchAlgorithmException if the hashing algorithm is not available
     * @see <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/security/MessageDigest.html">MessageDigest Class</a>
     * @see <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/charset/StandardCharsets.html">StandardCharsets Class</a>
     */
    public static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        if("".equals(password)){
            throw new IllegalArgumentException("Password cannot be empty");
        }
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        return digest.digest(password.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Converts a byte array to a Base64-encoded string.
     *
     * @param bytes the byte array to encode
     * @return the Base64-encoded string
     * @see <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Base64.html">Base64 Class</a>
     */
    public static String toBase64(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Decodes a Base64-encoded string to a byte array.
     *
     * @param base64String the Base64-encoded string
     * @return the decoded byte array
     * @see <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Base64.html">Base64 Class</a>
     */
    public static byte[] fromBase64(String base64String){
        return Base64.getDecoder().decode(base64String);
    }

    /**
     * Verifies if the provided password matches the stored hash.
     *
     * Uses a time-constant comparison to prevent timing attacks.
     *
     * @param password the password to check
     * @param storedHash the stored password hash
     * @param salt the salt used for hashing
     * @return true if the password matches, false otherwise
     * @throws NoSuchAlgorithmException if the hashing algorithm is not available
     * @see <a href="https://en.wikipedia.org/wiki/Timing_attack">Timing Attack Prevention</a>
     * @see <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/security/MessageDigest.html">MessageDigest Class</a>
     */
    public static boolean verifyPassword(String password, byte[] storedHash, byte[] salt) throws NoSuchAlgorithmException {
        byte[] hashToCheck = hashPassword(password, salt);
        if(hashToCheck.length != storedHash.length){
            return false;
        }
        int diff = 0;
        for(int i = 0; i < hashToCheck.length; i++){
            diff |= hashToCheck[i] ^ storedHash[i];
        }
        return diff == 0;
    }

}
