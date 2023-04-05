/*
 * Copyright (C) 2023, Partners of the EU funded DE4A project consortium
 *   (https://www.de4a.eu/consortium), under Grant Agreement No.870635
 * Author: University of Maribor (UM)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package um.si.de4a.resources.vp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VPAttachment {
    private Option options;
    @SerializedName("presentation_definition")
    private PresentationDefinition presentationDefinition;

    public VPAttachment(Option options, PresentationDefinition presentationDefinition) {
        this.options = options;
        this.presentationDefinition = presentationDefinition;
    }

    public Option getOptions() {
        return options;
    }

    public void setOptions(Option options) {
        this.options = options;
    }

    public PresentationDefinition getPresentationDefinition() {
        return presentationDefinition;
    }

    public void setPresentationDefinition(PresentationDefinition presentationDefinition) {
        this.presentationDefinition = presentationDefinition;
    }
}

class Option {
    private String challenge;
    private String domain;

    public Option(String challenge, String domain) {
        this.challenge = challenge;
        this.domain = domain;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}

class PresentationDefinition {
    @SerializedName("input_descriptors")
    private ArrayList<InputDescriptor> inputDescriptors;

    public PresentationDefinition(ArrayList<InputDescriptor> inputDescriptors) {
        this.inputDescriptors = inputDescriptors;
    }

    public ArrayList<InputDescriptor> getInputDescriptors() {
        return inputDescriptors;
    }

    public void setInputDescriptors(ArrayList<InputDescriptor> inputDescriptors) {
        this.inputDescriptors = inputDescriptors;
    }
}

class InputDescriptor {
    private String id;
    private String name;
    private ArrayList<Schema> schema;
    private ArrayList<Constraint> constraints;

    public InputDescriptor(String id, String name, ArrayList<Schema> schema, ArrayList<Constraint> constraints) {
        this.id = id;
        this.name = name;
        this.schema = schema;
        this.constraints = constraints;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Schema> getSchema() {
        return schema;
    }

    public void setSchema(ArrayList<Schema> schema) {
        this.schema = schema;
    }

    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(ArrayList<Constraint> constraints) {
        this.constraints = constraints;
    }
}

class Schema {
    private String uri;

    public Schema(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

class Constraint {
    private ArrayList<Field> fields;

    public Constraint(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }
}

class Field {
    private List<String> path;
    private Filter filter;

    public Field(List<String> path, Filter filter) {
        this.path = path;
        this.filter = filter;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}

class Filter {
    private String type;
    private String pattern;

    public Filter(String type, String pattern) {
        this.type = type;
        this.pattern = pattern;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}