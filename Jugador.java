import java.util.HashSet;
import java.util.Set;
import java.util.Random;
import javax.swing.JPanel;

public class Jugador {

    private final int TOTAL_CARTAS = 10;
    private final int MARGEN = 10;
    private final int DISTANCIA = 40;

    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private Random r = new Random();

    public void repartir() {
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.setLayout(null);
        pnl.removeAll();
        int posicion = MARGEN + TOTAL_CARTAS * DISTANCIA;

        for (Carta carta : cartas) {
            posicion -= DISTANCIA;
            carta.mostrar(pnl, posicion, MARGEN);
        }
        pnl.repaint();
    }

    public String getGrupos() {
        String textoGrupos = "";
        Set<Carta> cartasUtilizadas = new HashSet<>();   
        int[] contadores = new int[NombreCarta.values().length];
        for (Carta carta : cartas) {
            int posicion = carta.getNombre().ordinal();
            contadores[posicion]++;
        }

        for (int i = 0; i < contadores.length; i++) {
            if (contadores[i] >= 2) {
                if (textoGrupos.isEmpty()) {
                    textoGrupos = "Se encontraron los siguientes grupos:\n";
                }
                Grupo grupo = Grupo.values()[contadores[i]];
                textoGrupos += grupo + " DE " + NombreCarta.values()[i] + "\n";

                for (Carta carta : cartas) {
                    if (carta.getNombre().ordinal() == i) {
                        cartasUtilizadas.add(carta);
                    }
                }
            }
        }        
        AnalizadorEscaleras analizador = new AnalizadorEscaleras(cartas);
        AnalizadorEscaleras.ResultadoEscaleras resultadoEscaleras = analizador.procesar();
        
        cartasUtilizadas.addAll(resultadoEscaleras.cartasUtilizadas);      
        String textoSobrantes = buscarCartasSobrantes(cartasUtilizadas);
        int puntos = calcularPuntosSobrantes(cartasUtilizadas);

        String resultado = "";
        if (!textoGrupos.isEmpty()) {
            resultado += textoGrupos;
        }
        if (!resultadoEscaleras.texto.isEmpty()) {
            if (!resultado.isEmpty()) {
                resultado += "\n";
            }
            resultado += resultadoEscaleras.texto;
        }
        if (!textoSobrantes.isEmpty()) {
            if (!resultado.isEmpty()) {
                resultado += "\n";
            }
            resultado += textoSobrantes;
            resultado += "Puntos: " + puntos;
        }

        if (resultado.isEmpty()) {
            resultado = "No se encontraron grupos, escaleras ni cartas sobrantes.\nPuntos: 0";
        }

        return resultado;
    }

    private String buscarCartasSobrantes(Set<Carta> cartasUtilizadas) { 
        String resultado = "";
        for (Carta carta : cartas) {
            if (!cartasUtilizadas.contains(carta)) {
                if (resultado.isEmpty()) {
                    resultado = "Sobran:\n";
                }
                resultado += NombreCartaCorto.convertir(carta.getNombre()) + " DE " + carta.getPinta() + "\n";
            }
        }
        return resultado;
    }

    private int calcularPuntosSobrantes(Set<Carta> cartasUtilizadas) {
        int puntos = 0;
        for (Carta carta : cartas) {
            if (!cartasUtilizadas.contains(carta)) {
                puntos += ValorCarta.obtener(carta.getNombre());
            }
        }
        return puntos;
    }
}