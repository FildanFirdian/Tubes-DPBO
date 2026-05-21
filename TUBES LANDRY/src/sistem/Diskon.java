package sistem;

public class Diskon {
    private int idDiskon;
    private String namaDiskon;
    private double persenDiskon;
    private String syaratDiskon;
    
    public Diskon(){}
    
    public Diskon(int idDiskon, String namaDiskon, double persenDiskon, String syaratDiskon){
        this.idDiskon = idDiskon;
        this.namaDiskon = namaDiskon;
        this.persenDiskon = persenDiskon;
        this.syaratDiskon = syaratDiskon;
    }
    
    public double hitungPotongan(double Total){
        return Total * (persenDiskon / 100);
    }

    public String getNamaDiskon() {
        return namaDiskon;
    }

    public String getSyaratDiskon() {
        return syaratDiskon;
    }

}
