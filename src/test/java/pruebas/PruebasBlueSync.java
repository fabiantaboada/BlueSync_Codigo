package pruebas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import modelo.Cliente;
import modelo.Equipo;
import modelo.ServicioConstrucción;
import modelo.ServicioSuministro;

public class PruebasBlueSync {

    private static final double MARGEN_ERROR = 0.001;

    // ---------- ServicioConstrucción ----------

    @Test
    public void probarCostoConstrucción() {
        ServicioConstrucción construcción = new ServicioConstrucción();
        construcción.establecerLargo(10.0);
        construcción.establecerAncho(5.0);
        construcción.establecerCostoBasePorMetroCuadrado(1200.0);

        double resultadoEsperado = 60000.0;
        double resultadoObtenido = construcción.calcularCosto();

        assertEquals(resultadoEsperado, resultadoObtenido, MARGEN_ERROR);
    }

    @Test
    public void probarÁreaConstrucción() {
        ServicioConstrucción construcción = new ServicioConstrucción();
        construcción.establecerLargo(10.0);
        construcción.establecerAncho(5.0);

        assertEquals(50.0, construcción.calcularÁrea(), MARGEN_ERROR);
    }

    // ---------- Cliente ----------

    @Test
    public void probarEnmascaramientoCliente() {
        Cliente cliente = new Cliente("Juan Perez", "987654321", "juan@gmail.com", "Boleta", "12345678");

        String nombreProtegido = cliente.obtenerNombreProtegido();

        assertTrue(nombreProtegido.contains("*"));
        assertFalse(nombreProtegido.contains("Juan"));
    }

    @Test
    public void probarEnmascaramientoNombreCorto() {
        Cliente cliente = new Cliente("Al", "987654321", "al@gmail.com", "Boleta", "12345678");

        assertEquals("***", cliente.obtenerNombreProtegido());
    }

    // ---------- ServicioSuministro ----------

    @Test
    public void probarFiltroSuministrosSinStock() {
        List<Equipo> carrito = new ArrayList<>();

        Equipo equipo1 = new Equipo("Bomba de agua", 500.0, 5);
        Equipo equipo2 = new Equipo("Filtro", 200.0, 0);

        carrito.add(equipo1);
        carrito.add(equipo2);

        ServicioSuministro suministro = new ServicioSuministro();
        suministro.establecerEquipos(carrito);

        double resultadoEsperado = 500.0;
        double resultadoObtenido = suministro.calcularCosto();

        assertEquals(resultadoEsperado, resultadoObtenido, MARGEN_ERROR);
    }

    @Test
    public void probarSuministroSinEquipos() {
        ServicioSuministro suministro = new ServicioSuministro();

        assertEquals(0.0, suministro.calcularCosto(), MARGEN_ERROR);
    }

    // ---------- Equipo ----------

    @Test
    public void probarReducirInventarioNoBajaDeCero() {
        Equipo equipo = new Equipo("Filtro", 200.0, 1);

        equipo.reducirInventario();
        equipo.reducirInventario();

        assertEquals(0, equipo.obtenerCantidadEnAlmacén());
    }
}