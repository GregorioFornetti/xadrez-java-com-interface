package xadrez;

import enums.Cor;
import java.awt.Frame;

public class Dama extends Peca {

    public Dama(Cor cor, Frame framePrincipal) {
        super(cor, "dama", framePrincipal);
    }

    @Override
    public boolean checaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
        // Verifica se o movimento da dama é válido (diagonal, horizontal e na vertical, indefinidas casas).
        if (linhaOrigem == linhaDestino && colunaOrigem == colunaDestino)
            return false;
        
        return (Math.abs(linhaOrigem - linhaDestino) == Math.abs(colunaOrigem - colunaDestino)) ||
               (linhaOrigem == linhaDestino || colunaOrigem == colunaDestino);
    }
}
