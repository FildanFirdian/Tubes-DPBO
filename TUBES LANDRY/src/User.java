/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Azizi
 */
public abstract class User {
    private int idUser;
    private String name;
    private String noHp;
    private String alamat;
    private String username;
    private String password;
    
    
    public User(int idUser, String name, String noHp, String alamat, String username, String password){
        this.idUser = idUser;
        this.name = name;
        this.noHp = noHp;
        this.alamat = alamat;
        this.username = username;
        this.password = password;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void login(){
        System.out.println("Sudah memiliki akun");
    }
    
    public void logout(){
        System.out.println("Anda sudah keluar dari akun");
    }
    
    public abstract void tampilkanInfo();
}
