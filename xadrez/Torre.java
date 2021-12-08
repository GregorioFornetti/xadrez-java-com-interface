package xadrez;

import enums.Cor;
import java.awt.Frame;

public class Torre extends Peca {

    public Torre(Cor cor, Frame framePrincipal) {
        super(cor, "torre", framePrincipal);
    }

    @Override
    public boolean checaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
        // Checa se o movimento da torre é válido. A torre deve poder se movimentar na horizontal e vertical indefinidas casas.
        if (linhaOrigem == linhaDestino && colunaOrigem == colunaDestino)
            return false;
        
        return (linhaOrigem == linhaDestino || colunaOrigem == colunaDestino);
    }
}