package xadrez;

import enums.Cor;
import java.awt.Frame;

public class Bispo extends Peca {

    public Bispo(Cor cor, Frame framePrincipal) {
        super(cor, "bispo", framePrincipal);
    }

    @Override
    public boolean checaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
        // Verifica se o movimento do bispo é válido (na diagonal indefinidas casas)
        if (linhaOrigem == linhaDestino && colunaOrigem == colunaDestino)
            return false;
        
        return (Math.abs(linhaOrigem - linhaDestino) == Math.abs(colunaOrigem - colunaDestino));
    }
}
