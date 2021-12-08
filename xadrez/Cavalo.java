package xadrez;

import enums.Cor;
import java.awt.Frame;

public class Cavalo extends Peca {  

    public Cavalo(Cor cor, Frame framePrincipal) {
        super(cor, "cavalo", framePrincipal);
    }

    
    @Override
    public boolean checaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
        // Checa se o movimento do cavalo é válido (se é em L).
        if (linhaOrigem == linhaDestino && colunaOrigem == colunaDestino)
            return false;
        
        return (Math.abs(linhaOrigem - linhaDestino) == 2 && Math.abs(colunaOrigem - colunaDestino) == 1) ||
               (Math.abs(linhaOrigem - linhaDestino) == 1 && Math.abs(colunaOrigem - colunaDestino) == 2);
    }
}