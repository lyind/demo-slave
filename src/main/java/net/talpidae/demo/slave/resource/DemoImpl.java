/*
 * Copyright (C) 2017  Jonas Zeiger <jonas.zeiger@talpidae.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.talpidae.demo.slave.resource;

import net.talpidae.base.insect.config.SlaveSettings;
import net.talpidae.demo.slave.api.Demo;

import java.util.Random;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Path;

import lombok.val;


@Singleton
@Resource
@Path("/")
public class DemoImpl implements Demo
{
    private final SlaveSettings slaveSettings;

    private final Demo demo;


    @Inject
    public DemoImpl(SlaveSettings slaveSettings, Demo demo)
    {
        this.slaveSettings = slaveSettings;
        this.demo = demo;
    }


    @Override
    public HelloMessage getHello()
    {
        val random = new Random();

        val builder = new StringBuilder("hello from " + slaveSettings.getName());

        // 50% chance to get another hello from a remote server
        if (random.nextInt(2) > 0)
        {
            builder.append(" and ")
                    .append(demo.getHello().getSay());
        }

        return new HelloMessage(builder.toString());
    }
}