/**
 *
 */
package com.lemur.GLOBAL.model;

/**
 *
 */
public class Usuario
{
    private String nombreUsuario;
    private Boolean esSecretaria;
    private Boolean esMaterial;

    public Usuario(String nombreUsuario, boolean esSecretaria, boolean esMaterial)
    {
        this.nombreUsuario = nombreUsuario;
        this.esSecretaria = esSecretaria;
        this.esMaterial = esMaterial;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public Boolean getEsSecretaria() {
        return esSecretaria;
    }

    public Boolean getEsMaterial() {
        return esMaterial;
    }
}
