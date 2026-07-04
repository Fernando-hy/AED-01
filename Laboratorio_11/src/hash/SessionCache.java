package hash;

// Clase estructurada para operar como una memoria caché de sesiones de usuario,
// fundamentada en una Tabla Hash con resolución de colisiones por encadenamiento separado.
public class SessionCache {

    // Capacidad física fija de la tabla (óptimamente un número primo)
    static final int SIZE = 13;
    
    // Arreglo principal de listas enlazadas que alojará los registros de sesión
    public ListLinked<Register<Session>>[] table;

    // Supresión de advertencias del compilador generadas por la instanciación de arreglos genéricos
    @SuppressWarnings("unchecked")
    public SessionCache() {
        table = new ListLinked[SIZE];
        // Inicialización obligatoria de cada índice con una lista enlazada vacía
        // para prevenir errores de referencia nula (NullPointerException) durante la inserción
        for (int i = 0; i < SIZE; i++)
            table[i] = new ListLinked<>();
    }

    // Función de dispersión adaptada para cadenas de texto (Strings)
    private int hash(String token) {
        // Se emplea el algoritmo nativo 'hashCode' de Java, aplicando valor absoluto 
        // y aritmética modular para confinar el resultado a los límites del arreglo.
        return Math.abs(token.hashCode()) % SIZE;
    }

    // Operación de registro y alojamiento en memoria para una nueva sesión autenticada
    public void login(String token, String username, String role, long ttlMs) {
        // Determinación de la dirección física base
        int index    = hash(token);
        
        // Cálculo de la marca de tiempo de expiración (Timestamp actual + Tiempo de vida útil)
        long expires = System.currentTimeMillis() + ttlMs;
        
        // Instanciación del objeto contenedor de la sesión
        Session s    = new Session(token, username, role, expires);
        
        // Inserción en la estructura. Se emplea el hashCode del token como clave numérica del Register,
        // aunque la búsqueda real se efectuará evaluando el String completo para evitar falsos positivos por colisión.
        table[index].insertLast(new Register<>(token.hashCode(), s));
        System.out.println("  Login: " + username + " (" + token + ")");
    }

    // Procedimiento de validación de autenticidad y vigencia de una sesión
    public Session validate(String token) {
        // Cálculo del índice de acceso directo
        int index = hash(token);
        
        // Extracción del nodo cabecera de la lista correspondiente
        ListLinked.Node<Register<Session>> actual = table[index].getFirst();
        
        // Recorrido secuencial sobre la cadena de colisiones
        while (actual != null) {
            Session s = actual.getValue().value;
            // Se efectúa una comparación estricta (equals) del token para garantizar la identidad
            if (s.token.equals(token)) {
                // Se evalúa la marca de tiempo contra el reloj del sistema operativo
                if (System.currentTimeMillis() < s.expiresAt)
                    return s; // Sesión vigente y autorizada
                else
                    return null; // Sesión localizada pero temporalmente caducada
            }
            actual = actual.getNext();
        }
        return null; // Sesión inexistente en la estructura
    }

    // Operación de finalización manual y desvinculación de sesión (Logout)
    public void logout(String token) {
        int index = hash(token);
        ListLinked.Node<Register<Session>> actual = table[index].getFirst();
        
        // Búsqueda secuencial del elemento objetivo
        while (actual != null) {
            if (actual.getValue().value.token.equals(token)) {
                // Se delega la desvinculación física de la memoria a la estructura ListLinked
                table[index].removeNode(actual.getValue());
                System.out.println("  Logout: " + token);
                return; // Finalización temprana tras la supresión exitosa
            }
            actual = actual.getNext();
        }
    }

    // Rutina de mantenimiento (similar a un proceso de Garbage Collection).
    // Su propósito es depurar la tabla eliminando físicamente las sesiones que han superado su TTL.
    public void cleanExpired() {
        long now   = System.currentTimeMillis();
        int removed = 0; // Contador de optimización y métricas
        
        // Iteración exhaustiva sobre la totalidad de los índices de la tabla hash
        for (int i = 0; i < SIZE; i++) {
            ListLinked.Node<Register<Session>> actual = table[i].getFirst();
            
            // Recorrido de cada lista enlazada individual
            while (actual != null) {
                // Se preserva la referencia al siguiente nodo antes de una posible eliminación
                ListLinked.Node<Register<Session>> next = actual.getNext();
                
                // Validación heurística de vigencia temporal
                if (actual.getValue().value.expiresAt < now) {
                    // Desvinculación del nodo expirado
                    table[i].removeNode(actual.getValue());
                    removed++;
                }
                // Avance del puntero iterador
                actual = next;
            }
        }
        System.out.println("  Sesiones eliminadas (limpieza): " + removed);
    }

    // Método de auditoría para la cuantificación de las conexiones concurrentes activas
    public int activeSessions() {
        int count = 0;
        long now  = System.currentTimeMillis();
        
        // Recorrido integral de la estructura contabilizando únicamente elementos vigentes
        for (int i = 0; i < SIZE; i++) {
            ListLinked.Node<Register<Session>> actual = table[i].getFirst();
            while (actual != null) {
                // Se discrimina e ignora cualquier sesión caducada que aún persista en memoria
                if (actual.getValue().value.expiresAt > now) count++;
                actual = actual.getNext();
            }
        }
        return count; // Retorno de la métrica poblacional
    }
}