package xadrez;

import enums.Cor;
import java.awt.Frame;

public class Rei extends Peca {

    public Rei(Cor cor, Frame framePrincipal) {
        super(cor, "rei", framePrincipal);
    }

    @Override
    public boolean checaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
        // Checa se o movimento do rei é válido. O rei só pode se mover uma casa em todas direções.
        if (linhaOrigem == linhaDestino && colunaOrigem == colunaDestino)
            return false;
        
        return Math.abs(linhaOrigem - linhaDestino) <= 1 && Math.abs(colunaOrigem - colunaDestino) <= 1;
    }
}
