package rkm.client;

import java.awt.event.KeyEvent;

public class Test {

    static void print(int i) {
        System.out.println("" +  i);
    }

    public static void main(String[] args) {
        print(KeyEvent.VK_WINDOWS);
        print(KeyEvent.VK_CONTEXT_MENU);
    }


}
