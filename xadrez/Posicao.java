package xadrez;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Posicao extends JComponent {

    public static final int ALTURA = 70;
    public static final int LARGURA = 70;
    private Peca peca;
    private Marcacao marcacao;

    public Posicao(JFrame framePrincipal) {
        this.peca = null;
        this.marcacao = new Marcacao(framePrincipal);
        marcacao.setBounds(LARGURA - Marcacao.LARGURA, ALTURA - Marcacao.ALTURA, LARGURA, ALTURA);
    }

    public void apagarMarcacao() {
        marcacao.mudarMarcacaoParaVermelho();
        remove(marcacao);
    }

    public void mostrarMarcacaoVermelha() {
        marcacao.mudarMarcacaoParaVermelho();
        add(marcacao);
    }

    public void mostrarMarcacaoAzul() {
        marcacao.mudarMarcacaoParaAzul();
        add(marcacao);
    }

    public boolean isVazia() {
        return peca == null;
    }

    public Peca getPeca() {
        return peca;
    }

    public void adicionarPeca(Peca peca) {
        if (this.peca != null) {
            removerPeca();
        }
        this.peca = peca;
        peca.setBounds((LARGURA - Peca.LARGURA) / 2, (ALTURA - Peca.ALTURA) / 2, Peca.LARGURA, Peca.ALTURA);
        add(this.peca);
    }

    public void removerPeca() {
        remove(this.peca);
        this.peca = null;
    }
}
