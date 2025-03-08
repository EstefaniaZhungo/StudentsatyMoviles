package com.tefa.studentstayo.model;

public class Persona {

    private String cedula_persona;
    private String nombre;
    private String nombre2;
    private String apellido;
    private String apellido2;
    private String telefono;
    private String direccion;
    private int edad;
    private String genero;
    private String id_canton;
    // Nuevos atributos para contacto de emergencia
    private String nombreContactoEmergencia;
    private String telefonoContactoEmergencia;
    private String parentescoContactoEmergencia;

    // Nuevos atributos para contacto de emergencia #2
    private String nombreContactoEmergencia2;
    private String telefonoContactoEmergencia2;
    private String parentescoContactoEmergencia2;

    public Persona() {

    }

    public Persona(String cedula_persona, String nombre, String nombre2, String apellido, String apellido2, String telefono, String direccion, int edad, String genero, String id_canton, String nombreContactoEmergencia, String telefonoContactoEmergencia, String parentescoContactoEmergencia, String nombreContactoEmergencia2, String telefonoContactoEmergencia2, String parentescoContactoEmergencia2) {
        this.cedula_persona = cedula_persona;
        this.nombre = nombre;
        this.nombre2 = nombre2;
        this.apellido = apellido;
        this.apellido2 = apellido2;
        this.telefono = telefono;
        this.direccion = direccion;
        this.edad = edad;
        this.genero = genero;
        this.id_canton = id_canton;
        this.nombreContactoEmergencia = nombreContactoEmergencia;
        this.telefonoContactoEmergencia = telefonoContactoEmergencia;
        this.parentescoContactoEmergencia = parentescoContactoEmergencia;
        this.nombreContactoEmergencia2 = nombreContactoEmergencia2;
        this.telefonoContactoEmergencia2 = telefonoContactoEmergencia2;
        this.parentescoContactoEmergencia2 = parentescoContactoEmergencia2;
    }

    public String getCedula_persona() {
        return cedula_persona;
    }

    public void setCedula_persona(String cedula_persona) {
        this.cedula_persona = cedula_persona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getId_canton() {
        return id_canton;
    }

    public void setId_canton(String id_canton) {
        this.id_canton = id_canton;
    }

    public String getNombreContactoEmergencia() {
        return nombreContactoEmergencia;
    }

    public void setNombreContactoEmergencia(String nombreContactoEmergencia) {
        this.nombreContactoEmergencia = nombreContactoEmergencia;
    }

    public String getTelefonoContactoEmergencia() {
        return telefonoContactoEmergencia;
    }

    public void setTelefonoContactoEmergencia(String telefonoContactoEmergencia) {
        this.telefonoContactoEmergencia = telefonoContactoEmergencia;
    }

    public String getParentescoContactoEmergencia() {
        return parentescoContactoEmergencia;
    }

    public void setParentescoContactoEmergencia(String parentescoContactoEmergencia) {
        this.parentescoContactoEmergencia = parentescoContactoEmergencia;
    }

    public String getNombreContactoEmergencia2() {
        return nombreContactoEmergencia2;
    }

    public void setNombreContactoEmergencia2(String nombreContactoEmergencia2) {
        this.nombreContactoEmergencia2 = nombreContactoEmergencia2;
    }

    public String getTelefonoContactoEmergencia2() {
        return telefonoContactoEmergencia2;
    }

    public void setTelefonoContactoEmergencia2(String telefonoContactoEmergencia2) {
        this.telefonoContactoEmergencia2 = telefonoContactoEmergencia2;
    }

    public String getParentescoContactoEmergencia2() {
        return parentescoContactoEmergencia2;
    }

    public void setParentescoContactoEmergencia2(String parentescoContactoEmergencia2) {
        this.parentescoContactoEmergencia2 = parentescoContactoEmergencia2;
    }
}
