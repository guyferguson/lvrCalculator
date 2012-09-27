LVR Calculator

A tool to calculate LVRs on a set of loans and collaterals. Mainly for financial institutions who do home loans (although it could apply to any security type)

*****  Author: Guy Ferguson
*****  Version 1.0
*****  Date: July 2012
*****  contact:  guyferguson@tpg.com.au

An LVR represents the Loan-to-Valuation Ration of a loan.  It is one indicator of risk to the lender. If a property is valued at $500,000 and you are lending $400,000 towards the purchase of that property, your LVR is 400,000 / 500,000, or 80%.

A finance institution will often want to know all their current LVRs to see teh spread of risk, and to calculate a weighted average LVR, particularly useful if wanting to wrap up a set of loans into one package for sale.

LVR calculation can get tricky when one borrower may have several loans and several properties, with different mixes of properties to each loan.

This lvrCalculator algorithm works on a 'simplest-to-complex' method. If one member has the following loans:
	
	Loan A		Collateral A


	Loan B		Collateral A
			Collateral B


	Loan C		Collateral A
			Collateral B
			Collateral C


Then this algorithm will calculate an LVR for Loan A first, just Loan A/Collateral A.

It will then move on to Loan B, and deduct the 'used' value of Loan A from (Collateral A + Collateral B), as we adopt the philosophy that if we disposed of Collateral A, Loan A would first be paid out, and Collateral A would then only have the value of (old_Collateral A - Loan A)

When we get to Loan C, the denominator is (Collateral A + COllateral B + Collateral C - Loan A - Loan B)

****

This program takes two text files - one is a list of loan accounts, the other is a list of collaterals.  You would need to modify LoanAccountReader and CollateralReader to handle your data types.

*****
OUTPUT
After some initial totalling, then a listing of each collateral and which loans it is attached to (this is really debug info of a sort and can be suppressed once you are happy with the formats) - after that, you get the account number comma separated from the calculated LVR.  I chose that format for easy importation to Excel for further manipulation and sorting.

