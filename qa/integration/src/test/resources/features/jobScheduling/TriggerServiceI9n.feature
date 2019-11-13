###############################################################################
# Copyright (c) 2019 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@jobs
@triggerService
@integration

Feature: Trigger service tests

  Scenario: Adding "Device Connect" Schedule With All Valid Parameters
    Login as kapua-sys user and create a job with name job0.
    Add schedule0 with a valid start date to the created job.
    No errors should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create a job with the name "job0"
    When I found trigger properties with name "Device Connect"
    And A regular trigger creator with the name "schedule0"
    And The trigger is set to start today at 10:00.
    And I create a new trigger from the existing creator with previously defined date properties
    And I logout

    # ***********************
    # * Schedule Name Tests *
    # ***********************

  Scenario: Adding "Device Connect" Schedule Without Name
    Login as kapua-sys user and create a job with name job0.
    Try to create a new schedule with a valid start date but
    without defining its name. A KapuaIllegalNullArgumentException
    should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create a job with the name "job0"
    When I found trigger properties with name "Device Connect"
    And A regular trigger creator with the name ""
    And The trigger is set to start today at 10:00.
    When I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    And I create a new trigger from the existing creator with previously defined date properties
    Then An exception was thrown
    And I logout

  Scenario: Adding "Device Connect" Schedule With Name Only
    Login as kapua-sys user and create a job with name job0.
    Try to create a new schedule0, without defining any other property
    except the schedule name. A KapuaIllegalNullArgumentException should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create a job with the name "job0"
    When I found trigger properties with name "Device Connect"
    And A regular trigger creator with the name "schedule0"
    When I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    And I create a new trigger from the existing creator with previously defined date properties
    Then An exception was thrown
    And I logout

  Scenario: Adding "Device Connect" Schedule With Non-Unique Name
    Login as kapua-sys user and create a job with name job0.
    Add schedule1 to the created job. After the schedule1 is added
    successfully try to add a new schedule with the same name.
    A KapuaDuplicateNameException should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create a job with the name "job0"
    When I found trigger properties with name "Device Connect"
    And A regular trigger creator with the name "schedule1"
    And The trigger is set to start today at 10:00.
    And I create a new trigger from the existing creator with previously defined date properties
    And A regular trigger creator with the name "schedule1"
    And The trigger is set to start today at 12:00.
    When I expect the exception "KapuaDuplicateNameException" with the text "An entity with the same value for field already exists"
    And I create a new trigger from the existing creator with previously defined date properties
    Then An exception was thrown
    And I logout

  Scenario: Adding "Device Connect" Schedule With Max Length Name
    Login as kapua-sys user and create a job with name job0.
    Add a new schedule with 255 character long name.
    As this is the defined limit for the max number of characters
    for the schedule name, no errors should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create a job with the name "job0"
    When I found trigger properties with name "Device Connect"
    And A regular trigger creator with the name "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww"
    And The trigger is set to start today at 10:00.
    And I create a new trigger from the existing creator with previously defined date properties
    And I logout

  Scenario: Adding "Device Connect" Schedule With Min Length Name
    Login as kapua-sys user and create a job with name job0.
    Add a new schedule with one character long name.
    As this is the defined limit for the min number of characters
    for the schedule name, no errors should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create a job with the name "job0"
    When I found trigger properties with name "Device Connect"
    And A regular trigger creator with the name "w"
    And The trigger is set to start today at 10:00.
    And I create a new trigger from the existing creator with previously defined date properties
    And I logout

    # *****************************
    # * Schedule Start Date Tests *
    # *****************************

  Scenario: Adding "Device Connect" Schedule Without The Start Date Parameter
    Login as kapua-sys user and create a job with name job0.
    Try to create a new schedule0 without passing the value for start date property.
    A KapuaIllegalNullArgumentException should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create a job with the name "job0"
    When I found trigger properties with name "Device Connect"
    And A regular trigger creator with the name "schedule0"
    Then I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I create a new trigger from the existing creator with previously defined date properties
    And An exception was thrown
    And I logout

  Scenario: Adding "Device Connect" Schedule With Non-Unique Start Date Parameter
    Login as kapua-sys user and create a job with name job0.
    Create a new schedule0 with a specific start date parameter.
    Try to create a new schedule1 with the same start date parameter.
    No errors should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create a job with the name "job0"
    When I found trigger properties with name "Device Connect"
    And A regular trigger creator with the name "schedule0"
    And The trigger is set to start on 12-12-2020 at 10:10.
    And I create a new trigger from the existing creator with previously defined date properties
    And A regular trigger creator with the name "schedule1"
    And The trigger is set to start on 12-12-2020 at 10:10.
    And I create a new trigger from the existing creator with previously defined date properties
    And I logout

  Scenario: Adding "Device Connect" Schedule With Start Date Only
    Login as kapua-sys user and create a job with name job0.
    Try to create a new schedule with a defined start date but
    without setting the name parameter.
    A KapuaIllegalNullArgumentException should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create a job with the name "job0"
    When I found trigger properties with name "Device Connect"
    And A trigger creator without a name
    And The trigger is set to start on 12-12-2020 at 10:10.
    Then I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I create a new trigger from the existing creator with previously defined date properties
    And An exception was thrown
    And I logout

    # ***************************
    # * Schedule End Date Tests *
    # ***************************

  Scenario: Adding "Device Connect" Schedule With Non-Unique End Date Parameter
    Login as kapua-sys user and create a job with name job0.
    Create a new schedule0 with specific start and end date parameters.
    Try to create a new schedule1 with the same end date parameter.
    No errors should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create a job with the name "job0"
    When I found trigger properties with name "Device Connect"
    Then A regular trigger creator with the name "schedule0"
    And The trigger is set to start on 12-01-2020 at 10:10.
    And The trigger is set to end on 15-12-2020 at 10:10.
    Then I create a new trigger from the existing creator with previously defined date properties
    And A regular trigger creator with the name "schedule1"
    And The trigger is set to start on 12-12-2020 at 10:10.
    And The trigger is set to end on 15-12-2020 at 10:10.
    And I create a new trigger from the existing creator with previously defined date properties
    And I logout

  Scenario: Adding "Device Connect" Schedule With End Date Only
    Login as kapua-sys user and create a job with name job0.
    Try to create a schedule without defining any other property except the end date.
    A KapuaIllegalNullArgumentException should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create a job with the name "job0"
    When I found trigger properties with name "Device Connect"
    And A trigger creator without a name
    And The trigger is set to end on 12-12-2020 at 10:10.
    Then I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I create a new trigger from the existing creator with previously defined date properties
    And An exception was thrown
    And I logout

    # *************************************************
    # * Schedule Start And Stop Date Comparison Tests *
    # *************************************************

  Scenario: Adding "Device Connect" Schedule With End Time before Start time
    Login as kapua-sys user and create a job with name job0.
    Try to create a new schedule0 with start date later than the end date.
    A KapuaEndBeforeStartTimeException should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create a job with the name "job0"
    When I found trigger properties with name "Device Connect"
    And A regular trigger creator with the name "schedule0"
    And The trigger is set to start on 12-12-2020 at 10:00.
    And The trigger is set to end on 10-12-2020 at 10:00.
    When I expect the exception "KapuaEndBeforeStartTimeException" with the text "The start time cannot be later than the end time."
    And I create a new trigger from the existing creator with previously defined date properties
    And An exception was thrown
    And I logout

  Scenario: Adding "Device Connect" Schedule With the same Start and End time
    Login as kapua-sys user and create a job with name job0.
    Try to create a new schedule0 with start date the same as the end date.
    A KapuaException should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create a job with the name "job0"
    When I found trigger properties with name "Device Connect"
    And A regular trigger creator with the name "schedule0"
    And The trigger is set to start on 12-12-2020 at 10:00.
    And The trigger is set to end on 12-12-2020 at 10:00.
    When I expect the exception "KapuaException" with the text "Start and end time cannot be at the same point in time"
    And I create a new trigger from the existing creator with previously defined date properties
    And  An exception was thrown
    And I logout
