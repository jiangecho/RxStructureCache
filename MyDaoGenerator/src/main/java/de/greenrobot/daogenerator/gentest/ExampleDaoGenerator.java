/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
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
 */
package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * <p/>
 * Run it as a Java application (not Android).
 *
 * @author Markus
 */
public class ExampleDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.echo.rxstructurecache.dao");

        //addNote(schema);
        addCache(schema);
        new DaoGenerator().generateAll(schema, "../rxstructurecache/src/main/java");
    }

    private static void addNote(Schema schema) {
        Entity fans = schema.addEntity("Fans");
        fans.addIdProperty();
        fans.addStringProperty("name");
        fans.addStringProperty("number").notNull();
    }

    private static void addCache(Schema schema) {
        Entity cache = schema.addEntity("Cache");
        cache.addIdProperty();
        Property cacheId = cache.addLongProperty("cacheId").getProperty();
        cache.addStringProperty("content").notNull();
        cache.addIntProperty("version");
        Property type = cache.addStringProperty("type").getProperty();

        Index index = new Index();
        index.addProperty(cacheId);
        index.addProperty(type);
        index.makeUnique();
        cache.addIndex(index);
//        Property cacheId = new Property.PropertyBuilder(schema, cache, PropertyType.Long, "cacheId").getProperty();
//        Property type = new Property.PropertyBuilder(schema, cache, PropertyType.String, "type").getProperty();
//        index.addProperty(cacheId);
//        index.addProperty(type);
//        index.makeUnique();
//
//        cache.addIndex(index);

    }


}