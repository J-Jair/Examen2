public class ValorCarta {
    public static int obtener(NombreCarta nombre) {
        switch (nombre) {
            case DOS:
                return 2;
            case TRES:
                return 3;
            case CUATRO:
                return 4;
            case CINCO:
                return 5;
            case SEIS:
                return 6;
            case SIETE:
                return 7;
            case OCHO:
                return 8;
            case NUEVE:
                return 9;
            case AS:
            case DIEZ:
            case JACK:
            case QUEEN:
            case KING:
                return 10;
            default:
                return 0;
        }
    }
}