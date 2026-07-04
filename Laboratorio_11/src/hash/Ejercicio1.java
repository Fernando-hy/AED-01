package hash;

// Clase de carácter demostrativo para ilustrar el funcionamiento base de una función hash
// y el problema fundamental de las colisiones sin manejo explícito.
public class Ejercicio1 {

    // Se define un tamaño de tabla constante de 11 (número primo recomendado para funciones módulo)
    static final int SIZE = 11;
    // Declaración de la estructura física unidimensional que actuará como tabla hash
    static int[] table = new int[SIZE];

    public static void main(String[] args) {

        // Fase de inicialización de la estructura de datos
        // Se emplea el valor centinela '-1' para representar lógicamente una posición vacía
        for (int i = 0; i < SIZE; i++)
            table[i] = -1;

        // Conjunto de claves de prueba. 
        // Nota analítica: Todos los valores (3, 14, 25, 36, 47, 58) poseen un residuo de 3 al dividirse entre 11.
        int[] values = {3, 14, 25, 36, 47, 58};

        // Iteración sobre el arreglo de datos para su respectiva inserción
        for (int v : values) {
            // Se invoca a la función de dispersión para obtener la posición objetivo
            int pos = hash(v);
            
            // Inserción directa en memoria. 
            // Al no existir un algoritmo de resolución de colisiones, cada iteración 
            // sobrescribe el valor residente en la posición 3, perdiéndose el dato anterior.
            table[pos] = v;
            
            // Impresión del proceso de cálculo para trazabilidad en consola
            System.out.println("  h(" + v + ") = " + v + " % 11 = " + pos);
        }

        // Despliegue secuencial del estado final del arreglo en memoria
        for (int i = 0; i < SIZE; i++) {
            // Se evalúa el valor residente contra el centinela definido
            if (table[i] == -1)
                System.out.println("  [" + i + "] -> vacío");
            else
                System.out.println("  [" + i + "] -> " + table[i]);
        }

    }

    // Función de dispersión principal basada en el método de la división (operador módulo)
    static int hash(int x) {
        return x % SIZE;
    }
}