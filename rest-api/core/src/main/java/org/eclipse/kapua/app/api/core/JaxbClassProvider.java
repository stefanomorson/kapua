/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.kapua.app.api.core.exception.model.CleanJobDataExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.DeviceManagementRequestContentExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.DeviceManagementResponseCodeExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.DeviceManagementResponseContentExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.DeviceManagementSendExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.DeviceManagementTimeoutExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.DeviceNotConnectedExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.EntityNotFoundExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.ExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.IllegalArgumentExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.IllegalNullArgumentExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.InternalUserOnlyExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobAlreadyRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobEngineExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobInvalidTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingStepExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobNotRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobResumingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobScopedEngineExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStartingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStoppingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.MfaRequiredExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.SelfManagedOnlyExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.SubjectUnauthorizedExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.ThrowableInfo;
import org.eclipse.kapua.app.api.core.model.CountResult;
import org.eclipse.kapua.commons.core.ClassProvider;

/*
 * Provides list of rest-app-specific classes that may be serialized using JAXB  
 */
public class JaxbClassProvider implements ClassProvider {

    private List<Class<?>> classes = new ArrayList<>();

    public JaxbClassProvider() {

        classes.addAll(Arrays.asList(new Class<?> [] {

            // REST API utility models
            CountResult.class,

            // REST API exception models
            ThrowableInfo.class,
            ExceptionInfo.class,

            InternalUserOnlyExceptionInfo.class,
            SelfManagedOnlyExceptionInfo.class,
            SubjectUnauthorizedExceptionInfo.class,

            EntityNotFoundExceptionInfo.class,
            IllegalArgumentExceptionInfo.class,
            IllegalNullArgumentExceptionInfo.class,
            MfaRequiredExceptionInfo.class,

            // Jobs Exception Info
            CleanJobDataExceptionInfo.class,
            JobAlreadyRunningExceptionInfo.class,
            JobEngineExceptionInfo.class,
            JobScopedEngineExceptionInfo.class,
            JobInvalidTargetExceptionInfo.class,
            JobMissingStepExceptionInfo.class,
            JobMissingTargetExceptionInfo.class,
            JobNotRunningExceptionInfo.class,
            JobResumingExceptionInfo.class,
            JobRunningExceptionInfo.class,
            JobStartingExceptionInfo.class,
            JobStoppingExceptionInfo.class,

            // Device Management Exception Info
            DeviceManagementRequestContentExceptionInfo.class,
            DeviceManagementResponseCodeExceptionInfo.class,
            DeviceManagementResponseContentExceptionInfo.class,
            DeviceManagementSendExceptionInfo.class,
            DeviceManagementTimeoutExceptionInfo.class,
            DeviceNotConnectedExceptionInfo.class,            
        }));

    }

    @Override
    public List<Class<?>> getClasses() {
        return Collections.unmodifiableList(classes);
    }

}
