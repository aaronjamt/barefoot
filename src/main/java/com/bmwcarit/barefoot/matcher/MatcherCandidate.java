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

package com.bmwcarit.barefoot.matcher;

import org.json.JSONException;
import org.json.JSONObject;

import com.bmwcarit.barefoot.markov.StateCandidate;
import com.bmwcarit.barefoot.roadmap.RoadMap;
import com.bmwcarit.barefoot.roadmap.RoadPoint;

/**
 * Matching candidate for Hidden Markov Model (HMM) map matching representing a
 * position on the map.
 */
public class MatcherCandidate extends StateCandidate<MatcherCandidate, MatcherTransition, MatcherSample> {
    private final RoadPoint point;
    private final MatcherSample sample;

    /**
     * Creates a matching candidate.
     *
     * @param point
     *            {@link RoadPoint} object that is point on the map represented by
     *            matching candidate.
     */
    public MatcherCandidate(RoadPoint point) {
        this.point = point;
        this.sample = null;
    }

    /**
     * Creates a matching candidate with heading and velocity entries.
     *
     * @param point
     *            {@link RoadPoint} object that is point on the map represented by
     *            matching candidate.
     */
    public MatcherCandidate(RoadPoint point, MatcherSample sample) {
        this.point = point;
        this.sample = sample;

    }

    /**
     * Creates a matching candidate from its JSON representation.
     *
     * @param json
     *            JSON representation of matching candidate.
     * @param factory
     *            Matcher factory for creation of matching candidates, transitions
     *            and samples.
     * @param map
     *            {@link RoadMap} object used for creation of matching candidates,
     *            transitions and samples.
     * @throws JSONException
     *             thrown on JSON parse error.
     */
    public MatcherCandidate(JSONObject json, MatcherFactory factory, RoadMap map) throws JSONException {
        super(json, factory);
        point = RoadPoint.fromJSON(json.getJSONObject("roadpoint"), map);
        if (json.has("sample")) {
            sample = factory.sample(json.getJSONObject("sample"));
            // sample = new MatcherSample(json.getJSONObject("sample"));//(Point)
            // GeometryEngine.geometryFromWkt(json.getString("sample"),
            // WktImportFlags.wktImportDefaults,
            // Type.Point);
        } else {
            sample = null;
        }

    }

    /**
     * Gets {@link RoadPoint} as point on the map represented by the matching
     * candidate.
     *
     * @return {@link RoadPoint} object of the matching candidate.
     */
    public RoadPoint point() {
        return point;
    }

    public MatcherSample getSample() {
        return sample;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = super.toJSON();
        json.put("roadpoint", point.toJSON());

        if (sample != null) {
            json.put("sample", sample.toJSON());
        }
        return json;
    }

    @Override
    public String toString() {
        String s = "Edge-RefId: " + this.point().edge().base().refid() + ", fraction:" + this.point.fraction();
        return s;
    }

}
