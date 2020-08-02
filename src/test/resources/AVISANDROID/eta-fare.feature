Feature: Check case ETA request book from Pax
  As a operator
  I wan to request book from Pax, check ETA fare in CUE

  Background:
    Given Waiting open app success

  Scenario: 01. Check ETA fare request from Pax app
    Given Touch pickup address and input data
      | address    |
      | Hoang dieu |
    And Touch book "Now" button
    And Touch destination address and input data
      | address    |
      | Hoang dieu |
