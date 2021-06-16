// O. Bittel;
// 01.04.2021

package aufgabe3.scotlandYard;

import aufgabe2.DirectedGraph;
import aufgabe3.SYSimulation.sim.SYSimulation;

import java.util.*;

// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {

	SYSimulation sim = null;

	Map<V,Double> dist; 		// Distanz für jeden Knoten
	Map<V,V> pred; 				// Vorgänger für jeden Knoten
	IndexMinPQ<V,Double> cand; 	// Kandidaten als PriorityQueue PQ
	DirectedGraph<V> graph;
	Heuristic<V> h;
	V start;
	V goal;
	double INFINITE_DOUBLE = Double.MAX_VALUE;
	// ...

	/**
	 * Konstruiert ein Objekt, das im Graph g k&uuml;rzeste Wege
	 * nach dem A*-Verfahren berechnen kann.
	 * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewählt, dann ist das Verfahren identisch
	 * mit dem Dijkstra-Verfahren.
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
	 * dem Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
		dist = new HashMap<>();
		pred = new HashMap<>();
		cand = new IndexMinPQ<>();
		this.h = h;
		graph = g;
		// ...
	}

	/**
	 * Diese Methode sollte nur verwendet werden,
	 * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
	 * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
	 * <p>
	 * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
	 * <p><blockquote><pre>
	 *    if (sim != null)
	 *       sim.visitStation((Integer) v, Color.blue);
	 * </pre></blockquote>
	 * @param sim SYSimulation-Objekt.
	 */
	public void setSimulator(SYSimulation sim) {
		this.sim = sim;
	}

	/**
	 * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
	 * <p>
	 * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
	 * der als nächstes aus der Kandidatenliste besucht wird, animiert.
	 * @param s Startknoten
	 * @param g Zielknoten
	 */
	public void searchShortestPath(V s, V g) {
		// ...
		shortestPath(s, g, graph, dist, pred);
	}

	boolean shortestPath(V s, V z, DirectedGraph<V> g, Map<V, Double> dist, Map<V, V> pred) {
		cand.clear();
		LinkedList<V> kandlist = new LinkedList<>();
		start = s;
		goal = z;
		for (var v : g.getVertexSet()) {
			dist.put(v, INFINITE_DOUBLE);
			pred.put(v, null);
		}

		dist.put(s, 0.0);
		kandlist.add(s);

		while (!kandlist.isEmpty()) {
			V minV = s;
			double minDist = INFINITE_DOUBLE;

			for (var v: kandlist) {
				if (h == null) {
					if (dist.get(v) < minDist) {
						minDist = dist.get(v);
						minV = v;
					}

				} else {
					if ((dist.get(v) + h.estimatedCost(v, z)) < minDist) {
						minDist = dist.get(v) + h.estimatedCost(v, z);
						minV = v;
					}
				}
				if (v.equals(z)) {
					cand.add(v,dist.get(v));
					System.out.println("Besuche Knoten " + v + " mit d: " + dist.get(v));
					return true;
				}
			}

			kandlist.remove(minV);
			V v = minV;

			System.out.println("Besuche Knoten " + v + " mit d: " + dist.get(v));

			cand.add(minV, dist.get(minV));

			for (V w : g.getSuccessorVertexSet(v)) {
				if (dist.get(w) == INFINITE_DOUBLE) {
					kandlist.add(w);
				}
				if (dist.get(v) + g.getWeight(v, w) < dist.get(w)) {
					pred.put(w, v);
					dist.put(w, dist.get(v) + g.getWeight(v, w));
				}
			}
		}
		return false;
	}

	/**
	 * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return kürzester Weg als Liste von Knoten.
	 */
	public List<V> getShortestPath() {
		// ...
		try {
			LinkedList<V> shortestPath = new LinkedList<>();
			V z = pred.get(goal);
			shortestPath.add(goal);
			while (z != start) {
				shortestPath.add(z);
				z = pred.get(z);
			}
			shortestPath.add(start);
			Collections.reverse(shortestPath);
			return shortestPath;
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return Länge eines kürzesten Weges.
	 */
	public double getDistance() {
		// ...
		try {
			return dist.get(goal);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

}