/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.test.jms.injection.ejb;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.jms.*;
import java.lang.String;
import org.glassfish.test.jms.injection.ejb.SessionBeanInjection2;

/**
 *
 * @author LILIZHAO
 */
@Stateless(mappedName="SessionBeanInjection1/remote1")
@TransactionManagement(TransactionManagementType.BEAN)
public class SessionBeanInjection1 implements SessionBeanInjectionRemote1 {

    private static String preIdentical = "fingerPrint";
    private static String requestScope = "around RequestScoped";

    @EJB 
    SessionBeanInjectionRemote2 bean2;

    @Resource(mappedName = "jms/jms_unit_test_Queue")
    private Queue queue;

    @Inject
    @JMSConnectionFactory("jms/jms_unit_test_QCF")
    @JMSSessionMode(JMSContext.AUTO_ACKNOWLEDGE)
    private JMSContext jmsContext;

    public Boolean sendMessage(String text) {
        String context1 = "";
        try {
            JMSProducer producer = jmsContext.createProducer();
            TextMessage msg = jmsContext.createTextMessage(text);
            producer.send(queue, msg);
            System.out.println("JMSContext1:"+jmsContext.toString());
            context1 = jmsContext.toString();
        } catch (Exception e) {
            throw new EJBException(e);
        }

        String context2 = bean2.sendMessage(text);

        if (context1.indexOf(requestScope) != -1){
            System.out.println("The context variables used in the first call are in request scope.");
        }else{
            System.out.println("The context variables used in the first call are NOT in request scope.");
            return false;
        }
            
        if (context2.indexOf(requestScope) != -1){
            System.out.println("The context variables used in the second call are in request scope.");
        }else{
            System.out.println("The context variables used in the second call are NOT in request scope.");
            return false;
        }
        
        String context1Annotation = context1.substring(context1.indexOf(preIdentical),context1.indexOf(requestScope));
        String context2Annotation = context2.substring(context2.indexOf(preIdentical),context2.indexOf(requestScope));
        
        if(context1Annotation.equals(context2Annotation)){
            System.out.println("The context variables in the first and second calls to context.send() injected are using identical annotations.");
        }else{
            System.out.println("The context variables in the first and second calls to context.send() injected are not using identical annotations.");
            return false;
        }

        if (context1.substring(context1.indexOf(requestScope)).equals(context2.substring(context2.indexOf(requestScope)))) {
            System.out.println("The context variables used in the first and second calls to context.send() take place in the same request.");
        }else{
            System.out.println("The context variables used in the first and second calls to context.send() take place in the different request.");
            return false;
        }
        return true;
    }
}