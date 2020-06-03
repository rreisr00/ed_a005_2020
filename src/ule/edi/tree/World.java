package ule.edi.tree;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Un mundo es un árbol binario. 
 * En cada nodo de un mundo se almacena una lista de entidades, cada una con su tipo y
 * cardinalidad. Ver {@link Entity}.
 * 
 * Si se codifica "bajar por la izquierda" como "0" y
 * "bajar por la derecha" como "1", el camino desde un 
 * nodo N hasta un nodo M (en uno de sus sub-árboles) será la
 * cadena de 0s y 1s que indica cómo llegar desde N hasta M.
 *
 * Se define también el camino vacío desde un nodo N hasta
 * él mismo, como cadena vacía.
 * 
 * Por ejemplo, el mundo:
 * 
 * {[F(1)], {[F(1)], {[D(2), P(1)], ∅, ∅}, {[C(1)], ∅, ∅}}, ∅}
 * 
 * o lo que es igual:
 * 
 * [F(1)]
 * |  [F(1)]
 * |  |  [D(2), P(1)]
 * |  |  |  ∅
 * |  |  |  ∅
 * |  |  [C(1)]
 * |  |  |  ∅
 * |  |  |  ∅
 * |  ∅
 * 
 * contiene un bosque (forest) en "", otro en "0", dos dragones y una princesa en "00" y
 * un castillo en "01".
 * //@param <T>
 * 
 */
public class World extends AbstractBinaryTreeADT<LinkedList<Entity>> {
	
	/**
	 * Devuelve el mundo al que se llega al avanzar a la izquierda.
	 * 
	 * @return
	 */
	protected World getWorldLeft() {
		
		return (World) leftSubtree;
	}

	protected void setWorldLeft(World left) {
		
		this.leftSubtree = left;
	}
	
	/**
	 * Devuelve el mundo al que se llega al avanzar a la derecha.
	 * 
	 * @return
	 */
	protected World getWorldRight() {
		
		return (World) rightSubtree;
	}

	protected void setWorldRight(World right) {
		
		this.rightSubtree = right;
	}
	
	
	public static World createEmptyWorld() {
		return new World();
	}
	
	/**
	 * Inserta la entidad indicada en este árbol.
	 * 
	 * La inserción se produce en el nodo indicado por la dirección; todos
	 * los nodos recorridos para alcanzar aquél que no estén creados se
	 * inicializarán con una entidad 'unknown'.
	 * 
	 * La dirección se supondrá correcta, compuesta de cero o más 0s y 1s.
	 * 
	 * Dentro de la lista del nodo indicado, la inserción de nuevas entidades
	 * se realizará al final, como último elemento.
	 * 
	 * Por ejemplo, en un árbol vacío se pide insertar un 'dragón' en la
	 * dirección "00". El resultado final será:
	 * 
     * [U(1)]
     * |  [U(1)]
     * |  |  [D(1)]
     * |  |  |  ∅
     * |  |  |  ∅
     * |  |  ∅
     * |  ∅
     * 
     * La dirección "" indica la raíz, de forma que insertar un 'guerrero' en
     * "" en el árbol anterior genera:
     * 
     * [U(1), W(1)]
     * |  [U(1)]
     * |  |  [D(1)]
     * |  |  |  ∅
     * |  |  |  ∅
     * |  |  ∅
     * |  ∅
     * 
     * La inserción tiene en cuenta la cardinalidad, de forma que al volver a
     * insertar un guerrero en "" se tiene:
     * 
     * [U(1), W(2)]
     * |  [U(1)]
     * |  |  [D(1)]
     * |  |  |  ∅
     * |  |  |  ∅
     * |  |  ∅
     * |  ∅
     *  
	 * @param address dirección donde insertar la entidad.
	 * @param e entidad a insertar.
	 */
	public void insert(String address, Entity e) {
		//TODO implementar el metodo
		LinkedList<Entity> entities;
		String nextAddress = "";
		if (this.isEmpty()) {
			entities= new LinkedList<Entity>();
			this.setContent(entities);
			this.setWorldLeft(createEmptyWorld());
			this.setWorldRight(createEmptyWorld());
		}

		if(address.length() >= 2) {
			if(this.getContent().isEmpty()) {
				this.getContent().add(new Entity(0));
			}
			for(int i = 1; i < address.length(); i++){
				nextAddress = nextAddress + address.charAt(i);
			}
		}else if(address.equals("")){
			if(this.getContent().contains(e)){
				int index = 0;
				long count;
				while(this.getContent().get(index).equals(e)){
					index++;
				}
				count = this.getContent().get(index).getCount();
				this.getContent().get(index).setCount(count + e.getCount());
			}else{
				this.getContent().add(e);
			}
		}

		if(address.charAt(0) == '0'){
			this.getWorldLeft().insert(nextAddress, e);
		}else if(address.charAt(0) == '1'){
			this.getWorldRight().insert(nextAddress, e);
		}
	}

	
	
	/**
	 * Indica cuántas entidades del tipo hay en este mundo (en el árbol completo).
	 * 
	 * @param type tipo de entidad.
	 * @return cuántas entidades de ese tipo hay en este árbol.
	 */
	public long countEntity(int type) {
		// TODO Implementar el método	
		LinkedList<Entity> entities = new LinkedList<Entity>();
		Entity thisEntity = new Entity(type);
		long count = 0;
		if(!this.isEmpty()){
			if(this.getContent().contains(thisEntity)){
				int index = 0;
				while(this.getContent().get(index).equals(thisEntity)){
					index++;
				}
				count = this.getContent().get(index).getCount() + this.getWorldLeft().countEntity(type) + this.getWorldRight().countEntity(type);
			}else{
				count += this.getWorldLeft().countEntity(type) + this.getWorldRight().countEntity(type);
			}
		}
		return count;
	}
	
	
	

	/**
	 * Indica cuantas princesas accesibles hay en el árbol.
	 *  Además introduce en una lista de Strings las direcciones a los nodos en las que se encuentran dichas princesas. 
	 *  Se considera princesa accesible :
     *      - a toda princesa que en su camino desde la raíz hasta el nodo que la contiene 
     *      no hay ningún dragón, 
     *      - o si hay algún dragón en el camino desde la raíz hasta la princesa,  se cumpla que 
     *      desde el último dragón hasta la princesa haya al menos un castillo que la proteja 
     *      (podrían estar en el mismo nodo, tanto la princesa, como el castillo y el dragón).
     *      
     *      Ejemplo: Dado el árbol arbol1, siendo
     *       arbol1.toString()={[U(1)], {[U(1)], ∅, {[D(3)], {[P(4)], ∅, ∅}, ∅}},
     *                                  {[U(1)], {[P(7)], ∅, ∅}, {[C(1), D(1)], ∅, {[P(1)], ∅, ∅}}}}
     *     ( se ha insertado:  un dragón y un castillo en “11”, 7 princesas en “10”, un dragón en “01”, 
     *     4 princesas en “010” y 1 princesa en “111”) 
     *     
     *     Al llamar a arbol1.countAccesiblePrincess(lista) siendo lista una lista vacía, 
     *     devolverá 8 y la lista contendrá (“10”, “111”).
	 * @param lista donde dejará las direcciones a los nodos que contienen princesas accesibles.
	 * @return el número de princesas accesibles situadas 
	 */
	public long countAccesiblePrincess(List<String> lista){
		// TODO IMPLEMENTAR EL MÉTODO
		long count[] = {0};
		boolean wasDragon = false;
		String camino = "";
		if(!this.isEmpty()){
			this.countAccesiblePrincessRec(count, lista, camino, wasDragon);
		}
		return count[0];
	}


	private void countAccesiblePrincessRec(long count[], List<String> lista, String camino, boolean WasDragon){
		int index = 0;
		if(!this.isEmpty()){
			if(this.getContent().contains(Entity.dragons(1))) {
				if (this.getContent().contains(Entity.princesses(1))) {
					while(!this.getContent().get(index).equals(Entity.princesses(1))){
						if (this.getContent().get(index).equals(Entity.dragons(1))) {
							WasDragon = true;
						} else if (this.getContent().get(index).equals(Entity.castles(1))) {
							WasDragon = false;
						}
						index++;
					}
					if(!WasDragon){
						lista.add(camino);
						count[0] += this.getContent().get(index).getCount();
					}
				}
			}else{
				if(this.getContent().contains(Entity.princesses(1))){
					while(!this.getContent().get(index).equals(Entity.princesses(1))){
						if (this.getContent().get(index).equals(Entity.castles(1))) {
							WasDragon = false;
						}
						index++;
					}
					if(!WasDragon){
						lista.add(camino);
						count[0] += this.getContent().get(index).getCount();
					}
				}

			}
			for (int i = 0; i < this.getContent().size(); i++) {
				if (this.getContent().get(i).equals(Entity.dragons(1))) {
					WasDragon = true;
				} else if (this.getContent().get(i).equals(Entity.castles(1))) {
					WasDragon = false;
				}
			}

			this.getWorldLeft().countAccesiblePrincessRec(count, lista, camino + "0", WasDragon);
			this.getWorldRight().countAccesiblePrincessRec(count, lista, camino + "1", WasDragon);
		}
	}
	
	
	
	@Override
	public String toString() {
		if (! isEmpty()) {
			//	Construye el resultado de forma eficiente
			StringBuffer result = new StringBuffer();
				
			//	Raíz: Ordena la lista de entidades alfabeticamente
			Collections.sort(content);
			result.append("{" + content.toString());
			
			if (! tags.isEmpty()) {
				result.append(" [");
				
				List<String> sk = new LinkedList<String>(tags.keySet());
				
				Collections.sort(sk);
				for (String k : sk) {
					result.append("(" + k + ", " + tags.get(k) + "), ");
				}
				result.delete(result.length() - 2, result.length());
				result.append("]");
			}
			
			//	Y cada sub-árbol
			for (int i = 0; i < getMaxDegree(); i++) {
				result.append(", " + getSubtree(i).toString());
			}
			//	Cierra la "}" de este árbol
			result.append("}");
			
			return result.toString();
		} else {
			return AbstractTreeADT.EMPTY_TREE_MARK;
		}
	}

	
}
