package xadrez;

import enums.Cor;
import java.awt.Frame;

public class Peao extends Peca {

    public Peao(Cor cor, Frame framePrincipal) {
        super(cor, "peao", framePrincipal);
    }

    @Override
    public boolean checaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
        // Verifica se o movimento do peão é válido
        if (linhaOrigem == linhaDestino && colunaOrigem == colunaDestino)
            return false;
        
        if (getCor() == Cor.PRETO) {
            // Peão preto deve poder mover para baixo. Duas casas para baixo no começo e uma depois de se movimentar uma vez.
            return (linhaOrigem == 7 && linhaDestino >= 5 && linhaDestino < 7 && (colunaOrigem == colunaDestino)) 
                   || (linhaOrigem != 7 && linhaOrigem - linhaDestino == 1 && (colunaOrigem == colunaDestino)) 
                   || (linhaDestino - linhaOrigem == -1  && (Math.abs(colunaOrigem - colunaDestino) == 1));
        } else {
            // Peão branco deve poder mover para cima. Duas casas para cima no começo e uma depois de se movimentar uma vez.
            return (linhaOrigem == 2 && linhaDestino <= 4 && linhaDestino > 2 && (colunaOrigem == colunaDestino)) 
                   || (linhaOrigem != 2 && linhaDestino - linhaOrigem == 1 && (colunaOrigem == colunaDestino)) 
                   || (linhaDestino - linhaOrigem == 1  && (Math.abs(colunaOrigem - colunaDestino) == 1));
        }
        // OBS: os peões também podem se movimentar na diagonal em uma casa (para baixo ou para cima dependendo da cor). Isso só deve ser realizado se for para captura e deve ser feito no tabuleiro, nesse método, o movimento na diagonal em uma casa deve ser sempre válido.
    }

}
