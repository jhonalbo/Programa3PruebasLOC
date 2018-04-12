package co.edu.udea.logica;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContarLineas {

	public static void main(String[] args){
		System.out.println("Ingrese la ruta del archivo con los programas");
		Scanner entrada = new Scanner(System.in);
		String path = entrada.nextLine();
		entrada.close();
		generarInforme(path);		
		
	}	
	
	public static void generarInforme(String path){
			try {
				FileReader fr = new FileReader(path);
				BufferedReader bf = new BufferedReader(fr);
				Programa programa = listarPrograma(bf);
				System.out.print("PART NAME");
				System.out.print("\tNUMBER OF ITEMS");
				System.out.print("\t\tPART SIZE");
				System.out.print("\tTOTAL SIZE\n");
				System.out.println("------------------------------------------------------------------");
				for(int j=0; j<programa.getPartes().size(); j++){
					Parte parte = programa.getPartes().get(j);
					System.out.print(parte.getNombre());
					System.out.print("\t"+parte.getItems().size());
					System.out.print("\t\t\t"+parte.getSize()+"\n");
				}
				System.out.println("\t\t\t\t\t\t\t"+programa.getSize());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	}
	
	public static Programa listarPrograma(BufferedReader bf){
		String linea;
		long nLineasTotal = 0;
		long nPartes = 0;
		long nItems = 0;
		Programa programa = new Programa();
		try {
			List<Parte> listaPartes = new ArrayList<Parte>();
			bf.mark(1000000);
			while ((linea = bf.readLine())!=null) {
				linea = linea.trim();				
				if(linea==null){
					linea="";
				}
				if(validar(linea)){
					nLineasTotal++;
				}
				Pattern patternClass = Pattern.compile("(public|private|protected) class ([a-zA-Z0-9{ ]{0,100})");
			    Matcher matchClass = patternClass.matcher(linea);
			    //Cuenta la cantidad de partes y sus elementos y construye los objetos para la parte o clase
			    if(matchClass.matches()) {			    	
			    	nPartes++;
			    	String nombreParte = linea.substring(linea.lastIndexOf("class")+6, linea.length()-2).trim();
			    	Parte parte = new Parte();
			    	long nLineasParte = 1;
			    	parte.setNombre(nombreParte);
			    	List<Item> listaItems = new ArrayList<Item>();
			    	bf.mark(1000000);
			    	while ((linea = bf.readLine())!=null) {
			    		linea = linea.trim();
						if(linea==null){
							linea="";
						}
						if(validar(linea)){
							nLineasParte++;
							nLineasTotal++;
						}
			    		Pattern patternItem = Pattern.compile("(public|static|private|protected) ([a-zA-Z0-9\\s<>()"+Pattern.quote("[]")+"]{1,1000})[{]");
					    Matcher matchItem = patternItem.matcher(linea);
					    //Si lo que sigue no es un item sino otra clase
					    if (patternClass.matcher(linea).matches()){
					    	//Se pone el reader en la linea anterior para que sea leida de nuevo esta linea							    	
					    	bf.reset();
					    	break;
					    }
					    //Cuenta la cantidad de items o metodos y sus elementos y construye los objetos para el item o metodo
					    if(matchItem.matches()) {
					    	nItems++;
					    	Item item = new Item();
					    	item.setNombre(linea);
					    	listaItems.add(item);
					    }						    		
				    	bf.mark(1000000);
			    	}
			    	parte.setItems(listaItems);
			    	parte.setSize(nLineasParte);
			    	listaPartes.add(parte);
			    }
			    programa.setPartes(listaPartes);
			    programa.setSize(nLineasTotal);
			    bf.mark(1000000);
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return programa;
	}
	
	public static boolean validar(String linea){
		if(
				//comentario de una linea
				linea.startsWith("//")||
				//importaciones de librerias o clases
				linea.startsWith("import")||
				//lineas vac�as o con solo espacios
				"".equals(linea)){
			return false;
		}else{
			return true;
		}
	}
}