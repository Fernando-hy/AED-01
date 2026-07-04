package hash;

// Clase de validación orientada a comprobar el mecanismo de eliminación lógica
// y la reutilización de memoria en una Tabla Hash Cerrada (Direccionamiento Abierto).
public class Ejercicio4 {

    public static void main(String[] args) {

        // Se instancia una tabla hash cerrada con una capacidad física de 7.
        HashC<String> tabla = new HashC<>(7);

        // Fase 1: Generación de un clúster primario (agrupamiento).
        // Nota analítica: Todas las claves insertadas a continuación (5, 12, 19, 26) 
        // son congruentes módulo 7 (producen un residuo de 5).
        // Esto fuerza al algoritmo de prueba lineal a buscar posiciones sucesivas.
        tabla.insert(5,  "A");  // h(5) = 5. Ocupa el índice 5.
        tabla.insert(12, "B");  // h(12) = 5. Colisión. Ocupa el índice 6.
        tabla.insert(19, "C");  // h(19) = 5. Colisión. Desbordamiento circular, ocupa el índice 0.
        tabla.insert(26, "D");  // h(26) = 5. Colisión. Ocupa el índice 1.

        // Despliegue del estado de saturación inicial de la tabla.
        System.out.println(tabla);

        // Fase 2: Ejecución de eliminación lógica (Lazy Deletion).
        // Se suprime la clave 12. En memoria, el índice 6 no se vacía (mark = 0),
        // sino que transita a un estado de lápida (mark = -1) para preservar la integridad estructural.
        tabla.delete(12);
        System.out.println(tabla);

        // Fase 3: Validación del algoritmo de sondeo post-eliminación.
        // Se busca la clave 19. El algoritmo iniciará en el índice 5, avanzará al 6 
        // y debe ignorar la lápida (mark = -1) sin detener la iteración, logrando encontrar el dato en el índice 0.
        String r = tabla.search(19);
        System.out.println("Buscar 19: " + (r != null ? r : "no encontrado"));

        // Fase 4: Comprobación de la política de reciclaje de memoria.
        // Se inserta una nueva clave con residuo 5. Al recorrer la secuencia de prueba, 
        // el algoritmo detectará la lápida en el índice 6 y priorizará su reutilización 
        // en lugar de desplazar el elemento hasta el final del clúster.
        tabla.insert(33, "E");
        System.out.println(tabla);
    }
}