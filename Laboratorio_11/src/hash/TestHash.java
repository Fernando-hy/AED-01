package hash;

//34, 3, 7, 30, 11, 8, 7, 23, 41, 16, 34
public class TestHash {

    public static void main(String[] args) {

        // Se asigna el tamaño 13. Al ser un número primo, no requiere ajuste interno.
        HashC<String> tabla = new HashC<>(13);

        // Inserción de datos con direccionamiento directo (sin colisiones)
        tabla.insert(34, "Ana");   // 34%13 = 8
        tabla.insert(3,  "Luis");  // 3%13 = 3
        tabla.insert(7,  "Rosa");  // 7%13 = 7
        tabla.insert(30, "Pedro"); // 30%13 = 4
        tabla.insert(11, "Carla"); // 11%13 = 11
        
        // Inserción de datos forzando resolución de colisiones y actualización de duplicados
        tabla.insert(8,  "Jorge");  // Colisión en el índice 8, desplazamiento al índice 9
        tabla.insert(7,  "Rosa_2"); // Clave duplicada: actualización del valor en el índice 7
        tabla.insert(23, "Miguel"); // 23%13 = 10
        tabla.insert(41, "Diana");  // 41%13 = 2
        tabla.insert(16, "Carlos"); // Colisión en el índice 3, desplazamiento hasta el índice 5
        tabla.insert(34, "Ana_2");  // Clave duplicada: actualización del valor en el índice 8 

        System.out.print(tabla);

        // Verificación de los mecanismos de búsqueda
        buscar(tabla, 23);
        buscar(tabla, 99);  // Clave inexistente

        // Operación de borrado lógico
        tabla.delete(30);

        System.out.print(tabla);

        // Comprobación del estado post-eliminación
        buscar(tabla, 30);  // Búsqueda fallida esperada
        
        // Se valida que la búsqueda continúe correctamente a través de posiciones marcadas como lápidas
        buscar(tabla, 23);  
    }

    // Método auxiliar para la impresión estandarizada de los resultados de búsqueda
    private static void buscar(HashC<String> tabla, int clave) {
        String resultado = tabla.search(clave);
        if (resultado != null)
            System.out.println("  Clave " + clave + " → ENCONTRADO: " + resultado);
        else
            System.out.println("  Clave " + clave + " → NO ENCONTRADO");
    }
}