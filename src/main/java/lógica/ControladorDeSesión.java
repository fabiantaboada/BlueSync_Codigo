package lógica;

import persistencia.UsuarioDAO;

public class ControladorDeSesión {
    
    private UsuarioDAO usuarioDAO;

    public ControladorDeSesión() {
        this.usuarioDAO = new UsuarioDAO();
    }

    // Retorna un arreglo con [Rol, NombreReal] si es exitoso, o null si falla
    public String[] validarEIniciarSesión(String usuario, String clave) {
        String[] datosDeUsuario = usuarioDAO.validarInicioDeSesión(usuario, clave);
        
        if (datosDeUsuario != null) {
            String rolEncontrado = datosDeUsuario[0];
            String nombreEncontrado = datosDeUsuario[1];
            
            // Guardamos la sesión en memoria
            GestorDeSesión.iniciarSesión(usuario, rolEncontrado, nombreEncontrado);
            
            return datosDeUsuario;
        }
        
        return null;
    }
}