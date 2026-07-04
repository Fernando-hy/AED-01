package hash;

// Clase de validación destinada a evaluar el comportamiento de una Tabla Hash Abierta
// frente a múltiples colisiones resueltas mediante encadenamiento separado.
public class Ejercicio3 {

    public static void main(String[] args) {

        // Se inicializa la estructura de dispersión abierta definiendo un tamaño base de 7.
        HashO<String> tabla = new HashO<>(7);

        // Bloque 1: Generación de agrupamiento (clustering) en un índice específico.
        // Las siguientes cuatro claves comparten el mismo residuo (3) al operar módulo 7.
        // Esto obliga a la estructura a conformar una lista enlazada de cuatro nodos en la posición 3.
        tabla.insert(10, "Juan");   // h(10) = 10 % 7 = 3 (Cabeza de la lista)
        tabla.insert(17, "Ana");    // h(17) = 17 % 7 = 3 (Colisión -> Segundo nodo)
        tabla.insert(24, "Luis");   // h(24) = 24 % 7 = 3 (Colisión -> Tercer nodo)
        tabla.insert(31, "Rosa");   // h(31) = 31 % 7 = 3 (Colisión -> Cuarto nodo)

        // Bloque 2: Generación de una colisión secundaria en un índice distinto.
        // Ambas claves producen un residuo de 5 al operar módulo 7.
        tabla.insert(5,  "Pedro");  // h(5) = 5 % 7 = 5 (Cabeza de la lista)
        tabla.insert(12, "Carla");  // h(12) = 12 % 7 = 5 (Colisión -> Segundo nodo)

        // Despliegue visual del estado de las listas enlazadas alojadas en el arreglo principal.
        tabla.printTable();

        // Evaluación del rendimiento y precisión del algoritmo de búsqueda.
        // Dado que la clave '24' es el tercer elemento de su cadena, el método debe 
        // recorrer los nodos previos de la lista enlazada en el índice 3 antes de retornar el valor.
        String result = tabla.search(24);
        System.out.println("Buscar 24: " + (result != null ? result : "no encontrado"));

        // Comprobación de la estabilidad estructural ante eliminaciones.
        // Se procede a suprimir una clave ubicada en una posición intermedia ("Ana" en la lista del índice 3).
        // Esto verifica que los enlaces sucesores (el nodo de "Luis") no queden huérfanos.
        tabla.delete(17);
        
        // Impresión final para corroborar la correcta reconstitución de los punteros 
        // tras la desvinculación física del nodo en memoria.
        tabla.printTable();
    }
}