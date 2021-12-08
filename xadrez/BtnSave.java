package xadrez;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;

public class BtnSave extends JButton {
    /* 
        Botão de save do xadrez
        utilizado para criar o menu de carragamento de jogo do xadrez (partidas salvas)
    */
    private boolean isValid;
    public static final int LARGURA = 900;
    public static final int ALTURA = 80;

    public BtnSave(File file) {
        isValid = false;
        Scanner scan = null;
        try {
            scan = new Scanner(file);
            JLabel lblNomePartida = new JLabel(file.getName().replace(".txt", ""));
            lblNomePartida.setBounds(10, 5, LARGURA, 20);
            JLabel lblNomeJogadorBrancas = new JLabel("Jogador das brancas: " + scan.nextLine());
            lblNomeJogadorBrancas.setBounds(10, 30, LARGURA, 20);
            JLabel lblNomeJogadorPretas = new JLabel("Jogador das pretas: " + scan.nextLine());
            lblNomeJogadorPretas.setBounds(10, 55, LARGURA, 20);
            add(lblNomePartida);
            add(lblNomeJogadorBrancas);
            add(lblNomeJogadorPretas);
            isValid = true;
        } catch(IOException ex) {
            System.out.println("Não foi possível abrir o arquivo de save.");
        } catch(Exception ex) {
            System.out.println("Arquivo de save inválido");
        } finally {
            if (scan != null)
                scan.close();
        }
        repaint();
    }

    public boolean isValid() {  
        // Caso o botão tenha sido criado com sucesso, isValid retornará true.
        // Se o arquivo de save for inválido, provavelmente isValid retornará false.
        return isValid;
    }
}