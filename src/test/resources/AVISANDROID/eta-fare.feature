Feature: Check case ETA request book from Pax
  As a operator
  I wan to request book from Pax, check ETA fare in CUE

  Background:
    And an api token after login command center

#  Scenario: 01. Check ETA fare request from Pax app
#    Given Waiting open app success
#    Given Touch pickup address and input data
#      | mode   | address       |
#      | google | 21 hoang dieu |
#    And Touch book "Now" button
#    And Touch destination address and input data
#      | mode   | address            |
#      | google | 21 phan chau trinh |
#    And Touch request book button
#    When I want to get info ETA from CUE
#    Then I should get the response ETA message matches with
#      | distance | time | etaFare | typeRate | type |
#      | 0.9 公里   | 3分钟  | 66.65   | 0        | 0    |

#  Scenario: 02. Check ETA fare and service type hourly
#    Given Waiting open app success
#    Given Touch pickup address and input data
#      | mode   | address       |
#      | google | 21 hoang dieu |
#    And Touch book "Now" button
#    And Touch destination address and input data
#      | mode   | address            |
#      | google | 21 phan chau trinh |
#    And Select service type "Hourly"
#    And Touch request book button
#    When I want to get info ETA from CUE
#    Then I should get the response ETA message matches with
#      | distance | time | etaFare | typeRate | type |
#      | 0.9 公里   | 3分钟  | 50      | 1        | 3    |
#
#  Scenario: 03. Check request booking basic fare 0
#    Given Waiting open app success
#    Given Touch pickup address and input data
#      | mode   | address       |
#      | google | 21 hoang dieu |
#    And Select car type "Premium"
#    And Touch book "Now" button
#    And Touch destination address and input data
#      | mode   | address            |
#      | google | 21 phan chau trinh |
#    And Touch request book button
#    When I want to get content message
#    Then I should get the response message matches with
#      | res                                                                                                                                                      |
#      | We are unable to process your request, please change car type or booking type and try again. If you need any assistance, please contact our support team |

  Scenario: 04. Check ETA fare request from airport from Pax app
    Given Waiting open app success
    Given Touch pickup address and input data
      | mode   | address         |
      | google | sân bay đà nẵng |
    And Touch book "Now" button
    And Touch destination address and input data
      | mode   | address       |
      | google | 21 hoang dieu |
    And Touch request book button
    When I want to get info ETA from CUE
    Then I should get the response ETA message matches with
      | typeRate | type |
      | 0        | 1    |

  Scenario: 05. Check ETA fare request to airport from Pax app
    Given Waiting open app success
    Given Touch pickup address and input data
      | mode   | address       |
      | google | 21 hoang dieu |
    And Touch book "Now" button
    And Touch destination address and input data
      | mode   | address         |
      | google | sân bay đà nẵng |
    And Touch request book button
    When I want to get info ETA from CUE
    Then I should get the response ETA message matches with
      | typeRate | type |
      | 0        | 2    |