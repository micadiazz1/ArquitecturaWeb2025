package org.example.repository.mysql;

import org.example.DAO.ClienteDAO;
import org.example.entity.Cliente;
import org.example.entity.ClienteFactura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlClienteDAO implements ClienteDAO {

    /** Conexión activa a la base de datos MySQL */
    private final Connection conn;

    /**
     * Constructor de clase.
     * @param conn conexión con la base de datos
     */
    public MySqlClienteDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserta una lista de clientes en la base de datos usando batch.
     * @param clientes lista de objetos Cliente a insertar
     */
    @Override
    public void insertAll(List<Cliente> clientes) {
        String sql = "INSERT INTO cliente (idCliente, nombre, email) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Cliente cliente : clientes) {
                statement.setInt(1, cliente.getIdCliente());
                statement.setString(2, cliente.getNombre());
                statement.setString(3, cliente.getEmail());
                statement.addBatch();
            }
            statement.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("Insertados " + clientes.size() + " clientes.");
        } catch (SQLException e) {
            System.err.println("Error al insertar clientes en batch: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
        }
    }

    @Override
    public List<ClienteFactura> getClientsSortedByTotalBilling() {
        // La lista ahora es de ClienteFactura, como corresponde
        List<ClienteFactura> clientesOrdenados = new ArrayList<>();
        String query = "SELECT c.nombre, c.email, COUNT(f.idFactura) as cantidad_facturas " +
                "FROM cliente c " +
                "JOIN factura f ON c.idCliente = f.idCliente " +
                "GROUP BY c.idCliente, c.nombre, c.email " +
                "ORDER BY cantidad_facturas DESC"; // Usamos un alias para mayor claridad

        try (PreparedStatement ps = conn.prepareStatement(query);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                int cantidadFacturas = rs.getInt("cantidad_facturas");

                // Creamos un objeto ClienteFactura con los datos de la consulta
                clientesOrdenados.add(new ClienteFactura(nombre, email, cantidadFacturas));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener clientes ordenados por facturacion: " + e.getMessage());

        }
        return clientesOrdenados;
    }
}