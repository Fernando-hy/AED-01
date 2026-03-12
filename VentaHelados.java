import java.util.Scanner;

public class VentaHelados {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Variables de configuración
        String[] sabores = {"Vainilla", "Chocolate", "Fresa", "Lúcuma"};
        double[] precios = {3.50, 4.00, 3.50, 5.00};
        
        System.out.println("--- BIENVENIDO A LA HELADERÍA ---");
        System.out.println("Menú de Sabores:");
        
        for (int i = 0; i < sabores.length; i++) {
            System.out.println((i + 1) + ". " + sabores[i] + " - S/ " + precios[i]);
        }

        System.out.print("\nElija el número del sabor: ");
        int opcion = sc.nextInt();
        
        if (opcion < 1 || opcion > sabores.length) {
            System.out.println("Opción no válida. Reinicie el programa.");
            return;
        }

        System.out.print("¿Cuántos helados de " + sabores[opcion-1] + " desea?: ");
        int cantidad = sc.nextInt();

        double precioUnitario = precios[opcion - 1];
        double total = precioUnitario * cantidad;

        System.out.println("\n--- COMPROBANTE DE VENTA ---");
        System.out.println("Producto: Helado de " + sabores[opcion - 1]);
        System.out.println("Cantidad: " + cantidad);
        System.out.println("Precio Unitario: S/ " + precioUnitario);
        System.out.println("----------------------------");
        System.out.printf("TOTAL A PAGAR: S/ ", total);
        System.out.println("----------------------------");
        System.out.println("¡Gracias por su compra!");
        
        sc.close();
    }
}