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

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface of a router for routing in a directed {@link Graph}. The routing
 * functions return paths from a source {@link Point} to a target {@link Point}
 * that minimize a {@link Cost} function.
 *
 * @param <E>{@link
 *            AbstractEdge} type of the graph.
 * @param <P>
 *            P
 */
public interface Router<E extends AbstractEdge<E>, P extends Point<E>> {

    /**
     * Gets path, i.e. a sequence of {@link AbstractEdge}s, to each target
     * {@link Point} from exactly that source {@link Point} that has minimum cost
     * according to {@link Cost} function. Search depth of routing can be bound by
     * bounding {@link Cost} function and a maximum bounding cost value.
     *
     * @param source
     *            roadPoint Set of source {@link Point}s in the graph.
     * @param targets
     *            Set of target {@link Point}s in the graph.
     * @param cost
     *            Custom {@link Cost} function.
     * @param bound
     *            Bounding {@link Cost} function.
     * @param max
     *            Maximum bounding cost value to bound search depth.
     * @param deltaTime
     *            maximum time to reach destiny.
     * @param maxVelocity
     *            allowed increased speed to reach destiny.
     * @return Map of target {@link Point} to a tuple of path, i.e. a sequence of
     *         {@link AbstractEdge}s, and source {@link Point} that have minimum
     *         cost according to {@link Cost} function for reaching the target. If
     *         there is no path to target, it maps to null.
     */

    Map<P, List<E>> route(P source, Set<P> targets, Cost<E> cost, Cost<E> bound, Double max, Double deltaTime,
            Double maxVelocity);
}
