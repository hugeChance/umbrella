package com.bohai.subAccount;

import java.io.FileWriter;
import java.io.IOException;

public class WriteFileTest {
    
    public static void main(String[] args) {
        
        
            
            FileWriter fw = null;
            try {
                fw = new FileWriter("D:/111111111.txt");
                fw.write("helloworld\r\n 哈哈哈");  
                
            } catch (IOException e) {
                
            } finally {
                try {
                    fw.close();
                } catch (IOException e) {
                }
            }
    }

}
