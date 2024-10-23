package ru.mai.is.algorithm.stribog;

import java.io.File;


public interface Stribog {
    /**
     * 
     * @param file файл, для которого надо получить хеш
     * @param outputMode выбор длины хеша: <br><blockquote> true - 512 <br> false - 256</blockquote>
     * @return 256 или 512 разрядный хеш
     */
    public byte[] getHash(File file, boolean outputMode);
    
    /**
     * 
     * @param file файл, для которого надо получить хеш
     * @param outputMode выбор длины хеша: <br><blockquote> true - 512 <br> false - 256</blockquote>
     * @return 256 или 512 разрядный хеш
     */
    public byte[] getHash(String file, boolean outputMode);
    
    /**
     * 
     * @param message массив, для которого надо получить хеш
     * @param outputMode выбор длины хеша: <br><blockquote> true - 512 <br> false - 256</blockquote>
     * @return 256 или 512 разрядный хеш
     */
    public byte[] getHash(byte[] message, boolean outputMode);
    
}
