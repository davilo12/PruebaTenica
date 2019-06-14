package dominio;

import dominio.repositorio.RepositorioProducto;

import java.util.Calendar;
import java.util.Date;

import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

public class Vendedor {

	public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;
	private GarantiaExtendida garantiaExtendida;

	public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
		this.repositorioProducto = repositorioProducto;
		this.repositorioGarantia = repositorioGarantia;

	}

	public void generarGarantia(String codigo, String nombreCliente, Producto producto) {
		int c_vocales = validarVocales(codigo);
		if (c_vocales == 3) {
			throw new UnsupportedOperationException("Este producto no cuenta congarantía extendida");
		} else {
			Producto productoDb = repositorioProducto.obtenerPorCodigo(codigo);
			if (null != productoDb) {
				throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);
			}
			double costoGarantia = 0;
			Date fechaActual = new Date();
			Date fechaGarantia = fechaActual;
			if (producto.getPrecio() > 500000) {
				costoGarantia = producto.getPrecio() * 0.2;
				fechaGarantia = sumarDiasAFecha(fechaActual, (200 - (200 / 7)));
			} else {
				costoGarantia = producto.getPrecio() * 0.1;
				fechaGarantia = sumarDiasAFecha(fechaActual, 100);
			}
			garantiaExtendida = new GarantiaExtendida(producto, fechaActual, fechaGarantia, costoGarantia,
					nombreCliente);
			repositorioGarantia.agregar(garantiaExtendida);
		}
	}

	public static Date sumarDiasAFecha(Date fecha, int dias) {
		if (dias == 0)
			return fecha;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		return calendar.getTime();
	}

	private int validarVocales(String codigo) {
		int cantidad = 0;
		for (int i = 0; i < codigo.length(); i++) {
			switch (codigo.charAt(i)) {
			case 'A' | 'a':
				cantidad++;
				break;
			case 'E' | 'e':
				cantidad++;
				break;
			case 'I' | 'i':
				cantidad++;
				break;
			case 'O' | 'o':
				cantidad++;
				break;
			case 'U' | 'u':
				cantidad++;
				break;
			}
		}
		return cantidad;
	}

	public boolean tieneGarantia(String codigo) {
		boolean respuesta = false;
		Producto producto = repositorioProducto.obtenerPorCodigo(codigo);
		if (null != producto) {
			throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);
		}
		return respuesta;
	}

}
