/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.swagger.annotations.ApiModelProperty;
import org.eclipse.kapua.model.KapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;

/**
 * {@link DeviceCreator} encapsulates all the information needed to create a new {@link Device} in the system.<br>
 * The data provided will be used to seed the new {@link Device} and its related information.<br>
 * The fields of the {@link DeviceCreator} presents the attributes that are searchable for a given device.<br>
 * The DeviceCreator Properties field can be used to provide additional properties associated to the Device;
 * those properties will not be searchable through Device queries.<br>
 * The clientId field of the Device is used to store the MAC address of the primary network interface of the device.
 * 
 * @since 1.0.0
 */
@XmlRootElement(name = "deviceCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "groupId",
        "clientId",
        "status",
        "connectionId",
        "lastEventId",
        "displayName",
        "serialNumber",
        "modelId",
        "imei",
        "imsi",
        "iccid",
        "biosVersion",
        "firmwareVersion",
        "osVersion",
        "jvmVersion",
        "osgiFrameworkVersion",
        "applicationFrameworkVersion",
        "connectionInterface",
        "connectionIp",
        "applicationIdentifiers",
        "acceptEncoding",
        "customAttribute1",
        "customAttribute2",
        "customAttribute3",
        "customAttribute4",
        "customAttribute5"
}, //
        factoryClass = DeviceXmlRegistry.class, factoryMethod = "newDeviceCreator")
public interface DeviceCreator extends KapuaUpdatableEntityCreator<Device> {

    /**
     * Get the group identifier
     * 
     * @return
     */
    @XmlElement(name = "groupId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    public KapuaId getGroupId();

    /**
     * Set the group identifier
     * 
     * @param groupId
     */
    public void setGroupId(KapuaId groupId);

    /**
     * Get the client identifier
     * 
     * @return
     */
    @XmlElement(name = "clientId")
    public String getClientId();

    /**
     * Set the client identifier
     * 
     * @param clientId
     */
    public void setClientId(String clientId);

    /**
     * Get the device status
     * 
     * @return
     */
    @XmlElement
    public DeviceStatus getStatus();

    /**
     * Sets the device status
     * 
     * @param status
     */
    public void setStatus(DeviceStatus status);

    /**
     * Get the connection identifier
     * 
     * @return
     */
    @XmlElement(name = "connectionId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    public KapuaId getConnectionId();

    /**
     * Set the connection identifier
     * 
     * @param connectionId
     */
    public void setConnectionId(KapuaId connectionId);

    /**
     * Get the last {@link DeviceEvent} identifier
     * 
     * @return
     */
    @XmlElement(name = "lastEventId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    public KapuaId getLastEventId();

    /**
     * Set the last {@link DeviceEvent} identifier.
     * 
     * @param lastEventId
     */
    public void setLastEventId(KapuaId lastEventId);

    /**
     * Get the display name
     * 
     * @return
     */
    @XmlElement(name = "displayName")
    public String getDisplayName();

    /**
     * Set the display name
     * 
     * @param displayName
     */
    public void setDisplayName(String displayName);

    /**
     * Get the serial number
     * 
     * @return
     */
    @XmlElement(name = "serialNumber")
    public String getSerialNumber();

    /**
     * Set the serial number
     * 
     * @param serialNumber
     */
    public void setSerialNumber(String serialNumber);

    /**
     * Get the model identifier
     * 
     * @return
     */
    @XmlElement(name = "modelId")
    public String getModelId();

    /**
     * Set the model identifier
     * 
     * @param modelId
     */
    public void setModelId(String modelId);

    /**
     * Get the imei
     * 
     * @return
     */
    @XmlElement(name = "imei")
    public String getImei();

    /**
     * Set the imei
     * 
     * @param imei
     */
    public void setImei(String imei);

    /**
     * Get the imsi
     * 
     * @return
     */
    @XmlElement(name = "imsi")
    public String getImsi();

    /**
     * Set the imsi
     * 
     * @param imsi
     */
    public void setImsi(String imsi);

    /**
     * Get the iccid
     * 
     * @return
     */
    @XmlElement(name = "iccid")
    public String getIccid();

    /**
     * Set the iccid
     * 
     * @param iccid
     */
    public void setIccid(String iccid);

    /**
     * Get bios version
     * 
     * @return
     */
    @XmlElement(name = "biosVersion")
    public String getBiosVersion();

    /**
     * Set bios version
     * 
     * @param biosVersion
     */
    public void setBiosVersion(String biosVersion);

    /**
     * Get firmware version
     * 
     * @return
     */
    @XmlElement(name = "firmwareVersion")
    public String getFirmwareVersion();

    /**
     * Set firmware version
     * 
     * @param firmwareVersion
     */
    public void setFirmwareVersion(String firmwareVersion);

    /**
     * Get os version
     * 
     * @return
     */
    @XmlElement(name = "osVersion")
    public String getOsVersion();

    /**
     * Set os version
     * 
     * @param osVersion
     */
    public void setOsVersion(String osVersion);

    /**
     * Get jvm version
     * 
     * @return
     */
    @XmlElement(name = "jvmVersion")
    public String getJvmVersion();

    /**
     * Set jvm version
     * 
     * @param jvmVersion
     */
    public void setJvmVersion(String jvmVersion);

    /**
     * Get osgi framework version
     * 
     * @return
     */
    @XmlElement(name = "osgiFrameworkVersion")
    public String getOsgiFrameworkVersion();

    /**
     * Set osgi framework version
     * 
     * @param osgiFrameworkVersion
     */
    public void setOsgiFrameworkVersion(String osgiFrameworkVersion);

    /**
     * Get application framework version
     * 
     * @return
     */
    @XmlElement(name = "applicationFrameworkVersion")
    public String getApplicationFrameworkVersion();

    /**
     * Set application framework version
     * 
     * @param appFrameworkVersion
     */
    public void setApplicationFrameworkVersion(String appFrameworkVersion);

    /**
     * Gets the device's primary connection interface name
     * @return The device's primary connection interface name
     */
    @XmlElement(name = "connectionInterface")
    String getConnectionInterface();

    /**
     * Sets the device's primary connection interface name
     * @param connectionInterface The device's primary connection interface name
     */
    void setConnectionInterface(String connectionInterface);

    /**
     * Gets the device's primary connection interface IP address
     * @return The device's primary connection interface IP address
     */
    @XmlElement(name="connectionIp")
    String getConnectionIp();

    /**
     * Sets the device's primary connection interface IP address
     * @param connectionIp The device's primary connection interface IP address
     */
    void setConnectionIp(String connectionIp);

    /**
     * Get application identifiers
     * 
     * @return
     */
    @XmlElement(name = "applicationIdentifiers")
    public String getApplicationIdentifiers();

    /**
     * Set application identifiers
     * 
     * @param applicationIdentifiers
     */
    public void setApplicationIdentifiers(String applicationIdentifiers);

    /**
     * Get accept encoding flag
     * 
     * @return
     */
    @XmlElement(name = "acceptEncoding")
    public String getAcceptEncoding();

    /**
     * Set accept encoding flag
     * 
     * @param acceptEncoding
     */
    public void setAcceptEncoding(String acceptEncoding);

    /**
     * Get custom attribute 1
     * 
     * @return
     */
    @XmlElement(name = "customAttribute1")
    public String getCustomAttribute1();

    /**
     * Set custom attribute 1
     * 
     * @param customAttribute1
     */
    public void setCustomAttribute1(String customAttribute1);

    /**
     * Get custom attribute 2
     * 
     * @return
     */
    @XmlElement(name = "customAttribute2")
    public String getCustomAttribute2();

    /**
     * Set custom attribute 2
     * 
     * @param customAttribute2
     */
    public void setCustomAttribute2(String customAttribute2);

    /**
     * Get custom attribute 3
     * 
     * @return
     */
    @XmlElement(name = "customAttribute3")
    public String getCustomAttribute3();

    /**
     * Set custom attribute 3
     * 
     * @param customAttribute3
     */
    public void setCustomAttribute3(String customAttribute3);

    /**
     * Get custom attribute 4
     * 
     * @return
     */
    @XmlElement(name = "customAttribute4")
    public String getCustomAttribute4();

    /**
     * Set custom attribute 4
     * 
     * @param customAttribute4
     */
    public void setCustomAttribute4(String customAttribute4);

    /**
     * Get custom attribute 5
     * 
     * @return
     */
    @XmlElement(name = "customAttribute5")
    public String getCustomAttribute5();

    /**
     * Set custom attribute 5
     * 
     * @param customAttribute5
     */
    public void setCustomAttribute5(String customAttribute5);
}
