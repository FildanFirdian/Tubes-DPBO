package main;
public class Admin extends User {
    public Admin(int idUser, String nama, String noHp, String alamat, String username, String password){
        super(idUser,nama,noHp,alamat,username,password);
    }
    
    @Override
    public void login(){
        System.out.println("Admin " + " dengan username " +super.getUsername() + " berhasil login."); 
    }
    
    @Override
    public void logout(){
        System.out.println("Admin " + " dengan username " + super.getUsername() + " berhasil logout");
    }
    
    @Override
    public void tampilkanInfo(){
        System.out.println("=== Data Admin ===");
        System.out.println("ID User : " + super.getIdUser());
        System.out.println("Nama : " + super.getName());
        System.out.println("No Hp : " + super.getNoHp());
        System.out.println("Alamat : " + super.getAlamat());
        System.out.println("Username : " + super.getUsername());
        System.out.println("Password : " + super.getPassword());
    }
}