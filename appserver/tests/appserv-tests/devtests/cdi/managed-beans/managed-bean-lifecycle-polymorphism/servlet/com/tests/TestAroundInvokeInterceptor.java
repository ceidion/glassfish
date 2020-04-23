/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.tests;

import javax.interceptor.*;

@Tester @Interceptor
public class TestAroundInvokeInterceptor {
    public static int aroundInvokeCount = 0;

    public static void reset(){
        aroundInvokeCount = 0;
    }

   @AroundInvoke 
   public Object testMethod(InvocationContext ctx) throws Exception {
        System.out.println("TestAroundInvokeInterceptor:: aroundInvoke called - target:" + ctx.getTarget() + " , params:"+ ctx.getParameters());
        aroundInvokeCount ++;
        ctx.proceed();
        return null;
   }

}