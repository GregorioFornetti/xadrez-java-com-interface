package xadrez;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Marcacao extends JLabel {

    public static final int ALTURA = 70;
    public static final int LARGURA = 70;
    private boolean vermelho;
    private JFrame framePrincipal;

    public Marcacao(JFrame framePrincipal) {
        this.framePrincipal = framePrincipal;
        vermelho = false;
        setSize(LARGURA, ALTURA);
        mudarMarcacaoParaVermelho();
    }
    
    public void mudarMarcacaoParaVermelho() {
        if (!vermelho) {
            try {
                File file = new File("sprites/marcacao-vermelho.png");
                setIcon(new ImageIcon(ImageIO.read(file)));
                vermelho = true;
            } catch(IOException ex) {  
                // Tentar fazer uma mensagem de erro que as imagens da peça não foi encontrada
                JOptionPane.showMessageDialog(framePrincipal,
                "Não foi possível carregar a imagem de marcação vermelha.<br>Verifique se a dependência sprites/marcacao-vermelho.png está disponível.",
                "Erro ao carregar imagem de marcação de posição.",
                JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }

    public void mudarMarcacaoParaAzul() {
        if (vermelho) {
            try {
                File file = new File("sprites/marcacao-azul.png");
                setIcon(new ImageIcon(ImageIO.read(file)));
                vermelho = false;
            } catch(IOException ex) {  
                // Tentar fazer uma mensagem de erro que as imagens da peça não foi encontrada
                JOptionPane.showMessageDialog(framePrincipal,
                "Não foi possível carregar a imagem de marcação azul.<br>Verifique se a dependência sprites/marcacao-azul.png está disponível.",
                "Erro ao carregar imagem de marcação de posição.",
                JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }
}
