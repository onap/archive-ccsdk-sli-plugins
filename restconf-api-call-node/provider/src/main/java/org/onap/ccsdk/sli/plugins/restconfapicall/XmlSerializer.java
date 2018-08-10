/*-
 * ============LICENSE_START=======================================================
 * ONAP - CCSDK
 * ================================================================================
 * Copyright (C) 2018 Huawei Technologies Co., Ltd. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.ccsdk.sli.plugins.restconfapicall;

import java.util.Map;

import static org.onap.ccsdk.sli.plugins.restconfapicall.DataFormat.XML;

/**
 * Representation of XML serializer which encodes properties to XML and
 * decodes properties from XML with the data format serializer.
 */
public class XmlSerializer extends DataFormatSerializer {

    /**
     * Creates an instance of XML serializer.
     *
     * @param d data format serializer context
     */
    protected XmlSerializer(DataFormatSerializerContext d) {
        super(XML, d);
    }

    @Override
    public String encode(Map<String, String> param) {
        //TODO: Implementation code.
        return null;
    }

    @Override
    public Map<String, String> decode(String dataFormatBody) {
        //TODO: Implementation code.
        return null;
    }
}
