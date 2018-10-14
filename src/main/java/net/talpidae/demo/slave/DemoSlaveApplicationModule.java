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

package net.talpidae.demo.slave;


import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import net.talpidae.base.Base;
import net.talpidae.base.client.ClientModule;
import net.talpidae.base.client.GenericLoadBalancingProxyWebTargetProvider;
import net.talpidae.base.insect.metrics.MetricsSink;
import net.talpidae.base.insect.metrics.QueuedMetricsSink;
import net.talpidae.base.resource.RestModule;
import net.talpidae.base.util.Application;
import net.talpidae.base.util.auth.Authenticator;
import net.talpidae.base.util.session.SessionService;
import net.talpidae.demo.slave.api.Demo;
import net.talpidae.demo.slave.resource.DemoImpl;
import net.talpidae.demo.slave.util.auth.LocalAuthenticator;
import net.talpidae.demo.slave.util.session.LocalSessionService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DemoSlaveApplicationModule extends AbstractModule
{
    public static void main(String[] args)
    {
        Base.initializeApp(args, new DemoSlaveApplicationModule()).run();
    }


    @Override
    protected void configure()
    {
        install(new ClientModule());
        install(new RestModule());

        bind(Application.class).to(DemoSlaveApplication.class);

        bind(Authenticator.class).to(LocalAuthenticator.class);
        bind(SessionService.class).to(LocalSessionService.class);

        bind(DemoImpl.class);

        bind(new TypeLiteral<Class<Demo>>() {}).toInstance(Demo.class);
        bind(Demo.class).toProvider(new TypeLiteral<GenericLoadBalancingProxyWebTargetProvider<Demo>>() {});

        bind(MetricsSink.class).to(QueuedMetricsSink.class);
    }
}