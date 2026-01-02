package it.unicam.ids2026.hackhub.data;

public class DatiHackathon {
    private String nome;
    private String luogo;
    // private BigDecimal premioInDenaro; Da controllare
    private int dimensioneMaxTeam;
    private String regolamento;
    public DatiHackathon(String nome, String luogo,
                         int dimensioneMaxTeam, String regolamento) {
        this.nome = nome;
        this.luogo = luogo;
        // this.premioInDenaro = premioInDenaro;
        this.dimensioneMaxTeam = dimensioneMaxTeam;
        this.regolamento = regolamento;
    }
}
