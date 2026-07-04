// Se define el paquete 'hash'
package hash;
 
// Declara la clase genérica Register, que maneja valores de tipo 'E'. 
// Implementa Comparable para permitir que los registros se comparen y ordenen entre sí.
public class Register<E> implements Comparable<Register<E>> {
    
    // Declara la clave entera. Al ser 'protected', las clases en el mismo paquete o subclases pueden acceder a ella. 
    // En hashing, esta clave pasará por la función de dispersión para obtener el índice.
    protected int key;     
    
    // Declara el valor asociado a la clave. Al usar el genérico 'E', puedes guardar cualquier tipo de objeto (Strings, Objetos complejos, etc.).
    protected E value;     
 
    // Constructor de la clase que recibe la clave y el valor al momento de instanciar un nuevo registro.
    public Register(int key, E value) {
        // Asigna el parámetro 'key' al atributo propio de la instancia.
        this.key   = key;
        // Asigna el parámetro 'value' al atributo propio de la instancia.
        this.value = value;
    }
 
    // Sobrescribe el método de la interfaz Comparable para definir cómo comparar dos objetos Register.
    @Override
    public int compareTo(Register<E> r) {
        // Resta la clave del registro actual con la del registro 'r'. 
        // Retorna 0 si son iguales, negativo si el actual es menor, o positivo si es mayor.
        return this.key - r.key;
    }
 
    // Sobrescribe el método equals heredado de la clase Object para definir cuándo dos registros son considerados exactamente iguales.
    @Override
    public boolean equals(Object o) {
        // Verifica primero si el objeto 'o' que estamos recibiendo es realmente de tipo Register.
        if (o instanceof Register) {
            // Hace un casting (moldeo) seguro del objeto genérico 'o' a la clase Register<E>.
            Register<E> r = (Register<E>) o;
            // Retorna true si la clave del registro actual es igual a la clave del registro recibido. En hashing, la clave es el identificador único.
            return r.key == this.key;
        }
        // Si el objeto ni siquiera era un Register, automáticamente retorna false.
        return false;
    }
 
    // Método getter para obtener de forma segura el valor de la clave desde fuera de la clase.
    public int getKey() { return this.key; }
 
    // Sobrescribe el método toString para dar formato de texto legible a la instancia cuando decidas imprimir la tabla hash completa.
    @Override
    public String toString() {
        // Concatena la clave, dos puntos y el valor convertido a String. (Ejemplo: "58:DatoEjemplo").
        return this.key + ":" + this.value.toString();
    }
}