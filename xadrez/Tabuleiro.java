package xadrez;

import java.io.File;
import java.io.FileWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Font;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;
import java.io.IOException;
import enums.Cor;

public class Tabuleiro extends JComponent {

    public static final int QNT_LINHAS = 8;
    public static final int QNT_COLUNAS = 8;
    public static final int LARGURA = 560;
    public static final int ALTURA = 560;
	private File save;
	private boolean emXeque;
	private boolean jaMostrouMsgErroSalvar;
    private boolean posicaoOrigemSelecionada;
	private boolean esperandoPromocao;
	private boolean partidaFinalizada;
    private int linhaOrigem;
    private char colunaOrigem;
	private JLabel imgTabuleiro;
	private JLabel msgFinal;
    private Posicao[][] matrizPosicoes;
	private boolean peaoAcabouDeMovimentarDuasCasasParaFrente;
	private int linhaPeaoQueMovimentouDuasCasas;
	private char colunaQuePeaoMovimentouDuasCasas;
	private JFrame framePrincipal;

    public Tabuleiro(File save, JFrame framePrincipal) {
		this.framePrincipal = framePrincipal;
		this.save = save;
		peaoAcabouDeMovimentarDuasCasasParaFrente = false;
		esperandoPromocao = false;
        posicaoOrigemSelecionada = false;
		partidaFinalizada = false;
		emXeque = false;
		jaMostrouMsgErroSalvar = false;

		inicializarMsgFinal();
        setBounds(0, 0, LARGURA, ALTURA);
        setSize(LARGURA, ALTURA);
        inicializarPosicoes();
		desenharTabuleiro();
		repaint();
    }

	private void finalizarPartida() {
		partidaFinalizada = true;
	}

	public boolean isPartidaFinalizada() {
		// Partida estará finalizada quando ocorrer empate ou xeque-mate
		return partidaFinalizada;
	}

    public void escolherPosicaoInicial(int linha, char coluna, Cor corJogador) {
		// Marca a posição inicial na qual será feito o movimento.
		// A posição marcada precisa conter uma peça da mesma cor do jogador.
        if (esperandoPromocao || posicaoOrigemSelecionada || partidaFinalizada || !ehPosicaoDentroDoTabuleiro(linha, coluna)) {
            return;
        }
		if (!matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].isVazia() && matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].getPeca().getCor() == corJogador) {
			posicaoOrigemSelecionada = true;
			linhaOrigem = linha;
			colunaOrigem = coluna;

			matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].mostrarMarcacaoAzul();
			marcarPosicoesAlcancaveis(linhaOrigem, colunaOrigem, corJogador);
			repaint();
        }
    }

	public boolean isPosicaoOrigemSelecionada() {  // retorna true se ja tiver uma posicao inicial escolhida
		return posicaoOrigemSelecionada;
	}

    private int linhaParaIndex(int linha) {  
		// Transforma um valor de linha de xadrez (8 a 1) em index da matriz de posições.
		// Ex: matrizPosicoes[0] se refere a linha 8.
        return 8 - linha;
    }

    private int colunaParaIndex(char coluna) {
		// Transforma um valor da coluna de xadrez ('A' a 'H') em index da matriz de posições.
		// Ex: matrizPosicoes[x][0] se refere a linha x e coluna 'A'
        return coluna - 'A';
    }

    private void posicionarPosicao(Posicao posicao, int indiceY, int indiceX) {  // Posiciona a posição do xadrez na tela.
        posicao.setBounds(indiceX * Posicao.LARGURA, indiceY * Posicao.LARGURA, Posicao.LARGURA, Posicao.ALTURA);
    }

	private void marcarPosicoesAlcancaveis(int linhaOrigem, char colunaOrigem, Cor corJogador) {
		// Marca todas as posições alcançaveis a partir de uma posição de origem.
		for (int linhaDestino = 1; linhaDestino <= 8; linhaDestino++) {
			for (char colunaDestino = 'A'; colunaDestino <= 'H'; colunaDestino++) {
				if (checaMovimento(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino) || checaMovimentoEspecial(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino, corJogador))
					matrizPosicoes[linhaParaIndex(linhaDestino)][colunaParaIndex(colunaDestino)].mostrarMarcacaoVermelha();
			}
		}
	}

    private void inicializarPosicoes() {
		// Inicializa todas as posições do xadrez no seu devido lugar na tela e coloca as peças iniciais do xadrez.
		matrizPosicoes = new Posicao[8][8];
        for (int i = 0; i < QNT_LINHAS; i++) {
            for (int j = 0; j < QNT_COLUNAS; j++) {
                matrizPosicoes[i][j] = new Posicao(framePrincipal);
                posicionarPosicao(matrizPosicoes[i][j], i, j);
				add(matrizPosicoes[i][j]);
            }
        }

        for (int j = 0; j < QNT_COLUNAS; j++) {
            matrizPosicoes[1][j].adicionarPeca(new Peao(Cor.PRETO, framePrincipal));
            matrizPosicoes[6][j].adicionarPeca(new Peao(Cor.BRANCO, framePrincipal));
        }

        matrizPosicoes[0][0].adicionarPeca(new Torre(Cor.PRETO, framePrincipal));
        matrizPosicoes[0][1].adicionarPeca(new Cavalo(Cor.PRETO, framePrincipal));
        matrizPosicoes[0][2].adicionarPeca(new Bispo(Cor.PRETO, framePrincipal));
		matrizPosicoes[0][3].adicionarPeca(new Dama(Cor.PRETO, framePrincipal));
        matrizPosicoes[0][4].adicionarPeca(new Rei(Cor.PRETO, framePrincipal));
        matrizPosicoes[0][5].adicionarPeca(new Bispo(Cor.PRETO, framePrincipal));
        matrizPosicoes[0][6].adicionarPeca(new Cavalo(Cor.PRETO, framePrincipal));
        matrizPosicoes[0][7].adicionarPeca(new Torre(Cor.PRETO, framePrincipal));

        matrizPosicoes[7][0].adicionarPeca(new Torre(Cor.BRANCO, framePrincipal));
        matrizPosicoes[7][1].adicionarPeca(new Cavalo(Cor.BRANCO, framePrincipal));
        matrizPosicoes[7][2].adicionarPeca(new Bispo(Cor.BRANCO, framePrincipal));
		matrizPosicoes[7][3].adicionarPeca(new Dama(Cor.BRANCO, framePrincipal));
        matrizPosicoes[7][4].adicionarPeca(new Rei(Cor.BRANCO, framePrincipal));
        matrizPosicoes[7][5].adicionarPeca(new Bispo(Cor.BRANCO, framePrincipal));
        matrizPosicoes[7][6].adicionarPeca(new Cavalo(Cor.BRANCO, framePrincipal));
        matrizPosicoes[7][7].adicionarPeca(new Torre(Cor.BRANCO, framePrincipal));
    }

	private void inicializarMsgFinal() {
		int larguraMsg = 400;
        int alturaMsg = 30;

        Font font = new Font("sans-serif", Font.PLAIN, 25);
		msgFinal = new JLabel();
		msgFinal.setBounds(Tabuleiro.LARGURA / 2 - larguraMsg / 2, Tabuleiro.ALTURA / 2 - alturaMsg / 2, larguraMsg, alturaMsg);
		msgFinal.setFont(font);
	}

	private void mostrarMsgFinal(String msg) {
		msgFinal.setText(msg);
		add(msgFinal, 0);
	}

    private void limparMarcacoes() {
		// Apaga todas as marcações criadas no tabuleiro
        for (int i = 0; i < QNT_LINHAS; i++) 
            for (int j = 0; j < QNT_COLUNAS; j++) 
                matrizPosicoes[i][j].apagarMarcacao();
    }

    private boolean ehPosicaoDentroDoTabuleiro(int linha, char coluna) {
        return (linha >= 1 && linha <= 8) && (coluna >= 'A' && coluna <= 'H');
    }

    private void desenharTabuleiro() {  // Desenha o tabuleiro de xadrez.
        try {
            File file = new File("sprites/tabuleiro.png");
			imgTabuleiro = new JLabel(new ImageIcon(ImageIO.read(file)));
			imgTabuleiro.setBounds(0, 0, LARGURA, ALTURA);
            add(imgTabuleiro);
        } catch(IOException ex) {  
            JOptionPane.showMessageDialog(framePrincipal,
            "<html>Não foi possível carregar a imagem do tabuleiro em sprites/tabuleiro.png.<br>Verifique se todas dependências estão presentes</html>",
            "Erro ao carregar imagem do tabuleiro.",
            JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

	private boolean checaMovimentoEspecial(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino, Cor corJogador) {  // Verifica se um movimento especial pode acontecer (roque e captura em passant)
		return verificaRoque(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino, corJogador) || verificaCapturaEnPassant(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino, corJogador);
	}

    private boolean checaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
		// Verifica se o movimento é válido da posição origem até a posição de destino.
		// OBS: não é verificado movimentos especiais nesse método
		if(checaMovimentoPosicaoLivre(linhaOrigem, colunaOrigem)) {
			return false;
		}
		
		if(!(matrizPosicoes[linhaParaIndex(linhaOrigem)][colunaParaIndex(colunaOrigem)].getPeca().checaMovimento(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino))) {
			return false;
		}
		
		if (!checaMovimentoCaminhoLivre(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino)) {
			return false;
		}
		
		if(checaMovimentoPosicaoLivre(linhaDestino, colunaDestino)) {
			if(checaMovimentoPeaoDiagonal(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino)) {
				return false;
			}
			return true;
		}
		
		if(checaPosicaoDestinoCorDiferente(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino)) {
			if (checaMovimentoPeaoFrente(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino)) {
				return false;
			}
			return true;
		} else {
			return false;
		}
		
	}
	
	private boolean checaMovimentoPosicaoLivre(int linha, char coluna) {  // Verifica se a posição não possui peça
		return matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].isVazia();
	}
	
	private boolean checaMovimentoCaminhoLivre(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
		// Verifica se o caminho até o destino está livre (não possui peças no caminho)
		int indexLinhaOrigem = linhaParaIndex(linhaOrigem);
		int indexLinhaDestino = linhaParaIndex(linhaDestino);
		int indexColunaOrigem = colunaParaIndex(colunaOrigem);
		int indexColunaDestino = colunaParaIndex(colunaDestino);
		
		if ((linhaOrigem == linhaDestino) && (colunaOrigem < colunaDestino)) {	//Direita Reto
			for (int j = indexColunaOrigem + 1; j < indexColunaDestino; j++ ) {
				if (!matrizPosicoes[indexLinhaOrigem][j].isVazia()) {
					return false;
				}
			}	
		} else if ((linhaOrigem == linhaDestino) && (colunaOrigem > colunaDestino)) {	//Esquerda Reto
			for (int j = indexColunaOrigem - 1; j > indexColunaDestino; j-- ) {
				if (!matrizPosicoes[indexLinhaOrigem][j].isVazia()) {
					return false;
				}
			}	
		} else if ((linhaOrigem < linhaDestino) && (colunaOrigem == colunaDestino)) {	//Cima Reto
			for (int i = indexLinhaOrigem - 1; i > indexLinhaDestino; i-- ) {
				if (!matrizPosicoes[i][indexColunaOrigem].isVazia()) {
					return false;
				}
			}	
		} else if ((linhaOrigem > linhaDestino) && (colunaOrigem == colunaDestino)) {	//Baixo Reto
			for (int i = indexLinhaOrigem + 1; i < indexLinhaDestino; i++ ) {
				if (!matrizPosicoes[i][indexColunaOrigem].isVazia()) {
					return false;
				}
			}	
		} else if ((colunaDestino > colunaOrigem) && (-(linhaDestino - linhaOrigem) == (colunaDestino - colunaOrigem))) {	//Direita Baixo
			for (int k = 1; k < (indexColunaDestino - indexColunaOrigem); k++ ) {
				if (!matrizPosicoes[indexLinhaOrigem+k][indexColunaOrigem+k].isVazia()) {
					return false;
				}
			}	
		} else if ((colunaDestino > colunaOrigem) && ((linhaDestino - linhaOrigem) == (colunaDestino - colunaOrigem))) {	//Direita Cima
			for (int k = 1; k < (indexColunaDestino - indexColunaOrigem); k++ ) {
				if (!matrizPosicoes[indexLinhaOrigem-k][indexColunaOrigem+k].isVazia()) {
					return false;
				}
			}	
		} else if ((colunaDestino < colunaOrigem) && ((linhaDestino - linhaOrigem) == (colunaDestino - colunaOrigem))) {	//Esquerda Baixo
			for (int k = 1; k < indexColunaOrigem - indexColunaDestino; k++ ) {
				if (!matrizPosicoes[indexLinhaOrigem+k][indexColunaOrigem-k].isVazia()) {
					return false;
				}
			}	
		} else if ((colunaDestino < colunaOrigem) && (-(linhaDestino - linhaOrigem) == (colunaDestino - colunaOrigem))){	//Esquerda Cima
			for (int k = 1; k < indexColunaOrigem - indexColunaDestino; k++ ) {
				if (!matrizPosicoes[indexLinhaOrigem-k][indexColunaOrigem-k].isVazia()) {
					return false;
				}
			}	
		}
		return true;
	}
	
	private boolean checaPosicaoDestinoCorDiferente(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {  // Verifica se a peça da origem é de cor diferente da de destino
		return matrizPosicoes[linhaParaIndex(linhaOrigem)][colunaParaIndex(colunaOrigem)].getPeca().getCor() != 
			   matrizPosicoes[linhaParaIndex(linhaDestino)][colunaParaIndex(colunaDestino)].getPeca().getCor();	
	}
	
	private boolean checaMovimentoPeaoDiagonal(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
		// Verifica se o peao pode ir para diagonal (precisa comer a peça)
		if (matrizPosicoes[linhaParaIndex(linhaOrigem)][colunaParaIndex(colunaOrigem)].getPeca() instanceof Peao) {
			if (Math.abs(linhaOrigem - linhaDestino) == 1
					&& (Math.abs(colunaOrigem - colunaDestino) == 1)) {
				return true;
			}
		}
		return false;		
	}
	
	private boolean checaMovimentoPeaoFrente(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
		// Verifica se o peao pode ir para frente (não pode comer para frente)
		if ((matrizPosicoes[linhaParaIndex(linhaOrigem)][colunaParaIndex(colunaOrigem)].getPeca() instanceof Peao) && (colunaOrigem - colunaDestino == 0)) {
			return true;
		}
		return false;
		
	}

	private boolean verificarPosicaoControlada(int linhaPosicao, char colunaPosicao, Cor corJogador) {
		// Verifica se a posição passada está sendo controlada (o inimigo consegue chegar nela na próxima jogada).
		for (int linha = 1; linha <= 8; linha++) {
			for (char coluna = 'A'; coluna <= 'H'; coluna++) {
				
				if (!matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].isVazia() &&
					matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].getPeca().getCor() != corJogador &&
					checaMovimento(linha, coluna, linhaPosicao, colunaPosicao)) {
					
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean verificaXeque(Cor corJogador) {
		// Verifica se o jogador com a cor passada como parâmetro está em xeque.
        int linhaRei = -1;
        char colunaRei = 'x';
		for (int linha = 1; linha <= 8; linha++) {
			for (char coluna = 'A'; coluna <= 'H'; coluna++) {
				if (!matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].isVazia() &&
                    matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].getPeca() instanceof Rei && corJogador == matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].getPeca().getCor()) {
					linhaRei = linha;
                    colunaRei = coluna;
					break;
				}
			}
			if (linhaRei != -1) {
				break;
			}
		}
		
		return verificarPosicaoControlada(linhaRei, colunaRei, corJogador);
	}
	
	public boolean verificaXequeMate(Cor corJogador) {
		// Verifica se o jogador com a cor passada como parâmetro está em xeque-mate.
		if (verificaXeque(corJogador)) {
			for (int linha = 1; linha <= 8; linha++) {
				for (char coluna = 'A'; coluna <= 'H'; coluna++) {
					if (!matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].isVazia() && matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].getPeca().getCor() == corJogador) {
						if (impedeXeque(linha, coluna,corJogador)) {
							return false;
						}
					}
				}
			}
			return true;
			
		}
		
		return false;
		
	}
	
	private boolean impedeXeque(int linhaOrigem, char colunaOrigem, Cor corJogador) {
		// Verifica se a posição (que possui uma peça) consegue impedir o xeque
		Peca pecaOrigem = matrizPosicoes[linhaParaIndex(linhaOrigem)][colunaParaIndex(colunaOrigem)].getPeca();
		
		for (int linhaDestino = 1; linhaDestino <= 8; linhaDestino++) {
			for (char colunaDestino = 'A'; colunaDestino <= 'H'; colunaDestino++) {
				if (checaMovimento(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino)) {
					Peca pecaDestino = matrizPosicoes[linhaParaIndex(linhaDestino)][colunaParaIndex(colunaDestino)].getPeca();
					
                    matrizPosicoes[linhaParaIndex(linhaOrigem)][colunaParaIndex(colunaOrigem)].removerPeca();
                    matrizPosicoes[linhaParaIndex(linhaDestino)][colunaParaIndex(colunaDestino)].adicionarPeca(pecaOrigem);
					
					if (!verificaXeque(corJogador)) {
						voltaMovimento(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino, pecaOrigem, pecaDestino);
						return true;
					}
					
					voltaMovimento(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino, pecaOrigem, pecaDestino);					
				}
			}
		}
		return false;
	}
	
	private void voltaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino, Peca pecaOrigem, Peca pecaDestino) {
		// Volta o movimento, colocando a peça de origem na posição de origem e a peça que estava no destino (se tiver), na posição de destino novamente
        matrizPosicoes[linhaParaIndex(linhaOrigem)][colunaParaIndex(colunaOrigem)].adicionarPeca(pecaOrigem);
		
		if (pecaDestino != null) {
			matrizPosicoes[linhaParaIndex(linhaDestino)][colunaParaIndex(colunaDestino)].adicionarPeca(pecaDestino);
		} else {
			matrizPosicoes[linhaParaIndex(linhaDestino)][colunaParaIndex(colunaDestino)].removerPeca();
		}
		
	}

	public boolean realizaMovimento(int linhaDestino, char colunaDestino, Cor corJogador, boolean salvarMovimento, boolean buscarPromocao) {
		// Realiza o movimento caso seja válido
		// Caso salvar movimento seja true, o movimento será salvo no arquivo de save
		// Caso buscarPromocao seja true, se o peão for ser promovido uma tela de promoção será mostrada
        if (!posicaoOrigemSelecionada || esperandoPromocao || partidaFinalizada)
            return false;
		
		if (linhaDestino == linhaOrigem && colunaDestino == colunaOrigem) {
			posicaoOrigemSelecionada = false;
			limparMarcacoes();
			repaint();
			return false;
		}
		
		int indexLinhaOrigem = linhaParaIndex(linhaOrigem);
		int indexLinhaDestino = linhaParaIndex(linhaDestino);
		int indexColunaOrigem = colunaParaIndex(colunaOrigem);
		int indexColunaDestino = colunaParaIndex(colunaDestino);
		
		if(!ehPosicaoDentroDoTabuleiro(linhaOrigem, colunaOrigem) || !ehPosicaoDentroDoTabuleiro(linhaDestino, colunaDestino)) {
			return false;
		}
		
		if(!matrizPosicoes[indexLinhaOrigem][indexColunaOrigem].isVazia() && matrizPosicoes[indexLinhaOrigem][indexColunaOrigem].getPeca().getCor() != corJogador) {
			return false;
		}

		if (verificaRoque(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino, corJogador)) {
			realizaRoque(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino);
			finalizarJogada(corJogador, linhaDestino, colunaDestino, salvarMovimento);
			return true;	
		}

		if (verificaCapturaEnPassant(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino, corJogador)) {
			realizaCapturaEnPassant(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino);
			finalizarJogada(corJogador, linhaDestino, colunaDestino, salvarMovimento);
			return true;
		}
		
		if (!checaMovimento(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino)) {
			return false;
		}
		
		
		Peca pecaOrigem = matrizPosicoes[indexLinhaOrigem][indexColunaOrigem].getPeca();
		Peca pecaDestino = matrizPosicoes[indexLinhaDestino][indexColunaDestino].getPeca();
		
        matrizPosicoes[indexLinhaOrigem][indexColunaOrigem].removerPeca();
		matrizPosicoes[indexLinhaDestino][indexColunaDestino].adicionarPeca(pecaOrigem);
		
		if (verificaXeque(corJogador)) {
			voltaMovimento(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino, pecaOrigem, pecaDestino);
			return false;
		}
		
		if (verificarPromocao(linhaDestino, colunaDestino)) {
			esperandoPromocao = true;
			if (buscarPromocao)
				realizarPromocao(linhaDestino, colunaDestino, corJogador);
		}
		
		peaoAcabouDeMovimentarDuasCasasParaFrente = false;
		if (pecaOrigem instanceof Peao && Math.abs(linhaDestino - linhaOrigem) == 2) {
			peaoAcabouDeMovimentarDuasCasasParaFrente = true;
			linhaPeaoQueMovimentouDuasCasas = linhaDestino;
			colunaQuePeaoMovimentouDuasCasas = colunaDestino;
		}
		pecaOrigem.marcarMovimentacao();
		finalizarJogada(corJogador, linhaDestino, colunaDestino, salvarMovimento);
		return true;	
	}

	private void finalizarJogada(Cor corJogador, int linhaDestino, char colunaDestino, boolean salvarMovimento) {
		// Realiza todos os procedimentos para finalizar uma movimentação.
		Cor corOponente = Cor.getCorOponente(corJogador);
		atualizarXeque(corOponente);
		posicaoOrigemSelecionada = false;
		limparMarcacoes();
		if (salvarMovimento)
			salvarJogada(linhaDestino, colunaDestino);
		if (verificaXequeMate(corOponente)) {
			mostrarMsgFinal(corOponente + " recebeu xeque-mate !");
			finalizarPartida();
		}
		else if (verificarEmpate(corJogador))
			finalizarPartida();
		repaint();
	}

	private boolean verificaRoque(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino, Cor corJogador) {
		// Verifica se o roque pode ocorrer
		Peca rei = matrizPosicoes[linhaParaIndex(linhaOrigem)][colunaParaIndex(colunaOrigem)].getPeca();
		if (emXeque || !(rei instanceof Rei) || rei.jaMovimentou())
			return false;
		
		if (linhaOrigem == 8 && colunaOrigem == 'E' && linhaDestino == 8) {
			if (colunaDestino == 'G') {
				// Roque menor das pretas
				return !matrizPosicoes[linhaParaIndex(8)][colunaParaIndex('H')].isVazia() && matrizPosicoes[linhaParaIndex(8)][colunaParaIndex('H')].getPeca() instanceof Torre && !matrizPosicoes[linhaParaIndex(8)][colunaParaIndex('H')].getPeca().jaMovimentou() && checaMovimentoCaminhoLivre(linhaOrigem, colunaOrigem, 8, 'H') && !verificarPosicaoControlada(8, 'F', corJogador) && !verificarPosicaoControlada(8, 'G', corJogador);
			}
			else if (colunaDestino == 'C') {
				// Roque maior das pretas
				return !matrizPosicoes[linhaParaIndex(8)][colunaParaIndex('A')].isVazia() && matrizPosicoes[linhaParaIndex(8)][colunaParaIndex('A')].getPeca() instanceof Torre && !matrizPosicoes[linhaParaIndex(8)][colunaParaIndex('A')].getPeca().jaMovimentou() && checaMovimentoCaminhoLivre(linhaOrigem, colunaOrigem, 8, 'A') && !verificarPosicaoControlada(8, 'D', corJogador) && !verificarPosicaoControlada(8, 'C', corJogador);
			}
		}
		else if (linhaOrigem == 1 && colunaOrigem == 'E' && linhaDestino == 1) {
			if (colunaDestino == 'G') {
				// Roque menor das brancas
				return !matrizPosicoes[linhaParaIndex(1)][colunaParaIndex('H')].isVazia() && matrizPosicoes[linhaParaIndex(1)][colunaParaIndex('H')].getPeca() instanceof Torre && !matrizPosicoes[linhaParaIndex(1)][colunaParaIndex('H')].getPeca().jaMovimentou() && checaMovimentoCaminhoLivre(linhaOrigem, colunaOrigem, 1, 'H') && !verificarPosicaoControlada(1, 'F', corJogador) && !verificarPosicaoControlada(1, 'G', corJogador);
			} else if (colunaDestino == 'C') {
				// Roque maior das  brancas
				return !matrizPosicoes[linhaParaIndex(1)][colunaParaIndex('A')].isVazia() && matrizPosicoes[linhaParaIndex(1)][colunaParaIndex('A')].getPeca() instanceof Torre && !matrizPosicoes[linhaParaIndex(1)][colunaParaIndex('A')].getPeca().jaMovimentou() && checaMovimentoCaminhoLivre(linhaOrigem, colunaOrigem, 1, 'A') && !verificarPosicaoControlada(1, 'D', corJogador) && !verificarPosicaoControlada(1, 'C', corJogador);
			}
		}
		return false;
	}

	private void realizaRoque(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
		// Realiza o roque (utilizar esse método após verificar se o roque é válido)
		Peca torre;
		Peca rei = matrizPosicoes[linhaParaIndex(linhaOrigem)][colunaParaIndex(colunaOrigem)].getPeca();
		matrizPosicoes[linhaParaIndex(linhaOrigem)][colunaParaIndex(colunaOrigem)].removerPeca();
		matrizPosicoes[linhaParaIndex(linhaDestino)][colunaParaIndex(colunaDestino)].adicionarPeca(rei);
		rei.marcarMovimentacao();

		// Movimentar a torre
		if (linhaOrigem == 8) {
			if (colunaDestino == 'C') {
				// Roque maior das pretas
				torre = matrizPosicoes[linhaParaIndex(8)][colunaParaIndex('A')].getPeca();
				matrizPosicoes[linhaParaIndex(8)][colunaParaIndex('A')].removerPeca();
				matrizPosicoes[linhaParaIndex(8)][colunaParaIndex('D')].adicionarPeca(torre);
			} else {
				// Roque menor das pretas
				torre = matrizPosicoes[linhaParaIndex(8)][colunaParaIndex('H')].getPeca();
				matrizPosicoes[linhaParaIndex(8)][colunaParaIndex('H')].removerPeca();
				matrizPosicoes[linhaParaIndex(8)][colunaParaIndex('F')].adicionarPeca(torre);
			}
		} else {
			if (colunaDestino == 'C') {
				// Roque maior das brancas
				torre = matrizPosicoes[linhaParaIndex(1)][colunaParaIndex('A')].getPeca();
				matrizPosicoes[linhaParaIndex(1)][colunaParaIndex('A')].removerPeca();
				matrizPosicoes[linhaParaIndex(1)][colunaParaIndex('D')].adicionarPeca(torre);
			} else {
				// Roque menor das brancas
				torre = matrizPosicoes[linhaParaIndex(1)][colunaParaIndex('H')].getPeca();
				matrizPosicoes[linhaParaIndex(1)][colunaParaIndex('H')].removerPeca();
				matrizPosicoes[linhaParaIndex(1)][colunaParaIndex('F')].adicionarPeca(torre);
			}
		}
		torre.marcarMovimentacao();
	}

	private boolean verificaCapturaEnPassant(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino, Cor corJogador) {
		// Verifica se a captura en passant é válida
		Peca pecaOrigem = matrizPosicoes[linhaParaIndex(linhaOrigem)][colunaParaIndex(colunaOrigem)].getPeca();
		if (peaoAcabouDeMovimentarDuasCasasParaFrente && pecaOrigem instanceof Peao && linhaOrigem == linhaPeaoQueMovimentouDuasCasas && Math.abs(colunaOrigem - colunaQuePeaoMovimentouDuasCasas) == 1) {
			// Peao que irá se movimentar está do lado do peão inimigo que se moveu duas casas. Só falta verificar se o jogador escolheu o movimento de captura en passant e se ele n causa xeque
			Peca peaoQueMoveuDuasCasas = matrizPosicoes[linhaParaIndex(linhaPeaoQueMovimentouDuasCasas)][colunaParaIndex(colunaQuePeaoMovimentouDuasCasas)].getPeca();
			if (colunaDestino == colunaQuePeaoMovimentouDuasCasas && ((peaoQueMoveuDuasCasas.getCor() == Cor.BRANCO && linhaDestino - linhaOrigem == -1) || (peaoQueMoveuDuasCasas.getCor() == Cor.PRETO && linhaDestino - linhaOrigem == 1))) {
				// Realizar movimento e verificar se causa xeque
				realizaCapturaEnPassant(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino);
				if (verificaXeque(corJogador)) {
					voltaMovimentoCapturaEnPassant(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino, pecaOrigem, peaoQueMoveuDuasCasas);
					return false;
				}
				voltaMovimentoCapturaEnPassant(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino, pecaOrigem, peaoQueMoveuDuasCasas);
				return true;
			}
		}
		return false;
	}

	private void voltaMovimentoCapturaEnPassant(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino, Peca peaoAtaque, Peca peaoComido) {
		// Volta a situação inicial antes de ter sido realizado a captura en passant
		matrizPosicoes[linhaParaIndex(linhaDestino)][colunaParaIndex(colunaDestino)].removerPeca();
		matrizPosicoes[linhaParaIndex(linhaOrigem)][colunaParaIndex(colunaOrigem)].adicionarPeca(peaoAtaque);
		matrizPosicoes[linhaParaIndex(linhaPeaoQueMovimentouDuasCasas)][colunaParaIndex(colunaQuePeaoMovimentouDuasCasas)].adicionarPeca(peaoComido);
	}

	private void realizaCapturaEnPassant(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
		// Realiza a captura en passant 
		Peca peaoAtaque = matrizPosicoes[linhaParaIndex(linhaOrigem)][colunaParaIndex(colunaOrigem)].getPeca();
		matrizPosicoes[linhaParaIndex(linhaOrigem)][colunaParaIndex(colunaOrigem)].removerPeca();
		matrizPosicoes[linhaParaIndex(linhaDestino)][colunaParaIndex(colunaDestino)].adicionarPeca(peaoAtaque);
		matrizPosicoes[linhaParaIndex(linhaPeaoQueMovimentouDuasCasas)][colunaParaIndex(colunaQuePeaoMovimentouDuasCasas)].removerPeca();
	}

	private boolean verificarPromocao(int linhaDestino, char colunaDestino) {
		// Verifica se a promoção pode ser realizada
		return (linhaDestino == 8 || linhaDestino == 1) &&
			   matrizPosicoes[linhaParaIndex(linhaDestino)][colunaParaIndex(colunaDestino)].getPeca() instanceof Peao;
	}

	private void atualizarXeque(Cor corOponente) {  // Atualiza a situação de xeque do xadrez
		emXeque = verificaXeque(corOponente);
	}


	private void realizarPromocao(int linhaDestino, char colunaDestino, Cor corJogador) {
		// Cria botões de promoção para o usuário escolher para qual peça promover o peão
		int espacoSeparacao = 20;
		int larguraTelaPromocao = 250;
		int alturaTelaPromocao = 400;

		JPanel telaPromocao = new JPanel(new GridLayout(5,1,espacoSeparacao, espacoSeparacao));
		telaPromocao.setBorder(new EmptyBorder(espacoSeparacao, espacoSeparacao, espacoSeparacao, espacoSeparacao));
		telaPromocao.setBounds(LARGURA / 2 - larguraTelaPromocao / 2, ALTURA / 2 - alturaTelaPromocao / 2, larguraTelaPromocao, alturaTelaPromocao);

		JLabel tituloPromocao = new JLabel();
		tituloPromocao.setText("<html><body style='text-align:center;'>Escolha uma peça para<br>promover o peão</body></html>");
		tituloPromocao.setHorizontalAlignment(SwingConstants.CENTER);
		telaPromocao.add(tituloPromocao);
		JButton btnCavalo = null, btnTorre = null, btnBispo = null, btnDama = null;
		try {
			btnCavalo = new JButton("Cavalo", new ImageIcon(ImageIO.read(new File("sprites/cavalo-" + corJogador.toString().toLowerCase() + ".png"))));
			btnTorre = new JButton("Torre", new ImageIcon(ImageIO.read(new File("sprites/torre-" + corJogador.toString().toLowerCase() + ".png"))));
			btnBispo = new JButton("Bispo", new ImageIcon(ImageIO.read(new File("sprites/bispo-" + corJogador.toString().toLowerCase() + ".png"))));
			btnDama = new JButton("Dama", new ImageIcon(ImageIO.read(new File("sprites/dama-" + corJogador.toString().toLowerCase() + ".png"))));
		} catch(IOException ex) {
			btnCavalo = new JButton("Cavalo");
			btnTorre = new JButton("Torre");
			btnBispo = new JButton("Bispo");
			btnDama = new JButton("Dama");
		} finally {
			btnCavalo.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mousePressed(java.awt.event.MouseEvent evt) {
					realizarPromocao(linhaDestino, colunaDestino, new Cavalo(corJogador, framePrincipal), telaPromocao);
					salvarPromocao('C');
				}
			});
			telaPromocao.add(btnCavalo);

			btnTorre.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mousePressed(java.awt.event.MouseEvent evt) {
					realizarPromocao(linhaDestino, colunaDestino, new Torre(corJogador, framePrincipal), telaPromocao);
					salvarPromocao('T');
				}
			});
			telaPromocao.add(btnTorre);

			btnBispo.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mousePressed(java.awt.event.MouseEvent evt) {
					realizarPromocao(linhaDestino, colunaDestino, new Bispo(corJogador, framePrincipal), telaPromocao);
					salvarPromocao('B');
				}
			});
			telaPromocao.add(btnBispo);

			btnDama.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mousePressed(java.awt.event.MouseEvent evt) {
					realizarPromocao(linhaDestino, colunaDestino, new Dama(corJogador, framePrincipal), telaPromocao);
					salvarPromocao('D');
				}
			});
			telaPromocao.add(btnDama);
		}
		add(telaPromocao, 0);
		repaint();
	}

	private void realizarPromocao(int linhaDestino, char colunaDestino, Peca pecaASerColocada, JComponent telaPromocao) {
		// Coloca a nova peça da promoção no lugar do peão promovido
		matrizPosicoes[linhaParaIndex(linhaDestino)][colunaParaIndex(colunaDestino)].adicionarPeca(pecaASerColocada);
		remove(telaPromocao);
		esperandoPromocao = false;
		repaint();
	}

	public boolean realizarPromocao(int linha, char coluna, char representacaoPecaPromocao, Cor corJogador) {
		// Realiza a promoção a partir de um caractere representando a peça de promoção.
		if (verificarPromocao(linha, coluna)) {
			esperandoPromocao = false;
			if (representacaoPecaPromocao == 'C') {
				matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].adicionarPeca(new Cavalo(corJogador, framePrincipal));
			} else if (representacaoPecaPromocao == 'T') {
				matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].adicionarPeca(new Torre(corJogador, framePrincipal));
			} else if (representacaoPecaPromocao == 'B') {
				matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].adicionarPeca(new Bispo(corJogador, framePrincipal));
			} else if (representacaoPecaPromocao == 'D') {
				matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].adicionarPeca(new Dama(corJogador, framePrincipal));
			} else {
				esperandoPromocao = true;
				return false;
			}
			
			return true;
		}
		return false;
	}

	private void salvarJogada(int linhaDestino, char colunaDestino) {
		// Salva a jogada no formato: <coluna origem><linha origem>-<coluna destino><linha destino>
		try {
			FileWriter writer = new FileWriter(save, true);
			writer.append("" + colunaOrigem + linhaOrigem + "-" + colunaDestino + linhaDestino);
			writer.append(System.lineSeparator());
			writer.close();	
		} catch (IOException ex) {
			// Mostrar apenas uma vez a mensagem de erro com o dialogo.
			if (jaMostrouMsgErroSalvar) {
				JOptionPane.showMessageDialog(framePrincipal,
				"Erro ao salvar jogada.",
				"Não foi possível salvar o jogo. É possível continuar jogando, porém não será possível carregar esse jogo depois.",
				JOptionPane.WARNING_MESSAGE);
				jaMostrouMsgErroSalvar = true;
			}

		}
	}

	private void salvarPromocao(char caractereRepresentacaoPeca) {
		// Salva o caractere que representa a peça pela qual o peao se transformou na promoção
		// D = dama, C = cavalo, T = torre, B = bispo
		try {
			FileWriter writer = new FileWriter(save, true);
			writer.append(caractereRepresentacaoPeca);
			writer.append(System.lineSeparator());
			writer.close();
		} catch (IOException ex) {
			if (jaMostrouMsgErroSalvar) {
				JOptionPane.showMessageDialog(framePrincipal,
				"Erro ao salvar promoção.",
				"Não foi possível salvar o jogo. É possível continuar jogando, porém não será possível carregar esse jogo depois.",
				JOptionPane.WARNING_MESSAGE);
				jaMostrouMsgErroSalvar = true;
			}
		} 
	}

	private boolean verificarEmpate(Cor corJogador) {
		// Verifica se uma das condições de empate ocorreu
		if (verificarEmpatePorReiAfogado(Cor.getCorOponente(corJogador))) {
			mostrarMsgFinal("Empate por rei afogado");
			return true;
		}
		else if (verificarEmpatePorFaltaDePeca()) {
			mostrarMsgFinal("Empate por falta de material");
			return true;
		}
		return false;
	}

	private boolean verificarEmpatePorReiAfogado(Cor corJogador) {
		// Verifica se o rei do jogador com mesma cor passada no parametro está "afogado".
		// Para estar afogado, o rei não está em xeque, mas todos os movimentos que ele pode fazer, o colocará em xeque.
		if (verificaXeque(corJogador)) {
			for (int linha = 1; linha <= 8; linha++) {
				for (char coluna = 'A'; coluna <= 'H'; coluna++) {
					if (!matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].isVazia() && matrizPosicoes[linhaParaIndex(linha)][colunaParaIndex(coluna)].getPeca().getCor() == corJogador) {
						if (impedeXeque(linha, coluna,corJogador)) {
							return false;
						}
					}
				}
			}
			return true;
			
		}
		
		
		return false;
	}

	private boolean verificarEmpatePorFaltaDePeca() {
		return verificarFaltaDePeca(Cor.BRANCO) && verificarFaltaDePeca(Cor.PRETO);
	}

	private boolean verificarFaltaDePeca(Cor corJogador) {
		// Para que tenha falta de peça, os dois jogadores precisam ter: apenas 1 rei, 1 rei e 1 cavalo ou 1 rei e 1 bispo
		int qntCavalos = 0;
		int qntBispos = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (!matrizPosicoes[i][j].isVazia() && matrizPosicoes[i][j].getPeca().getCor() == corJogador) {
					Peca peca = matrizPosicoes[i][j].getPeca();
					if (peca instanceof Torre || peca instanceof Dama || peca instanceof Peao)
						return false;
					else if (peca instanceof Cavalo)
						qntCavalos++;
					else if (peca instanceof Bispo)
						qntBispos++;
				}
			}
		}
		return !(qntCavalos >= 2 || qntBispos >= 2 || (qntCavalos >= 1 && qntBispos >= 1));
	}
}
