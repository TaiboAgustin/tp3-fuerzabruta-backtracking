package persistencia;

public class IncompatibilidadDato {
    private String __primeraNombre;
    private String __segundoNombre;

    protected IncompatibilidadDato(){
        
    }
    protected String ElegirPrimeraPersona(){
        return __primeraNombre;
    }
    protected String ElegirSegundaPersona(){
        return __segundoNombre;
    }
    protected void ingresarPrimeraNombre(String nombre){
        this.__primeraNombre=nombre;
    }
    protected void ingresarSegundoNombre(String nombre){
        this.__segundoNombre=nombre;
    }
}
