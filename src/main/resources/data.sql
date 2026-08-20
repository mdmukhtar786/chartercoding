-- =============================================
-- Seed Customers
-- =============================================
INSERT INTO customer (name, email) VALUES ('Alice Johnson',  'alice@example.com');
INSERT INTO customer (name, email) VALUES ('Bob Martinez',   'bob@example.com');
INSERT INTO customer (name, email) VALUES ('Carol Williams', 'carol@example.com');
INSERT INTO customer (name, email) VALUES ('David Lee',      'david@example.com');

-- =============================================
-- Seed Transactions  (3-month window)
-- Points formula:
--   2 pts per $ over $100
--   1 pt  per $ between $50 and $100
-- =============================================

-- Alice Johnson (id=1)
-- Jan: $120 => 2*20 + 1*50 = 90 pts  |  $85 => 1*35 = 35 pts  => Jan total = 125
-- Feb: $200 => 2*100 + 1*50 = 250 pts | $45  => 0 pts           => Feb total = 250
-- Mar: $110 => 2*10 + 1*50 = 70 pts   | $60  => 1*10 = 10 pts  => Mar total = 80
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (1, 120.00, '2024-01-05');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (1,  85.00, '2024-01-20');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (1, 200.00, '2024-02-10');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (1,  45.00, '2024-02-25');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (1, 110.00, '2024-03-08');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (1,  60.00, '2024-03-22');

-- Bob Martinez (id=2)
-- Jan: $50  => 0 pts           | $75  => 1*25 = 25 pts  => Jan total = 25
-- Feb: $130 => 2*30+1*50=110   | $90  => 1*40 = 40 pts  => Feb total = 150
-- Mar: $180 => 2*80+1*50=210   | $30  => 0 pts           => Mar total = 210
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (2,  50.00, '2024-01-11');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (2,  75.00, '2024-01-28');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (2, 130.00, '2024-02-14');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (2,  90.00, '2024-02-20');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (2, 180.00, '2024-03-05');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (2,  30.00, '2024-03-19');

-- Carol Williams (id=3)
-- Jan: $250 => 2*150+1*50=350  | $100 => 1*50=50 pts    => Jan total = 400
-- Feb: $55  => 1*5=5 pts        | $70  => 1*20=20 pts   => Feb total = 25
-- Mar: $300 => 2*200+1*50=450  | $80  => 1*30=30 pts    => Mar total = 480
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (3, 250.00, '2024-01-03');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (3, 100.00, '2024-01-17');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (3,  55.00, '2024-02-09');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (3,  70.00, '2024-02-23');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (3, 300.00, '2024-03-12');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (3,  80.00, '2024-03-27');

-- David Lee (id=4)
-- Jan: $40  => 0 pts           | $95  => 1*45=45 pts    => Jan total = 45
-- Feb: $160 => 2*60+1*50=170   | $110 => 2*10+1*50=70  => Feb total = 240
-- Mar: $75  => 1*25=25 pts     | $220 => 2*120+1*50=290 => Mar total = 315
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (4,  40.00, '2024-01-07');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (4,  95.00, '2024-01-30');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (4, 160.00, '2024-02-06');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (4, 110.00, '2024-02-18');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (4,  75.00, '2024-03-14');
INSERT INTO transaction (customer_id, amount, transaction_date) VALUES (4, 220.00, '2024-03-29');
