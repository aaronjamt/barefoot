/*
 * Copyright (C) 2015, BMW Car IT GmbH
 *
 * Author: Sebastian Mattheis <sebastian.mattheis@bmw-carit.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.bmwcarit.barefoot.topology;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.bmwcarit.barefoot.roadmap.Road;

/**
 * Path of edges in a graph.
 *
 * @param <E>
 *            {@link AbstractEdge} type used in the graph.
 */
public class Path<E extends AbstractEdge<E>> {
    private final Point<E> source;
    private Point<E> target;
    private final LinkedList<E> edges;

    public Path(Point<E> single) {
        this.source = single;
        this.target = single;
        this.edges = new LinkedList<>(Arrays.asList(single.edge()));
        if (!valid()) {
            throw new RuntimeException("unvalid path");
        }
    }

    /**
     * Creates a {@link Path} object.
     *
     * @param source
     *            Start/source {@link Point} of the path.
     * @param target
     *            End/target {@link Point} of the path.
     * @param edges
     *            Sequence of {@link AbstractEdge}s that make the path.
     */
    public Path(Point<E> source, Point<E> target, List<E> edges) {
        this.source = source;
        this.target = target;
        this.edges = new LinkedList<>(edges);
        if (!valid()) {
            throw new RuntimeException("unvalid path");
        }
    }

    /**
     * Gets the sequence of {@link AbstractEdge}s that make the path.
     *
     * @return Sequence of {@link AbstractEdge}s that make the path.
     */
    public List<E> path() {
        return edges;
    }

    /**
     * Gets the start/source {@link Point} of the path.
     *
     * @return Start/source {@link Point} of the path.
     */
    public Point<E> source() {
        return source;
    }

    /**
     * Gets end/target {@link Point} of the path.
     *
     * @return End/target {@link Point} of the path.
     */
    public Point<E> target() {
        return target;
    }

    /**
     * Checks if the path is valid, i.e. if the sequence of {@link AbstractEdge}s is
     * connected and connects source and target {@link Point}.
     *
     * @return True if the path is valid, false otherwise.
     */
    boolean valid() {
        if (edges.getFirst().id() != source.edge().id()) {
            return false;
        }

        if (edges.getLast().id() != target.edge().id()) {
            return false;
        }

        if (source.edge().id() == target.edge().id() && source.fraction() > target.fraction() && edges.size() == 1) {
            return false;
        }

        for (int i = 0; i < edges.size() - 1; i++) {
            Iterator<E> successors = edges.get(i).successors();
            boolean successor = false;

            while (successors.hasNext()) {
                if (successors.next().id() == edges.get(i + 1).id()) {
                    successor = true;
                }
            }

            if (!successor) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if a U-Turn occurred on this route.
     * 
     * @return boolean
     */
    public boolean isUturn() {
        long lastRoadID = ((Road) source.edge()).base().id();
        long lastEdgeID = source.edge().id();

        for (int i = 1; i < edges.size(); ++i) {
            if (((Road) edges.get(i)).base().id() == lastRoadID && edges.get(i).id() != lastEdgeID) {
                return true;
            }
            lastRoadID = ((Road) edges.get(i)).base().id();
            lastEdgeID = edges.get(i).id();
        }
        if (((Road) target.edge()).base().id() == lastRoadID && target.edge().id() != lastEdgeID) {
            return true;
        }

        return false;
    }

    /**
     * Returns if the route includes a tunnel segment.
     * 
     * @return boolean
     */
    public boolean hasTunnel() {
        if (((Road) source.edge()).base().getTunnel()) {
            return true;
        }

        if (((Road) target.edge()).base().getTunnel()) {
            return true;
        }
        for (int i = 1; i < edges.size(); ++i) {
            if (((Road) edges.get(i)).base().getTunnel()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns if the route includes a tunnel segment.
     * 
     * @return boolean
     */
    public double tunnelLength() {
        double length = 0d;
        if (((Road) source.edge()).base().getTunnel()) {
            length += ((Road) source.edge()).length();
        }

        if (((Road) target.edge()).base().getTunnel()) {
            length += ((Road) target.edge()).length();
        }
        for (int i = 1; i < edges.size(); ++i) {
            if (((Road) edges.get(i)).base().getTunnel()) {
                length += ((Road) edges.get(i)).length();
            }
        }

        return length;
    }

    /**
     * Gets cost value of the path for an arbitrary {@link Cost} function.
     *
     * @param cost
     *            {@link Cost} function to be used.
     * @return Cost value of the path.
     */
    public double cost(Cost<E> cost) {
        double value = cost.cost(source.edge(), 1 - source.fraction());
        for (int i = 1; i < edges.size(); ++i) {
            value += cost.cost(edges.get(i));
        }

        value -= cost.cost(target.edge(), 1 - target.fraction());
        return value;
    }

    /**
     * Adds a {@link Path} at the end of this path.
     *
     * @param other
     *            {@link Path} ot be added at the end of this path.
     * @return True if path can be added, i.e. result is a valid path, false
     *         otherwise.
     */
    public boolean add(Path<E> other) {
        if (target.edge().id() != other.source.edge().id() && target.edge().target() != other.source.edge().source()) {
            return false;
        }

        if (target.edge().id() == other.source.edge().id() && target.fraction() != other.source.fraction()) {
            return false;
        }

        if (target.edge().id() != other.source.edge().id()
                && (target.fraction() != 1 || other.source.fraction() != 0)) {
            return false;
        }

        if (target.edge().id() != other.source.edge().id()) {
            edges.add(other.edges.getFirst());
        }

        for (int i = 1; i < other.edges.size(); ++i) {
            edges.add(other.edges.get(i));
        }

        target = other.target;

        return true;
    }
}
