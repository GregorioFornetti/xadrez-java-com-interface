package xadrez;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.awt.GridLayout;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

public class Gerenciador extends JFrame {

    public static final int LARGURA = 1024;
    public static final int ALTURA = 680;
    private JButton btnVoltar;
    private JComponent currentPage;
    private JComponent menuBtnsContainer;

    public Gerenciador() {
        setSize(LARGURA, ALTURA);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setTitle("Xadrez");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        criarBtnsMenu();
        btnVoltar.setVisible(false);
        repaint();
    }

    public void criarBtnsMenu() {
        int espacamentoVerticalBtns = 50;
        int qntBtns = 3;
        int larguraBtn = 200;
        int alturaBtn = 30;
        int alturaContainer = alturaBtn * qntBtns + espacamentoVerticalBtns * (qntBtns - 1);

        menuBtnsContainer = new JPanel(new GridLayout(qntBtns, 1, 0, espacamentoVerticalBtns));
        menuBtnsContainer.setBounds(LARGURA / 2 - larguraBtn / 2, ALTURA / 2 - alturaContainer, larguraBtn,
                alturaContainer);

        btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(LARGURA / 2 - larguraBtn / 2, ALTURA - 100, larguraBtn, alturaBtn);
        btnVoltar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                voltarParaMenuInicial();
            }
        });
        add(btnVoltar);

        JButton btnComecarJogo = new JButton("Começar novo jogo");
        btnComecarJogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                currentPage = comecarNovoJogo(evt);
                sairDoMenuInicial();
            }
        });
        menuBtnsContainer.add(btnComecarJogo);

        JButton btnCarregarJogo = new JButton("Carregar jogo");
        btnCarregarJogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                currentPage = mostrarMenuDeSaves(evt);
                sairDoMenuInicial();
            }
        });
        menuBtnsContainer.add(btnCarregarJogo);

        JButton btnInstrucoes = new JButton("Instruções");
        btnInstrucoes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                currentPage = mostrarInstrucoes(evt);
                if (currentPage != null)
                    sairDoMenuInicial();
            }
        });
        menuBtnsContainer.add(btnInstrucoes);

        add(menuBtnsContainer);
    }

    public JComponent comecarNovoJogo(java.awt.event.MouseEvent evt) {
        Jogo jogo = new Jogo(this);
        jogo.setBounds(0, 0, Jogo.LARGURA, Jogo.ALTURA);
        add(jogo);
        repaint();
        return jogo;
    }

    public JComponent comecarJogoCarregado(File file) {
        Jogo jogo = new Jogo(file, this);
        jogo.setBounds(0, 0, Jogo.LARGURA, Jogo.ALTURA);
        add(jogo);
        remove(currentPage);
        repaint();
        return jogo;
    }

    public JComponent mostrarInstrucoes(java.awt.event.MouseEvent evt) {
        try {
            File file = new File("sprites/instrucoes.png");
            JLabel instrucoes = new JLabel(new ImageIcon(ImageIO.read(file)));
            instrucoes.setBounds(0, 0, 1024, 600);
            add(instrucoes);
            repaint();
            return instrucoes;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
            "<html>Ocorreu um erro ao carregar a imagem de instruções.<br>Verifique se a dependência sprites/tabuleiro.png está disponível.</html>",
            "Erro ao carregar imagem de instruções.",
            JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public File[] getSaves() {
        File pastaSaves = new File("saves");
        File[] saves = pastaSaves.listFiles();
        Arrays.sort(saves, new Comparator<File>() {
            public int compare(File file1, File file2) {
                return -Long.compare(file1.lastModified(), file2.lastModified());
            }
        });
        return saves;
    }

    public JComponent mostrarMenuDeSaves(java.awt.event.MouseEvent evt) {
        int alturaBtnSave = 80;
        int espacamentoVerticalBtnSave = 20;
        int larguraBtnSave = 900;
        File[] saves = getSaves();
        int alturaTotalPainel = (alturaBtnSave + espacamentoVerticalBtnSave) * saves.length;

        JPanel painelSaves = new JPanel(new GridLayout(saves.length, 1, 0, espacamentoVerticalBtnSave));
        painelSaves.setPreferredSize(new DimensionUIResource(larguraBtnSave, alturaTotalPainel));
        for (File save : saves) {
            BtnSave btnSave = new BtnSave(save);
            btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    JLabel painelCarregamento = criarPainelCarregamento();
                    add(painelCarregamento,0);
                    repaint();
                    new Thread() {
                        public void run() {
                            currentPage = comecarJogoCarregado(save);
                            remove(painelCarregamento);
                            repaint();
                        }
                    }.start();
                }
            });
            if (btnSave.isValid())
                painelSaves.add(btnSave);
        }

        JScrollPane menuSaves = new JScrollPane(painelSaves);
        menuSaves.setBounds(LARGURA / 2 - (larguraBtnSave + 20) / 2, 10, larguraBtnSave + 20,
                Math.min(560, alturaTotalPainel + 10));
        add(menuSaves);
        repaint();
        return menuSaves;
    }

    public void sairDoMenuInicial() {
        menuBtnsContainer.setVisible(false);
        btnVoltar.setVisible(true);
    }

    public void voltarParaMenuInicial() {
        menuBtnsContainer.setVisible(true);
        btnVoltar.setVisible(false);
        remove(currentPage);
        repaint();
    }

    private JLabel criarPainelCarregamento() {
        ImageIcon loadingIcon = new ImageIcon("sprites/ajax-loader-lg.gif");
        JLabel loadingLabel = new JLabel(loadingIcon);
        loadingLabel.setBounds(LARGURA / 2 - 40, ALTURA / 2 - 80, 80, 80);
        loadingLabel.setHorizontalAlignment(JLabel.CENTER);
        loadingIcon.setImageObserver(loadingLabel);
        return loadingLabel;
    }

    public static void main(String[] args) {
        new Gerenciador();
    }
}