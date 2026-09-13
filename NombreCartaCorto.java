public class NombreCartaCorto {
    public static String convertir(NombreCarta nombre) {

        switch (nombre) {

            case AS:
                return "A";

            case DOS:
                return "2";

            case TRES:
                return "3";

            case CUATRO:
                return "4";

            case CINCO:
                return "5";

            case SEIS:
                return "6";

            case SIETE:
                return "7";

            case OCHO:
                return "8";

            case NUEVE:
                return "9";

            case DIEZ:
                return "10";

            case JACK:
                return "J";

            case QUEEN:
                return "Q";

            case KING:
                return "K";

            default:
                return nombre.toString();
        }
    }
    
}
