package sistem;

public class StatusLaundry {
    private int idStatus;
    private String namaStatus;
    private String keterangan;

    public StatusLaundry(int idStatus, String namaStatus, String keterangan) {
        this.idStatus = idStatus;
        this.namaStatus = namaStatus;
        this.keterangan = keterangan;
    }

    public void updateStatus(String namaStatus, String keterangan) {
        this.namaStatus = namaStatus;
        this.keterangan = keterangan;
    }

    public String getNamaStatus(){
        return namaStatus;
    }

    public String getKeterangan() {
        return keterangan;
    }
}
