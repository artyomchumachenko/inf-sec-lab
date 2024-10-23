package ru.mai.is.stribog;

import org.junit.jupiter.api.Test;

import ru.mai.is.algorithm.stribog.StribogImpl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static ru.mai.is.algorithm.stribog.util.StringToByteArray.bytesToHex;
import static ru.mai.is.algorithm.stribog.util.StringToByteArray.castStringToBytes;

public class StribogImplTest {
    
    /**
     * Test of getHash method, of class StribogImpl.
     */
    @Test
    public void TestFromGOST_512_1() {
        System.out.println("getHash");
        byte[] message = {
             (byte) 0x32, (byte) 0x31, (byte) 0x30, (byte) 0x39, (byte) 0x38, (byte) 0x37, (byte) 0x36, (byte) 0x35,
             (byte) 0x34, (byte) 0x33, (byte) 0x32, (byte) 0x31, (byte) 0x30, (byte) 0x39, (byte) 0x38, (byte) 0x37,
             (byte) 0x36, (byte) 0x35, (byte) 0x34, (byte) 0x33, (byte) 0x32, (byte) 0x31, (byte) 0x30, (byte) 0x39,
             (byte) 0x38, (byte) 0x37, (byte) 0x36, (byte) 0x35, (byte) 0x34, (byte) 0x33, (byte) 0x32, (byte) 0x31,
             (byte) 0x30, (byte) 0x39, (byte) 0x38, (byte) 0x37, (byte) 0x36, (byte) 0x35, (byte) 0x34, (byte) 0x33,
             (byte) 0x32, (byte) 0x31, (byte) 0x30, (byte) 0x39, (byte) 0x38, (byte) 0x37, (byte) 0x36, (byte) 0x35,
             (byte) 0x34, (byte) 0x33, (byte) 0x32, (byte) 0x31, (byte) 0x30, (byte) 0x39, (byte) 0x38, (byte) 0x37,
             (byte) 0x36, (byte) 0x35, (byte) 0x34, (byte) 0x33, (byte) 0x32, (byte) 0x31, (byte) 0x30};
        boolean outputMode = true;
        StribogImpl instance = new StribogImpl();
        byte[] expResult = {
            (byte) 0x48, (byte) 0x6f, (byte) 0x64, (byte) 0xc1, (byte) 0x91, (byte) 0x78, (byte) 0x79, (byte) 0x41,
            (byte) 0x7f, (byte) 0xef, (byte) 0x08, (byte) 0x2b, (byte) 0x33, (byte) 0x81, (byte) 0xa4, (byte) 0xe2,
            (byte) 0x11, (byte) 0xc3, (byte) 0x24, (byte) 0xf0, (byte) 0x74, (byte) 0x65, (byte) 0x4c, (byte) 0x38,
            (byte) 0x82, (byte) 0x3a, (byte) 0x7b, (byte) 0x76, (byte) 0xf8, (byte) 0x30, (byte) 0xad, (byte) 0x00,
            (byte) 0xfa, (byte) 0x1f, (byte) 0xba, (byte) 0xe4, (byte) 0x2b, (byte) 0x12, (byte) 0x85, (byte) 0xc0,
            (byte) 0x35, (byte) 0x2f, (byte) 0x22, (byte) 0x75, (byte) 0x24, (byte) 0xbc, (byte) 0x9a, (byte) 0xb1,
            (byte) 0x62, (byte) 0x54, (byte) 0x28, (byte) 0x8d, (byte) 0xd6, (byte) 0x86, (byte) 0x3d, (byte) 0xcc,
            (byte) 0xd5, (byte) 0xb9, (byte) 0xf5, (byte) 0x4a, (byte) 0x1a, (byte) 0xd0, (byte) 0x54, (byte) 0x1b};

        byte[] result = instance.getHash(message, outputMode);
        System.out.println("Message in Hex: " + bytesToHex(message));
        System.out.println("Result in Hex: " + bytesToHex(result));
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of getHash method, of class StribogImpl.
     */
    @Test
    public void TestFromGOST_256_1() {
        String textForHashing = "323130393837363534333231303938373635343332313039383736353433323130393837363534333231303938373635343332313039383736353433323130";
        String expectedResultOfHashing = "00557be5e584fd52a449b16b0251d05d27f94ab76cbaa6da890b59d8ef1e159d";

        byte[] byteArrayForHashing = castStringToBytes(textForHashing);
        byte[] expectedByteArrayForHashing = castStringToBytes(expectedResultOfHashing);

        boolean outputMode = false; // Задаём для метода хэширования длину хэш-кода 256 бит
        StribogImpl stribog = new StribogImpl();

        byte[] resultByteArray = stribog.getHash(byteArrayForHashing, outputMode);

        System.out.println("Message in Hex: " + textForHashing);
        System.out.println("Result in Hex: " + bytesToHex(resultByteArray));

        assertArrayEquals(expectedByteArrayForHashing, resultByteArray);
    }
    
    /**
     * Test of getHash method, of class StribogImpl.
     */
    @Test
    public void TestFromGOST_512_2() {
        System.out.println("getHash");
        byte[] message = {
             (byte) 0xfb, (byte) 0xe2, (byte) 0xe5, (byte) 0xf0, (byte) 0xee, (byte) 0xe3, (byte) 0xc8, (byte) 0x20,
             (byte) 0xfb, (byte) 0xea, (byte) 0xfa, (byte) 0xeb, (byte) 0xef, (byte) 0x20, (byte) 0xff, (byte) 0xfb,
             (byte) 0xf0, (byte) 0xe1, (byte) 0xe0, (byte) 0xf0, (byte) 0xf5, (byte) 0x20, (byte) 0xe0, (byte) 0xed,
             (byte) 0x20, (byte) 0xe8, (byte) 0xec, (byte) 0xe0, (byte) 0xeb, (byte) 0xe5, (byte) 0xf0, (byte) 0xf2,
             (byte) 0xf1, (byte) 0x20, (byte) 0xff, (byte) 0xf0, (byte) 0xee, (byte) 0xec, (byte) 0x20, (byte) 0xf1,
             (byte) 0x20, (byte) 0xfa, (byte) 0xf2, (byte) 0xfe, (byte) 0xe5, (byte) 0xe2, (byte) 0x20, (byte) 0x2c,
             (byte) 0xe8, (byte) 0xf6, (byte) 0xf3, (byte) 0xed, (byte) 0xe2, (byte) 0x20, (byte) 0xe8, (byte) 0xe6,
             (byte) 0xee, (byte) 0xe1, (byte) 0xe8, (byte) 0xf0, (byte) 0xf2, (byte) 0xd1, (byte) 0x20, (byte) 0x2c,
             (byte) 0xe8, (byte) 0xf0, (byte) 0xf2, (byte) 0xe5, (byte) 0xe2, (byte) 0x20, (byte) 0xe5, (byte) 0xd1};
        boolean outputMode = true;
        StribogImpl instance = new StribogImpl();
        byte[] expResult = {
            (byte) 0x28, (byte) 0xfb, (byte) 0xc9, (byte) 0xba, (byte) 0xda, (byte) 0x03, (byte) 0x3b, (byte) 0x14, 
            (byte) 0x60, (byte) 0x64, (byte) 0x2b, (byte) 0xdc, (byte) 0xdd, (byte) 0xb9, (byte) 0x0c, (byte) 0x3f, 
            (byte) 0xb3, (byte) 0xe5, (byte) 0x6c, (byte) 0x49, (byte) 0x7c, (byte) 0xcd, (byte) 0x0f, (byte) 0x62, 
            (byte) 0xb8, (byte) 0xa2, (byte) 0xad, (byte) 0x49, (byte) 0x35, (byte) 0xe8, (byte) 0x5f, (byte) 0x03,
            (byte) 0x76, (byte) 0x13, (byte) 0x96, (byte) 0x6d, (byte) 0xe4, (byte) 0xee, (byte) 0x00, (byte) 0x53, 
            (byte) 0x1a, (byte) 0xe6, (byte) 0x0f, (byte) 0x3b, (byte) 0x5a, (byte) 0x47, (byte) 0xf8, (byte) 0xda, 
            (byte) 0xe0, (byte) 0x69, (byte) 0x15, (byte) 0xd5, (byte) 0xf2, (byte) 0xf1, (byte) 0x94, (byte) 0x99, 
            (byte) 0x6f, (byte) 0xca, (byte) 0xbf, (byte) 0x26, (byte) 0x22, (byte) 0xe6, (byte) 0x88, (byte) 0x1e};
        byte[] result = instance.getHash(message, outputMode);
        System.out.println("Message in Hex: " + bytesToHex(message));
        System.out.println("Result in Hex: " + bytesToHex(result));
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of getHash method, of class StribogImpl.
     */
    @Test
    public void TestFromGOST_256_2() {
        System.out.println("getHash");
        byte[] message = {
                (byte) 0xfb, (byte) 0xe2, (byte) 0xe5, (byte) 0xf0, (byte) 0xee, (byte) 0xe3, (byte) 0xc8, (byte) 0x20,
                (byte) 0xfb, (byte) 0xea, (byte) 0xfa, (byte) 0xeb, (byte) 0xef, (byte) 0x20, (byte) 0xff, (byte) 0xfb,
                (byte) 0xf0, (byte) 0xe1, (byte) 0xe0, (byte) 0xf0, (byte) 0xf5, (byte) 0x20, (byte) 0xe0, (byte) 0xed,
                (byte) 0x20, (byte) 0xe8, (byte) 0xec, (byte) 0xe0, (byte) 0xeb, (byte) 0xe5, (byte) 0xf0, (byte) 0xf2,
                (byte) 0xf1, (byte) 0x20, (byte) 0xff, (byte) 0xf0, (byte) 0xee, (byte) 0xec, (byte) 0x20, (byte) 0xf1,
                (byte) 0x20, (byte) 0xfa, (byte) 0xf2, (byte) 0xfe, (byte) 0xe5, (byte) 0xe2, (byte) 0x20, (byte) 0x2c,
                (byte) 0xe8, (byte) 0xf6, (byte) 0xf3, (byte) 0xed, (byte) 0xe2, (byte) 0x20, (byte) 0xe8, (byte) 0xe6,
                (byte) 0xee, (byte) 0xe1, (byte) 0xe8, (byte) 0xf0, (byte) 0xf2, (byte) 0xd1, (byte) 0x20, (byte) 0x2c,
                (byte) 0xe8, (byte) 0xf0, (byte) 0xf2, (byte) 0xe5, (byte) 0xe2, (byte) 0x20, (byte) 0xe5, (byte) 0xd1};

        boolean outputMode = false;
        StribogImpl instance = new StribogImpl();
        byte[] expResult = {
                (byte) 0x50, (byte) 0x8f, (byte) 0x7e, (byte) 0x55, (byte) 0x3c, (byte) 0x06, (byte) 0x50, (byte) 0x1d,
                (byte) 0x74, (byte) 0x9a, (byte) 0x66, (byte) 0xfc, (byte) 0x28, (byte) 0xc6, (byte) 0xca, (byte) 0xc0,
                (byte) 0xb0, (byte) 0x05, (byte) 0x74, (byte) 0x6d, (byte) 0x97, (byte) 0x53, (byte) 0x7f, (byte) 0xa8,
                (byte) 0x5d, (byte) 0x9e, (byte) 0x40, (byte) 0x90, (byte) 0x4e, (byte) 0xfe, (byte) 0xd2, (byte) 0x9d};
        byte[] result = instance.getHash(message, outputMode);
        System.out.println("Message in Hex: " + bytesToHex(message));
        System.out.println("Result in Hex: " + bytesToHex(result));
        assertArrayEquals(expResult, result);
    }
}
