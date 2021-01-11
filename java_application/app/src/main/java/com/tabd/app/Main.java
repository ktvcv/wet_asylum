package com.tabd.app;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Map;
import java.sql.Struct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import oracle.jdbc.internal.OracleTypes;

public class Main
{
    public static void gestionarFamilias(Database db) throws SQLException
    {
        int option;
        
        do
        {
            System.out.println("=================================================================");
            System.out.println("=================================================================");
            
            System.out.println("Bienvenido a la gestión de las familias.");
            System.out.println("¿Que desea hacer?");

            System.out.println("1º) Mostrar todas las familias existentes");
            System.out.println("2º) Buscar una familia por su nombre");
            System.out.println("3º) Modificar el telefono de una familia existente");
            System.out.println("4º) Modificar el email de una familia existente");
            System.out.println("5º) Modificar la direccion de una familia existente");
            System.out.println("6º) Borrar una familia existente");
            System.out.println("7º) Crear una familia");
            System.out.print("Su elección (escriba '0' para salir) ==> ");
            
            try
            {
                Scanner s = new Scanner(System.in);
                option = s.nextInt();
            } catch(Exception e)
            {
                option = -1;
            }
            
            switch(option)
            {
                case 0: {} ; break;
                case 1: {
                    // 1º) Mostrar todas las familias existentes
                    
                    Family.printFamilys(
                        Family.listAllFamilys(db)
                    );
                }; break;
                case 2: {
                    // 2º) Buscar una familia por su nombre
                    
                    Scanner s = new Scanner(System.in);
                    System.out.print("Introduzca el nombre de la familia a buscar ==> ");
                    String line = s.nextLine();
                    
                    Family.printFamilys(
                        Family.searchFamilys(db, line, true)
                    );
                }; break;
                case 3: {
                    // 3º) Modificar el telefono de una familia existente
                    
                    Scanner s = new Scanner(System.in);
                    Scanner ss = new Scanner(System.in);
                    
                    System.out.print("Introduzca el id de la familia a modificar ==> ");
                    BigDecimal id = s.nextBigDecimal();
                    
                    System.out.print("Introduzca el nuevo telefono ==> ");
                    String phone = ss.nextLine();
                    
                    Family f = new Family(db, id);
                    
                    if(f.familyExists())
                    {
                        if(f.updatePhone(phone))
                        {
                            System.out.println("Familia actualizada correctamente.");
                        } else
                        {
                            System.err.println("Familia no actualizada. Vuelva a intentarlo.");
                        }
                    } else
                    {
                        System.err.println("La familia con el id " + id + " no existe. Por favor, pruebe otro id.");
                    }
                }; break;
                case 4: {
                    // 4º) Modificar el email de una familia existente
                    
                    Scanner s = new Scanner(System.in);
                    Scanner ss = new Scanner(System.in);
                    
                    System.out.print("Introduzca el id de la familia a modificar ==> ");
                    BigDecimal id = s.nextBigDecimal();
                    
                    System.out.print("Introduzca el nuevo email ==> ");
                    String email = ss.nextLine();
                    
                    Family f = new Family(db, id);
                    
                    if(f.familyExists())
                    {
                        if(f.updateEmail(email))
                        {
                            System.out.println("Familia actualizada correctamente.");
                        } else
                        {
                            System.err.println("Familia no actualizada. Vuelva a intentarlo.");
                        }
                    } else
                    {
                        System.err.println("La familia con el id " + id + " no existe. Por favor, pruebe otro id.");
                    }
                }; break;
                case 5: {
                    // 5º) Modificar la direccion de una familia existente
                    
                    Scanner s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el id de la familia a modificar la dirección ==> ");
                    BigDecimal id = s.nextBigDecimal();
                    
                    Family f = new Family(db, id);
                    
                    if(f.familyExists())
                    {
                        Object[] dir = new Object[4];
                        s = new Scanner(System.in);
                        
                        System.out.print("Introduzca la calle de la dirección ==> ");
                        dir[0] = s.nextLine();

                        s = new Scanner(System.in);

                        System.out.print("Introduzca el numero de la casa de la dirección (introduzca un número, cualquier otra cosa dara un error) ==> ");
                        dir[1] = s.nextInt();

                        s = new Scanner(System.in);

                        System.out.print("Introduzca el apartamento de la dirección ==> ");
                        dir[2] = s.nextLine();

                        s = new Scanner(System.in);

                        System.out.print("Introduzca el codigo postal de la dirección ==> ");
                        dir[3] = s.nextLine();

                        Struct address = db.callFunction("createAddress", dir, OracleTypes.STRUCT, "ADDRESS_OBJTYP");
                        
                        if(f.updateAddress(address))
                        {
                            System.out.println("La dirección de la familia ha sido actualizada correctamente.");
                        } else
                        {
                            System.err.println("Ha ocurrido un error con la actualización. Por favor, vuelta a intentarlo.");
                        }
                    } else
                    {
                        System.err.println("La familia con el id " + id + " no existe. Por favor, pruebe otro id.");
                    }
                }; break;
                case 6: {
                    // 6º) Borrar una familia existente
                    
                    Scanner s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el id de la familia a borrar ==> ");
                    BigDecimal id = s.nextBigDecimal();
                    
                    Family f = new Family(db, id);
                    
                    if(f.familyExists())
                    {
                        if(f.destroy())
                        {
                            System.out.println("Familia borrada correctamente.");
                        } else
                        {
                            System.err.println("Familia no borrada. Vuelva a intentarlo.");
                        }
                    } else
                    {
                        System.err.println("La familia con el id " + id + " no existe. Por favor, pruebe otro id.");
                    }
                }; break;
                case 7: {
                    // 7º) Crear una familia
                    
                    Scanner s = new Scanner(System.in);
                    Object[] values = new Object[5];
                    
                    System.out.print("Introduzca el nombre ==> ");
                    values[0] = s.nextLine();
                    
                    s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el nombre de contacto ==> ");
                    values[1] = s.nextLine();
                    
                    s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el telefono ==> ");
                    values[2] = s.nextLine();
                    
                    s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el email ==> ");
                    values[3] = s.nextLine();
                    
                    s = new Scanner(System.in);
                    
                    Object[] dir = new Object[4];
                    
                    System.out.print("Introduzca la calle de la dirección ==> ");
                    dir[0] = s.nextLine();
                    
                    s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el numero de la casa de la dirección (introduzca un número, cualquier otra cosa dara un error) ==> ");
                    dir[1] = s.nextInt();
                    
                    s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el apartamento de la dirección ==> ");
                    dir[2] = s.nextLine();
                    
                    s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el codigo postal de la dirección ==> ");
                    dir[3] = s.nextLine();
                    
                    values[4] = db.callFunction("createAddress", dir, OracleTypes.STRUCT, "ADDRESS_OBJTYP");
                    
                    if(Family.createFamily(db, values))
                    {
                        System.out.println("Familia creada correctamente.");
                    } else
                    {
                        System.err.println("Familia no creada. Vuelva a intentarlo.");
                    }
                }; break;
                default:
                {
                    System.err.println("Elección incorrecta. Por favor, introduzca una opción valida.");
                }
            }
        } while(option != 0);
    }
    
    public static void gestionarAnimales(Database db) throws SQLException, ParseException
    {
        int option;
        do
        {
            System.out.println("=================================================================");
            System.out.println("=================================================================");
            
            System.out.println("Bienvenido a la gestión de los animales.");
            System.out.println("¿Que desea hacer?");

            System.out.println("1º) Mostrar todos los animales existentes");
            System.out.println("2º) Buscar un animal por su nombres");
            System.out.println("3º) Modificar el nombre de un animal existente");
            System.out.println("4º) Modificar la fecha de nacimiento de un animal existente");
            System.out.println("5º) Modificar el tipo de animal (dog o cat)");
            System.out.println("6º) Borrar un animal existente");
            System.out.println("7º) Crear un animal");
            System.out.println("8º) Asignar un animal a una familia");
            System.out.println("9º) Desasignar un animal a una familia");
            System.out.print("Su elección (escriba '0' para salir) ==> ");
            
            try
            {
                Scanner s = new Scanner(System.in);
                option = s.nextInt();
            } catch(Exception e)
            {
                option = -1;
            }
            
            switch(option)
            {
                case 0: {} ; break;
                case 1: {
                    // 1º) Mostrar todos los animales existentes
                    
                    Pet.printPets(
                        Pet.listAllPets(db)
                    );
                }; break;
                case 2: {
                    // 2º) Buscar un animal por su nombres
                    
                    Scanner s = new Scanner(System.in);
                    System.out.print("Introduzca el nombre de la familia a buscar ==> ");
                    String line = s.nextLine();
                    
                    Family.printFamilys(
                        Family.searchFamilys(db, line, true)
                    );
                }; break;
                case 3: {
                    // 3º) Modificar el nombre de un animal existente
                    
                    Scanner s = new Scanner(System.in);
                    Scanner ss = new Scanner(System.in);
                    
                    System.out.print("Introduzca el id del animal a modificar ==> ");
                    BigDecimal id = s.nextBigDecimal();
                    
                    System.out.print("Introduzca el nuevo nombre del animal ==> ");
                    String name = ss.nextLine();
                    
                    Pet p = new Pet(db, id);
                    
                    if(p.petExists())
                    {
                        if(p.updateName(name))
                        {
                            System.out.println("Animal actualizado correctamente.");
                        } else
                        {
                            System.err.println("Animal no actualizado. Vuelva a intentarlo.");
                        }
                    } else
                    {
                        System.err.println("El animal con el id " + id + " no existe. Por favor, pruebe otro id.");
                    }
                }; break;
                case 4: {
                    // 4º) Modificar la fecha de nacimiento de un animal existente
                    
                    Scanner s = new Scanner(System.in);
                    Scanner ss = new Scanner(System.in);
                    
                    System.out.print("Introduzca el id del animal a modificar ==> ");
                    BigDecimal id = s.nextBigDecimal();
                    
                    System.out.print("Introduzca la nueva fecha de nacimiento (YYYY-MM-DD) ==> ");
                    String date = ss.nextLine();
                    
                    Pet p = new Pet(db, id);
                    
                    if(p.petExists())
                    {
                        if(p.updateDateBirth(date))
                        {
                            System.out.println("Animal actualizado correctamente.");
                        } else
                        {
                            System.err.println("Animal no actualizado. Vuelva a intentarlo.");
                        }
                    } else
                    {
                        System.err.println("El animal con el id " + id + " no existe. Por favor, pruebe otro id.");
                    }
                }; break;
                case 5: {
                    // 5º) Modificar el tipo de animal (dog o cat)
                    
                    Scanner s = new Scanner(System.in);
                    Scanner ss = new Scanner(System.in);
                    
                    System.out.print("Introduzca el id del animal a modificar el tipo ==> ");
                    BigDecimal id = s.nextBigDecimal();
                    
                    System.out.print("Introduzca el nuevo tipo del animal (dog o cat) ==> ");
                    String type = ss.nextLine();
                    
                    Pet p = new Pet(db, id);
                    
                    if(p.petExists())
                    {
                        if(p.updateType(type))
                        {
                            System.out.println("El tipo del animal ha sido actualizado correctamente.");
                        } else
                        {
                            System.err.println("Ha ocurrido un error con la actualización. Por favor, vuelta a intentarlo.");
                        }
                    } else
                    {
                        System.err.println("El animal con el id " + id + " no existe. Por favor, pruebe otro id.");
                    }
                }; break;
                case 6: {
                    // 6º) Borrar un animal existente
                    
                    Scanner s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el id del animal a borrar ==> ");
                    BigDecimal id = s.nextBigDecimal();
                    
                    Pet p = new Pet(db, id);
                    
                    if(p.petExists())
                    {
                        if(p.destroy())
                        {
                            System.out.println("Animal borrado correctamente.");
                        } else
                        {
                            System.err.println("Animal no borrado. Vuelva a intentarlo.");
                        }
                    } else
                    {
                        System.err.println("El animal con el id " + id + " no existe. Por favor, pruebe otro id.");
                    }
                }; break;
                case 7: {
                    // 7º) Crear un animal
                    
                    Scanner s = new Scanner(System.in);
                    Object[] values = new Object[4];
                    
                    System.out.print("Introduzca el nombre ==> ");
                    values[0] = s.nextLine();
                    
                    s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el genero (0 para macho, o 1 para hembra) ==> ");
                    values[1] = s.nextInt();
                    
                    s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el tipo de animal (dog o cat) ==> ");
                    values[2] = s.nextLine();
                    
                    // put the today as date of arrival pet
                    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
                    java.util.Date date = new java.util.Date();
                    values[3] = sdf.format(date);
                    
                    if(Pet.createPet(db, values))
                    {
                        System.out.println("Animal creado correctamente.");
                    } else
                    {
                        System.err.println("Animal no creado. Vuelva a intentarlo.");
                    }
                }; break;
                case 8: {
                    // 8º) Asignar un animal a una familia
                    
                    Scanner s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el id del animal ==> ");
                    BigDecimal idPet = s.nextBigDecimal();
                    
                    s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el id de una familia ==> ");
                    BigDecimal idFamily = s.nextBigDecimal();
                    
                    Family f = new Family(db, idFamily);
                    Pet p = new Pet(db, idPet);
                    
                    if(f.familyExists() && p.petExists())
                    {
                        if(p.setFamily(idFamily))
                        {
                            System.out.println("El animal ha sido adoptado correctamente.");
                        } else
                        {
                            System.err.println("Error a la hora de adoptar un animal.");
                        }
                    } else
                    {
                        System.err.println("El animal o familia con el id insertado no existe. Por favor, pruebe otro id.");
                    }
                }; break;
                case 9: {
                    // 9º) Desasignar un animal a una familia
                    Scanner s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el id del animal ==> ");
                    BigDecimal idPet = s.nextBigDecimal();
                    
                    Pet p = new Pet(db, idPet);
                    
                    if(p.petExists())
                    {
                        if(p.deleteFamily())
                        {
                            System.out.println("Se ha eliminado la adopción de forma correcta.");
                        } else
                        {
                            System.err.println("Error a la hora de eliminar la adopción.");
                        }
                    } else
                    {
                        System.err.println("El animal o familia con el id insertado no existe. Por favor, pruebe otro id.");
                    }
                }; break;
                default:
                {
                    System.err.println("Elección incorrecta. Por favor, introduzca una opción valida.");
                }
            }
        } while(option != 0);
    }
    
    public static void gestionarTratamientos(Database db) throws SQLException
    {
        int option;
        do
        {
            System.out.println("=================================================================");
            System.out.println("=================================================================");
            
            System.out.println("Bienvenido a la gestión de los tratamientos.");
            System.out.println("¿Que desea hacer?");

            System.out.println("1º) Mostrar los tipos de tratamientos existentes");
            System.out.println("2º) Crear un tipo de tratamiento");
            System.out.println("3º) Obtener todos los tratamientos de un animal");
            System.out.print("Su elección (escriba '0' para salir) ==> ");
            
            try
            {
                Scanner s = new Scanner(System.in);
                option = s.nextInt();
            } catch(Exception e)
            {
                option = -1;
            }
            
            switch(option)
            {
                case 0: {}; break;
                case 1: {
                    // 1º) Mostrar los tipos de tratamientos existentes
                    
                    TreatmentType.printTreatmentTypes(
                        TreatmentType.listAllTreatmentTypes(db)
                    );
                }; break;
                case 2: {
                    // 2º) Crear un tipo de tratamiento
                    
                    Scanner s = new Scanner(System.in);
                    Object[] values = new Object[1];
                    
                    System.out.print("Introduzca el nombre ==> ");
                    values[0] = s.nextLine();
                    
                    if(TreatmentType.createTreatmentType(db, values))
                    {
                        System.out.println("Tipo creado correctamente.");
                    } else
                    {
                        System.err.println("Tipo no creado. Vuelva a intentarlo.");
                    }
                }; break;
                case 3: {
                    // 3º) Obtener todos los tratamientos de un animal
                    
                    Scanner s = new Scanner(System.in);
                    
                    System.out.print("Introduzca el id del animal ==> ");
                    BigDecimal id = s.nextBigDecimal();
                    
                    Pet p = new Pet(db, id);
                    
                    if(p.petExists())
                    {
                        p.printTreatmentList();
                    } else
                    {
                        System.err.println("El animal con el id " + id + " no existe. Por favor, pruebe otro id.");
                    }
                }; break;
                default:
                {
                    System.err.println("Elección incorrecta. Por favor, introduzca una opción valida.");
                }
            }
        } while(option != 0);
    }
    
    public static void main(String[] args) throws SQLException, ParseException
    {
        ProcessBuilder pb = new ProcessBuilder();
        
        Map<String, String> env = pb.environment();
        env.put("DB_HOST", "localhost");
        env.put("DB_USER", "user");
        env.put("DB_PASS", "user");
        
        Database db = new Database(pb);
        
        int option;
        do
        {
            System.out.println("=================================================================");
            System.out.println("=================================================================");
            
            System.out.println("Bienvenido a la aplicación de gestión de la Veterinaria.");
            System.out.println("¿Que desea hacer?");
            
            System.out.println("1º) Gestionar Familias");
            System.out.println("2º) Gestionar Animales");
            System.out.println("3º) Gestionar Tratamientos");

            System.out.print("Su elección (escriba '0' para salir) ==> ");
            
            try
            {
                Scanner s = new Scanner(System.in);
                option = s.nextInt();
            } catch(Exception e)
            {
                option = -1;
            }
            
            switch(option)
            {
                case 0: {} ; break;
                case 1: gestionarFamilias(db); break;
                case 2: gestionarAnimales(db); break;
                case 3: gestionarTratamientos(db); break;
                default:
                {
                    System.err.println("Elección incorrecta. Por favor, introduzca una opción valida.");
                }
            }
        } while(option != 0);
    }
}
