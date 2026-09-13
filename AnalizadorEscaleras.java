import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorEscaleras {

    private final Carta[] cartas;

    public AnalizadorEscaleras(Carta[] cartas) {
        this.cartas = cartas;
    }

    public ResultadoEscaleras procesar() {
        Carta[] ordenadas = cartas.clone();
        Arrays.sort(ordenadas, Comparator.comparingInt(Carta::getIndice));

        List<BloqueSecuencia> bloques = new ArrayList<>();
        int longitudMaxima = 1;       
        int i = 0;
        while (i < ordenadas.length) {
            int inicio = i;
            int fin = i;
            while (fin + 1 < ordenadas.length && 
                   ordenadas[fin + 1].getPinta() == ordenadas[inicio].getPinta() && 
                   ordenadas[fin + 1].getIndice() == ordenadas[fin].getIndice() + 1) {
                fin++;
            }
            int longitud = fin - inicio + 1;
            if (longitud >= 2) {
                bloques.add(new BloqueSecuencia(inicio, fin, longitud));
                if (longitud > longitudMaxima) {
                    longitudMaxima = longitud;
                }
            }
            i = fin + 1;
        }

        if (longitudMaxima < 2) {
            return new ResultadoEscaleras("", new HashSet<>());
        }

        StringBuilder textoResultado = new StringBuilder();
        Set<Carta> cartasEnEscalera = new HashSet<>();

        for (BloqueSecuencia bloque : bloques) {            
            for (int j = bloque.inicio; j <= bloque.fin; j++) {
                cartasEnEscalera.add(ordenadas[j]);
            }
            
            if (bloque.longitud == longitudMaxima) {
                if (textoResultado.length() == 0) {
                    textoResultado.append("Se encontraron las siguientes escaleras:\n");
                }
                Grupo grupo = Grupo.values()[bloque.longitud];
                Pinta pinta = ordenadas[bloque.inicio].getPinta();
                NombreCarta cartaInicial = ordenadas[bloque.inicio].getNombre();
                NombreCarta cartaFinal = ordenadas[bloque.fin].getNombre();
                textoResultado.append(grupo).append(" DE ").append(pinta)
                              .append(" DE ").append(cartaInicial).append(" A ").append(cartaFinal).append("\n");
            }
        }

        return new ResultadoEscaleras(textoResultado.toString(), cartasEnEscalera);
    }
        
    public static class ResultadoEscaleras {
        public String texto;
        public Set<Carta> cartasUtilizadas;

        public ResultadoEscaleras(String texto, Set<Carta> cartasUtilizadas) {
            this.texto = texto;
            this.cartasUtilizadas = cartasUtilizadas;
        }
    }

    private static class BloqueSecuencia {
        int inicio, fin, longitud;
        public BloqueSecuencia(int inicio, int fin, int longitud) {
            this.inicio = inicio;
            this.fin = fin;
            this.longitud = longitud;
        }
    }
}