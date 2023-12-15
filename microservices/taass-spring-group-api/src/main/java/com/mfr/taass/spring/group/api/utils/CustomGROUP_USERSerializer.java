/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.mfr.taass.spring.group.api.entities.Groups;
import com.mfr.taass.spring.group.api.entities.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luca
 */
public class CustomGROUP_USERSerializer extends StdSerializer<List<Groups>>{
    public CustomGROUP_USERSerializer() {
        this(null);
    }

    public CustomGROUP_USERSerializer(Class<List<Groups>> t) {
        super(t);
    }
    @Override
    public void serialize(
            List<Groups> groups,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {

        List<Groups> grp = new ArrayList<>();
        for (Groups s : groups) {
            s.setUsers(null);
            grp.add(s);
        }
        generator.writeObject(grp);
    }
}
