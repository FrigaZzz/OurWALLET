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
import com.mfr.taass.spring.group.api.entities.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luca
 */
public class CustomUSER_GROUP_Serializer extends StdSerializer<List<User>>{
    public CustomUSER_GROUP_Serializer() {
        this(null);
    }

    public CustomUSER_GROUP_Serializer(Class<List<User>> t) {
        super(t);
    }
    @Override
    public void serialize(
            List<User> users,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {

        List<User> studs = new ArrayList<>();
        for (User s : users) {
            s.setCommonFundGroups(null);
            studs.add(s);
        }
        generator.writeObject(studs);
    }
}
