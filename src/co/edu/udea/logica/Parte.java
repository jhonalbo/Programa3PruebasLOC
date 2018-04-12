package co.edu.udea.logica;

import java.util.List;

public class Parte {
	String nombre;
	List<Item> items;
	long size;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}	
}
