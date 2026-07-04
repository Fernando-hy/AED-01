package hash;

// Clase de validación destinada a la ejecución y prueba de la estructura Hash Abierta (Separate Chaining)
public class TestHashO {

    // Punto de entrada principal para la ejecución de las pruebas unitarias
    public static void main(String[] args) {

        // Se instancia la tabla hash abierta definiendo una capacidad inicial de 13 (número primo)
        HashO<String> tabla = new HashO<>(13);

        // Fase de inserción de datos en la estructura
        // Las claves que produzcan colisión (mismo índice base) conformarán una cadena enlazada en dicha posición
        tabla.insert(34, "Ana");      // Índice de alojamiento: 34 % 13 = 8
        tabla.insert(3,  "Luis");     // Índice de alojamiento: 3 % 13 = 3
        tabla.insert(7,  "Rosa");     // Índice de alojamiento: 7 % 13 = 7
        tabla.insert(30, "Pedro");    // Índice de alojamiento: 30 % 13 = 4
        tabla.insert(11, "Carla");    // Índice de alojamiento: 11 % 13 = 11
        
        // Se fuerza una colisión. El registro es anexado al final de la lista ubicada en el índice 8
        tabla.insert(8,  "Jorge");    
        
        // Comprobación de integridad ante datos repetidos: se sobrescribe el dato del nodo coincidente
        tabla.insert(7,  "Rosa_2");   // Duplicado → actualiza
        
        tabla.insert(23, "Miguel");   // Índice de alojamiento: 23 % 13 = 10
        tabla.insert(41, "Diana");    // Índice de alojamiento: 41 % 13 = 2
        
        // Colisión adicional evaluada en el índice 3
        tabla.insert(16, "Carlos");   
        
        // Segunda comprobación de integridad para actualización de registros
        tabla.insert(34, "Ana_2");    // Duplicado → actualiza

        // Se despliega el estado físico de los índices y sus respectivas listas
        tabla.printTable();

        // Pruebas de trazabilidad de los elementos ingresados
        buscar(tabla, 30);  // Búsqueda de clave confirmada
        buscar(tabla, 99);  // Validación de respuesta ante clave nula/inexistente en la estructura

        // Ejecución de la operación de borrado
        // En esta modalidad abierta, la eliminación implica la desvinculación física del nodo en memoria
        tabla.delete(30);

        // Comprobación estructural post-borrado
        tabla.printTable();

        // Se corrobora la efectividad de la eliminación y la persistencia de las claves adyacentes
        buscar(tabla, 30);  // Se espera confirmación de ausencia (registro eliminado)
        buscar(tabla, 23);  // Se verifica el acceso ininterrumpido al resto de los componentes
    }

    // Procedimiento auxiliar encargado de estandarizar la invocación y el reporte de resultados en las consultas
    private static void buscar(HashO<String> tabla, int clave) {
        String resultado = tabla.search(clave);
        if (resultado != null)
            // Impresión del valor tras una localización exitosa
            System.out.println("  Clave " + clave + " → ENCONTRADO: " + resultado);
        else
            // Confirmación de registro no localizado
            System.out.println("  Clave " + clave + " → NO ENCONTRADO");
    }
}