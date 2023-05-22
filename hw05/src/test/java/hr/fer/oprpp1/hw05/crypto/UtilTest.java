package hr.fer.oprpp1.hw05.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {

    @Test
    public void testHexToByte_IllegalInput(){
        assertThrows(NullPointerException.class, () -> Util.hexToByte(null));
        assertThrows(IllegalArgumentException.class, () -> Util.hexToByte("a"));
        assertThrows(IllegalArgumentException.class, () -> Util.hexToByte("1234abfez"));
    }

    @Test
    public void testHexToByte(){
        assertArrayEquals(new byte[] {}, Util.hexToByte(""));
        assertArrayEquals(new byte[] {1, -82, 34}, Util.hexToByte("01aE22"));
    }

    @Test
    public void testByteToHex_IllegalInput(){
        assertThrows(NullPointerException.class, () -> Util.byteToHex(null));
    }

    @Test
    public void testByteToHex(){
        assertEquals("", Util.byteToHex(new byte[] {}));
        assertEquals("01ae22", Util.byteToHex(new byte[] {1, -82, 34}));
    }


}
