package xadrez;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import enums.Cor;
import java.awt.Frame;

public abstract class Peca extends JLabel {

    public static final int LARGURA = 64;
    public static final int ALTURA = 64;
    private boolean jaMovimentou;
    private Cor cor;
    
    public Peca(Cor cor, String nomePeca, Frame framePrincipal) {
        this.cor = cor;
        setSize(LARGURA, ALTURA);
        desenhar(nomePeca, framePrincipal);
        jaMovimentou = false;
    }

    public void marcarMovimentacao() {
        jaMovimentou = true;
    }

    public boolean jaMovimentou() {
        return jaMovimentou;
    }


    public Cor getCor() {
        return cor;
    }

    public void desenhar(String nomePeca, Frame frame) {
        
        try {
            File file = new File("sprites/" + nomePeca.toLowerCase() + "-" + cor.toString().toLowerCase() + ".png");
            setIcon(new ImageIcon(ImageIO.read(file)));
        } catch(IOException ex) {  
            // Tentar fazer uma mensagem de erro que as imagens da peça não foi encontrada
            
            JOptionPane.showMessageDialog(frame,
            "<html>Não foi possível carregar a imagem " + "sprites/" + nomePeca.toLowerCase() + "-" + cor.toString().toLowerCase() + ".png.<br> Verifique se está com todas as dependências do jogo junto ao executável.</html>",
            "Erro ao carregar imagem da peça.",
            JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public abstract boolean checaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino);
}
