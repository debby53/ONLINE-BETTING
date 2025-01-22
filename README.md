# Online Betting Management System

## Overview

The Online Betting Management System is designed to provide a secure, feature-rich platform for managing betting activities. 
It implements object-oriented principles like encapsulation, inheritance, and abstraction to ensure modularity and extensibility.
Users can create accounts, place bets, manage betting history, and for admins, manage games. 
The system includes robust validation and exception handling to enhance reliability and user experience.

## Features

## Core Functionalities

User Account Management: Users can create accounts, log in securely, and manage their account balance.

Betting: Regular users can place bets and view their betting history.

Game Management: Admin users can add and remove games from the system.

Auditing and Notifications: Admins can log actions and generate reports. Users receive notifications for successful bets or low balances.

## Key Highlights

Encapsulation: All sensitive attributes are private, and access is controlled through validation-enabled getter and setter methods.

Inheritance: Common attributes and methods for all users are implemented in the base class, User, and extended in RegularUser and AdminUser.

Abstraction/Interfaces: Interfaces (Auditable, Notifiable) are used for auditing and notification functionality.

Custom Exceptions: Ensures meaningful error handling for scenarios like insufficient balance or invalid inputs.
