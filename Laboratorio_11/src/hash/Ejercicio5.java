package hash;

// Clase orientada a la demostración práctica del mecanismo de redimensionamiento
// dinámico (Rehashing) en una Tabla Hash Cerrada mediante monitoreo del factor de carga.
public class Ejercicio5 {

    // Constante que define el umbral máximo de saturación tolerado (75%).
    // Sobrepasar este límite degrada severamente el rendimiento del direccionamiento abierto.
    static final double LOAD_FACTOR_LIMIT = 0.75;
    
    // Variables de estado global para la administración de la tabla
    static int m;             // Capacidad física actual de la tabla
    static int[] table;       // Estructura de almacenamiento unidimensional
    static int count = 0;     // Registro del número de celdas ocupadas actualmente

    public static void main(String[] args) {

        // Se inicializa la estructura con una capacidad base de 7
        initTable(7);

        // Conjunto de datos a procesar. 
        // Nota analítica: La inserción del quinto elemento (4) elevará la ocupación
        // a 5/7 (aprox 0.71). La inserción del sexto elemento (11) forzará el factor
        // a 6/7 (0.85), detonando así la condición de redimensionamiento.
        int[] values = {2, 9, 16, 23, 4, 11};

        // Bucle de evaluación e inserción secuencial
        for (int v : values) {
            // Validación preventiva: Se calcula la saturación antes de ejecutar la inserción
            if (loadFactor() > LOAD_FACTOR_LIMIT) {
                rehash(); // Se invoca la rutina de expansión si se supera el 0.75
            }
            // Inserción del elemento en la estructura activa (original o nueva)
            insert(v);
            
            // Traza de ejecución para monitorear el crecimiento del factor de carga
            System.out.println("  Insertado " + v + " | α = " + count + "/" + m + " = " + String.format("%.2f", loadFactor()));
        }

        System.out.println("\nTabla final:");
        printTable();
    }

    // Procedimiento de asignación de memoria y preparación de la estructura
    static void initTable(int size) {
        m     = size;
        table = new int[m];
        // Se establece el centinela '-1' como indicador lógico de celda disponible
        for (int i = 0; i < m; i++) table[i] = -1;
        // Se reinicia el contador poblacional
        count = 0;
    }

    // Función de dispersión principal basada en aritmética modular
    static int hash(int key) {
        // Se emplea el valor absoluto para garantizar índices positivos en memoria
        return Math.abs(key) % m;
    }

    // Algoritmo de inserción estándar empleando sondeo lineal (Linear Probing)
    static void insert(int value) {
        int index = hash(value);
        // Búsqueda de disponibilidad desplazando el índice secuencialmente ante colisiones
        while (table[index] != -1)
            index = (index + 1) % m;
        
        // Alojamiento físico del dato y actualización del estado de ocupación
        table[index] = value;
        count++;
    }

    // Función evaluadora de saturación.
    // Retorna la relación matemática entre las celdas ocupadas y la capacidad total (α = n/m).
    static double loadFactor() {
        return (double) count / m;
    }

    // Subrutina crítica de re-dispersión (Rehashing)
    static void rehash() {
        // Se preserva una referencia temporal al arreglo saturado y a su tamaño
        int[] old = table;
        int oldM  = m;
        
        // Se instancia una nueva estructura con capacidad expandida.
        // Por convención, se selecciona un número primo que aproxima el doble de la capacidad anterior (7 * 2 = 14 -> 17).
        initTable(17);
        
        System.out.println("\n  ** Rehashing: nueva tabla tamaño " + m + " **");
        
        // Proceso de migración de datos:
        // Cada elemento residente en la tabla antigua es sometido a un recálculo
        // de su índice base según el nuevo tamaño (m = 17) para garantizar la dispersión.
        for (int v : old)
            if (v != -1) insert(v); // Se omiten los espacios vacíos ('-1')
            
        System.out.println("  Tabla tras rehashing:");
        printTable();
    }

    // Subrutina auxiliar para el volcado del mapa de memoria en la salida estándar
    static void printTable() {
        for (int i = 0; i < m; i++) {
            if (table[i] == -1)
                System.out.println("  [" + i + "] -> vacío");
            else
                System.out.println("  [" + i + "] -> " + table[i]);
        }
    }
}