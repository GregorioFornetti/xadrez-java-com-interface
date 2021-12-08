package xadrez;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.Color;

import enums.Cor;

public class Jogo extends JComponent {
    
    public static final int LARGURA = 1024;
    public static final int ALTURA = 600;
    private String nomeJogadorBrancas;
    private String nomeJogadorPretas;
    private JComponent formulario;
    private Tabuleiro tabuleiro;
    private Placar placar;
    private Cor corJogadorAtual;
    private int qntJogadas;
    private JFrame framePrincipal;

    public Jogo(JFrame framePrincipal)  {
        this.framePrincipal = framePrincipal;
        criarFormularioInicioDePartida();
    }

    public Jogo(File save, JFrame framePrincipal)  {
        this.framePrincipal = framePrincipal;
        carregarJogo(save);
    }

    private void iniciarPartida(File save) {
        tabuleiro = new Tabuleiro(save, framePrincipal);
        tabuleiro.setBounds(0, 0, 560, 560);
        add(tabuleiro);
        tabuleiro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                movimentarPecaTabuleiro(evt);
            }
        });

        corJogadorAtual = Cor.BRANCO;
        qntJogadas = 0;

        placar = new Placar(qntJogadas + 1, nomeJogadorBrancas, nomeJogadorPretas);
        placar.setBounds(Tabuleiro.LARGURA + 10, 50, Placar.LARGURA, Placar.ALTURA);
        placar.atualizarPlacar(qntJogadas + 1, Cor.BRANCO);
        add(placar);
        repaint();
    }

    private void movimentarPecaTabuleiro(java.awt.event.MouseEvent evt) {
        char coluna = (char)('A' + evt.getX() / 70);
        int linha = 8 - (evt.getY() / 70);
        realizaMovimento(linha, coluna);
    }

    private void realizaMovimento(int linha, char coluna) {
        if (tabuleiro.isPosicaoOrigemSelecionada()) {
            if (tabuleiro.realizaMovimento(linha, coluna, corJogadorAtual, true, true)) {
                qntJogadas++;
                if (corJogadorAtual == Cor.BRANCO) 
                    corJogadorAtual = Cor.PRETO;
                else 
                    corJogadorAtual = Cor.BRANCO;
                placar.atualizarPlacar(qntJogadas + 1, corJogadorAtual);
            }
        } else {
            tabuleiro.escolherPosicaoInicial(linha, coluna, corJogadorAtual);
        }
    }

    private void criarFormularioInicioDePartida() {
        int alturaFormulario = 160;
        int larguraFormulario = 600;
        formulario = new JPanel(new GridLayout(4,3,5,20));
        formulario.setBounds(LARGURA / 2 - larguraFormulario / 2, ALTURA / 2 - alturaFormulario / 2 - 100, larguraFormulario, alturaFormulario);

        JLabel labelNomePartida = new JLabel("Nome da partida: ");
        labelNomePartida.setHorizontalAlignment(JLabel.RIGHT);
        JTextField inputNomePartida = new JTextField();
        JLabel labelErroNomePartida = criarLabelErro();

        JLabel labelNomeJogadorDasBrancas = new JLabel("Nome do jogador das brancas: ");
        labelNomeJogadorDasBrancas.setHorizontalAlignment(JLabel.RIGHT);
        JTextField inputNomeJogadorDasBrancas = new JTextField();
        JLabel labelErroNomeBrancas = criarLabelErro();
        
        JLabel labelNomeJogadorDasPretas = new JLabel("Nome do jogador das pretas: ");
        labelNomeJogadorDasPretas.setHorizontalAlignment(JLabel.RIGHT);
        JTextField inputNomeJogadorDasPretas = new JTextField();
        JLabel labelErroNomePretas = criarLabelErro();

        ImageIcon loadingIcon = new ImageIcon("sprites/ajax-loader-sm.gif");
        JLabel labelLoading = new JLabel(loadingIcon);
        loadingIcon.setImageObserver(labelLoading);
        labelLoading.setHorizontalAlignment(JLabel.LEFT);
        labelLoading.setVisible(false);
        
        JButton btnComecarPartida = new JButton("Começar partida");
        btnComecarPartida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                String nomePartida = inputNomePartida.getText().trim();
                nomeJogadorBrancas = inputNomeJogadorDasBrancas.getText().trim();
                nomeJogadorPretas = inputNomeJogadorDasPretas.getText().trim();

                try {
                    File save = salvarInicioPartida(nomePartida, nomeJogadorBrancas, nomeJogadorPretas, labelErroNomePartida);
                    if (save != null) {
                        labelLoading.setVisible(true);
                        repaint();

                        new Thread() {
                            public void run() {
                                iniciarPartida(save);
                                remove(formulario);
                                repaint();
                            }
                        }.start();
                    }
                } catch(IOException ex) {
                    JOptionPane.showMessageDialog(framePrincipal,
                    "Não foi possível iniciar a partida, pois ocorreu um erro ao criar o arquivo de save. Verifique se o nome do save é válido.",
                    "Erro ao criar arquivo de save inicial.",
                    JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        formulario.add(labelNomePartida);
        formulario.add(inputNomePartida);
        formulario.add(labelErroNomePartida);
        formulario.add(labelNomeJogadorDasBrancas);
        formulario.add(inputNomeJogadorDasBrancas);
        formulario.add(labelErroNomeBrancas);
        formulario.add(labelNomeJogadorDasPretas);
        formulario.add(inputNomeJogadorDasPretas);
        formulario.add(labelErroNomePretas);
        formulario.add(new JPanel());
        formulario.add(btnComecarPartida);
        formulario.add(labelLoading);
        add(formulario);
    }

    private File salvarInicioPartida(String nomePartida, String nomeJogadorBrancas, String nomeJogadorPretas, JLabel labelErroNomePartida) throws IOException {
        
        if (nomePartida.isEmpty()) {
            int numPartida = 1;
            while (true) {
                nomePartida = "Partida salva " + numPartida;
                File file = new File("saves/" + nomePartida + ".txt");
                if (!file.isFile())
                    break;
                numPartida++;
            }
        } else if (Pattern.compile("[^a-zA-Z0-9 ]").matcher(nomePartida).find()) {
            labelErroNomePartida.setText("Nome inválido !");
            repaint();
            return null;
        }
        
        File save = new File("saves/" + nomePartida + ".txt");
        if (save.isFile()) {
            labelErroNomePartida.setText("Save já existe !");
            repaint();
            return null;
        }
        FileWriter writer = new FileWriter(save);
        writer.write(nomeJogadorBrancas);
        writer.write(System.lineSeparator());
        writer.write(nomeJogadorPretas);
        writer.write(System.lineSeparator());
        writer.close();
        return save;
    }

    private JLabel criarLabelErro() {
        JLabel lblErro = new JLabel();
        lblErro.setForeground(Color.RED);
        return lblErro;
    }

    private void carregarJogo(File save) {
        Scanner scan = null;
		try {
			scan = new Scanner(save);
			
			nomeJogadorBrancas = scan.nextLine();
			nomeJogadorPretas = scan.nextLine();
			
			iniciarPartida(save);
			
			qntJogadas = 0;
            corJogadorAtual = Cor.BRANCO;
            int linhaOrigem = 0, linhaDestino = 0;
            char colunaOrigem = 'x', colunaDestino = 'x';
			while (scan.hasNextLine()) {
				String dados = scan.nextLine();
                if (dados.length() == 1) {  // Promoção do peão
                    if (!tabuleiro.realizarPromocao(linhaDestino, colunaDestino, dados.charAt(0), Cor.getCorOponente(corJogadorAtual)))
                        throw new DataFormatException();
                } else {  // Jogada comum
                    
                    linhaOrigem = Character.getNumericValue(dados.charAt(1));
                    colunaOrigem = dados.charAt(0);
                    linhaDestino = Character.getNumericValue(dados.charAt(4));
                    colunaDestino = dados.charAt(3);
                    
                    tabuleiro.escolherPosicaoInicial(linhaOrigem, colunaOrigem, corJogadorAtual);
                    if (!tabuleiro.realizaMovimento(linhaDestino, colunaDestino, corJogadorAtual, false, !scan.hasNextLine())) {
                        throw new DataFormatException();
                    }
                    
                    qntJogadas++;
                    corJogadorAtual = Cor.getCorOponente(corJogadorAtual);
                }
		    }			
			placar.atualizarPlacar(qntJogadas + 1, corJogadorAtual);

	    } catch (StringIndexOutOfBoundsException | DataFormatException e) {
	    	JOptionPane.showMessageDialog(framePrincipal,
            "<html>Os dados do arquivo não são válidos.<br> Talvez o arquivo esteja corrompido ou ele realmente não é um arquivo de save.</html>",
            "Erro ao carregar arquivo de save",
            JOptionPane.ERROR_MESSAGE);
	    } catch (Exception e) {
            JOptionPane.showMessageDialog(framePrincipal,
            "Ocorreu um erro ao carregar o arquivo de save.",
            "Erro ao carregar arquivo de save",
            JOptionPane.ERROR_MESSAGE);
        } finally {
	    	if (scan != null) {
				scan.close();
	    	}
        }
	}

}