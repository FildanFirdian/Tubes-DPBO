package sistem;

public class StatusLaundry {
    private int idStatus;
    private String namaStatus;

    public StatusLaundry(int idStatus, String namaStatus) {
        this.idStatus = idStatus;
        this.namaStatus = namaStatus;
    }

    public int getIdStatus() { return idStatus; }
    public String getNamaStatus() { return namaStatus; }

    @Override
    public String toString() {
        return namaStatus;
    }
}