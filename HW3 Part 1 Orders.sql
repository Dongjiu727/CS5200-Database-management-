-- HW3 Part1 Orders
-- Create Schema for HW_03
DROP DATABASE IF EXISTS HW3_Orders;
CREATE DATABASE HW3_Orders;
USE HW3_Orders;

-- Create tables in the HW3_Orders database
CREATE TABLE HW3_Orders.Item 
    (
    itemNumber INT AUTO_INCREMENT NOT NULL,
    description VARCHAR(255),
    unitCost DECIMAL(15,3) NOT NULL,
    taxable BOOLEAN NOT NULL,
    CONSTRAINT pk_Item_itemNumber PRIMARY KEY (itemNumber)
    );
    
CREATE TABLE HW3_Orders.StateCodes
    (
    stateCode VARCHAR(45) NOT NULL,
    CONSTRAINT pk_StateCodes_stateCode PRIMARY KEY (stateCode)
    );
    
CREATE TABLE HW3_Orders.Customer
    (
    customerID INT AUTO_INCREMENT,
    name VARCHAR(45) NOT NULL,
    streetAddress VARCHAR(255),
    city VARCHAR(40),
    stateCode VARCHAR(45),
    zip VARCHAR(10) NOT NULL,
    CONSTRAINT pk_Customer_customerID PRIMARY KEY (customerID),
    CONSTRAINT fk_Customer_stateCode FOREIGN KEY (stateCode)
        REFERENCES StateCodes(stateCode)
        ON UPDATE CASCADE
        ON DELETE CASCADE
	);

CREATE TABLE HW3_Orders.Order
    (
    orderNumber INT AUTO_INCREMENT NOT NULL,
    customerID INT,
    orderDate DATE NOT NULL,
    customerMessage VARCHAR(255),
    taxRate DECIMAL(10,4) NOT NULL,
    totalCost DECIMAL(15,3) NOT NULL,
    CONSTRAINT pk_Order_orderNumber PRIMARY KEY (orderNumber),
    CONSTRAINT fk_Order_customerID FOREIGN KEY (customerID)
        REFERENCES Customer(customerID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
	);
    
CREATE TABLE HW3_Orders.OrderItem
    (
    orderNumber INT NOT NULL,
    itemNumber INT NOT NULL,
    quantity INT NOT NULL,
    totalCost DECIMAL(15,3) NOT NULL,
    CONSTRAINT pf_OrderItem_orderNumber_itemNumber PRIMARY KEY (orderNumber,itemNumber),
    CONSTRAINT kf_OrderItem_orderNumber FOREIGN KEY (orderNumber)
       REFERENCES `Order`(orderNumber)
       ON UPDATE CASCADE
       ON DELETE CASCADE,
	CONSTRAINT kf_OrderItem_itemNumber FOREIGN KEY (itemNumber)
       REFERENCES Item(itemNumber)
       ON UPDATE CASCADE
	   ON DELETE CASCADE
	);
    
    
USE HW3_Orders; 
-- Inset some data into the database for testing
-- Sample data for StateCodes
INSERT INTO HW3_Orders.StateCodes (stateCode) VALUES ('CA');
INSERT INTO HW3_Orders.StateCodes (stateCode) VALUES ('NY');
INSERT INTO HW3_Orders.StateCodes (stateCode) VALUES ('TX');
INSERT INTO HW3_Orders.StateCodes (stateCode) VALUES ('CT');

-- Sample data for Customer
INSERT INTO HW3_Orders.Customer (name, streetAddress, city, stateCode, zip) VALUES ('Robert Mitchell', '1234 Main St', 'Bigtown', 'CT', '51238');
INSERT INTO HW3_Orders.Customer (name, streetAddress, city, stateCode, zip) VALUES ('Alice Johnson', '456 Elm St', 'Sometown', 'CA', '90210');
INSERT INTO HW3_Orders.Customer (name, streetAddress, city, stateCode, zip) VALUES ('John Smith', '789 Oak St', 'Othertown', 'NY', '10001');
-- Sample data for Item
INSERT INTO HW3_Orders.Item (description, unitCost, taxable) VALUES ('Widget A', 19.95, 1);
INSERT INTO HW3_Orders.Item (description, unitCost, taxable) VALUES ('Widget B', 24.99, 1);
INSERT INTO HW3_Orders.Item (description, unitCost, taxable) VALUES ('Gadget C', 39.99, 0);

-- Sample data for Order
INSERT INTO HW3_Orders.Order (customerID, orderDate, customerMessage, taxRate, totalCost) VALUES (1, '2023-10-15', 'Urgent delivery', 0.0825, 50.00);
INSERT INTO HW3_Orders.Order (customerID, orderDate, customerMessage, taxRate, totalCost) VALUES (2, '2023-10-16', 'Standard delivery', 0.0825, 49.98);
INSERT INTO HW3_Orders.Order (customerID, orderDate, customerMessage, taxRate, totalCost) VALUES (3, '2023-10-17', 'Next-day delivery', 0.0825, 65.00);

-- Sample data for OrderItem
INSERT INTO HW3_Orders.OrderItem (orderNumber, itemNumber, quantity, totalCost) VALUES (1, 1, 2, 39.90);
INSERT INTO HW3_Orders.OrderItem (orderNumber, itemNumber, quantity, totalCost) VALUES (1, 2, 1, 24.99);
INSERT INTO HW3_Orders.OrderItem (orderNumber, itemNumber, quantity, totalCost) VALUES (2, 1, 3, 59.85);
INSERT INTO HW3_Orders.OrderItem (orderNumber, itemNumber, quantity, totalCost) VALUES (3, 3, 2, 79.98);
INSERT INTO HW3_Orders.OrderItem (orderNumber, itemNumber, quantity, totalCost) VALUES (3, 1, 1, 19.95);

-- Q1
-- Insert the stateCode record into Table StateCodes
INSERT INTO HW3_Orders.StateCodes (stateCode) VALUES ('IL');

-- Insert the customer data in to the table Customer
INSERT INTO  HW3_Orders.Customer (name,streetAddress,city,stateCode,zip)
    VALUES ('Robert Mitchell', '123 Main St', 'Anytown', 'IL', '51236');

-- Q2
SELECT o.orderNumber as `order Number`, c.name as `customer name`, c.city, c.stateCode, c.zip 
FROM `Order` as o
JOIN Customer as c
ON o.customerID = c.customerID
ORDER BY O.orderDate ASC;

-- Q3
SELECT c.customerID, c.name as `customer name`, SUM(o1.totalCost) as `total amount`
FROM Customer as c
LEFT JOIN `Order` as o1
ON c.customerID = o1.customerID
GROUP BY c.customerID,c.name;
-- Here, I am selecting all the customers, regardless of whether they have made a purchase or not.

-- Q4
SELECT i.itemNumber, COUNT(DISTINCT c.customerID) as `number of customer`
FROM Item as i
LEFT JOIN orderItem as o1
ON i.itemNumber = o1.itemNumber 
LEFT JOIN `Order` as o2
ON o1.orderNumber = o2.orderNumber
LEFT JOIN Customer as c
ON o2.customerID = c.customerID
GROUP BY i.itemNumber
HAVING `number of customer` > 0;

-- Q5
SELECT c.zip as `zipcode`, c.stateCode as `state`, SUM(o1.totalCost) as `total order value`
FROM Customer as c
JOIN `Order` as o1
ON c.customerID = o1.customerID
GROUP BY c.zip,c.stateCode
WITH ROLLUP
HAVING `total order value` IS NOT NULL;    