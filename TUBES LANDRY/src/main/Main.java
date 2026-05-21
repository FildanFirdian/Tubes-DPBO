package main;

import actor.Admin;
import actor.Pelanggan;
import actor.User;

public class Main {
    public static void main(String[] args){
        User p1 = new Pelanggan(1, "Azizi", "08123456789", "Jl. Merdeka No. 123", "azizi123", "202020", 100, true);
        User a1 = new Admin(2, "Reja", "08198765432", "Jl. Sudirman No. 456", "admin1", "admin123");

        p1.login();
        System.out.println();
        p1.tampilkanInfo();
        System.out.println();
        a1.login();
        System.out.println();
        a1.tampilkanInfo();
    }
}
