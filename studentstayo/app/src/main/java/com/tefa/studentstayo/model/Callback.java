package com.tefa.studentstayo.model;

public interface Callback {
    void onEncabezadoObtenido(long idEncabezado);
    void onError(String errorMessage);
}
