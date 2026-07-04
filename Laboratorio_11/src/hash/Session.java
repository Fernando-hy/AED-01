package hash;

// Clase destinada a modelar la estructura de datos de una sesión de sistema o usuario.
public class Session {
    
    // Identificador único de la sesión (comúnmente empleado como la clave de búsqueda en la tabla hash)
    String token;
    
    // Credencial de identidad del usuario asociado a la conexión activa
    String username;
    
    // Nivel de privilegios o perfil de autorización asignado al usuario
    String role;
    
    // Marca de tiempo (timestamp en milisegundos) que define el límite de validez temporal de la sesión
    long expiresAt;

    // Método constructor encargado de la inicialización de los atributos base al instanciar el objeto
    public Session(String token, String username, String role, long expiresAt) {
        this.token     = token;
        this.username  = username;
        this.role      = role;
        this.expiresAt = expiresAt;
    }

    // Sobrescritura del método de representación textual para facilitar el volcado 
    // de datos (dumping) y la trazabilidad durante la depuración de la estructura
    @Override
    public String toString() {
        return token + " | " + username + " | " + role;
    }
}