package hash;

// Declaración de la clase genérica HashO estructurada para soportar encadenamiento separado
public class HashO<E extends Comparable<E>> {

    // Arreglo de listas enlazadas; cada índice almacena una estructura secuencial en lugar de un único elemento
    private ListLinked<Register<E>>[] table;  
    private int m;                            // Capacidad física de la tabla (óptimamente un número primo)

    // Se suprime la advertencia del compilador derivada de la creación de un arreglo de tipos genéricos
    @SuppressWarnings("unchecked")
    public HashO(int n) {
        // Se determina el tamaño operativo asegurando que sea un número primo
        this.m     = nextPrime(n);
        // Instanciación del arreglo de listas enlazadas
        this.table = new ListLinked[m];
        
        // Inicialización obligatoria: se asigna una lista vacía en cada posición del arreglo
        // para prevenir excepciones de referencia nula (NullPointerException) durante las inserciones
        for (int i = 0; i < m; i++)
            this.table[i] = new ListLinked<>();
    }

    // Algoritmo de cálculo del número primo consecutivo más cercano
    private int nextPrime(int n) {
        if (n <= 2) return 2;
        int candidate = (n % 2 == 0) ? n + 1 : n;
        while (!isPrime(candidate))
            candidate += 2;
        return candidate;
    }

    // Verificación matemática de primalidad optimizada mediante raíz cuadrada
    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++)
            if (n % i == 0) return false;
        return true;
    }

    // Función de dispersión principal
    private int functionHash(int key) {
        // Se aplica valor absoluto para prevenir el cálculo de índices negativos en arreglos
        // en caso de que se reciban claves con valores menores a cero
        return Math.abs(key) % m;
    }

    // Operación de inserción de elementos en la estructura de encadenamiento
    public void insert(int key, E value) {
        // Cálculo del índice de dispersión base
        int index = functionHash(key);
        // Construcción del nuevo nodo de registro
        Register<E> newReg = new Register<>(key, value);

        // Se extrae la referencia al primer elemento de la lista alojada en el índice calculado
        ListLinked.Node<Register<E>> actual = table[index].getFirst();
        
        // Fase de validación de duplicidad: se recorre la lista enlazada local
        while (actual != null) {
            // Si la clave ya se encuentra registrada en la cadena
            if (actual.getValue().getKey() == key) {
                // Se actualiza el valor del registro existente, sobrescribiendo el dato anterior
                actual.setValue(newReg);
                System.out.println("  Actualizado " + newReg + " en posición " + index);
                return; // Se finaliza la operación
            }
            actual = actual.getNext();
        }

        // Si se agota el recorrido sin hallar la clave, se confirma que es un elemento nuevo.
        // Se inserta al final de la lista enlazada correspondiente para resolver la colisión.
        table[index].insertLast(newReg);
        System.out.println("  Insertado " + newReg + " en posición " + index + "  (hash=" + index + ")");
    }

    // Operación de búsqueda de un valor mediante su clave asociada
    public E search(int key) {
        // Determinación del índice donde la clave debería estar almacenada
        int index = functionHash(key);

        // Se extrae el nodo inicial de la cadena ubicada en el índice
        ListLinked.Node<Register<E>> actual = table[index].getFirst();
        
        // Recorrido secuencial exclusivo sobre la lista de la posición calculada
        while (actual != null) {
            // Si se detecta coincidencia estricta de clave, se extrae y retorna el valor contenido
            if (actual.getValue().getKey() == key)
                return actual.getValue().value;
            actual = actual.getNext();
        }
        // Retorno nulo indicativo de la ausencia del elemento en la estructura
        return null;
    }

    // Operación de supresión física del registro en la estructura de encadenamiento
    public void delete(int key) {
        // Cálculo del índice de alojamiento de la cadena
        int index = functionHash(key);
        // Se crea un registro temporal ('dummy') configurado únicamente con la clave objetivo.
        // Este objeto servirá como parámetro de comparación para el método removeNode de la lista.
        Register<E> target = new Register<>(key, null);

        // Se delega la búsqueda y desvinculación de la memoria al método interno de ListLinked.
        // La variable 'removed' captura el estado booleano resultante de la operación.
        boolean removed = table[index].removeNode(target);
        
        // Notificación del resultado de la transacción en consola
        if (removed)
            System.out.println("  Eliminado clave " + key + " de posición " + index);
        else
            System.out.println("  Clave " + key + " no encontrada.");
    }

    // Método de diagnóstico para la visualización del estado interno de la tabla de dispersión
    public void printTable() {
        System.out.println("\nD.Real\tContenido");
        // Iteración secuencial sobre el arreglo principal
        for (int i = 0; i < m; i++) {
            System.out.print("[" + i + "]\t");
            // Se valida si el índice contiene una estructura sin nodos
            if (table[i].isEmptyList()) {
                System.out.println("empty");
            } else {
                // Invocación delegada a la subrutina de impresión de la propia lista enlazada
                table[i].print();
            }
        }
    }
}