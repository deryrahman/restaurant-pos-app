package com.blibli.future.pos.restaurant.common;

public class TransactionHandler {
    public static Object runTransaction(Transaction transaction) throws Exception {
        TransactionHelper.init();
        try {
            System.out.println("begin transaction");
            Object result = transaction.execute(TransactionHelper.getConnection());

            System.out.println("commit transaction");
            TransactionHelper.commit();
            return result;
        } catch (Exception e){
            System.out.println("rolling back...");
            TransactionHelper.rollback();
            throw e;
        } finally {
            TransactionHelper.close();
        }
    }
}
