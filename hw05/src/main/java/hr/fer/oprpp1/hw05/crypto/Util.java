package hr.fer.oprpp1.hw05.crypto;

/**
 * Class with some utility methods for crypto operations.
 *
 * @author Ian Golob
 */
public class Util {

    /**
     * Returns a byte array calculated from the given hex string.
     * @param hex Hex string to calculate the byte array from.
     * @return Byte array created from the given hex string.
     * @throws IllegalArgumentException If the given hex input contains illegal characters or if it is odd-sized.
     * @throws NullPointerException If the given hex input is null.
     */
    public static byte[] hexToByte(String hex){

        if(hex == null){
            throw new NullPointerException("The given hex input is null.");
        }

        if(hex.length() % 2 != 0){
            throw new IllegalArgumentException("Hex input has the wrong amount of digits.");
        }

        byte[] bytes = new byte[hex.length()/2];

        for(int i = 0; i < hex.length(); i += 2){
            bytes[i/2] = (byte) ((hexToByte(hex.charAt(i)) << 4) + hexToByte(hex.charAt(i+1)));
        }

        return bytes;
    }

    /**
     * Returns the byte representation of a hex digit.
     * @param hex Hex digit to calculate the byte representation from.
     * @return The byte representation of a hex digit.
     * @throws IllegalArgumentException If the given char is not a valid hex digit.
     */
    private static byte hexToByte(char hex){
        return switch (Character.toLowerCase(hex)){
            case '0' -> 0;
            case '1' -> 1;
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;
            case 'a' -> 10;
            case 'b' -> 11;
            case 'c' -> 12;
            case 'd' -> 13;
            case 'e' -> 14;
            case 'f' -> 15;
            default -> throw new IllegalArgumentException("Illegal hex given, got:" + hex);
        };
    }

    /**
     * Returns a hex string calculated from the given byte array.
     * @param byteArray Byte array to calculate the hex string from.
     * @return Hex string calculated from the given byte array.
     * @throws NullPointerException If the given byte array is null.
     */
    public static String byteToHex(byte[] byteArray){

        if(byteArray == null){
            throw new NullPointerException("The given byte array is null.");
        }

        StringBuilder stringBuilder = new StringBuilder();

        for(byte b: byteArray){
            stringBuilder.append(byteToChar(((byte) (Byte.toUnsignedInt(b) >> 4))));
            stringBuilder.append(byteToChar(((byte) (Byte.toUnsignedInt(b) % 16))));
        }

        return stringBuilder.toString();
    }

    /**
     * Returns the hex character from the given byte.
     * @param byte4bit The 4 bit byte to calculate the hex character from.
     * @return The hex character from the given byte.
     * @throws IllegalArgumentException If the given byte is smaller than 0 or greater than 15.
     */
    private static char byteToChar(byte byte4bit){
        return switch (byte4bit){
            case 0 -> '0';
            case 1 -> '1';
            case 2 -> '2';
            case 3 -> '3';
            case 4 -> '4';
            case 5 -> '5';
            case 6 -> '6';
            case 7 -> '7';
            case 8 -> '8';
            case 9 -> '9';
            case 10 -> 'a';
            case 11 -> 'b';
            case 12 -> 'c';
            case 13 -> 'd';
            case 14 -> 'e';
            case 15 -> 'f';
            default -> throw new IllegalArgumentException("Illegal byte given, got:" + byte4bit);
        };
    }

}
