Feature: Check case ETA request book from Pax
  As a operator
  I wan to request book from Pax, check ETA fare in CUE

  Background:
    Given Waiting open app success
    And an api token after login command center

  Scenario: 01. Check ETA fare request from Pax app
    Given Touch pickup address and input data
      | address       |
      | 21 hoang dieu |
    And Touch book "Now" button
    And Touch destination address and input data
      | address            |
      | 21 phan chau trinh |

    And Touch request book button
    When I want to get info ETA from CUE
    Then I should get the response ETA message matches with
      | distance | etaFare | time |
      |          |         |      |