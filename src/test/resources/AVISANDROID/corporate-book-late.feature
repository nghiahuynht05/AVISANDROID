Feature: Check info book request type corporate
  As a operator,
  I wan to check info corporate book.

  Background:
    And an api token after login command center

  Scenario: 01. Check case payment method CASH of book
    Given Waiting open app success
    And Touch book "Reservation" button
    And Set time pickup before "2" hours and "1" minustes from the current time
    And Select payment method "Cash"
    And Touch destination address and input data
      | mode   | address            |
      | google | 21 phan chau trinh |
    And Touch request book button
    When I want to get info payment method from CUE
    Then I should get the response payment method message matches with
      | paymentType |
      | 1           |
#
#  Scenario: 02. Check case payment method PERSONAL CARD of book
#    Given Waiting open app success
#    And Touch book "Now" button
#    And Select payment method "Cash"
#    And Touch destination address and input data
#      | mode   | address            |
#      | google | 21 phan chau trinh |
#    And Touch request book button
#    When I want to get info payment method from CUE
#    Then I should get the response payment method message matches with
#      | paymentType |
#      | 2           |
#
#  Scenario: 03. Check case payment type CASH of book request from corp traveler
#    Given Waiting open app success
#    And Select type of ride "Personal"
#    And Touch book "Now" button
#    And Select payment method "Cash"
#    And Touch destination address and input data
#      | mode   | address            |
#      | google | 21 phan chau trinh |
#    And Touch request book button
#    When I want to get info payment method from CUE
#    Then I should get the response payment method message matches with
#      | paymentType |
#      | 1           |
