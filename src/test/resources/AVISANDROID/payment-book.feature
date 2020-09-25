Feature: Check case payment by all payment method
  As a operator,
  I wan to check payment method of booking.

  Background:
    And an api token after login command center

  Scenario: 01. Check case payment method CASH of book
    Given Waiting open app success
    And Touch book "Now" button
    And Select payment method "Cash"
    And Touch destination address and input data
      | mode   | address            |
      | google | 21 phan chau trinh |
    And Touch request book button
    When I want to get info payment method from CUE
    Then I should get the response payment method message matches with
      | paymentType |
      | 1           |

  Scenario: 02. Check case payment method PERSONAL CARD of book
    Given Waiting open app success
    And Touch book "Now" button
    And Select payment method "Cash"
    And Touch destination address and input data
      | mode   | address            |
      | google | 21 phan chau trinh |
    And Touch request book button
    When I want to get info payment method from CUE
    Then I should get the response payment method message matches with
      | paymentType |
      | 2           |

  Scenario: 03. Check case payment type CASH of book request from corp traveler
    Given Waiting open app success
    And Select type of ride "Personal"
    And Touch book "Now" button
    And Select payment method "Cash"
    And Touch destination address and input data
      | mode   | address            |
      | google | 21 phan chau trinh |
    And Touch request book button
    When I want to get info payment method from CUE
    Then I should get the response payment method message matches with
      | paymentType |
      | 1           |

  Scenario: 04. Check case payment type PERSONAL CARD of book request from corp traveler
    Given Waiting open app success
    And Select type of ride "Personal"
    And Touch book "Now" button
    And Select payment method "Personal card"
    And Touch destination address and input data
      | mode   | address            |
      | google | 21 phan chau trinh |
    And Touch request book button
    When I want to get info payment method from CUE
    Then I should get the response payment method message matches with
      | paymentType |
      | 2           |

  Scenario: 05. Check case payment type DIRECT INVOICING of book request from corp traveler
    Given Waiting open app success
    And Select type of ride "Business"
    And Touch book "Now" button
    And Select payment method "Direct invoicing"
    And Touch destination address and input data
      | mode   | address            |
      | google | 21 phan chau trinh |
    And Touch request book button
    When I want to get info payment method from CUE
    Then I should get the response payment method message matches with
      | paymentType |
      | 5           |

  Scenario: 06. Check case payment type CORPORATE PREPAID of book request from corp traveler
    Given Waiting open app success
    And Select type of ride "Business"
    And Touch book "Now" button
    And Select payment method "Corporate Prepaid"
    And Touch destination address and input data
      | mode   | address            |
      | google | 21 phan chau trinh |
    And Touch request book button
    When I want to get info payment method from CUE
    Then I should get the response payment method message matches with
      | paymentType |
      | 7           |

  Scenario: 07. Check case payment type CORPORATE CARD of book request from corp traveler
    Given Waiting open app success
    And Select type of ride "Business"
    And Touch book "Now" button
    And Select payment method "Corporate card"
    And Touch destination address and input data
      | mode   | address            |
      | google | 21 phan chau trinh |
    And Touch request book button
    When I want to get info payment method from CUE
    Then I should get the response payment method message matches with
      | paymentType |
      | 4           |
