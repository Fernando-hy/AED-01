package hash;

// Clase orientada a la evaluación práctica de dos algoritmos de resolución de colisiones
// mediante direccionamiento abierto: Sondeo Lineal y Sondeo Cuadrático.
public class Ejercicio2 {

    // Se establece una capacidad operativa fija de 7 (número primo) para ambas estructuras
    static final int SIZE = 7;

    public static void main(String[] args) {
        // Conjunto de datos predefinido para la saturación de un mismo índice.
        // Nota analítica: Los valores 10, 17, 24 y 31 comparten el mismo residuo (3) al operar módulo 7.
        int[] values = {10, 17, 24, 31, 4};

        System.out.println("Sondeo Lineal");
        // Declaración e inicialización del arreglo para la prueba de sondeo lineal
        int[] tableLinear = new int[SIZE];
        // Se define el valor centinela '-1' como indicador lógico de espacio disponible
        for (int i = 0; i < SIZE; i++) tableLinear[i] = -1;

        // Proceso iterativo de inserción empleando desplazamiento secuencial
        for (int v : values) {
            int pos = insertLinear(tableLinear, v);
            System.out.println("  h(" + v + ")=" + hash(v) + " -> pos=" + pos);
            // Visualización de la evolución estructural tras cada cálculo de posición
            printTable(tableLinear); 
        }

        System.out.println("Sondeo Cuadratico");
        // Declaración e inicialización del arreglo para la prueba de sondeo cuadrático
        int[] tableQuad = new int[SIZE];
        for (int i = 0; i < SIZE; i++) tableQuad[i] = -1;

        // Proceso iterativo de inserción empleando desplazamiento exponencial
        for (int v : values) {
            int pos = insertQuadratic(tableQuad, v);
            System.out.println("  h(" + v + ")=" + hash(v) + " -> pos=" + pos);
            // Visualización de la dispersión de los datos en memoria
            printTable(tableQuad); 
        }
    }

    // Función de dispersión aritmética fundamental (método del residuo)
    static int hash(int x) {
        return x % SIZE;
    }

    // Algoritmo de Sondeo Lineal (Linear Probing)
    static int insertLinear(int[] table, int value) {
        // Determinación de la dirección de origen (índice base)
        int index = hash(value);
        int i = 0; // Variable contadora de factor de desplazamiento
        
        // Ciclo de evaluación de disponibilidad de celdas.
        // El salto de búsqueda se incrementa en magnitud de 1 (i = 0, 1, 2, 3...)
        while (table[(index + i) % SIZE] != -1) i++;
        
        // Asignación de la dirección física final localizada
        int pos = (index + i) % SIZE;
        // Inserción del dato en la estructura
        table[pos] = value;
        return pos; // Retorno de la coordenada física de alojamiento
    }

    // Algoritmo de Sondeo Cuadrático (Quadratic Probing)
    static int insertQuadratic(int[] table, int value) {
        // Determinación de la dirección de origen (índice base)
        int index = hash(value);
        int i = 0; // Variable contadora de factor de desplazamiento
        
        // Ciclo de evaluación de disponibilidad de celdas.
        // El salto de búsqueda obedece a una función polinomial de segundo grado (i^2 = 0, 1, 4, 9...)
        // Esta estrategia mitiga el fenómeno de agrupamiento primario (primary clustering).
        while (table[(index + i * i) % SIZE] != -1) i++;
        
        // Asignación de la dirección física final localizada tras el ajuste cuadrático
        int pos = (index + i * i) % SIZE;
        // Inserción del dato en la estructura
        table[pos] = value;
        return pos; // Retorno de la coordenada física de alojamiento
    }

    // Subrutina de apoyo visual para el despliegue del mapa de memoria en consola
    static void printTable(int[] table) {
        for (int i = 0; i < SIZE; i++) {
            // Discriminación del contenido del arreglo versus el valor centinela
            if (table[i] == -1)
                System.out.println("    [" + i + "] -> vacío");
            else
                System.out.println("    [" + i + "] -> " + table[i]);
        }
    }
}