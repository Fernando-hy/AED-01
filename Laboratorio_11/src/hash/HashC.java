package hash;

import java.util.ArrayList;

public class HashC<E extends Comparable<E>> {

    // Nodo interno de la estructura de la tabla
    protected class Element {
        int mark; // Estados: 0 = vacío, 1 = ocupado, -1 = borrado (lápida)
        Register<E> reg;

        public Element(int mark, Register<E> reg) {
            this.mark = mark;
            this.reg  = reg;
        }
    }

    protected ArrayList<Element> table;
    protected int m;  // Tamaño de la tabla

    public HashC(int n) {
        // Se ajusta el tamaño al número primo más cercano para reducir colisiones
        this.m     = nextPrime(n);  
        this.table = new ArrayList<Element>(m);
        
        // Se inicializa la tabla insertando elementos vacíos
        for (int i = 0; i < m; i++)
            this.table.add(new Element(0, null));  
    }

    // Calcula el número primo mayor o igual a n
    private int nextPrime(int n) {
        if (n <= 2) return 2;
        int candidate = (n % 2 == 0) ? n + 1 : n;
        while (!isPrime(candidate))
            candidate += 2;
        return candidate;
    }

    // Verifica si un número es primo (optimizado calculando hasta su raíz cuadrada)
    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++)
            if (n % i == 0) return false;
        return true;
    }

    // Función hash principal basada en el método de la división (módulo)
    private int functionHash(int key) {
        return key % m;
    }

    // Algoritmo de prueba lineal para la resolución de colisiones
    private int linearProbing(int dressHash, int key) {
        int posInit  = dressHash;
        int firstDel = -1; // Almacena la posición de la primera lápida encontrada

        do {
            Element item = table.get(dressHash);

            if (item.mark == 0) {
                // Se encontró un espacio vacío. Se prioriza el uso de una lápida previa si existe.
                return (firstDel != -1) ? firstDel : dressHash;

            } else if (item.mark == -1) {
                // Posición eliminada: se registra la ubicación, pero continúa la búsqueda de duplicados.
                if (firstDel == -1) firstDel = dressHash;

            } else if (item.reg.getKey() == key) {
                // La clave ya existe en la estructura. Se retorna la posición para su actualización.
                return dressHash;
            }

            // Avance lineal y circular dentro de los límites de la tabla
            dressHash = (dressHash + 1) % m;

        } while (dressHash != posInit); // Condición de parada: recorrido completo de la tabla

        // Si la tabla se encuentra llena, se retorna la lápida encontrada (o -1 si no hay disponibilidad)
        return firstDel; 
    }

    public void insert(int key, E reg) {
        int index = functionHash(key);
        int pos   = linearProbing(index, key);

        if (pos == -1) {
            System.out.println("  ERROR: Tabla llena. No es posible insertar la clave " + key);
            return;
        }

        // Se inserta o actualiza el registro en la posición designada
        table.get(pos).reg  = new Register<>(key, reg);
        table.get(pos).mark = 1; // Se actualiza el estado a 'ocupado'
        System.out.println("  Insertado " + key + " en pos " + pos + " (hash=" + index + ")");
    }

    public E search(int key) {
        int index = functionHash(key);
        int start = index;

        do {
            Element item = table.get(index);

            if (item.mark == 0) {
                return null;  // Se alcanzó una celda vacía, indicando la ausencia de la clave
                
            } else if (item.mark == 1 && item.reg.getKey() == key) {
                return item.reg.value;  // Búsqueda exitosa
            }
            // Las posiciones con estado -1 (lápidas) son ignoradas para continuar el sondeo

            index = (index + 1) % m;

        } while (index != start);

        return null; // Búsqueda fallida tras recorrer toda la estructura
    }

    // Eliminación lógica del elemento (tombstone)
    public void delete(int key) {
        int index = functionHash(key);
        int start = index;

        do {
            Element item = table.get(index);

            if (item.mark == 0) {
                System.out.println("  Clave " + key + " no encontrada.");
                return;
                
            } else if (item.mark == 1 && item.reg.getKey() == key) {
                item.mark = -1;  // Se marca el registro como borrado preservando la cadena de búsqueda
                System.out.println("  Eliminación lógica de clave " + key + " en pos " + index);
                return;
            }

            index = (index + 1) % m;

        } while (index != start);

        System.out.println("  Clave " + key + " no encontrada.");
    }

    @Override
    public String toString() {
        String s = "D.Real\tD.Hash\tRegister\n";
        int i = 0;
        for (Element item : table) {
            s += (i++) + " -->\t";
            if (item.mark == 1)
                s += functionHash(item.reg.key) + "\t" + item.reg + "\n";
            else
                s += "empty\n";
        }
        return s;
    }
}