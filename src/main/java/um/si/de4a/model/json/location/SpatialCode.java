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
package um.si.de4a.model.json.location;

import com.google.gson.annotations.SerializedName;
import um.si.de4a.model.json.Title;

public class SpatialCode {
    @SerializedName("@targetFrameworkUrl")
    private String targetFrameworkUrl;

    @SerializedName("@targetNotation")
    private String targetNotation;

    @SerializedName("@uri")
    private String uri;

    private Title targetFrameworkName;

    private Title targetName;

    public SpatialCode(String targetFrameworkUrl, String targetNotation, String uri, Title targetFrameworkName, Title targetName) {
        this.targetFrameworkUrl = targetFrameworkUrl;
        this.targetNotation = targetNotation;
        this.uri = uri;
        this.targetFrameworkName = targetFrameworkName;
        this.targetName = targetName;
    }

    public String getTargetFrameworkUrl() {
        return targetFrameworkUrl;
    }

    public void setTargetFrameworkUrl(String targetFrameworkUrl) {
        this.targetFrameworkUrl = targetFrameworkUrl;
    }

    public String getTargetNotation() {
        return targetNotation;
    }

    public void setTargetNotation(String targetNotation) {
        this.targetNotation = targetNotation;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Title getTargetFrameworkName() {
        return targetFrameworkName;
    }

    public void setTargetFrameworkName(Title targetFrameworkName) {
        this.targetFrameworkName = targetFrameworkName;
    }

    public Title getTargetName() {
        return targetName;
    }

    public void setTargetName(Title targetName) {
        this.targetName = targetName;
    }
}
