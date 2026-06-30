package persistencia;

public class IncompatibilidadDato {
    private String nombrePersona1;
    private String nombrePersona2;

    protected IncompatibilidadDato(){
        
    }
    protected String getPrimeraPersona(){
        return nombrePersona1;
    }
    protected String getSegundaPersona(){
        return nombrePersona2;
    }
    protected void setPrimeraNombre(String nombre){
        this.nombrePersona1=nombre;
    }
    protected void setSegundoNombre(String nombre){
        this.nombrePersona2=nombre;
    }
}
    