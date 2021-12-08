package enums;

public enum Cor {  // Cores das peças/jogadores do xadrez.
    BRANCO, PRETO;


    // Retorna a cor do oponente
    public static Cor getCorOponente(Cor cor) {
        if (cor == BRANCO)
            return PRETO;
        return BRANCO;
    }
}
