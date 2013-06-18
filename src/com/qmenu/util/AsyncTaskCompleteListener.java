package com.qmenu.util;

public interface AsyncTaskCompleteListener<T> {
   public void onTaskComplete(String metodo, int transacao, T result);
}
