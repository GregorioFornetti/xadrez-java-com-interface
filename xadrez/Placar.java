package xadrez;

import javax.swing.*;

import enums.Cor;

import java.awt.Font;

public class Placar extends JComponent {
    
    private JLabel lblQntJogadas;
    private JLabel lblJogador;
    private String nomeJogadorDasBrancas;
    private String nomeJogadorDasPretas;
    public static final int ALTURA = 300;
    public static final int LARGURA = 300;

    public Placar(int numJogadaAtual, String nomeJogadorDasBrancas, String nomeJogadorDasPretas) {
        this.nomeJogadorDasPretas = nomeJogadorDasPretas;
        this.nomeJogadorDasBrancas = nomeJogadorDasBrancas;
        setBounds(0, 0, LARGURA, ALTURA);

        lblQntJogadas = new JLabel("Jogada: " + numJogadaAtual);
        lblJogador = new JLabel("Vez de: ");
        Font font = new Font("sans-serif", Font.PLAIN, 25);
        lblQntJogadas.setFont(font);
        lblJogador.setFont(font);
        lblQntJogadas.setBounds(30, 30, 270, 50);
        lblJogador.setBounds(30, 110, 270, 190);

        add(lblQntJogadas);
        add(lblJogador);
    }

    public void atualizarPlacar(int numJogadaAtual, Cor corJogadorAtual) {
        lblQntJogadas.setText("Jogada: " + numJogadaAtual);
        if (corJogadorAtual == Cor.BRANCO) {
            if (nomeJogadorDasBrancas.isEmpty()) {
                lblJogador.setText("Vez de: " + Cor.BRANCO);
            } else {
                lblJogador.setText("<html>Vez de: " + nomeJogadorDasBrancas + " (" + Cor.BRANCO + ")</html>");
            }
        }
        else {
            if (nomeJogadorDasPretas.isEmpty()) {
                lblJogador.setText("Vez de: " + Cor.PRETO);
            } else {
                lblJogador.setText("<html>Vez de: " + nomeJogadorDasPretas + " (" + Cor.PRETO + ")</html>");
            }
        }
        repaint();
    } 
}
