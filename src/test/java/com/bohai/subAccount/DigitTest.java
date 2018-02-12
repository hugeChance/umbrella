package com.bohai.subAccount;

import java.math.BigDecimal;

public class DigitTest {

    public static void main(String[] args) {
        
        BigDecimal bg = new BigDecimal("20");
        
        System.out.println(bg.scale());
    }
}
