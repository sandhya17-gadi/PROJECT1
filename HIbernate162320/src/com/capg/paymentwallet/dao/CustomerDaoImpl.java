package com.capg.paymentwallet.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.LoggingPermission;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.capg.paymentwallet.bean.CustomerBean;
import com.capg.paymentwallet.bean.DepositBean;
import com.capg.paymentwallet.bean.FundTransferBean;
import com.capg.paymentwallet.bean.WalletTransaction;
import com.capg.paymentwallet.bean.WithdrawBean;
import com.capg.paymentwallet.exception.CustomerException;
import com.capg.paymentwallet.exception.CustomerExceptionMessage;
import com.capg.paymentwallet.service.CustomerServiceImpl;

public class CustomerDaoImpl implements ICustomerDao {

	//private static ArrayList<CustomerBean> list1 = new ArrayList<CustomerBean>();
	double amt;
	double amt1;
	double amt2;
	
	
	/*private static ArrayList<String> printList1 = new ArrayList<String>();
	CustomerDataBase customerDatabase = new CustomerDataBase();
	CustomerBean customerbean = new CustomerBean();*/

	@Override
	public boolean createAccount(CustomerBean customerbean)
			throws CustomerException {
       try
       {

		 EntityManagerFactory factory=Persistence.createEntityManagerFactory("JPA-PU");
	     EntityManager em=factory.createEntityManager();
	     em.getTransaction().begin();
	     em.persist(customerbean);
	     em.getTransaction().commit();
	     em.close();
	     factory.close();
	     return true;
       }
       catch(Exception e)
       {
    	   return false;
       }
	}

	@Override
	public double showBalance(BigInteger phoneNo) {
		
		double amt=0.0;

		
		
		try
	       {

			 EntityManagerFactory factory=Persistence.createEntityManagerFactory("JPA-PU");
		     EntityManager em=factory.createEntityManager();
		    CustomerBean bean= em.find(CustomerBean.class, phoneNo);
		    amt=bean.getBalance();
		     em.close();
		     factory.close();
		     return amt;
	       }
		   catch(Exception e)
		{
			   return amt;
		}
		
	}

	@Override
	public double deposit(BigInteger phoneNo, double rupees) {

		double amt = 0.0;
		WalletTransaction transaction=new WalletTransaction();
		try {

			EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA-PU");
			EntityManager em = factory.createEntityManager();
			CustomerBean bean = em.find(CustomerBean.class, phoneNo);
			amt = bean.getBalance()+rupees;
			transaction.setTransactionAmt(rupees);
			transaction.setPhone(phoneNo);
			transaction.setTransactionDate(new Date());
			bean.addTransaction(transaction);
			bean.setBalance(amt);
			//System.out.println(bean);
			em.getTransaction().begin();
			em.merge(bean);
			em.getTransaction().commit();
			em.close();
			
			factory.close();
			return amt;
		} catch (Exception e) {
			return amt;
		}
		
		
		
	}

	@Override
	public double withdraw(BigInteger phoneNo, double rupees) {
		
		double amt = 0.0;
		
		WalletTransaction transaction=new WalletTransaction();
        try {

			EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA-PU");
			EntityManager em = factory.createEntityManager();
			CustomerBean bean = em.find(CustomerBean.class, phoneNo);
			if(bean.getBalance()>500)
				{
				   amt = bean.getBalance()-rupees; 
				   
				   transaction.setTransactionAmt(rupees);
					transaction.setPhone(phoneNo);
					transaction.setTransactionDate(new Date());
					bean.addTransaction(transaction);
					
				   bean.setBalance(amt);
				   em.getTransaction().begin();
				   em.merge(bean);
				   em.getTransaction().commit();
				}
			else
			{
				System.out.println("insufficient funds...");
			}
			
			em.close();
			factory.close();
			return amt;
		} catch (Exception e) {
			return amt;
		}
		

	}

	@Override
	public double fundTransfer(BigInteger phoneNo, double balance, BigInteger anotherPhnNum) {
		// TODO Auto-generated method stub
		double amt=0.0;
		double amt1=0.0;
		boolean isValid=false;
		try {

			EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA-PU");
			EntityManager em = factory.createEntityManager();
			CustomerBean bean = em.find(CustomerBean.class, phoneNo);
			CustomerBean bean1=em.find(CustomerBean.class,anotherPhnNum);
			if(bean.getBalance()>500)
				{
				    isValid=true;
				    amt=withdraw(phoneNo, balance);
				    System.out.println("in fund with..." +amt);
				    em.getTransaction().begin();
				    em.merge(bean);
				    em.getTransaction().commit();
				    if(isValid)
				    {
				    	amt1=deposit(anotherPhnNum,balance);
				    	em.getTransaction().begin();
				    	em.merge(bean1);
				    	em.getTransaction().commit();
				    }
				    return amt;
				    
				}
			else
			{
				System.out.println("inSufficient balance...");
			}
		}
		catch(Exception e)
		{
			return amt;
		}
		return 0;
	}

	@Override
	public ArrayList<String> addTransaction(double amount, String msg, LocalDate date, LocalTime time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerBean printTransaction(BigInteger phno) {
		// TODO Auto-generated method stub
		CustomerBean bean=null;
		try{
			
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA-PU");
			EntityManager em = factory.createEntityManager();
			 bean = em.find(CustomerBean.class, phno);
			
			
			return bean;
			
		}catch(Exception e){
			return bean;
		}
		
	}

	/*@Override
	public double fundTransfer(BigInteger phoneNum, double balance,
			BigInteger anotherphoneNum) {
          double amt2=0;
		FundTransferBean fundTransferBean = new FundTransferBean();

		boolean isAdded = false;
		if (phoneNum.equals(customerDatabase.getCustomerList().get(0).getPhoneNo())) {

			fundTransferBean.setFundtransferAmt(balance);
			double amount = withdraw(phoneNum, balance);
			customerDatabase.getCustomerList().get(0).setBalance(amount);
			amt2 = customerDatabase.getCustomerList().get(1).getBalance()+ balance;
			
               

			

		}
		//System.out.println("fundTransfer"+ customerDatabase.getCustomerList().get(0).getBalance());
		//return customerDatabase.getCustomerList().get(0).getBalance();
		return amt2;

	}

	@Override
	public ArrayList<String> addTransaction(double amount, String msg,
			LocalDate date, LocalTime time) {
		// TODO Auto-generated method stub

		
		printTransaction();
		return printList1;
  
	}
	//CustomerDaoImpl
	

	@Override
	public   ArrayList<String> printTransaction() {

		Iterator<String> iterator = printList1.iterator();
		// System.out.println("Date       " + "Time        " + "Amount   "
		// + " Operation   ");
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		System.out.println(printList1);
		return printList1;
	}
*/
}
