package hash;

// Clase destinada a la validación empírica del comportamiento temporal y estructural
// del gestor de sesiones en caché basado en tablas hash.
public class TestSessionCache {

    // Se declara la propagación de InterruptedException dado el uso de mecanismos
    // de suspensión del hilo (Thread.sleep) para la simulación del paso del tiempo.
    public static void main(String[] args) throws InterruptedException {

        // Instanciación de la estructura principal de la caché de sesiones
        SessionCache cache = new SessionCache();

        // 1. Simulación de autenticaciones con diferentes límites de vigencia temporal (TTL)
        // Se asocian tokens alfanuméricos con identificadores de usuario y roles específicos.
        cache.login("abc123", "Ana",   "admin", 5000);  // Asignación de TTL: 5000 ms (5 segundos)
        cache.login("xyz789", "Luis",  "user",  1000);  // Asignación de TTL: 1000 ms (1 segundo)
        cache.login("tok456", "Maria", "user",  5000);  // Asignación de TTL: 5000 ms (5 segundos)

        // Se suspende el hilo de ejecución principal durante 1.5 segundos (1500 ms).
        // Este retraso inducido garantiza matemáticamente la caducidad del token de "Luis".
        Thread.sleep(1500);
        
        System.out.println("\nValidando tokens:");
        
        // 2. Verificación del estado de los tokens tras el paso del tiempo
        validar(cache, "abc123"); // Se espera confirmación de validez (faltan 3.5s para expirar)
        validar(cache, "xyz789"); // Se espera rechazo por caducidad (expiró hace 0.5s)
        validar(cache, "tok456"); // Se espera confirmación de validez (faltan 3.5s para expirar)

        // 3. Prueba de finalización de sesión controlada (Logout explícito)
        System.out.println("\nLogout:");
        // Se efectúa la remoción física del registro asociado a "Ana" de la memoria
        cache.logout("abc123");

        // 4. Ejecución del proceso de depuración de memoria (Garbage Collection algorítmico)
        System.out.println("\nLimpiando expiradas:");
        // Se invoca la rutina que recorre la tabla para suprimir los nodos caducados 
        // (en este caso, el token remanente de "Luis")
        cache.cleanExpired();
        
        // Auditoría final para cuantificar las conexiones supervivientes
        // Solo la sesión de "Maria" debería permanecer activa y registrada
        System.out.println("  Sesiones activas: " + cache.activeSessions());
    }

    // Subrutina auxiliar para la estandarización del reporte de estado de las consultas
    static void validar(SessionCache cache, String token) {
        // Se delega la evaluación al método validate, el cual retorna el objeto o valor nulo
        Session s = cache.validate(token);
        
        // Discriminación y formateo del resultado en la salida estándar
        if (s != null)
            System.out.println("  Válido: " + s);
        else
            System.out.println("  Inválido/Expirado: " + token);
    }
}