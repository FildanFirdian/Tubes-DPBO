package sistem;
import java.util.Date;

public class Member{
    private int idMember;
    private String levelMember;
    private int poin;
    private Date masaAktif;
    
    
    public Member(int idMember, String levelMember, int poin, Date masaAktif){
        this.idMember = idMember;
        this.levelMember = levelMember;
        this.poin = poin;
        this.masaAktif = masaAktif;
    }
    
    public void tambahPoin(int jumlahPoin){
        this.poin += jumlahPoin;
    }
    
    public boolean cekKeanggotaan(){
        Date sekarang = new Date();
        return masaAktif.after(sekarang);
    }

    public int getPoin() {
        return poin;
    }

    public String getLevelMember() {
        return levelMember;
    }
    
}
