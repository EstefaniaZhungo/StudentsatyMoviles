package com.tefa.studentstayo.model;

public interface EncabezadoCallBack {
    void onReservaObtenido(long idReserva);
    void onError(String errorMessage);
}
