package actor;

public class Pelanggan extends User {
    private int poinMember;
    private boolean statusMember;
    
    public Pelanggan(int idUser, String nama, String noHp, String alamat, String username, String password, int poinMember, boolean statusMember){
        super(idUser,nama,noHp,alamat,username,password);
        this.poinMember = poinMember;
        this.statusMember = statusMember;
    }
    
    public void daftarMember(){
        if (statusMember == true){
            System.out.println(super.getName() + " berhasil daftar sebagai member");
        }else{
            System.out.println(super.getName() + "gagal mendaftar sebagai member");
        }
    }
    
    public void lihatStatus(){
        if(statusMember){
            System.out.println(super.getName() + " adalah member aktif");
        }else{
            System.out.println(super.getName() + " belum menjadi member");
        }
    }
    
    public void Payment(){
        System.out.println(super.getName() + " sudah melakukan transaksi");
    }
    
    @Override
    public void login(){
        System.out.println("Pelanggan " + " dengan username " + super.getUsername() + " berhasil login."); 
    }
    
    @Override
    public void logout(){
        System.out.println("Pelanggan " + " dengan username " + super.getUsername() + " berhasil logout");
    }
    
    @Override
    public void tampilkanInfo(){
        System.out.println("=== Data Pelanggan ===");
        System.out.println("ID User : " + super.getIdUser());
        System.out.println("Nama : " + super.getName());
        System.out.println("No Hp : " + super.getNoHp());
        System.out.println("Alamat : " + super.getAlamat());
        System.out.println("Username : " + super.getUsername());
        System.out.println("Password : " + super.getPassword());
    }
}
