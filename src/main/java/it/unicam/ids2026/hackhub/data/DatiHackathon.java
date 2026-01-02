package it.unicam.ids2026.hackhub.data;
import lombok.Getter;
import lombok.Setter;

public class DatiHackathon {
    @Getter
    private String nome;
    @Getter
    private String luogo;
    /* private BigDecimal premioInDenaro;
     Da controllare se poi si decide di implementare la classe
     Money
     */
    @Getter
    private int dimensioneMaxTeam;
    @Getter @Setter
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
