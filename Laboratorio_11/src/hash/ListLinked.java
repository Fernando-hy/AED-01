package hash;

// Se elimina la restricción 'extends Comparable<T>' para permitir el almacenamiento de cualquier tipo de objeto
public class ListLinked<T> {  
    private Node<T> first;

    public ListLinked() {
        // Se inicializa la lista vacía asignando una referencia nula al primer nodo
        this.first = null;
    }

    public boolean isEmptyList() {
        // Verifica si la lista carece de elementos evaluando el estado de la referencia inicial
        return this.first == null;
    }

    public void insertFirst(T value) {
        // Se instancia un nuevo nodo con la información proporcionada
        Node<T> newNode = new Node<>(value);
        // Si la estructura no está vacía, el nuevo nodo se enlaza al elemento previamente posicionado al inicio
        if (!isEmptyList())
            newNode.setNext(this.first);
        // Se actualiza el puntero principal para que apunte al elemento recién insertado
        this.first = newNode;
    }

    public void insertLast(T value) {
        Node<T> newNode = new Node<>(value);
        if (isEmptyList()) {
            // Ante una lista vacía, el nuevo nodo asume la posición inicial
            this.first = newNode;
        } else {
            // Se ejecuta un recorrido secuencial desde el inicio para localizar el último elemento
            Node<T> actual = this.first;
            while (actual.getNext() != null)
                actual = actual.getNext();
            // Se enlaza el último nodo existente con la nueva instancia
            actual.setNext(newNode);
        }
    }

    public boolean removeNode(T value) {
        // Se interrumpe la operación retornando falso si la estructura carece de elementos
        if (isEmptyList()) return false;

        // Se evalúa la primera posición empleando 'equals' para validar la coincidencia de objetos
        if (this.first.getValue().equals(value)) {  
            // Se descarta el primer nodo desplazando la referencia inicial al segundo elemento
            this.first = this.first.getNext();
            return true;
        }
        
        // Se inicializa un puntero de iteración
        Node<T> actual = this.first;
        // El avance continúa mientras exista un elemento sucesor y su valor no corresponda al parámetro buscado
        while (actual.getNext() != null && !actual.getNext().getValue().equals(value))
            actual = actual.getNext();

        // Si se alcanza el final de la lista sin éxito en la búsqueda, se retorna falso
        if (actual.getNext() == null) return false;
        
        // Se puentea lógicamente el nodo encontrado, desenlazándolo de la estructura
        actual.setNext(actual.getNext().getNext());
        return true;
    }

    public boolean search(T value) {
        if (isEmptyList()) return false;
        Node<T> actual = this.first;
        // Se realiza un recorrido iterativo nodo por nodo
        while (actual != null) {
            // Ante la primera coincidencia exacta, se finaliza la búsqueda retornando verdadero
            if (actual.getValue().equals(value)) return true;
            actual = actual.getNext();
        }
        // Retorno por defecto tras agotar los elementos sin hallar el objetivo
        return false;
    }

    public int size() {
        int count = 0;
        Node<T> actual = this.first;
        // Se incrementa un contador por cada nodo iterado hasta encontrar una referencia nula
        while (actual != null) { 
            count++; 
            actual = actual.getNext(); 
        }
        return count;
    }

    // Extracción de elementos por índice numérico (requerido para recorridos en estructuras como GraphLink)
    public T get(int index) {
        Node<T> actual = this.first;
        // Se efectúa un salto secuencial proporcional a la magnitud del índice especificado
        for (int i = 0; i < index; i++)
            actual = actual.getNext();
        // Se extrae y retorna el contenido del nodo en la posición final alcanzada
        return actual.getValue();
    }
    
    public void removeAt(int index) {
        if (isEmptyList()) return;
        if (index == 0) {
            // Eliminación directa de la cabeza de la lista si el índice es cero
            first = first.getNext();
            return;
        }
        Node<T> actual = first;
        // El recorrido se detiene un nodo antes de la posición de destino para permitir la manipulación de los punteros
        for (int i = 0; i < index - 1; i++)
            actual = actual.getNext();
        // Descarte lógico del nodo objetivo mediante alteración del enlace sucesor
        actual.setNext(actual.getNext().getNext());
    }

    public void print() {
        if (isEmptyList()) {
            System.out.println("La lista no tiene elementos");
        } else {
            Node<T> actual = this.first;
            // Despliegue visual de los elementos contenidos interconectados por indicadores de referencia
            while (actual != null) {
                System.out.print(actual.getValue() + " -> ");
                actual = actual.getNext();
            }
            // Indicador de terminación de la secuencia enlazada
            System.out.println("null");
        }
    }

    // Permite el acceso externo a la referencia principal de la estructura
    public Node<T> getFirst() { return this.first; }

    // Definición encapsulada de la estructura unitaria de datos (Nodo)
    public static class Node<T> {  // Se suprime 'extends Comparable<T>' para homogenizar su comportamiento genérico
        private T value;        // Almacenamiento del dato en bruto
        private Node<T> next;   // Referencia direccional hacia el próximo elemento en memoria

        public Node(T value) {
            this.value = value;
            // Todo nodo es instanciado como un elemento aislado por defecto
            this.next = null;
        }

        // Métodos de encapsulamiento estándar para lectura y escritura de atributos (Getters/Setters)
        public T getValue()               { return value; }
        public void setValue(T value)     { this.value = value; }
        public Node<T> getNext()          { return next; }
        public void setNext(Node<T> next) { this.next = next; }
    }
}