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

package com.bmwcarit.barefoot.road;

import java.io.Serializable;
import java.nio.ByteBuffer;

import com.bmwcarit.barefoot.roadmap.Road;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.geometry.OperatorExportToWkb;
import com.esri.core.geometry.OperatorImportFromWkb;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.WkbExportFlags;
import com.esri.core.geometry.WkbImportFlags;

/**
 * Road data structure for a road segment.
 *
 * Provides topological information, i.e. {@link BaseRoad#source()} and
 * {@link BaseRoad#target()}), road type information, i.e.
 * {@link BaseRoad#oneway()}, {@link BaseRoad#type()},
 * {@link BaseRoad#priority()} and {@link BaseRoad#maxspeed(Heading)}), and
 * geometrical information (e.g. {@link BaseRoad#length()} and
 * {@link BaseRoad#geometry()}).
 */
public class BaseRoad implements Serializable {
    private static final long serialVersionUID = 1L;
    private final long id;
    private final long refid;
    private final long source;
    private final long target;
    private final boolean oneway;
    private final short type;
    private final float priority;
    private final float maxspeedForward;
    private final float maxspeedBackward;
    private final float length;
    private final byte[] geometry;
    private final Boolean tunnel;
    private final Boolean tunnelEntry;
    private final int direction;
    private final String country;

    /**
     * Constructs {@link BaseRoad} object.
     *
     * @param id
     *            Unique road identifier.
     * @param source
     *            Source vertex identifier (in road topology representation).
     * @param target
     *            Target vertex identifier (in road topology representation).
     * @param refid
     *            Identifier of road referring to some source data.
     * @param oneway
     *            Indicator if this road is a one-way road.
     * @param type
     *            Identifier of this road's type.
     * @param priority
     *            Road priority factor, which is greater or equal than one.
     * @param maxspeedForward
     *            Maximum speed limit for passing this road from source to target.
     * @param maxspeedBackward
     *            Maximum speed limit for passing this road from target to source.
     * @param length
     *            Length of road geometry in meters.
     * @param geometry
     *            Road's geometry from source to target as {@link Polyline} object.
     */
    public BaseRoad(long id, long source, long target, long refid, boolean oneway, short type, float priority,
            float maxspeedForward, float maxspeedBackward, float length, Polyline geometry) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.refid = refid;
        this.oneway = oneway;
        this.type = type;
        this.priority = priority;
        this.maxspeedForward = maxspeedForward;
        this.maxspeedBackward = maxspeedBackward;
        this.length = length;
        this.geometry = OperatorExportToWkb.local().execute(WkbExportFlags.wkbExportLineString, geometry, null).array();
        this.tunnel = null;
        this.tunnelEntry = null;
        this.direction = 3;
        this.country = null;
    }

    /**
     * Constructs {@link BaseRoad} object.
     *
     * @param id
     *            Unique road identifier.
     * @param source
     *            Source vertex identifier (in road topology representation).
     * @param target
     *            Target vertex identifier (in road topology representation).
     * @param osmId
     *            Identifier of corresponding OpenStreetMap road.
     * @param oneway
     *            Indicator if this road is a one-way road.
     * @param type
     *            Identifier of this road's type.
     * @param priority
     *            Road priority factor, which is greater or equal than one.
     * @param maxspeedForward
     *            Maximum speed limit for passing this road from source to target.
     * @param maxspeedBackward
     *            Maximum speed limit for passing this road from target to source.
     * @param length
     *            Length of road geometry in meters.
     * @param wkb
     *            Road's geometry in WKB format from source to target.
     */
    public BaseRoad(long id, long source, long target, long osmId, boolean oneway, short type, float priority,
            float maxspeedForward, float maxspeedBackward, float length, byte[] wkb) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.refid = osmId;
        this.oneway = oneway;
        this.type = type;
        this.priority = priority;
        this.maxspeedForward = maxspeedForward;
        this.maxspeedBackward = maxspeedBackward;
        this.length = length;
        this.geometry = wkb;
        this.tunnel = null;
        this.tunnelEntry = null;
        this.direction = 3;
        this.country = null;
    }

    /**
     * Constructs {@link BaseRoad} object.
     *
     * @param id
     *            Unique road identifier.
     * @param source
     *            Source vertex identifier (in road topology representation).
     * @param target
     *            Target vertex identifier (in road topology representation).
     * @param osmId
     *            Identifier of corresponding OpenStreetMap road.
     * @param oneway
     *            Indicator if this road is a one-way road.
     * @param type
     *            Identifier of this road's type.
     * @param priority
     *            Road priority factor, which is greater or equal than one.
     * @param maxspeedForward
     *            Maximum speed limit for passing this road from source to target.
     * @param maxspeedBackward
     *            Maximum speed limit for passing this road from target to source.
     * @param length
     *            Length of road geometry in meters.
     * @param wkb
     *            Road's geometry in WKB format from source to target.
     * @param tunnel
     *            'true' if segment is a tunnel.
     * @param tunnelEntry
     *            'true' if segment is last element before tunnel.
     */
    public BaseRoad(long id, long source, long target, long osmId, boolean oneway, short type, float priority,
            float maxspeedForward, float maxspeedBackward, float length, byte[] wkb, boolean tunnel,
            boolean tunnelEntry) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.refid = osmId;
        this.oneway = oneway;
        this.type = type;
        this.priority = priority;
        this.maxspeedForward = maxspeedForward;
        this.maxspeedBackward = maxspeedBackward;
        this.length = length;
        this.geometry = wkb;
        this.tunnel = tunnel;
        this.tunnelEntry = tunnelEntry;
        this.direction = 3;
        this.country = null;
    }

    /**
     * Constructs {@link BaseRoad} object.
     *
     * @param id
     *            Unique road identifier.
     * @param source
     *            Source vertex identifier (in road topology representation).
     * @param target
     *            Target vertex identifier (in road topology representation).
     * @param osmId
     *            Identifier of corresponding OpenStreetMap road.
     * @param direction
     *            Direction of this road segment (traffic restriction).
     * @param type
     *            Identifier of this road's type.
     * @param priority
     *            Road priority factor, which is greater or equal than one.
     * @param maxspeedForward
     *            Maximum speed limit for passing this road from source to target.
     * @param maxspeedBackward
     *            Maximum speed limit for passing this road from target to source.
     * @param length
     *            Length of road geometry in meters.
     * @param wkb
     *            Road's geometry in WKB format from source to target.
     * @param tunnel
     *            'true' if segment is a tunnel.
     * @param tunnelEntry
     *            'true' if segment is last element before tunnel.
     */
    public BaseRoad(long id, long source, long target, long osmId, int direction, short type, float priority,
            float maxspeedForward, float maxspeedBackward, float length, byte[] wkb, boolean tunnel,
            boolean tunnelEntry) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.refid = osmId;
        this.direction = direction;
        this.type = type;
        this.priority = priority;
        this.maxspeedForward = maxspeedForward;
        this.maxspeedBackward = maxspeedBackward;
        this.length = length;
        this.geometry = wkb;
        this.tunnel = tunnel;
        this.tunnelEntry = tunnelEntry;
        if (direction == 3) {
            this.oneway = false;
        } else {
            this.oneway = true;
        }
        this.country = null;

    }

    /**
     * Constructs {@link BaseRoad} object.
     *
     * @param id
     *            Unique road identifier.
     * @param source
     *            Source vertex identifier (in road topology representation).
     * @param target
     *            Target vertex identifier (in road topology representation).
     * @param osmId
     *            Identifier of corresponding OpenStreetMap road.
     * @param direction
     *            Direction of this road segment (traffic restriction).
     * @param type
     *            Identifier of this road's type.
     * @param priority
     *            Road priority factor, which is greater or equal than one.
     * @param maxspeedForward
     *            Maximum speed limit for passing this road from source to target.
     * @param maxspeedBackward
     *            Maximum speed limit for passing this road from target to source.
     * @param length
     *            Length of road geometry in meters.
     * @param wkb
     *            Road's geometry in WKB format from source to target.
     * @param tunnel
     *            'true' if segment is a tunnel.
     * @param tunnelEntry
     *            'true' if segment is last element before tunnel.
     * @param country
     *            Unique identifier of one country (e.g. GERMANY).
     */
    public BaseRoad(long id, long source, long target, long osmId, int direction, short type, float priority,
            float maxspeedForward, float maxspeedBackward, float length, byte[] wkb, boolean tunnel,
            boolean tunnelEntry, String country) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.refid = osmId;
        this.direction = direction;
        this.type = type;
        this.priority = priority;
        this.maxspeedForward = maxspeedForward;
        this.maxspeedBackward = maxspeedBackward;
        this.length = length;
        this.geometry = wkb;
        this.tunnel = tunnel;
        this.tunnelEntry = tunnelEntry;
        if (direction == 3) {
            this.oneway = false;
        } else {
            this.oneway = true;
        }
        this.country = country;

    }

    /**
     * Constructs {@link BaseRoad} object from BaseRoad Object.
     *
     * @param base
     *            BaseRoad to construct.
     * @param heading
     *            Inverts geometry if driving backwards.
     * 
     */
    public BaseRoad(BaseRoad base, Heading heading) {

        this.id = base.id;
        this.source = base.source;
        this.target = base.target;
        this.refid = base.refid;
        this.direction = base.direction;
        this.type = base.type;
        this.priority = base.priority;
        this.maxspeedForward = base.maxspeedForward;
        this.maxspeedBackward = base.maxspeedBackward;
        this.length = base.length;
        if (heading == Heading.backward) {
            this.geometry = OperatorExportToWkb.local()
                    .execute(WkbExportFlags.wkbExportLineString, Road.invert(base.geometry()), null).array();
        } else {
            this.geometry = base.geometry;
        }
        this.tunnel = base.tunnel;
        this.tunnelEntry = base.tunnelEntry;
        this.oneway = base.oneway;
        this.country = null;

    }

    /**
     * Gets unique road identifier.
     *
     * @return Unique road identifier.
     */
    public long id() {
        return id;
    }

    /**
     * Gets source vertex identifier.
     *
     * @return Source vertex identifier.
     */
    public long source() {
        return source;
    }

    /**
     * Gets target vertex identifier.
     *
     * @return Target vertex identifier.
     */
    public long target() {
        return target;
    }

    /**
     * Gets identifier of road reference from the source.
     * <p>
     * <b>Note:</b> A routable road map requires splitting of roads into segments to
     * build a road topology (graph). Since OpenStreetMap roads span often roads
     * over multiple intersections, they must be split into multiple road segments.
     * Hence, it is a one-to-many relationship.
     *
     * @return Identifier of referred OpenStreetMap road.
     */
    public long refid() {
        return refid;
    }

    /**
     * Gets a boolean if this is a one-way.
     *
     * @return True if this road is a one-way road, false otherwise.
     */
    public boolean oneway() {
        return oneway;
    }

    /**
     * Gets road's type identifier.
     *
     * @return Road type identifier.
     */
    public short type() {
        return type;
    }

    /**
     * Gets road's priority factor, i.e. an additional cost factor for routing, and
     * must be greater or equal to one. Higher priority factor means higher costs.
     *
     * @return Road's priority factor.
     */
    public float priority() {
        return priority;
    }

    /**
     * Gets road's maximum speed for respective heading in kilometers per hour.
     *
     * @param heading
     *            {@link Heading} for which maximum speed must be returned.
     * @return Maximum speed in kilometers per hour.
     */
    public float maxspeed(Heading heading) {
        return heading == Heading.forward ? maxspeedForward : maxspeedBackward;
    }

    /**
     * Gets road length in meters.
     *
     * @return Road length in meters.
     */
    public float length() {
        return length;
    }

    /**
     * Gets road's geometry as a {@link Polyline} from the road's source to its
     * target.
     *
     * @return Road's geometry as {@link Polyline} from source to target.
     */
    public Polyline geometry() {
        return (Polyline) OperatorImportFromWkb.local().execute(WkbImportFlags.wkbImportDefaults, Type.Polyline,
                ByteBuffer.wrap(geometry), null);
    }

    /**
     * Gets road's geometry as a {@link ByteBuffer} in WKB format from the road's
     * source to its target.
     *
     * @return Road's geometry as a {@link ByteBuffer} in WKB format from the road's
     *         source to its target.
     */
    public byte[] wkb() {
        return geometry;
    }

    /**
     * Is road segment a tunnel?
     *
     * @return 'true' if segment is a tunnel.
     */
    public Boolean getTunnel() {
        return tunnel;
    }

    /**
     * Is road segment last element before tunnel?
     *
     * @return 'true' if segment is last element before tunnel.
     */
    public Boolean getTunnelEntry() {
        return tunnelEntry;
    }

    /**
     * @return the direction 1 = Start->End 2 = End -> Start 3 = both
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Returns the unique country identifier for a BaseRoad.
     * 
     * @return country for a BaseRoad or null if not set.
     */
    public String getCountry() {
        return country;
    }

}
