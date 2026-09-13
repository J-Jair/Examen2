public class NombreCartaCorto {
    private static final String[] CORTOS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    public static String convertir(NombreCarta nombre) {
        int index = nombre.ordinal();
        if (index >= 0 && index < CORTOS.length) {
            return CORTOS[index];
        }
        return nombre.toString();
    }
}