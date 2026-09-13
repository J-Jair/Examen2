import java.util.Arrays;
import java.util.Comparator;
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

            carta.mostrar(
                    pnl,
                    posicion,
                    MARGEN
            );
        }

        pnl.repaint();
    }

    public String getGrupos() {

        String textoGrupos = "";

        /*
         * Contiene las cartas que ya pertenecen
         * a un grupo o a una escalera.
         */
        Set<Carta> cartasUtilizadas = new HashSet<>();


        // ==================================================
        // 1. BUSCAR GRUPOS POR MISMO NOMBRE
        // ==================================================

        int[] contadores =
                new int[NombreCarta.values().length];


        for (Carta carta : cartas) {

            int posicion =
                    carta.getNombre().ordinal();

            contadores[posicion]++;
        }


        for (int i = 0; i < contadores.length; i++) {

            if (contadores[i] >= 2) {

                if (textoGrupos.isEmpty()) {

                    textoGrupos =
                            "Se encontraron los siguientes grupos:\n";
                }

                Grupo grupo =
                        Grupo.values()[contadores[i]];


                textoGrupos +=
                        grupo
                        + " de "
                        + NombreCarta.values()[i]
                        + "\n";


                /*
                 * Las cartas de este mismo nombre
                 * ya están utilizadas.
                 */
                for (Carta carta : cartas) {

                    if (carta.getNombre().ordinal() == i) {

                        cartasUtilizadas.add(carta);
                    }
                }
            }
        }


        // ==================================================
        // 2. BUSCAR ESCALERAS
        // ==================================================

        String textoEscaleras =
                buscarEscalerasMismaPinta(cartasUtilizadas);


        // ==================================================
        // 3. BUSCAR CARTAS SOBRANTES
        // ==================================================

        String textoSobrantes =
                buscarCartasSobrantes(cartasUtilizadas);


        // ==================================================
        // 4. CALCULAR PUNTOS DE LAS CARTAS SOBRANTES
        // ==================================================

        int puntos =
                calcularPuntosSobrantes(cartasUtilizadas);


        // ==================================================
        // 5. ARMAR EL RESULTADO
        // ==================================================

        String resultado = "";


        if (!textoGrupos.isEmpty()) {

            resultado += textoGrupos;
        }


        if (!textoEscaleras.isEmpty()) {

            if (!resultado.isEmpty()) {

                resultado += "\n";
            }

            resultado += textoEscaleras;
        }


        if (!textoSobrantes.isEmpty()) {

            if (!resultado.isEmpty()) {

                resultado += "\n";
            }

            resultado += textoSobrantes;
        }


        // ==================================================
        // MOSTRAR LOS PUNTOS
        // ==================================================

        if (!textoSobrantes.isEmpty()) {

            if (!resultado.isEmpty()) {

                resultado += "\n";
            }

            resultado +=
                    "Puntos: "
                    + puntos;
        }


        if (resultado.isEmpty()) {

            resultado =
                    "No se encontraron grupos, escaleras ni cartas sobrantes.\n"
                    + "Puntos: 0";
        }


        return resultado;
    }


    // ======================================================
    // BUSCAR ESCALERAS DE LA MISMA PINTA
    // ======================================================

    private String buscarEscalerasMismaPinta(
            Set<Carta> cartasUtilizadas) {


        Carta[] ordenadas =
                cartas.clone();


        Arrays.sort(
                ordenadas,
                Comparator.comparingInt(
                        Carta::getIndice
                )
        );


        // ==================================================
        // BUSCAR LA ESCALERA MÁS LARGA
        // ==================================================

        int longitudMaxima = 1;


        for (int i = 0;
             i < ordenadas.length;
             i++) {


            int inicio = i;
            int fin = i;


            while (
                    fin + 1 < ordenadas.length

                    &&

                    ordenadas[fin + 1].getPinta()
                            ==
                    ordenadas[inicio].getPinta()

                    &&

                    ordenadas[fin + 1].getIndice()
                            ==
                    ordenadas[fin].getIndice() + 1
            ) {

                fin++;
            }


            int longitud =
                    fin - inicio + 1;


            if (
                    longitud >= 2
                    &&
                    longitud > longitudMaxima
            ) {

                longitudMaxima =
                        longitud;
            }


            i = fin;
        }


        if (longitudMaxima < 2) {

            return "";
        }


        // ==================================================
        // MARCAR CARTAS QUE PERTENECEN A ESCALERAS
        // ==================================================

        for (int i = 0;
             i < ordenadas.length;
             i++) {


            int inicio = i;
            int fin = i;


            while (
                    fin + 1 < ordenadas.length

                    &&

                    ordenadas[fin + 1].getPinta()
                            ==
                    ordenadas[inicio].getPinta()

                    &&

                    ordenadas[fin + 1].getIndice()
                            ==
                    ordenadas[fin].getIndice() + 1
            ) {

                fin++;
            }


            int longitud =
                    fin - inicio + 1;


            /*
             * Cualquier grupo de 2 o más cartas
             * consecutivas deja de ser sobrante.
             */
            if (longitud >= 2) {

                for (
                        int j = inicio;
                        j <= fin;
                        j++
                ) {

                    cartasUtilizadas.add(
                            ordenadas[j]
                    );
                }
            }


            i = fin;
        }


        // ==================================================
        // MOSTRAR LAS ESCALERAS MÁS LARGAS
        // ==================================================

        String resultado = "";


        for (int i = 0;
             i < ordenadas.length;
             i++) {


            int inicio = i;
            int fin = i;


            while (
                    fin + 1 < ordenadas.length

                    &&

                    ordenadas[fin + 1].getPinta()
                            ==
                    ordenadas[inicio].getPinta()

                    &&

                    ordenadas[fin + 1].getIndice()
                            ==
                    ordenadas[fin].getIndice() + 1
            ) {

                fin++;
            }


            int longitud =
                    fin - inicio + 1;


            if (
                    longitud == longitudMaxima
                    &&
                    longitud >= 2
            ) {


                if (resultado.isEmpty()) {

                    resultado =
                            "Se encontraron las siguientes escaleras:\n";
                }


                Grupo grupo =
                        Grupo.values()[longitud];


                Pinta pinta =
                        ordenadas[inicio].getPinta();


                NombreCarta cartaInicial =
                        ordenadas[inicio].getNombre();


                NombreCarta cartaFinal =
                        ordenadas[fin].getNombre();


                resultado +=
                        grupo
                        + " de "
                        + pinta
                        + " de "
                        + cartaInicial
                        + " a "
                        + cartaFinal
                        + "\n";
            }


            i = fin;
        }


        return resultado;
    }


    // ======================================================
    // BUSCAR CARTAS SOBRANTES
    // ======================================================

    private String buscarCartasSobrantes(
            Set<Carta> cartasUtilizadas) {


        String resultado = "";


        for (Carta carta : cartas) {

            if (!cartasUtilizadas.contains(carta)) {

                if (resultado.isEmpty()) {

                    resultado =
                            "Sobran:\n";
                }


                resultado +=
                        NombreCartaCorto.convertir(
                                carta.getNombre()
                        )
                        + " de "
                        + carta.getPinta()
                        + "\n";
            }
        }


        return resultado;
    }


    // ======================================================
    // CALCULAR PUNTOS DE LAS CARTAS SOBRANTES
    // ======================================================

    private int calcularPuntosSobrantes(
            Set<Carta> cartasUtilizadas) {


        int puntos = 0;


        for (Carta carta : cartas) {

            /*
             * Solo sumamos las cartas que sobran.
             */
            if (!cartasUtilizadas.contains(carta)) {

                puntos +=
                        ValorCarta.obtener(
                                carta.getNombre()
                        );
            }
        }


        return puntos;
    }
}